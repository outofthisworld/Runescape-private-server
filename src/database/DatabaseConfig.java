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

package database;

import java.util.logging.Logger;

public class DatabaseConfig {

    public static final String ITEMS_COLLECTION = "Items";
    public static final String NPC_COLLECTION = "Npcs";
    public static final String PLAYERS_COLLECTION = "Players";
    private static final Logger logger = Logger.getLogger(DatabaseConfig.class.getName());
    public static String DB_HOST = "127.0.0.1";
    public static int DB_PORT = 27017;
    public static String DB_NAME = "Evolution";
    public static String DB_PASS = "Mongo";
    public static String DB_USER = "Mongo";

    static {

        if (System.getenv("DB_HOST") != null) {
            DatabaseConfig.DB_HOST = System.getenv("DB_HOST");
        }


        if (System.getenv("DB_PORT") != null) {
            int DB_PORT1;
            try {
                DB_PORT1 = Integer.valueOf(System.getenv("DB_PORT"));
            } catch (Exception e) {
                DB_PORT1 = 27017;
            }
            DatabaseConfig.DB_PORT = DB_PORT1;
        }


        if (System.getenv("DB_NAME") != null) {
            DatabaseConfig.DB_NAME = System.getenv("DB_NAME");
        }


        if (System.getenv("DB_USER") != null) {
            DatabaseConfig.DB_USER = System.getenv("DB_USER");
        }


        if (System.getenv("DB_PASS") != null) {
            DatabaseConfig.DB_PASS = System.getenv("DB_PASS");
        }

    }
}
