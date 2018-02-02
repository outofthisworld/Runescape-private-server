package net.packets.outgoing;

import net.Client;
import net.buffers.OutputBuffer;

public class OutgoingPacket {


    public OutgoingPacket() {

    }

    public void send(Client c) {
        c.write(getOutputBuffer());
    }

    public OutputBuffer getOutputBuffer() {

    }

    public static class Opcodes {
        /**
         * The opcode the display an item on the ground.
         */
        public static final int DISPLAY_GROUND_ITEM = 44;
        /**
         * Assigns an interface to one of the menu tabs in-game.
         */
        public static final int SET_SIDEBAR_INTERFACE = 71;
        /**
         * Updates the players XY coordinates.
         * <p>
         * Structure:
         * x - 1 byte - the players x coordinate. Transformation type C.
         * y - 1 byte - the players y coordinate. Transformation type C.
         */
        public static final int UPDATE_PLAYER_XY = 85;
        /**
         * Displays an interface.
         * <p>
         * Structure:
         * id - Big endian word (2 bytes)
         */
        public static final int SEND_INTERFACE = 97;
        /**
         * The logout packet
         */
        public static final int LOGOUT = 109;
        /**
         * Attaches text to an interface, e.g when hovering skills.
         * <p>
         * Structure:
         * MessageLength - 4 bytes - big endian - length of the message
         * Message - the message bytes
         * EndOfMessage - 1 byte - the end of message placeholder (10)
         * InterfaceId -  2 bytes - The id of the interface
         */
        public static final int INTERFACE_TEXT = 126;
        /**
         * Sends new level and exp of skills to the client.
         */
        public static final int UPDATE_SKILL = 134;
        /**
         * Removes an item from the ground
         */
        public static final int REMOVE_GROUND_ITEM = 156;
        /**
         * Sends an interface in the chatbox.
         * <p>
         * Structure:
         * id - Little endian word (2 bytes) - The chat interface to open
         */
        public static final int SEND_CHAT_INTERFACE = 164;
        /**
         * Updates the players XY coordinates.
         */
        public static final int SEND_MESSAGE = 253;
        /**
         * Updates the players XY coordinates.
         */
        public static final int OPEN_WELCOME_INTERFACE = 176;
        /**
         * Updates the players XY coordinates.
         */
        public static final int CHAT_PRIVACY_SETTINGS = 206;
        /**
         * Updates the players XY coordinates.
         */
        public static final int CLOSE_ALL_INTERFACES = 219;
    }
}
