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

import com.mongodb.MongoClient;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private static final Logger logger = Logger.getLogger(Database.class.getName());
    private static MongoClient mongoClient;


    private Database() {

    }

    public static void init() {
        try {
            Database.mongoClient = new MongoClient(DatabaseConfig.DB_HOST, DatabaseConfig.DB_PORT);
        } catch (Exception e) {
            Database.logger.log(Level.INFO, String.format("Unable to connect to database using connection : %s %d", DatabaseConfig.DB_HOST, DatabaseConfig.DB_PORT));
            e.printStackTrace();
            Database.mongoClient.close();
            throw e;
        }
    }

    public static MongoClient getClient() {
        return Database.mongoClient;
    }
}
