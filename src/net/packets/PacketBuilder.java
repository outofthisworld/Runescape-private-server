package net.packets;

import net.Client;
import net.buffers.OutputBuffer;

/**
 * The type Packet builder.
 */
public class PacketBuilder {
    private final Client c;
    private OutputBuffer outputBuffer = OutputBuffer.create();

    /**
     * Instantiates a new Packet builder.
     *
     * @param c the c
     */
    public PacketBuilder(Client c) {
        this.c = c;
    }

    private void createFrame(int packetId) {
        outputBuffer.writeByte(packetId + c.getOutCipher().getNextValue());
    }

    /**
     * Logout packet builder.
     *
     * @return the packet builder
     */
/*
        109: Disconnects the client from the server.
     */
    public PacketBuilder logout() {
        if (c.getPlayer().save()) {
            createFrame(Packet.OutgoingPackets.LOGOUT);
        }
        return this;
    }


    /**
     * Send message packet builder.
     *
     * @param s the s
     * @return the packet builder
     */
/*
        253: Sends a server message (e.g. 'Welcome to RuneScape') or trade/duel request.
    */
    public PacketBuilder sendMessage(String s) {
        createFrame(Packet.OutgoingPackets.SEND_MESSAGE);
        byte[] sBytes = s.getBytes();
        outputBuffer.writeByte(sBytes.length);
        outputBuffer.writeBytes(sBytes);
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
    public PacketBuilder sendInterfaceText(String s, int id) {
        createFrame(126);
        byte[] strBytes = s.getBytes();
        outputBuffer.writeBigWORD(strBytes.length + 1 + 2);
        outputBuffer.writeBytes(s.getBytes());
        outputBuffer.writeByte(10); //End string
        client.getOutStream().writeWordA(id);
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
    public PacketBuilder createGroundItem(int itemId, int x, int y) {
        client.getOutStream().createFrame(85);
        client.getOutStream().writeByteC(x);
        client.getOutStream().writeByteC(y);
        client.getOutStream().createFrame(44);
        client.getOutStream().writeWordBigEndianA(itemId); // itemId
        client.getOutStream().writeWord(1); // amount
        client.getOutStream().writeByte(0); // x(4 MSB) y(LSB) coords
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
    public PacketBuilder createGroundItem(int itemID, int itemX, int itemY,
            int itemAmount) {// Phate: Omg fucking sexy! creates item at
        // absolute X and Y
        client.getOutStream().createFrame(85); // Phate: Spawn ground item
        client.getOutStream().writeByteC(( itemY - 8 * client.mapRegionY ));
        client.getOutStream().writeByteC(( itemX - 8 * client.mapRegionX ));
        client.getOutStream().createFrame(44);
        client.getOutStream().writeWordBigEndianA(itemID);
        client.getOutStream().writeWord(itemAmount);
        client.getOutStream().writeByte(0); // x(4 MSB) y(LSB) coords
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
    public PacketBuilder removeGroundItem(int itemX, int itemY, int itemID) {
        client.getOutStream().createFrame(85); // Phate: Item Position Frame
        client.getOutStream().writeByteC(( itemY - 8 * client.mapRegionY ));
        client.getOutStream().writeByteC(( itemX - 8 * client.mapRegionX ));
        client.getOutStream().createFrame(156); // Phate: Item Action: Delete
        client.getOutStream().writeByteS(0); // x(4 MSB) y(LSB) coords
        client.getOutStream().writeWord(itemID); // Phate: Item ID
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
    public PacketBuilder openWelcomeScreen(int recoveryChange,
            boolean memberWarning, int messages, int lastLoginIP, int lastLogin) {
        client.getOutStream().createFrame(176);
        client.getOutStream().writeByteC(recoveryChange);
        client.getOutStream().writeWordA(messages); // # of unread messages
        client.getOutStream().writeByte(memberWarning ? 1 : 0); // 1 for member
        // on
        // non-members world warning
        client.getOutStream().writeDWord_v2(lastLoginIP); // ip of last login
        client.getOutStream().writeWord(lastLogin); // days
        return this;
    }

    /**
     * 71: Assigns an interface to one of the tabs in the game sidebar.
     *
     * @param menuId The sidebar.
     * @param form   The interface.
     * @return The action.
     */
    public PacketBuilder setSidebarInterface(int menuId, int form) {
        client.getOutStream().createFrame(71);
        client.getOutStream().writeWord(form);
        client.getOutStream().writeByteA(menuId);
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
    public PacketBuilder setSkillLevel(int skillNum, int currentLevel, int XP) {
        client.getOutStream().createFrame(134);
        client.getOutStream().writeByte(skillNum);
        client.getOutStream().writeDWord_v1(XP);
        client.getOutStream().writeByte(currentLevel);
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
    public PacketBuilder setChatOptions(int publicChat, int privateChat,
            int tradeBlock) {
        client.getOutStream().createFrame(206);
        client.getOutStream().writeByte(publicChat); // On = 0, Friends = 1, Off
        // = 2, Hide =
        // 3
        client.getOutStream().writeByte(privateChat); // On = 0, Friends = 1,
        // Off = 2
        client.getOutStream().writeByte(tradeBlock); // On = 0, Friends = 1, Off
        // = 2
        return this;
    }


    /**
     * 97: Displays a normal interface.
     *
     * @param id The interface id.
     * @return The action.
     */
    public PacketBuilder sendInterface(int id) {
        client.getOutStream().createFrame(97);
        client.getOutStream().writeWord(id);
        return this;
    }

    /**
     * 164: Shows an interface in the chat box.
     *
     * @param id The interface id.
     * @return The action.
     */
    public PacketBuilder sendChatInterface(int id) {
        client.getOutStream().createFrame(164);
        client.getOutStream().writeWordBigEndian(id);
        return this;
    }


    /**
     * Sends the level up.
     *
     * @param skillId The skill id.
     * @return The action.
     */
    public PacketBuilder sendLevelUp(int skillId) {
        sendMessage("Congratulations! You are now level "
                            + client.playerLevel[ skillId ] + " "
                            + Skills.SKILL_NAME[ skillId ][ 1 ] + ".");
        sendChatInterface(client.getLevelUpInterfaces()[ skillId ][ 0 ]);
        sendString("@dbl@Congratulations, you just advanced "
                           + Skills.SKILL_NAME[ skillId ][ 0 ] + " level.",
                   client.getLevelUpInterfaces()[ skillId ][ 1 ]);
        sendString("Your " + Skills.SKILL_NAME[ skillId ][ 1 ] + " level is now "
                           + client.playerLevel[ skillId ] + ".",
                   client.getLevelUpInterfaces()[ skillId ][ 2 ]);
        return this;
    }

    /**
     * Closes all the interfaces.
     *
     * @return The action.
     */
    public PacketBuilder closeInterfaces() {
        client.getOutStream().createFrame(219);
        return this;
    }


    /**
     * Build output buffer.
     *
     * @return the output buffer
     */
    public OutputBuffer build() {
        OutputBuffer current = outputBuffer;
        outputBuffer = OutputBuffer.create();
        return current;
    }

    /**
     * Send.
     */
    public void send() {
        c.write(build());
    }
}
