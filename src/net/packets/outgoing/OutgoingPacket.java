package net.packets.outgoing;

import net.Client;
import net.buffers.OutputBuffer;

/**
 * The type Outgoing packet.
 */
public class OutgoingPacket {

    /**
     * The Output buffer.
     */
    protected final OutputBuffer outputBuffer;

    /**
     * Instantiates a new Outgoing packet.
     *
     * @param out the out
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
         * Resets all animations for players and npcs in the surrounding area.
         */
        public static final int RESET_ANIMATIONS = 1;
        /**
         * The constant DISPLAY_STATIONARY_ANIMATION.
         */
        public static final int DISPLAY_STATIONARY_ANIMATION = 4;
        /**
         * The constant FLASH_SIDEBAR_ICON.
         * Description
         * This packet causes a sidebar icon to start flashing.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Byte Special S	The sidebar ID.
         * <p>
         * Values
         * The below are the different values for this packet.
         * <p>
         * Value	Icon
         * 0	Attack type
         * -1	Stats
         * -2	Quests
         * -3	Inventory
         * -4	Wearing
         * -5	Prayer
         * -6	Magic
         * -7	EMPTY
         * -8	Friends list
         * -9	Ignore list
         * -10	Log out
         * -11	Settings
         * -12	Emotes
         * -13	Music
         */
        public static final int FLASH_SIDEBAR_ICON = 24;

        /**
         * The constant INPUT_AMOUNT.
         * Displays the "Input amount" interface.
         * Description
         * Sending the packet to the client will make the client open up the "Input amount" interface over the chatbox
         * for things such as Buy X and Bank X.
         */
        public static final int INPUT_AMOUNT = 27;
        /**
         * The constant UPDATE_INVENTORY_ITEM.
         */
        public static final int UPDATE_INVENTORY_ITEM = 34;
        /**
         * The constant CAMERA_SHAKE.
         * Description
         * Begins camera oscillation, which is implemented using a configurable sinusoidal oscillator to offset a
         * specific degree of freedom.
         * <p>
         * Packet Structure
         * Data type	Description
         * Byte	Parameter (camera X, Z, Y, yaw, pitch)
         * Byte	Jitter - for randomization
         * Byte	Amplitude
         * Byte	Frequency (scaled by 100)
         * Other Information
         * The oscillate event enables the client to oscillate one of 5 of it's position parameters, i.e. corresponding
         * to the camera's degrees of freedom; parameters 0, 1, and 2 refer to the location of the camera, while 3 and 4
         * deal with the camera's orientation. Together, these enable complex effects involving manipulation of the
         * camera position to give rise to simulated earth-quakes and camera shock.
         * <p>
         * Parameter	Description
         * 0	Camera location along world X axis (a horizontal axis, aligned with map grid X)
         * 1	Camera location along world Z axis (vertical axis)
         * 2	Camera location along world Y axis (a horizontal axis, aligned with map grid Y)
         * 3	Camera orientation in world X plane w.r.t. world Z axis, i.e. yaw
         * 4	Camera orientation in world Z plane w.r.t. world X axis, i.e. pitch
         * Note there is no built-in way to manipulate camera roll, as this is not one of the camera's degrees of
         * freedom.
         * What it's doing
         * Every time the world is rendered, each camera parameter that is enabled for oscillation is offset by a value
         * computed as follows:
         * <p>
         * Calculation	Formula
         * Delta	(int) ((Math.random() * (double) (jitter * 2 + 1) - (double) jitter) + Math.sin((double) phase *
         * ((double) frequency / 100D)) * (double) amplitude);
         * Each parameter's phase accumulator (phase) is incremented by 1 each logic update.
         * Parameter
         * The offset itself is detailed as follows for each parameter:
         * <p>
         * Parameter	Action
         * 0	camera_x += delta
         * 1	camera_z += delta
         * 2	camera_y += delta
         * 3	camera_yaw = camera_yaw + delta & 0x7ff;
         * 4	camera_pitch += delta
         * Note that the camera's yaw is corrected modulo 0x7ff, or 2048, which is equivalent to 2 radians in Jagex's
         * binary angle system. This is not done to the camera pitch, which is instead clamped (see below).
         * Note
         * For oscillating the camera pitch, clamping is done to ensure the angle not out of bounds:
         * <p>
         * if (camera_pitch < 128) then camera_pitch = 128
         * if (camera_pitch > 383) then camera_pitch = 383
         * This is do to Jagex restricting the possible range of orientations the camera may take.
         */
        public static final int CAMERA_SHAKE = 35;

