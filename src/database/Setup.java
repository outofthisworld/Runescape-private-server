package database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import world.entity.npc.NpcDefinition;
import world.item.ItemDefinition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Setup {

    private static final Gson builder = new GsonBuilder().setPrettyPrinting().create();
    private static ItemDefinition[] defs;
    private static NpcDefinition[] npcDefs;

    public static void insertItemDefs() {
        MongoCollection<Document> items = Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(DatabaseConfig.ITEMS_COLLECTION);
        for (ItemDefinition def : Setup.defs) {
            try {
                items.insertOne(Document.parse(Setup.builder.toJson(def, ItemDefinition.class)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void insertNpcDefs() {
        MongoCollection<Document> npcs = Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(DatabaseConfig.NPC_COLLECTION);
        for (ItemDefinition def : Setup.defs) {
            try {
                npcs.insertOne(Document.parse(Setup.builder.toJson(def, NpcDefinition.class)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> void insertAll(T[] items, Class<T> klazz, String dbName, String collectionName) {
        MongoCollection<Document> collection = Database.getClient().getDatabase(dbName).getCollection(collectionName);
        for (T t : items) {
            try {
                collection.insertOne(Document.parse(Setup.builder.toJson(t, klazz)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public static String loadDefAsString(String file) throws IOException {
        return new String(Files.readAllBytes(new File(file).toPath()));
    }


    public static void main(String[] args) {
        try {
            Setup.defs = Setup.builder.fromJson(Setup.loadDefAsString("./data/item_def.json"), ItemDefinition[].class);
            Setup.npcDefs = Setup.builder.fromJson(Setup.loadDefAsString("./data/npc_def.json"), NpcDefinition[].class);

            Setup.insertAll(Setup.defs, ItemDefinition.class, DatabaseConfig.DB_NAME, DatabaseConfig.ITEMS_COLLECTION);
            Setup.insertAll(Setup.npcDefs, NpcDefinition.class, DatabaseConfig.DB_NAME, DatabaseConfig.NPC_COLLECTION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
