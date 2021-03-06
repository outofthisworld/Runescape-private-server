package world;

public final class WorldConfig {
    /**
     * Game settings /Misc settings
     */
    public static final String SERVER_NAME = "TriScape";
    public static final String WEBSITE_URL = "http://www.TriScape.org";
    public static final int WORLD_TICK_RATE_MS = 600;
    public static final int MAX_PLAYERS_IN_WORLD = 20000;
    public static final int MAX_NPCS_IN_WORLD = 20000;

    /**
     * Script settings
     */
    public static final String WORLD_SCRIPTS_DIR = "src/world/scripts/";

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
     * NpcDefinition drop percentage settings
     */
    public static final double DROP_ALWAYS = 100.0d;
    public static final double DROP_COMMON = 30.3d;
    public static final double DROP_UNCOMMON = 13.33d;
    public static final double DROP_RARE = 4.77d;
    public static final double DROP_NEAR_IMPOSSIBLE = 0.5d;
    /**
     * The app mode/server mode. Development when not in production.
     */
    public static final WorldMode APP_MODE = WorldMode.DEVELOPMENT;

    private WorldConfig() {
    }
}
