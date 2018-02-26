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

package net.packets.incoming;

import net.buffers.InputBuffer;
import net.impl.session.Client;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public abstract class IncomingPacket {

    private static final HashMap<Integer, IncomingPacket> packets = new HashMap<>(255);


    private static final IncomingPacket[] incomingPackets = new IncomingPacket[]{
            new BankPacket(),
            new CameraMovementPacket(),
            new ChatPrivacyPacket(),
            new DesignScreenPacket(),
            new ForwardDialoguePacket(),
            new GroundItemPacket(),
            new IdlePacket(),
            new InteractInterfacePacket(),
            new InteractItemPacket(),
            new InteractNpcPacket(),
            new InteractObjectPacket(),
            new InteractPlayerPacket(),
            new ItemOnPacket(),
            new MagicOnPacket(),
            new MouseClickPacket(),
            new PlayerCommandPacket(),
            new PlayerReportingPacket(),
            new PlayerRequestPacket(),
            new PrivateChatPacket(),
            new RegionPacket(),
            new UnknownPacket(),
            new WalkingPacket(),
            new WindowFocusChangedPacket()
    };
    private static final int packetSizes[] = {0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
            0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
            0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
            0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
            2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
            0, 0, 0, 12, 0, 0, 0, 0, 8, 0, // 50
            0, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
            6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
            0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
            0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
            0, 13, 0, -1, 0, 0, 0, 0, 0, 0,// 100
            0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
            1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
            0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
            0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
            0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
            0, 0, 0, 0, -1, -1, 0, 0, 0, 0,// 160
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
            0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
            0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
            2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
            4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
            0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
            1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
            0, 4, 0, 0, 0, 0, -1, 0, -1, 4,// 240
            0, 0, 6, 6, 0, 0, 0 // 250
    };

    static {
        for (int i = 0; i < incomingPackets.length; i++) {
            IncomingPacket p = incomingPackets[i];
            Set<Integer> packetOpcodes = p.getOpcodes();
            if (packetOpcodes == null) continue;
            packetOpcodes.forEach(op -> packets.put(op, p));
        }
    }

    public IncomingPacket() {
    }

    public static Optional<IncomingPacket> getForId(int id) {
        return Optional.ofNullable(packets.get(id));
    }

    public static int getPacketSizeForId(int id) {
        if (id < 0 || id >= IncomingPacket.packetSizes.length) {
            return 0;
        }
        return IncomingPacket.packetSizes[id];
    }

    public abstract void handle(Client c, int packetOpcode, InputBuffer in) throws Exception;

    public abstract boolean handlesOpcode(int opcode);

    public abstract Set<Integer> getOpcodes();


    public static class Opcodes {

        /**
         * Sent went the player leaves the client window.
         * Length: 1
         */
        public static final int WINDOW_FOCUS_CHANGE = 3;
        /**
         * Send when a player continues a dialogue.
         * Length: 2
         */
        public static final int FORWARD_DIALOGUE = 40;
        /**
         * Sent when the player moves the camera.
         * Length:4
         */
        public static final int CAMERA_MOVEMENT = 86;
        /**
         * Sent when a player changes their privacy options (i.e. public chat).
         * Length:3
         */
        public static final int PRIVACY_OPTIONS = 95;
        /**
         * Sent when a player is choosing their character design options.
         * Length: 13
         */
        public static final int DESIGN_SCREEN = 101;
        /**
         * Sent when the player enters a command in the chat box (e.g. "::command")
         * Length: VARIABLE_BYTE
         */
        public static final int PLAYER_COMMAND = 103;

        /**
         * Sent when the player enters a chat message
         * Length: Var
         */
        public static final int CHAT_PACKET = 4;

        /**
         * Sent when the player clicks somewhere on the game screen.
         * Length: 4
         */
        public static final int MOUSE_CLICK = 241;
        /**
         * Unk
         * Length: 15
         */
        public static final int UNKNOWN = 246;
        /********************* START ITEM ON PACKET************************/
        /**
         * Sent when the player uses an item on another player.
         * Length: 8
         */
        public static final int ITEM_ON_PLAYER = 14;
        /**
         * Sent when a player uses an item with another item.
         * Length: 4
         */
        public static final int ITEM_ON_ITEM = 53;
        /**
         * Sent when a player uses an item on an NPC.
         * Length: 4
         */
        public static final int ITEM_ON_NPC = 57;
        /**
         * Sent when a player uses an item with another item on the floor.
         * Length: 10
         */
        public static final int ITEM_ON_FLOOR = 25;
        /**
         * Sent when a a player uses an item on an object.
         * Length: 12
         */
        public static final int ITEM_ON_OBJECT = 192;
        /********************* END ITEM ON PACKET************************/
        /********************* START NPC ACTION PACKET************************/
        /**
         * Sent when a player clicks first option of an NPC, such as "Talk."
         * Actions on NPCS, when they click an option.
         * Length: 2
         */
        public static final int NPC_ACTION_1 = 155;
        public static final int NPC_ACTION_2 = 17;
        public static final int NPC_ACTION_4 = 18;
        public static final int NPC_ACTION_3 = 21;
        /**
         * Sent to validate npc option 4. (client action 478).
         * Length:1
         */
        public static final int NPC_OPTION_4_ANTI_CHEAT = 85;
        /**
         * Send to validate npc option 3 (client action 965)
         * Length: 1
         **/
        public static final int NPC_OPTION_3_ANTI_CHEAT = 152;
        /**
         * Validates NPC option 2
         * Length: 1
         */
        public static final int NPC_OPTION_2_ANTI_CHEAT = 230;
        /**
         * Sent when a player attacks an NPC.
         * Length: 2
         */
        public static final int ATTACK_NPC = 72;
        /********************* END NPC ACTION PACKET************************/
        /********************* START MAGIC ON PACKET************************/
        /**
         * Sent when a player uses magic on an object.
         * Length: 4
         */
        public static final int MAGIC_ON_OBJECT = 35;
        /**
         * Sent when a player uses magic on an npc.
         * Length: 4
         */
        public static final int MAGIC_ON_NPC = 131;
        /**
         * Send when a player uses a spell on a ground item.
         * Length: 8
         */
        public static final int MAGIC_ON_GROUND_ITEM = 181;
        /**
         * Sent when a player casts magic on the items in their inventory.
         * Length: 8
         */
        public static final int MAGIC_ON_ITEM = 237;
        /**
         * Sent when a player attempts to cast magic on another player.
         * Length: 4
         */
        public static final int MAGIC_ON_PLAYER = 249;
        /********************* END MAGIC ON PACKET************************/
        /********************* START BANK PACKET************************/
        /**
         * Sent when a player banks all of a certain item that they have in their inventory.
         * Length: 6
         */
        public static final int BANK_ALL_ITEMS = 129;
        /**
         * Sent when a player equips an item.
         * Length: 6
         */
        public static final int BANK_10 = 43;
        /**
         * Sent when a player banks 5 of a certain item.
         * Length: 6
         */
        public static final int BANK_5 = 117;
        /**
         * Sent when a player requests to bank an X amount of items
         * Length: 6
         */
        public static final int BANK_X_ITEMS_1 = 135;
        /**
         * Sent when a player enters an X amount of items they want to bank.
         * Length: 4
         */
        public static final int BANK_X_ITEMS_2 = 208;
        /**
         * Validates banking options
         * Length: 2
         */
        public static final int VALIDATE_BANKING_ANTI_CHEAT = 200;
        /********************* START BANK PACKET************************/

        /********************* START PLAYER WALK PACKET************************/
        /**
         * Sent when the player walks regularly.
         * Length: VARIABLE_BYTE
         */
        public static final int PLAYER_WALK = 164;
        /**
         * Sent when the player walks using the map. Has 14 additional (assumed to be anticheat) bytes added to the end
         * of it that are ignored.
         * Length: VARIABLE_BYTE
         */
        public static final int MAP_WALK = 248;
        /**
         * Sent when the player should walk somewhere according to a certain action performed, such as clicking an
         * object.
         * Length: var
         */
        public static final int WALK_ON = 98;
        /**
         * Send to validate walking.
         * Length: 4
         */
        public static final int WALKING_ANTI_CHEAT = 36;
        /********************* END PLAYER WALK PACKET************************/

        /**
         * This is for interacting with objects in the world
         * <p>
         * Sent when the player clicks the first option of an object, such as "Cut" for trees.
         * Sent when the player clicks the second option available for an object.
         * Sent when the player clicks the third action available for an object
         * Length: 6
         */
        public static final int OBJECT_ACTION_1 = 132;
        public static final int OBJECT_ACTION_2 = 252;
        public static final int OBJECT_ACTION_3 = 70;

        /**
         * Sent when a player uses the 4th option of an object.
         * Length: 6
         */
        public static final int OBJECT_OPTION_4 = 228;
        /**
         * Send when a player uses the 2nd option of an object
         * Length: 6
         */
        public static final int OBJECT_OPTION_2 = 234;

        /**
         * Validates clicking object option 4
         * Length: 4
         */
        public static final int OBJECT_OPTION_4_ANTI_CHEAT = 183;


        /********************* START PLAYER OPTION PACKET************************/
        /**
         * Sent when a player follows another player.
         * Length: 2
         */
        public static final int FOLLOW_PLAYER = 39; //could be 139
        /**
         * Sent when a player selects the attack option on another player..
         * Length: 2
         */
        public static final int ATTACK_PLAYER = 73;
        /**
         * Send with client action 561, 6 has to do with player option 1
         * Length: 0
         **/
        public static final int PLAYER_OPTION_1_ANTI_CHEAT = 136;
        /**
         * Validates player option 2
         * Length: 1
         */
        public static final int PLAYER_OPTION_2_ANTI_CHEAT = 189;
        /********************* END PLAYER OPTION PACKET************************/
        /********************* START ITEM ACTION PACKET************************/
        /**
         * SlotItem actions
         * Sent when the player clicks the first option of an item, such as "Bury" for bones.
         * <p>
         * Length:6
         */
        public static final int ITEM_ACTION_1 = 122;
        /**
         * Sent when a player uses an item. This is an alternate item option.
         * This packet is sent when a player clicks the alternate second option of an item.
         * Length: 1
         */
        public static final int ALTERNATE_ITEM_OPTION = 16;
        /**
         * SlotItem actions
         * Sent when the player clicks the first option of an item, such as "Bury" for bones.
         * <p>
         * Length:6
         */
        public static final int ITEM_ACTION_3 = 75;
        /**
         * Sent when a player equips an item.
         * Length: 6
         */
        public static final int EQUIP_ITEM = 41;
        /**
         * Sent when a player unequips an item.
         * Length: 6
         **/
        public static final int UNEQUIP_ITEM = 145;
        /**
         * Sent when a player moves an item from one slot to another.
         * Length: 7
         */
        public static final int MOVE_ITEM = 214;
        /**
         * Sent when a player wants to drop an item onto the ground.
         * Length:6
         */
        public static final int DROP_ITEM_GROUND = 87;
        /**
         * Sent when a player attempts to light logs on fire.
         * Length:6
         */
        public static final int LIGHT_ITEM = 79;
        /********************* END ITEM ACTION PACKET************************/
        /********************* START PRIVATE CHAT PACKET ***********************/
        /**
         * Sent when a player removes a player from their ignore list.
         * Length: 8
         */
        public static final int REMOVE_IGNORE = 74;
        /**
         * Sent when a player adds a player to their ignore list.
         * Length: 8
         */
        public static final int ADD_IGNORE = 133;
        /**
         * Sent when a player sends a private message to another player.
         * Length: VARIABLE BYTE
         */
        public static final int PRIVATE_MESSAGE = 126;
        /**
         * Sent when a player adds a friend to their friend list.
         * Length: 8
         */
        public static final int ADD_FRIEND = 188;
        /**
         * Sent when a player removes a friend from their friend list.
         * Length: 8
         */
        public static final int REMOVE_FRIEND = 215;
        /********************* END PRIVATE CHAT PACKET ***********************/
        /********************* START REPORT PACKET ***********************/
        /**
         * Sent when a player reports another player.
         * Length: 8
         */
        public static final int REPORT_PLAYER = 218;
        /**
         * Sent when a moderator or administrator selects the second option of a player.
         * Length: 2
         */
        public static final int REPORT_ABUSE = 153;
        /**
         * Sent when a players account is flagged.
         * Length: 1
         */
        public static final int FLAG_PLAYER = 45;
        /********************* END REPORT PACKET ***********************/
        /********************* PLAYER REQUEST PACKET ***********************/
        /**
         * Sent when a player accepts another players duel request.
         * Length: 2
         */
        public static final int ACCEPT_CHALLENGE = 128;
        /**
         * Sent when a player Requests a trade from another player. (e.g. "Sending Trade Request...")
         * Length: 2
         **/
        public static final int TRADE_REQUEST = 139; // could be 39
        /*********************  END PLAYER REQUEST PACKET ***********************/

        /*********************  INTERFACE OPTION PACKET ***********************/
        /**
         * Sent while typing onto an interface.
         * Length: 1
         */
        public static final int TYPING_INTO_INTERFACE = 60;
        /**
         * Sent when a player presses the close, exit or cancel button on an interface.
         * Length: 0
         */
        public static final int CLOSE_INTERFACE = 130;
        /**
         * Sent when a player clicks an in-game button.
         * Length: 2
         */
        public static final int INTERFACE_BUTTON_CLICK = 185;

        /*********************  END INTERFACE OPTION PACKET ***********************/
        /*********************  GROUND ITEM PACKET ***********************/
        /**
         * Sent when the player picks up an item from the ground.
         * Length: 6
         */
        public static final int PICKUP_GROUND_ITEM = 236;
        /**
         * Sent when the player clicks the first option for a ground item (I.E. 'Light Logs')
         * Length: 6
         */
        public static final int GROUND_ITEM_ACTION_1 = 253;
        /*********************  END GROUND ITEM PACKET ***********************/
        /*********************  IDLE PACKETS ***********************/
        /**
         * Sent when the player has no actions for this cycle.
         * Length: 0
         */
        public static final int IDLE = 0;
        /**
         * Sent when the player has become idle and should be logged out.
         * Length: 0
         */
        public static final int IDLE_LOGOUT = 202;
        /*********************  END IDLE PACKETS ***********************/
        /*********************  REGION PACKETS ***********************/

        /**
         * Sent when the client finishes loading a map region.
         * Length: 0
         */
        public static final int REGION_LOADED = 121;
        /**
         * Sent when a player enters a new map region.
         * Length: 0
         */
        public static final int REGION_CHANGE = 210;

        /********************* END REGION PACKETS ***********************/
    }
}

