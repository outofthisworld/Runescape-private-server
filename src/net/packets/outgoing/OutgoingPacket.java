package net.packets.outgoing;

import net.Client;
import net.buffers.OutputBuffer;

/**
 * The type Outgoing packet.
 */
public class OutgoingPacket {

    protected final OutputBuffer outputBuffer;

    /**
     * Instantiates a new Outgoing packet.
     */
    public OutgoingPacket(OutputBuffer out) {
        outputBuffer = out;
    }

    /**
     * Send.
     *
     * @param c the c
     */
    public void send(Client c) {
        c.write(toOutputBuffer());
    }

    /**
     * Gets output buffer.
     *
     * @return the output buffer
     */
    public OutputBuffer toOutputBuffer() {
        return new OutputBuffer(outputBuffer.toArray());
    }

    /**
     * The type Opcodes.
     */
    public static class Opcodes {

        /**
         * The constant RESET_ANIMATIONS.
         */
        public static final int RESET_ANIMATIONS = 1;
        /**
         * The constant DISPLAY_STATIONARY_ANIMATION.
         */
        public static final int DISPLAY_STATIONARY_ANIMATION = 4;
        /**
         * The constant FLASH_SIDEBAR_ICON.
         */
        public static final int FLASH_SIDEBAR_ICON = 24;
        /**
         * The constant UPDATE_INVENTORY_ITEM.
         */
        public static final int UPDATE_INVENTORY_ITEM = 34;
        /**
         * The constant CAMERA_SHAKE.
         */
        public static final int CAMERA_SHAKE = 35;

        /**
         * The opcode the display an item on the ground.
         */
        public static final int DISPLAY_GROUND_ITEM = 44;
        /**
         * The constant ADD_FRIEND.
         */
        public static final int ADD_FRIEND = 50;

        /**
         * The constant SET_INVENTORY_STACK_SIZE.
         */
        public static final int SET_INVENTORY_STACK_SIZE = 53;
        /**
         * The constant REGIONAL_PACKET.
         */
        public static final int REGIONAL_PACKET = 60;
        /**
         * The constant SHOW_MULTI_COMBAT.
         */
        public static final int SHOW_MULTI_COMBAT = 61;
        /**
         * The constant RESET_BUTTON_STATE.
         */
        public static final int RESET_BUTTON_STATE = 68;
        /**
         * The constant SET_INTERFACE_OFFSET.
         */
        public static final int SET_INTERFACE_OFFSET = 70;
        /**
         * The constant CLEAR_INTERFACE_INVENTORY.
         */
        public static final int CLEAR_INTERFACE_INVENTORY = 72;
        /**
         * The constant LOAD_MAP_REGION.
         */
        public static final int LOAD_MAP_REGION = 73;
        /**
         * The constant PLAY_SONG.
         */
        public static final int PLAY_SONG = 74;
        /**
         * The constant NPC_HEAD_ON_INTERFACE.
         */
        public static final int NPC_HEAD_ON_INTERFACE = 75;
        /**
         * The constant RESET_PLAY_DESTINATION.
         */
        public static final int RESET_PLAY_DESTINATION = 78;
        /**
         * The constant SET_SCROLLBAR_POSITION.
         */
        public static final int SET_SCROLLBAR_POSITION = 79;
        /**
         * The constant BEGIN_PLAYER_UPDATING.
         */
        public static final int BEGIN_PLAYER_UPDATING = 81;
        /**
         * The constant SET_MINIMAP_STATE.
         */
        public static final int SET_MINIMAP_STATE = 99;
        /**
         * The constant REMOVE_OBJECT.
         */
        public static final int REMOVE_OBJECT = 101;
        /**
         * The constant ADD_PLAYER_OPTION.
         */
        public static final int ADD_PLAYER_OPTION = 104;
        /**
         * The constant INTEFACE_OVER_TAB.
         */
        public static final int INTEFACE_OVER_TAB = 106;
        /**
         * The constant RESET_CAMERA_POSITION.
         */
        public static final int RESET_CAMERA_POSITION = 107;
        /**
         * The constant SEND_RUN_ENERGY_LEVEL.
         */
        public static final int SEND_RUN_ENERGY_LEVEL = 110;
        /**
         * The constant SEND_SYSTEM_UPDATE.
         */
        public static final int SEND_SYSTEM_UPDATE = 114;
        /**
         * The constant CREATE_PROJECTILE.
         */
        public static final int CREATE_PROJECTILE = 117;
        /**
         * The constant QUEUE_SONG.
         */
        public static final int QUEUE_SONG = 121;
        /**
         * The constant INTERFACE_COLOR.
         */
        public static final int INTERFACE_COLOR = 122;
        /**
         * The constant SPAWN_OBJECT.
         */
        public static final int SPAWN_OBJECT = 151;
        /**
         * The constant SEND_HIDDEN_INTERFACE.
         */
        public static final int SEND_HIDDEN_INTERFACE = 171;
        /**
         * The constant SEND_AUDIO.
         */
        public static final int SEND_AUDIO = 174;
        /**
         * The constant PLAYER_HEAD_ON_INTERFACE.
         */
        public static final int PLAYER_HEAD_ON_INTERFACE = 185;
        /**
         * The constant ENTER_NAME_DIALOGUE.
         */
        public static final int ENTER_NAME_DIALOGUE = 187;
        /**
         * The constant SEND_PRIVATE_MESSAGE.
         */
        public static final int SEND_PRIVATE_MESSAGE = 196;
        /**
         * The constant SET_INTERFACE_ANIMATION.
         */
        public static final int SET_INTERFACE_ANIMATION = 200;
        /**
         * The constant SEND_WALKABLE_INTERFACE.
         */
        public static final int SEND_WALKABLE_INTERFACE = 208;
        /**
         * The constant SEND_ADD_IGNORE.
         */
        public static final int SEND_ADD_IGNORE = 214;
        /**
         * The constant OPEN_CHATBOX_INTERFACE.
         */
        public static final int OPEN_CHATBOX_INTERFACE = 218;
        /**
         * The constant FRIENDS_LIST_STATUS.
         */
        public static final int FRIENDS_LIST_STATUS = 221;
        /**
         * The constant SET_INTERFACE_MODEL_ROTATION_ZOOM.
         */
        public static final int SET_INTERFACE_MODEL_ROTATION_ZOOM = 230;
        /**
         * The constant SEND_PLAYER_WEIGHT.
         */
        public static final int SEND_PLAYER_WEIGHT = 240;
        /**
         * The constant CONSTRUCT_MAP_REGION.
         */
        public static final int CONSTRUCT_MAP_REGION = 241;
        /**
         * The constant DISPLAY_ITEM_IN_INTERFACE.
         */
        public static final int DISPLAY_ITEM_IN_INTERFACE = 246;
        /**
         * The constant DISPLAY_INVENTORY_OVERLAY.
         */
        public static final int DISPLAY_INVENTORY_OVERLAY = 248;
        /**
         * The constant INIT_PLAYER.
         */
        public static final int INIT_PLAYER = 249;
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
         * Server message
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

        /**
         * The constant DISPLAY_HINT_ICON.
         */
        public static final int DISPLAY_HINT_ICON = 254;
    }
}
