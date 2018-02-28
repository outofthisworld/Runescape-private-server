/*
 Project by outofthisworld24
 All rights reserved.
 */

/*
 * Project by outofthisworld24
 * All rights reserved.
 */

/*------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 Project by outofthisworld24
 All rights reserved.
 -----------------------------------------------------------------------------*/

package net.packets.outgoing;

import net.buffers.ByteTransformationType;
import net.buffers.IBufferReserve;
import net.buffers.Order;
import net.buffers.OutputBuffer;
import net.impl.session.Client;
import util.Preconditions;
import world.entity.player.Player;
import world.entity.update.player.PlayerUpdateBlock;
import world.entity.update.player.PlayerUpdateMask;
import world.storage.SimpleCache;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Packet builder.
 */
public class OutgoingPacketBuilder {
    private final Client c;
    /*
        The update buffer.
    */
    private final OutputBuffer update = OutputBuffer.create(4096, 1024);
    private OutputBuffer outputBuffer;

    /**
     * Instantiates a new Packet builder.
     *
     * @param c the c
     */
    public OutgoingPacketBuilder(Client c) {
        this.c = c;
        assignOutputBuffer();
    }

    public int bytesWritten() {
        return outputBuffer.position();
    }

    private void assignOutputBuffer() {
        outputBuffer = OutputBuffer.create(1024, 512);
    }

    private OutputBuffer createHeader(int packetId) {
        byte b = (byte) (packetId + c.getOutCipher().getNextValue());
        outputBuffer.writeByte(b);
        return outputBuffer;
    }

    private IBufferReserve<OutputBuffer> createHeader(int packetId, int reserveBytes) {
        createHeader(packetId);
        return outputBuffer.createByteReserve(reserveBytes);
    }

    /**
     * 109: Logout packet builder.
     *
     * @return the packet builder
     */
    public OutgoingPacketBuilder logout() {
        createHeader(OutgoingPacket.Opcodes.LOGOUT);
        return this;
    }

    /**
     * 253: Send message packet builder.
     *
     * @param s the s
     * @return the packet builder
     */
    public OutgoingPacketBuilder sendMessage(String s) {
        createHeader(OutgoingPacket.Opcodes.SEND_MESSAGE, 1)
                .toBuffer()
                .writeBytes(s.getBytes())
                .writeByte(10)
                .toLastReserve()
                .writeBytesSinceReserve();
        return this;
    }

    /**
     * Add player options outgoing packet builder.
     *
     * @param optionPosition the option position
     * @param flag           the flag
     * @param actions        the actions
     * @return the outgoing packet builder
     */
    public OutgoingPacketBuilder addPlayerOptions(int optionPosition, int flag, String[] actions) {

        List<byte[]> list = Arrays.stream(actions).map(String::getBytes).collect(Collectors.toList());
        Optional<Integer> totalBytes = list.stream().map(b -> b.length).reduce((integer, integer2) -> integer + integer2);

        int total;
        total = totalBytes.orElse(0);

        createHeader(OutgoingPacket.Opcodes.ADD_PLAYER_OPTION).writeByte(total + 2)
                .writeByte(optionPosition, ByteTransformationType.C)
                .writeByte(flag, ByteTransformationType.A);

        return this;
    }

    /**
     * Send interface text
     * 126: Attaches text to an interface.
     *
     * @param s  the s
     * @param id the id
     * @return the packet builder
     */
    public OutgoingPacketBuilder sendInterfaceText(String s, int id) {
        createHeader(126);
        byte[] strBytes = s.getBytes();
        outputBuffer.writeBigWord(strBytes.length + 1 + 2);
        outputBuffer.writeBytes(s.getBytes());
        outputBuffer.writeByte(10); //End string
        outputBuffer.writeBigWordTypeA(id);
        return this;
    }

    /**
     * Create ground item packet builder.
     * 85: Updates the players localX and localY
     * 44: Display's a ground item at a specified coordinate.
     *
     * @param itemId the item id
     * @param x      the x
     * @param y      the y
     * @return the packet builder
     */
    public OutgoingPacketBuilder createGroundItem(int itemId, int x, int y) {
        createHeader(OutgoingPacket.Opcodes.UPDATE_PLAYER_XY);
        outputBuffer.writeByte(x, ByteTransformationType.C);
        outputBuffer.writeByte(y, ByteTransformationType.C);
        createHeader(OutgoingPacket.Opcodes.DISPLAY_GROUND_ITEM);
        outputBuffer.writeLittleWordTypeA(itemId);
        outputBuffer.writeBigWord(1);
        outputBuffer.writeByte(0);
        return this;
    }