        /**
         * The constant FORCE_CLIENT_SETTING.
         * The client stores various user settings in an array, the default values are also stored in another array.
         * This packet changes the default value for a setting and its current value to the one given.
         * <p>
         * Packet Structure
         * Data type	Description
         * Short Little Endian	Setting ID number.
         * Byte	New value (and default value) for the setting.
         * Other Information
         * Opcode 87 (length 6) is extremely similar in structure, but the new value is received as an Middle Endian
         * Small Int. This suggests its for use with bigger setting values.
         */
        public static final int FORCE_CLIENT_SETTING = 36;

        /**
         * The opcode the display an item on the ground.
         */
        public static final int DISPLAY_GROUND_ITEM = 44;
        /**
         * The constant ADD_FRIEND.
         * Sends friend data to the client Attempts to update player node, if player isn't in the friends list and there
         * is space, the player is added to the friend list.
         * <p>
         * Packet Structure
         * Data type	Description
         * Long	The player name
         * Byte	The world (10 = "online" for most clients, 0 = logged out)
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
         * Description
         * Sending this packet to the client will make the client show the player if they are in a multi-combat zone.
         * <p>
         * States:
         * <p>
         * 0 - Not in a multi-combat zone, no crossbones in bottom-right.
         * 1 - In a multi-combat zone, crossbones in bottom-right.
         * Packet Structure
         * Data Type	Description
         * byte	The state.
         */
        public static final int SHOW_MULTI_COMBAT = 61;
        /**
         * The constant RESET_BUTTON_STATE.
         * This packet resets the states for all user settings (inc. buttons).
         */
        public static final int RESET_BUTTON_STATE = 68;
        /**
         * The constant SET_INTERFACE_OFFSET.
         * Interface offset
         * Description
         * Sets the offset for drawing of an interface.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Short	The X offset
         * Little Endian Short	The Y offset
         * Little Endian Short	The interface ID
         */
        public static final int SET_INTERFACE_OFFSET = 70;
        /**
         * The constant CLEAR_INTERFACE_INVENTORY.
         * Description
         * This packet creates a loop through a given inventory interface id and sets the item ids to negative one and
         * the item stacks to zero.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Little Endian Short	The interface ID.
         */
        public static final int CLEAR_INTERFACE_INVENTORY = 72;
        /**
         * The constant LOAD_MAP_REGION.
         * <p>
         * Description
         * Makes the client load the specified map region.
         * <p>
         * Packet Structure
         * Data type	Description
         * Short Special A	Region X coordinate (absolute X / 8) plus 6.
         * Short	Region Y coordinate (absolute Y / 8) plus 6.
         * Other Information
         * There are various loops/arrays within the map region loading functionality of the client which have been
         * misunderstood by many.
         * <p>
         * Loop type	Description
         * 104 x 104	Maximum size of the client's load area
         * 8 x 8	Load blocks to speed up loading NPCs, Items and Objects
         * 13 x 13	Number of load blocks to load
         */
        public static final int LOAD_MAP_REGION = 73;
        /**
         * The constant PLAY_SONG.
         * <p>
         * Description
         * Sending this packet to the client will cause the client to start playing a song.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Little Endian Short	The song ID.
         */
        public static final int PLAY_SONG = 74;
        /**
         * The constant NPC_HEAD_ON_INTERFACE.
         * Description
         * Places the head of an NPC on an interface
         * <p>
         * Packet Structure
         * Data Type	Description
         * Little Endian Short Special A	The NPC ID
         * Little Endian Short Special A	The 'slot' ID for where you wish to place the head
         */
        public static final int NPC_HEAD_ON_INTERFACE = 75;
        /**
         * The constant RESET_PLAYER_DESTINATION.
         * Description
         * Sending the packet to the client will make the client reset the player's destination and effectively stop
         * them from walking.
         */
        public static final int RESET_PLAYER_DESTINATION = 78;
        /**
         * The constant SET_SCROLLBAR_POSITION.
         * <p>
         * Description
         * This packet sets the scrollbar position of an interface.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Little Endian Short	The interface ID.
         * Short Special A	The position of the scrollbar.
         */
        public static final int SET_SCROLLBAR_POSITION = 79;
        /**
         * The constant BEGIN_PLAYER_UPDATING.
         * This packet begins the player updating.
         */
        public static final int BEGIN_PLAYER_UPDATING = 81;
        /**
         * The constant SET_MINIMAP_STATE.
         * Description
         * This packet sets the Minimaps state
         * <p>
         * States:
         * <p>
         * 0 - Active: Clickable and viewable
         * 1 - Locked: viewable but not clickable
         * 2 - Blacked-out: Minimap is replaced with black background
         * Packet Structure
         * Data Type	Description
         * byte	The state.
         */
        public static final int SET_MINIMAP_STATE = 99;
        /**
         * The constant REMOVE_OBJECT.
         * Description
         * This packet requests the client to remove an object.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Negated byte	object type << 2 + object rotation & 3
         * byte	0
         */
        public static final int REMOVE_OBJECT = 101;
        /**
         * The constant ADD_PLAYER_OPTION.
         * Packet Structure
         * Data Type	Description
         * Byte C	The option position.
         * Byte A	Flag
         * String	Action text.
         */
        public static final int ADD_PLAYER_OPTION = 104;
        /**
         * The constant INTEFACE_OVER_TAB.
         * Description
         * This packet draws an interface over the tab area.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Byte Special C	Interface ID
         */
        public static final int INTEFACE_OVER_TAB = 106;
        /**
         * The constant RESET_CAMERA_POSITION.
         */
        public static final int RESET_CAMERA_POSITION = 107;
        /**
         * The constant SEND_RUN_ENERGY_LEVEL.
         * Description
         * Sends how much run energy the player currently has.
         * <p>
         * Packet Structure
         * Data type	Description
         * Byte	The energy level.
         */
        public static final int SEND_RUN_ENERGY_LEVEL = 110;
        /**
         * The constant SEND_SYSTEM_UPDATE
         * Description
         * A timer showing how many seconds until a 'System Update' will appear in the lower left hand corner of the
         * game screen. After the timer reaches 0 all players are disconnected and are unable to log in again until
         * server is restarted. Players connecting will receive a message stating, "The server is being updated. Please
         * wait 1 minute and try again." (unless stated otherwise).
         * <p>
         * Packet Structure
         * Data type	Description
         * Little Endian Short	Time until an update.
         * .
         */
        public static final int SEND_SYSTEM_UPDATE = 114;
        /**
         * The constant CREATE_PROJECTILE.
         * Description
         * Creates a projectile.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Byte	Position offset
         * Byte	Second X offset
         * Byte	Second Y offset
         * Short	Target
         * Little Endian Short	Graphic ID
         * Byte	Starting height
         * Byte	Ending height
         * Little Endian Short	Starting time
         * Little Endian Short	Speed
         * Byte	Initial slope
         * Byte	Initial distance from source
         */
        public static final int CREATE_PROJECTILE = 117;
        /**
         * The constant QUEUE_SONG.
         * Description
         * This packet queue's a song to be played next. The client then proceeds to request the queued song using the
         * ondemand protocol.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Little Endian Short Special A	The id of the next song.
         * Short Special A The id of the previous song
         */
        public static final int QUEUE_SONG = 121;
        /**
         * The constant INTERFACE_COLOR.
         * Description
         * This packet changes the color of an interface that is text.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Little Endian Short Special A	The interface ID.
         * Little Endian Short Special A	The color.
         * Information
         * You use this packet to change the color of text in an interface.
         * <p>
         * Color	Code
         * Green	0x3366
         * Yellow	0x33FF66
         * Red	0x6000
         */
        public static final int INTERFACE_COLOR = 122;
        /**
         * The constant SPAWN_OBJECT.
         * Description
         * This packet requests the client to spawn an object.
         * <p>
         * Packet Structure
         * Data Type	Description
         * subtrahend byte	0
         * Little endian byte	ObjectID
         * subtrahend byte	object type << 2 + object rotation & 3
         */
        public static final int SPAWN_OBJECT = 151;
        /**
         * The constant SEND_HIDDEN_INTERFACE.
         * Description
         * Sets an interface to be hidden until hovered over.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Byte	Hidden until hovered
         * Short	Interface Id
         */
        public static final int SEND_HIDDEN_INTERFACE = 171;
        /**
         * The constant SEND_AUDIO.
         * Description
         * Sets what audio/sound is to play at a certain moment.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Word	The sound id.
         * Byte	The volume.
         * Word	The delay.
         */
        public static final int SEND_AUDIO = 174;
        /**
         * Description
         * This packet sends a players head to an interface
         * <p>
         * Packet Structure
         * Data Type	Description
         * Little Endian Short A
         * The constant PLAYER_HEAD_ON_INTERFACE.
         */
        public static final int PLAYER_HEAD_ON_INTERFACE = 185;
        /**
         * The constant ENTER_NAME_DIALOGUE.
         * Description
         * Sending the packet to the client will make the client open up the "Enter name" interface for things such as
         * friend-adding.
         */
        public static final int ENTER_NAME_DIALOGUE = 187;

