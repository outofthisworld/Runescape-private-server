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

package world;

public final class WorldConfig {
    /**
     * Misc settings
     */
    public static final int WORLD_TICK_RATE_MS = 600;
    public static final int MAX_PLAYERS_IN_WORLD = 20000;

    /**
     * Virtual world settings.
     */
    public static final int MAX_VIRTUAL_WORLDS = 10;
    public static final int NUM_VIRTUAL_WORLDS = 1;

    /**
     * Player settings
     */
    public static final int PLAYER_START_X = 2606;
    public static final int PLAYER_START_Y = 3093;
    public static final int PLAYER_START_Z = 0;

    /**
        Npc drop percentage settings
     */
    public static final double DROP_ALWAYS = 100.0d;
    public static final double DROP_UNCOMMON = 30.0d;
    public static final double DROP_COMMON = 70.0d;
    public static final double DROP_RARE = 93.33d;
    public static final double DROP_NEAR_IMPOSSIBLE = 97.9;

    public static final boolean DEBUG = false;

    private WorldConfig() { }
}