    /**
     * Creates a ground item.
     * 85: Updates the players localX and localY
     * 44: Display's a ground item at a specified coordinate.
     *
     * @param itemID     The item id.
     * @param itemX      The x coord.
     * @param itemY      The y coord.
     * @param itemAmount The amount.
     * @return The action.
     */
    public OutgoingPacketBuilder createGroundItem(int itemID, int itemX, int itemY, int itemAmount) {// Phate: Omg fucking sexy! creates item at
        /*
        // absolute X and Y
        client.getOutStream().createHeader(85); // Phate: Spawn ground item
        client.getOutStream().writeByteC((itemY - 8 * client.mapRegionY));
        client.getOutStream().writeByteC((itemX - 8 * client.mapRegionX));
        client.getOutStream().createHeader(44);
        client.getOutStream().writeWordBigEndianA(itemID);
        client.getOutStream().writeWord(itemAmount);
        client.getOutStream().writeByte(0); // x(4 MSB) y(LSB) coords*/
        return this;
    }

    /**
     * Removes an item from the ground.
     * <p>
     * 85: Updates the players localX and localY
     * 156: Removes an item on the ground.
     *
     * @param itemX  The x coord.
     * @param itemY  The y coord.
     * @param itemID The id.
     * @return The action.
     */
    public OutgoingPacketBuilder removeGroundItem(int itemX, int itemY, int itemID) {
        /*client.getOutStream().createHeader(85); // Phate: SlotItem Position Frame
        client.getOutStream().writeByteC((itemY - 8 * client.mapRegionY));
        client.getOutStream().writeByteC((itemX - 8 * client.mapRegionX));
        client.getOutStream().createHeader(156); // Phate: SlotItem Action: Delete
        client.getOutStream().writeByteS(0); // x(4 MSB) y(LSB) coords
        client.getOutStream().writeWord(itemID); // Phate: SlotItem ID*/
        return this;
    }


    /**
     * Init player outgoing packet builder.
     *
     * @param membership  the membership
     * @param playerIndex the player index
     * @return the outgoing packet builder
     */
    public OutgoingPacketBuilder initPlayer(int membership, int playerIndex) {

        createHeader(OutgoingPacket.Opcodes.INIT_PLAYER)
                .writeByte(membership, ByteTransformationType.A)
                .writeLittleWordTypeA(playerIndex);
        return this;
    }

    /**
     * Open welcome screen packet builder.
     *
     * @param recoveryChange the recovery change
     * @param memberWarning  the member warning
     * @param messages       the messages
     * @param lastLoginIP    the last login ip
     * @param lastLogin      the last login
     * @return the packet builder
     */
    public OutgoingPacketBuilder openWelcomeScreen(int recoveryChange, boolean memberWarning, int messages, int lastLoginIP, int lastLogin) {
        /*client.getOutStream().createHeader(176);
        client.getOutStream().writeByteC(recoveryChange);
        client.getOutStream().writeWordA(messages); // # of unread messages
        client.getOutStream().writeByte(memberWarning ? 1 : 0); // 1 for member
        // on
        // non-members world warning
        client.getOutStream().writeDWord_v2(lastLoginIP); // ip of last login
        client.getOutStream().writeWord(lastLogin); // days*/
        return this;
    }

    /**
     * 71: Assigns an interface to one of the tabs in the game sidebar.
     *
     * @param menuId The sidebar.
     * @param form   The interface.
     * @return The action.
     */
    public OutgoingPacketBuilder setSidebarInterface(int menuId, int form) {
        createHeader(71).writeBigWord(form).writeByte(menuId, ByteTransformationType.A);
        return this;
    }

    /**
     * 134: Updates a players skill of current lvl and experience
     *
     * @param skillNum     The skill id.
     * @param currentLevel The level.
     * @param XP           The xp.
     * @return The action.
     */
    public OutgoingPacketBuilder setSkillLevel(int skillNum, int currentLevel, int XP) {
        createHeader(OutgoingPacket.Opcodes.UPDATE_SKILL)
                .writeByte(skillNum).
                order(Order.BIG_MIDDLE_ENDIAN)
                .writeBytes(XP, 4)
                .writeByte(currentLevel);
        /*client.getOutStream().createHeader(134);
        client.getOutStream().writeByte(skillNum);
        client.getOutStream().writeDWord_v1(XP);
        client.getOutStream().writeByte(currentLevel);*/
        return this;
    }

