/*
 Project by outofthisworld24
 All rights reserved.
 */

package database;

import com.mongodb.MongoClient;

import java.util.logging.Logger;

public class Database {
    private static final Logger logger = Logger.getLogger(Database.class.getName());
    private static MongoClient mongoClient;

    static {
        Database.mongoClient = new MongoClient(DatabaseConfig.DB_HOST, DatabaseConfig.DB_PORT);
    }

    private Database() {
    }

    public static MongoClient getClient() {
        return Database.mongoClient;
    }
}