        /**
         * The constant SEND_PRIVATE_MESSAGE.
         * Send Private Message
         * Send Message Sends Message to another user.
         * Opcode	196
         * Type	VARIABLE_SHORT
         * Length	N/A
         * Description
         * Sending a private message to another user on the server.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Long	Player name.
         * Int	?
         * Byte	Player rights./
         * public static final int SEND_PRIVATE_MESSAGE = 196;
         * /**
         */
        public static final int SEND_PRIVATE_MESSAGE = 196;
        /**
         * The constant SET_INTERFACE_ANIMATION.
         * Description
         * Sets an interface's model animation.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Short	The interface ID
         * Short	The animation ID
         */
        public static final int SET_INTERFACE_ANIMATION = 200;
        /**
         * The constant SEND_WALKABLE_INTERFACE.
         * Description
         * This packet displays an interface in walkable mode.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Little Endian Short interface id
         */
        public static final int SEND_WALKABLE_INTERFACE = 208;
        /**
         * The constant SEND_ADD_IGNORE.
         * Description
         * Sends the IDs of all the users that this player has in their ignore.
         * <p>
         * This packet has a slightly different structure than the other packets.
         * <p>
         * int entries = packetSize / 8;
         * for (int i = 0; i < entries; i++) {
         * ignoreList[i] = stream.readLong();
         * }
         * By looking at the rest of the 317 protocol, there doesn't seem to be a way to change the list dynamically. It
         * seems as though that whenever the player decides to add or remove a player from their list, it must send all
         * the values again.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Long	The Unique Identifier of the player(s) (possibly determined by their username).
         */
        public static final int SEND_ADD_IGNORE = 214;
        /**
         * The constant OPEN_CHATBOX_INTERFACE.
         * Description
         * Sending this packet to the client will cause the client to open an interface over the chatbox.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Little Endian Short Special A
         */
        public static final int OPEN_CHATBOX_INTERFACE = 218;
        /**
         * The constant FRIENDS_LIST_STATUS.
         * Description
         * This packet sends the first list load status.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Byte	The status of the friends list.
         * Values
         * The below are the different values for this packet.
         * <p>
         * Value	Response
         * 0	Loading
         * 1	Connecting
         * 2	Loaded
         */
        public static final int FRIENDS_LIST_STATUS = 221;
        /**
         * The constant SET_INTERFACE_MODEL_ROTATION_ZOOM.
         * Description
         * Changes the zoom and rotation of the interface id's media given.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Word Special A	The zoom.
         * Word	The interface id.
         * Word	The rotation1.
         * Little Endian Word Special A The rotation2.
         */
        public static final int SET_INTERFACE_MODEL_ROTATION_ZOOM = 230;
        /**
         * The constant SEND_PLAYER_WEIGHT.
         * <p>
         * Weight
         * Description
         * Sends how much weight of equipment the player is wearing (e.g. Rune plate-body is 9.04kg).
         * <p>
         * Packet Structure
         * Data type	Description
         * Short	The amount of weight.
         */
        public static final int SEND_PLAYER_WEIGHT = 240;
        /**
         * The constant CONSTRUCT_MAP_REGION.
         * <p>
         * Description
         * The construct map region packet sends a dynamic map region that is constructed by using groups of 8*8 tiles.
         * It is generally used for instanced areas, such as fight caves, and in later revisions, player owned houses.
         * <p>
         * Packet Structure
         * Data type	Description
         * Short Special A	The region Y coordinate (absolute Y coordinate / 8), plus 6.
         * Bit block	See below.
         * Short	The region X coordinate (absolute X coordinate / 8), plus 6.
         * Bit block
         * The bit block actually contains the 'palette' of map regions to make up the new region.
         * <p>
         * There is a loop, like this, used to construct it:
         * <p>
         * for(int z = 0; z < 4; z++) {
         * for(int x = 0; x < 13; x++) {
         * for(int y = 0; y < 13; y++) {
         * // data for this region
         * }
         * }
         * }
         * The individual format in each iteration of the loop is:
         * <p>
         * 1 bit - set to 0 to indicate to display nothing, 1 to display a region
         * 26 bits - if the flag above is set to 1 - region x << 14 | region y << 3
         */
        public static final int CONSTRUCT_MAP_REGION = 241;
        /**
         * The constant DISPLAY_ITEM_IN_INTERFACE.
         * <p>
         * Description
         * Displays an item model inside an interface.
         * <p>
         * Packet Structure
         * Data type	Description
         * Little Endian Short	Interface ID.
         * Short	The item's model zoom.
         * Short	The item ID.
         */
        public static final int DISPLAY_ITEM_IN_INTERFACE = 246;
        /**
         * The constant DISPLAY_INVENTORY_OVERLAY.
         * Description
         * This packet overlays an interface in the inventory area. This is used in trading and staking.
         * <p>
         * Example
         * sendFrame248(3323, 3321);
         * That will set the open interface to interface 3323, which is the trade interface, with the inventory overlay
         * interface as 3321, which is an inventory type interface with offer actions.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Short Special A	The interface to open.
         * Short	The interface to overlay the inventory area.
         */
        public static final int DISPLAY_INVENTORY_OVERLAY = 248;
        /**
         * The constant INIT_PLAYER.
         * Description
         * Sends the player membership flag and player list index.
         * <p>
         * Packet Structure
         * Data type	Description
         * Byte Special A	Membership flag (1 = member, 0 = free).
         * Little Endian Short Special A	Player list index.
         */
        public static final int INIT_PLAYER = 249;
        /**
         * Assigns an interface to one of the menu tabs in-game.
         * Description
         * This packet assigns an interface to one of the tabs in the game sidebar.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Short	The sidebar ID.
         * Byte Special A	The interface ID.
         * Values
         * The below are the different values for this packet.
         * <p>
         * Value	Icon	Norm. ID
         * 0	Attack type	2433
         * 1	Stats	3917
         * 2	Quests	638
         * 3	Inventory	3213
         * 4	Wearing	1644
         * 5	Prayer	5608
         * 6	Magic	1151
         * 7	EMPTY
         * 8	Friends list	5065
         * 9	Ignore list	5715
         * 10	Log out	2449
         * 11	Settings	4445
         * 12	Emotes	147
         * 13	Music	6299
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
         * Description
         * Displays a normal non-walkable interface with the given id.
         * <p>
         * Packet Structure
         * Data type	Description
         * Short	The interface id.
         */
        public static final int SEND_INTERFACE = 97;
        /**
         * The logout packet
         */
        public static final int LOGOUT = 109;
        /**
         * Attaches text to an interface, e.g when hovering skills.
         * <p>
         * Description
         * Sets the text for the specific interface.
         * <p>
         * Packet Structure
         * Data Type	Description
         * RS_String	The new text for the interface
         * Short A	The interface id
         * Information
         * I do not recommend you use this to change the text color. I refer you to Interface Color for the proper way
         * to do so.
         */
        public static final int INTERFACE_TEXT = 126;
        /**
         * Sends new level and exp of skills to the client.
         * Description
         * This packet changes the experience and level of a given skill id.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Byte	The skill ID.
         * Middle-Endian Small Integer	The skill experience.
         * Byte	The skill level.
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
         * Description
         * Sends a server side message (e.g. 'Welcome to RuneScape'), or trade/duel/challenge request.
         * <p>
         * The format for sending such requests is: '[player][request]'. Where request type can be one of the following:
         * ':duelreq:', ':chalreq:' or ':tradereq:'. An example for trading the player 'mopar' would be:
         * 'mopar:tradereq:'.
         * <p>
         * Packet Structure
         * Data type	Description
         * RS String	The message.
         */
        public static final int SEND_MESSAGE = 253;
        /**
         * Updates the players XY coordinates.
         */
        public static final int OPEN_WELCOME_INTERFACE = 176;
        /**
         * Description
         * This packet sends the chat privacy settings.
         * <p>
         * Packet Structure
         * Data Type	Description
         * Byte	Public chat setting.
         * Byte	Private chat setting.
         * Byte	Trade setting.
         */
        public static final int CHAT_PRIVACY_SETTINGS = 206;
        /**
         * Closes all open interfaces
         */
        public static final int CLOSE_ALL_INTERFACES = 219;

        /**
         * The constant DISPLAY_HINT_ICON.
         * Description
         * Displays a hint icon.
         * <p>
         * Packet Structure
         * Data type	Description
         * Byte	The Icon type
         * if type == 1
         * Data type	Description
         * Short	Icon NPC target
         * if type >= 2 && type <= 6
         * Data type	Description
         * Short	Icon X
         * Short	Icon Y
         * Byte	Icon draw height
         * if type == 10
         * Data type	Description
         * Short	Icon player target
         */
        public static final int DISPLAY_HINT_ICON = 254;
    }
}
