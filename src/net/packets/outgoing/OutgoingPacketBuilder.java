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

import net.buffers.IBufferReserve;
import net.buffers.OutputBuffer;
import net.impl.session.Client;
import world.entity.player.Player;
import world.entity.update.PlayerUpdateBlock;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Packet builder.
 */
public class OutgoingPacketBuilder {
    private final Client c;
    //Clear every cycle
    private final HashMap<Player, OutputBuffer> playerMovementCache = new HashMap<>();
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

    private void assignOutputBuffer() {
        outputBuffer = OutputBuffer.create(4096, 1024);
    }

    private OutputBuffer createHeader(int packetId) {
        outputBuffer.writeByte(packetId + c.getOutCipher().getNextValue());
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
        byte[] sBytes = s.getBytes();
        createHeader(OutgoingPacket.Opcodes.SEND_MESSAGE).writeByte(sBytes.length + 1).writeBytes(sBytes).writeByte(10);
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
        if (totalBytes.isPresent()) {
            total = totalBytes.get();
        } else {
            total = 0;
        }

        createHeader(OutgoingPacket.Opcodes.ADD_PLAYER_OPTION).writeByte(total + 2)
                .writeByte(optionPosition, OutputBuffer.ByteTransformationType.C)
                .writeByte(flag, OutputBuffer.ByteTransformationType.A);

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
        outputBuffer.writeBigWORD(strBytes.length + 1 + 2);
        outputBuffer.writeBytes(s.getBytes());
        outputBuffer.writeByte(10); //End string
        outputBuffer.writeBigWORDA(id);
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
        outputBuffer.writeByte(x, OutputBuffer.ByteTransformationType.C);
        outputBuffer.writeByte(y, OutputBuffer.ByteTransformationType.C);
        createHeader(OutgoingPacket.Opcodes.DISPLAY_GROUND_ITEM);
        outputBuffer.writeLittleWORDA(itemId);
        outputBuffer.writeBigWORD(1);
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
        /*client.getOutStream().createHeader(85); // Phate: Item Position Frame
        client.getOutStream().writeByteC((itemY - 8 * client.mapRegionY));
        client.getOutStream().writeByteC((itemX - 8 * client.mapRegionX));
        client.getOutStream().createHeader(156); // Phate: Item Action: Delete
        client.getOutStream().writeByteS(0); // x(4 MSB) y(LSB) coords
        client.getOutStream().writeWord(itemID); // Phate: Item ID*/
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
                .writeByte(membership, OutputBuffer.ByteTransformationType.A)
                .writeLittleWORDA(playerIndex);
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
        /*client.getOutStream().createHeader(71);
        client.getOutStream().writeWord(form);
        client.getOutStream().writeByteA(menuId);*/
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

    /**
     * Player update outgoing packet builder.
     *
     * @return the outgoing packet builder
     */

    public OutgoingPacketBuilder playerUpdate() {
        Player player = c.getPlayer();

        /*
            Teleporting / region change
        */
        IBufferReserve<OutputBuffer> reserve = createHeader(81, 2);


        if (player.isTeleporting() || player.regionHasChanged()) {
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
                    .writeBits(player.getHeight(), 2)
                  /*
                   * This indicates that the client should discard the walking queue.
                  */
                    .writeBits(player.isTeleporting() ? 1 : 0, 1)
                 /*
                   * This flag indicates if an update block is appended.
                   */
                    .writeBits(player.getUpdateFlags().isUpdateRequired() ? 1 : 0, 1)
                  /*
                   * The local Y position of this player.
                   */
                    .writeBits(player.getPosition().getLocalY(player.getLastPosition()), 7)

                  /*
                   * The local X position of this player.
                   */
                    .writeBits(player.getPosition().getLocalX(player.getLastPosition()), 7);
        } else {
            updatePlayerMovement(player);
        }

        /*
            If any updates flags are set for this player, append the update block.
         */
        if (player.getUpdateFlags().anySet()) {
            /*
                Get the update block cache for this world.
             */
            Cache playerUpdateBlockCache = player.getWorld().getPlayerUpdateBlockCache();

            PlayerUpdateBlock pUpdateBlock;
            if (playerUpdateBlockCache.contains(player)) {
                pUpdateBlock = updateBlockCache.get(player);
            } else {
                /*
                Cache this update block, so it can be used later.
                */
                pUpdateBlock = player.getPlayerUpdateBlock().build(player.getUpdateFlags());
                playerUpdateBlockCache.put(player, pUpdateBlock);
            }

            //Pipe the update block to the current outputBuffer and rewind so the update block can be used again.
            pUpdateBlock.getBlock().pipeTo(outputBuffer, false).rewind();
        }


        writer.writeBits(8, player.getLocalPlayers().size());

        for (Iterator<Player> iterator = player.getLocalPlayers().iterator(); iterator
                .hasNext(); ) {

            Player other = iterator.next();

            if (World.getPlayers()[other.getSlot()] != null && other.isRegistered()
                    && other.getPosition().isWithinDistance(player.getPosition(),
                    Position.VIEWING_DISTANCE)) {
                updatePlayerMovement(other, writer);

                if (other.getUpdateFlags().isUpdateRequired()) {
                    appendUpdates(other, update, false, false);
                }
            } else {
                iterator.remove();
                writer.writeBit(true);
                writer.writeBits(2, 3);
            }
        }

        //Write how many bytes the packet contains
        reserve.writeValue(reserve.bytesSinceReserve());
    }

    private void updatePlayerMovement(Player player) {

        if (playerMovementCache.containsKey(player)) {
            OutputBuffer cachedMovement = playerMovementCache.get(player);
            OutputBuffer duplicateMovement = cachedMovement.duplicate();
            playerMovementCache.put(player, duplicateMovement);
            cachedMovement.pipeTo(outputBuffer);
            return;
        }


        OutputBuffer playerMovementBuf = OutputBuffer.create(2, 1);

               /*
             * Check which type of movement took place.
             */
        if (player.getWalkingDirection() == -1) {
                  /*
                   * If no movement did, check if an update is required.
                   */
            if (player.getUpdateFlags().isUpdateRequired()) {
                        /*
                         * Signify that an update happened.
                         */
                playerMovementBuf.writeBit(true)
                        /*
                         * Signify that there was no movement.
                         */
                        .writeBits(0, 2);
            } else {
                        /*
                         * Signify that nothing changed.
                         */
                playerMovementBuf.writeBit(false);
            }
        } else if (player.getRunningDirection() == -1) {
                  /*
                   * The player moved but didn't run. Signify that an update is required.
                   */
            playerMovementBuf.writeBit(true)

                  /*
                   * Signify we moved one tile.
                   */
                    .writeBits(2, 1)

                  /*
                   * Write the primary sprite (i.e. walk direction).
                   */
                    .writeBits(3, player.getWalkingDirection())

                  /*
                   * Write a flag indicating if a block update happened.
                   */
                    .writeBit(player.getUpdateFlags().isUpdateRequired());


        } else {
                  /*
                   * The player ran. Signify that an update happened.
                   */
            playerMovementBuf.writeBit(true)

                  /*
                   * Signify that we moved two tiles.
                   */
                    .writeBits(2, 2)

                  /*
                   * Write the primary sprite (i.e. walk direction).
                   */
                    .writeBits(3, player.getWalkingDirection())

                  /*
                   * Write the secondary sprite (i.e. run direction).
                   */
                    .writeBits(3, player.getRunningDirection())

                  /*
                   * Write a flag indicating if a block update happened.
                   */
                    .writeBit(player.getUpdateFlags().isUpdateRequired());
        }

        playerMovementCache.put(player, playerMovementBuf.duplicate());
        playerMovementBuf.pipeTo(outputBuffer);
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
}
