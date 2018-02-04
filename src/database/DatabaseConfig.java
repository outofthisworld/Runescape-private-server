package database;

import java.util.logging.Logger;

public class DatabaseConfig {

    public static final String DB_HOST;
    public static final int DB_PORT;
    public static final String DB_NAME;
    public static final String DB_PASS;
    public static final String DB_USER;
    private static final Logger logger = Logger.getLogger(DatabaseConfig.class.getName());

    static {
        if (System.getenv("DB_URL") == null) {
            System.getenv().put("DB_HOST", "localhost");
        }

        DB_HOST = System.getenv("DB_URL");


        if (System.getenv("DB_PORT") == null) {
            System.getenv().put("DB_PORT", "27017");
        }

        int DB_PORT1;
        try {
            DB_PORT1 = Integer.valueOf(System.getenv("DB_PORT"));
        } catch (Exception e) {
            DB_PORT1 = 27017;
        }

        DB_PORT = DB_PORT1;
        if (System.getenv("DB_NAME") == null) {
            System.getenv().put("DB_NAME", "evolution");
        }

        DB_NAME = System.getenv("DB_NAME");


        if (System.getenv("DB_USER") == null) {
            System.getenv().put("DB_USER", "mongo");
        }

        DB_USER = System.getenv("DB_USER");

        if (System.getenv("DB_PASS") == null) {
            System.getenv().put("DB_PASS", "mongo");
        }

        DB_PASS = System.getenv("DB_PASS");

    }
}
