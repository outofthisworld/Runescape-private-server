package database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;

public class Database {
    private final static MongoClient mongoClient;

    static {
        mongoClient = new MongoClient(DatabaseConfig.DB_HOST, DatabaseConfig.DB_PORT);
        Database.mongoClient.getCredentialsList().add(MongoCredential.createCredential(DatabaseConfig.DB_USER, DatabaseConfig.DB_NAME, DatabaseConfig.DB_PASS.toCharArray()));
    }


    private Database() {

    }

    public static MongoClient getClient() {
        return Database.mongoClient;
    }
}