    /**
     * 206: Sends the chat privacy settings.
     *
     * @param publicChat  The public chat option.
     * @param privateChat The private chat option.
     * @param tradeBlock  The trade block option.
     * @return chat options
     */
    public OutgoingPacketBuilder setChatOptions(int publicChat, int privateChat, int tradeBlock) {
        createHeader(OutgoingPacket.Opcodes.CHAT_PRIVACY_SETTINGS)
                .writeByte(publicChat).writeByte(privateChat).writeByte(tradeBlock);
        return this;
    }

    public OutgoingPacketBuilder setRunEnergy(int runEnergy) {
        Preconditions.inRangeClosed(runEnergy, 0, 100);
        createHeader(OutgoingPacket.Opcodes.SEND_RUN_ENERGY_LEVEL)
                .writeByte(runEnergy);
        return this;
    }

    /**
     * 97: Displays a normal interface.
     *
     * @param id The interface id.
     * @return The action.
     */
    public OutgoingPacketBuilder sendInterface(int id) {
        /*client.getOutStream().createHeader(97);
        client.getOutStream().writeWord(id);*/
        return this;
    }

    /**
     * 164: Shows an interface in the chat box.
     *
     * @param id The interface id.
     * @return The action.
     */
    public OutgoingPacketBuilder sendChatInterface(int id) {
        /*client.getOutStream().createHeader(164);
        client.getOutStream().writeWordLittleEndian(id);*/
        return this;
    }

    /**
     * Sends the level up.
     *
     * @param skillId The skill id.
     * @return The action.
     */
    public OutgoingPacketBuilder sendLevelUp(int skillId) {
        /*sendMessage("Congratulations! You are now level " + client.playerLevel[skillId] + " " + Skill.SKILL_NAME[skillId][1] + ".");
        sendChatInterface(client.getLevelUpInterfaces()[skillId][0]);
        sendString("@dbl@Congratulations, you just advanced " + Skill.SKILL_NAME[skillId][0] + " level.", client.getLevelUpInterfaces()[skillId][1]);
        sendString("Your " + Skill.SKILL_NAME[skillId][1] + " level is now " + client.playerLevel[skillId] + ".", client.getLevelUpInterfaces()[skillId][2]);*/
        return this;
    }

    /**
     * Closes all the interfaces.
     *
     * @return The action.
     */
    public OutgoingPacketBuilder closeInterfaces() {
        createHeader(219);
        return this;
    }

    /*
            needDrawTabArea = true;
				int i9 = inStream.readUnsignedWord(); //interface id
				RSInterface rSInterface_2 = RSInterface.interfaceCache[i9];
				while (inStream.currentOffset < pktSize) {
					int j20 = inStream.method422(); //slot
					int i23 = inStream.readUnsignedWord(); //item id
					int l25 = inStream.readUnsignedByte();//amount
					if (l25 == 255) {
						l25 = inStream.readDWord();
					}
					if (j20 >= 0 && j20 < rSInterface_2.inv.length) {
						rSInterface_2.inv[j20] = i23;
						rSInterface_2.invStackSizes[j20] = l25;
					}
				}
				pktType = -1;
     */
    public OutgoingPacketBuilder updateItem(int interfaceId, int slot, int itemId, int amount) {
        IBufferReserve<OutputBuffer> res = createHeader(OutgoingPacket.Opcodes.UPDATE_INVENTORY_ITEM, 2);
        outputBuffer.writeBigWord(interfaceId);
        _updateItem(slot, itemId, amount);
        res.writeBytesSinceReserve();
        return this;
    }


    private void _updateItem(int slot, int itemId, int amount) {
        if (slot < 128) {
            outputBuffer.writeByte(slot);
        } else {
            outputBuffer.writeBigWord(slot); // + 32768??
        }
        outputBuffer.writeBigWord(itemId);
        if (amount > 254) {
            outputBuffer.writeByte(255);
            outputBuffer.writeBigDWORD(amount);
        } else {
            outputBuffer.writeByte(amount);
        }
    }

