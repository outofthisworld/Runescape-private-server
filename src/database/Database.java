package database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private static final Logger logger = Logger.getLogger(Database.class.getName());
    private static MongoClient mongoClient;


    private Database() {

    }

    public static void init() {
        try {
            Database.mongoClient = new MongoClient(new ServerAddress(DatabaseConfig.DB_HOST, DatabaseConfig.DB_PORT));
        } catch (Exception e) {
            Database.logger.log(Level.SEVERE, String.format("Unable to connect to database using connection : %s %d", DatabaseConfig.DB_HOST, DatabaseConfig.DB_PORT));
            throw new ExceptionInInitializerError(e);
        }

        Database.mongoClient.getCredentialsList().add(MongoCredential.createCredential(DatabaseConfig.DB_USER, DatabaseConfig.DB_NAME, DatabaseConfig.DB_PASS.toCharArray()));
    }

    public static MongoClient getClient() {
        return Database.mongoClient;
    }
}
