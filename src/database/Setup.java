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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import world.definitions.ItemDefinition;
import world.definitions.NpcDefinition;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Setup {
    private static final Gson builder = new GsonBuilder().setPrettyPrinting().create();
    private static ItemDefinition[] defs;
    private static NpcDefinition[] npcDefs;


    public static <T> void insertAll(T[] items, Class<T> klazz, String idField, String dbName, String collectionName) {
        MongoCollection<Document> collection = Database.getClient().getDatabase(dbName).getCollection(collectionName);
        for (T t : items) {
            try {
                Document d = Document.parse(Setup.builder.toJson(t, klazz));
                if (idField != null) {
                    Field f = klazz.getDeclaredField(idField);
                    f.setAccessible(true);
                    // d.remove("id");
                    d.put("_id", f.get(t));
                }
                collection.insertOne(d);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String loadDefAsString(String file) throws IOException {
        System.out.println(new File(file).getAbsolutePath());
        return new String(Files.readAllBytes(Paths.get(new File(file).getAbsolutePath())));
    }

    public static void main(String[] args) {
        Database.init();
        /*Database.getClient().dropDatabase(DatabaseConfig.DB_NAME);
        try {
            Setup.defs = Setup.builder.fromJson(Setup.loadDefAsString("src/database/data/item_def.json"), ItemDefinition[].class);
            Setup.npcDefs = Setup.builder.fromJson(Setup.loadDefAsString("src/database/data/npc_def.json"), NpcDefinition[].class);

            Setup.insertAll(Setup.defs, ItemDefinition.class, "id", DatabaseConfig.DB_NAME, DatabaseConfig.ITEMS_COLLECTION);
            Setup.insertAll(Setup.npcDefs, NpcDefinition.class, "id", DatabaseConfig.DB_NAME, DatabaseConfig.NPC_COLLECTION);


        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }
}