    public OutgoingPacketBuilder updateItems(int interfaceId, int[] slots, int[] itemIds, int[] stackSizes, boolean ignoreNullItems) {
        Preconditions.notNull(itemIds, stackSizes);
        if (itemIds.length != stackSizes.length || stackSizes.length != slots.length) {
            throw new IllegalArgumentException("Invalid lengths");
        }
        IBufferReserve<OutputBuffer> res = createHeader(OutgoingPacket.Opcodes.UPDATE_INVENTORY_ITEM, 2);
        outputBuffer.writeBigWord(interfaceId);
        for (int i = 0; i < slots.length; i++) {
            if (ignoreNullItems && itemIds[i] <= 0) {
                continue;
            }

            _updateItem(slots[i], itemIds[i], stackSizes[i]);
        }
        res.writeBytesSinceReserve();
        return this;
    }

    private void appendPlayerUpdateBlock(Player player, OutputBuffer update) {
        Objects.requireNonNull(player);

        /*
            If there are no updates, just return.
         */
        if (!player.getUpdateFlags().anySet()) {
            return;
        }

        /*
           Get the update block cache for this world.
         */
        SimpleCache<String, PlayerUpdateBlock> playerUpdateBlockCache = player.getWorld().getPlayerUpdateBlockCache();

        /*
            The player update block, either built or retrieved from the cache.
         */
        PlayerUpdateBlock pUpdateBlock = null;


        if (playerUpdateBlockCache.contains(player.getUsername())) {
            pUpdateBlock = playerUpdateBlockCache.load(player.getUsername());

            if (pUpdateBlock.getMask() != player.getUpdateFlags().getMask()) {
                pUpdateBlock = null;
            }
        }

        if (pUpdateBlock == null) {
            /*
                Build the player update block from the flags.
            */
            pUpdateBlock = player.getPlayerUpdateBlock().build(player.getUpdateFlags());
            /*
                Cache this update block, so it can be used later.
             */
            playerUpdateBlockCache.store(player.getUsername(), pUpdateBlock);
        }

        //Pipe the update block to the current outputBuffer and rewind so the update block can be used again.
        pUpdateBlock.getBlock().pipeTo(update, false);
    }

    public OutgoingPacketBuilder updateRegion() {
        createHeader(73);
        /**
         * * Short Special A	Region X coordinate (absolute X / 8) plus 6.
         * Short	Region Y coordinate (absolute Y / 8) plus 6.
         */
        outputBuffer.writeBigWordTypeA(c.getPlayer().getPosition().getChunkX()).
                writeBigWord(c.getPlayer().getPosition().getChunkY());
        return this;
    }


    /**
     * Player update outgoing packet builder.
     *
     * @return the outgoing packet builder
     */

    public OutgoingPacketBuilder playerUpdate() {
        Player player = c.getPlayer();


        IBufferReserve<OutputBuffer> reserve = createHeader(81, 2);

        /*
            Update the players movement.
         */
        updatePlayerMovement(player, true);
        /*
            Append the current players update block.
         */
        appendPlayerUpdateBlock(player, update);


        outputBuffer.writeBits(player.getLocalPlayers().size(), 8);

        for (Iterator<Player> iterator = player.getLocalPlayers().iterator(); iterator
                .hasNext(); ) {

            Player other = iterator.next();


            if (other.getWorld().getPlayer(other.getSlotId()) != null &&
                    other.getWorld().getPlayer(other.getSlotId()).getUsername().equals(other.getUsername()) &&
                    player.getPosition().isInViewingDistance(other.getPosition())) {
                updatePlayerMovement(other, false);
                appendPlayerUpdateBlock(other, update);
            } else {
                iterator.remove();
                outputBuffer.writeBit(true);
                outputBuffer.writeBits(3, 2);
            }
        }

        //Find players in the surrounding area to this player
        Set<Player> playersInRegion = player.getWorld().getPlayersByQuadRegion(player.getPosition().getRegionPosition());

        Iterator<Player> it = playersInRegion.iterator();
        int playersAdded = 0;
        for(;it.hasNext();){
            Player p = it.next();

            if (p == player) {
                continue;
            }

            if (player.getLocalPlayers().contains(p)) {
                continue;
            }

            if (player.getLocalPlayers().size() >= 79
                    || playersAdded >= 25) {
                break;
            }

            if (player.getPosition().isInViewingDistance(p.getPosition())) {


                int offsetY = p.getPosition().getVector().getY() - player.getPosition().getVector().getY();
                int offsetX = p.getPosition().getVector().getX() - player.getPosition().getVector().getX();
                //System.out.println(offsetX);
                //System.out.println(offsetY);
                //Adds a players to the local player list, and in-view of other players.
                outputBuffer.writeBits(p.getSlotId(), 11)
                        .writeBit(true)
                        .writeBit(true)
                        .writeBits(offsetY, 5)
                        .writeBits(offsetX, 5);

                player.getLocalPlayers().add(p);
                playersAdded++;
                p.getUpdateFlags().setFlag(PlayerUpdateMask.APPEARANCE);
                appendPlayerUpdateBlock(p, update);
            }

        }



        //1: our player movement
        //2: Local players size
        //3: other player movement
        //4: New local players and there relative x and y
        //3: Our player updates
        //4: Other player updates
        //5: New local player updates
        if (update.position() > 0) {
            outputBuffer.writeBits(2047, 11);
            update.pipeTo(outputBuffer, true);
        }

        //Write how many bytes the packet contains
        reserve.writeValue(reserve.bytesSinceReserve());
        update.clear();
        return this;
    }

    private void updatePlayerMovement(Player player, boolean thisPlayer) {

        if (thisPlayer && (player.isTeleporting() || player.isRegionChanged())) {

             /*
              * Update required bit
             */
            outputBuffer.writeBit(true)
                /*
                 * This value indicates the player teleported.
                 */
                    .writeBits(3, 2)
                 /*
                 * The players height
                 */
                    .writeBits(player.getPosition().getVector().getZ(), 2)
                  /*
                   * This indicates that the client should discard the walking queue.
                  */
                    .writeBits(player.isTeleporting() ? 1 : 0, 1)
                 /*
                   * This flag indicates if an update block is appended.
                   */
                    .writeBits(player.getUpdateFlags().anySet() ? 1 : 0, 1)
                  /*
                   * The local Y position of this player.
                   */
                    .writeBits(player.getPosition().getLocalY(), 7)
                  /*
                   * The local X position of this player.
                   */
                    .writeBits(player.getPosition().getLocalX(), 7);

        } else {

            /*
             * Check which type of movement took place.
             */
            if (!player.getMovement().isMoving()) {
                  /*
                   * If no movement did, check if an update is required.
                   */
                if (player.getUpdateFlags().anySet()) {
                        /*
                         * Signify that an update happened.
                         */
                    outputBuffer.writeBit(true)
                        /*
                         * Signify that there was no movement.
                         */
                            .writeBits(0, 2);
                } else {
                        /*
                         * Signify that nothing changed.
                         */
                    outputBuffer.writeBit(false);
                }
            } else if (!player.getMovement().isRunning()) {

                  /*
                   * The player moved but didn't run. Signify that an update is required.
                   */
                outputBuffer.writeBit(true)

                  /*
                   * Signify we moved one tile.
                   */
                        .writeBits(1, 2)

                  /*
                   * Write the primary sprite (i.e. walk direction).
                   */
                        .writeBits(player.getMovement().getWalkDirection(), 3)

                  /*
                   * Write a flag indicating if a block update happened.
                   */
                        .writeBit(player.getUpdateFlags().anySet());


            } else {
                System.out.println("Running : ");
                System.out.println("Walk dir : " + player.getMovement().getWalkDirection());
                System.out.println("Run dir : " + player.getMovement().getRunDirection());
                  /*
                   * The player ran. Signify that an update happened.
                   */
                outputBuffer.writeBit(true)

                  /*
                   * Signify that we moved two tiles.
                   */
                        .writeBits(2, 2)

                  /*
                   * Write the primary sprite (i.e. walk direction).
                   */
                        .writeBits(player.getMovement().getWalkDirection(), 3)

                  /*
                   * Write the secondary sprite (i.e. run direction).
                   */
                        .writeBits(player.getMovement().getRunDirection(), 3)

                  /*
                   * Write a flag indicating if a block update happened.
                   */
                        .writeBit(player.getUpdateFlags().anySet());
            }
        }
    }


    /**
     * Build output buffer.
     *
     * @return the output buffer
     */
    public OutputBuffer build() {
        OutputBuffer current = outputBuffer;
        assignOutputBuffer();
        return current;
    }

    /**
     * Send.
     */
    public void send() {
        c.write(build());
    }

    public void clearInventory(int inventoryId) {
        createHeader(72).writeLittleWORD(inventoryId);
    }
}
