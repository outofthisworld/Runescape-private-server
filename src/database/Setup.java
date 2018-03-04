package database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import world.definitions.IDefinition;
import world.definitions.item.*;
import world.definitions.npc.NpcDefinition;
import world.definitions.npc.NpcDropDefinition;
import world.definitions.npc.ShopDefinition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * The type Setup.
 */
public class Setup {
    private static final Gson builder = new GsonBuilder().setPrettyPrinting().create();
    private static ItemDefinition[] defs;
    private static NpcDefinition[] npcDefs;
    private static ItemRequirementDefinition[] itemRequirements;
    private static WeaponAnimationDefinition[] weaponAnimations;
    private static WeaponInterfaceDefinition[] weaponInterfaces;
    private static WeaponPoisonDefinition[] weaponPoisons;
    private static NpcDropDefinition[] npcDrops;
    private static ShopDefinition[] shops;


    /**
     * Insert all.
     *
     * @param <T>            the type parameter
     * @param items          the items
     * @param klazz          the klazz
     * @param dbName         the db name
     * @param collectionName the collection name
     */
    public static <T extends IDefinition> void insertAll(T[] items, Class<T> klazz, String dbName, String collectionName, boolean useId) {
        MongoCollection<Document> collection = Database.getClient().getDatabase(dbName).getCollection(collectionName);
        for (T t : items) {
            try {
                Document d = Document.parse(Setup.builder.toJson(t, klazz));
                if (useId) {
                    d.put("_id", t.getId());
                }
                collection.insertOne(d);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Load def as string string.
     *
     * @param file the file
     * @return the string
     * @throws IOException the io exception
     */
    public static String loadDefAsString(String file) throws IOException {
        File f = new File(file);
        if (!f.exists()) {
            throw new RuntimeException("Invalid definitions file");
        }
        return new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())));
    }


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        //Database.init();
        Database.getClient().dropDatabase(DatabaseConfig.DB_NAME);
        try {

            /*
                Load json files into array.
             */
            Setup.defs = Setup.builder.fromJson(Setup.loadDefAsString("src/database/data/json/items/item_definitions.json"), ItemDefinition[].class);
            Setup.npcDefs = Setup.builder.fromJson(Setup.loadDefAsString("src/database/data/json/npcs/npc_definitions.json"), NpcDefinition[].class);
            Setup.itemRequirements = Setup.builder.fromJson(Setup.loadDefAsString("src/database/data/json/equipment/equipment_requirements.json"), ItemRequirementDefinition[].class);
            Setup.weaponAnimations = Setup.builder.fromJson(Setup.loadDefAsString("src/database/data/json/equipment/weapon_animations.json"), WeaponAnimationDefinition[].class);
            Setup.weaponInterfaces = Setup.builder.fromJson(Setup.loadDefAsString("src/database/data/json/equipment/weapon_interfaces.json"), WeaponInterfaceDefinition[].class);
            Setup.weaponPoisons = Setup.builder.fromJson(Setup.loadDefAsString("src/database/data/json/equipment/weapon_poison.json"), WeaponPoisonDefinition[].class);
            Setup.npcDrops = Setup.builder.fromJson(Setup.loadDefAsString("src/database/data/json/npcs/npc_drops.json"), NpcDropDefinition[].class);
            Setup.shops = Setup.builder.fromJson(Setup.loadDefAsString("src/database/data/json/shops/shops.json"), ShopDefinition[].class);
            Setup.insertAll(Setup.defs, ItemDefinition.class, DatabaseConfig.DB_NAME, DatabaseConfig.ITEMS_COLLECTION, true);
            Setup.insertAll(Setup.npcDefs, NpcDefinition.class, DatabaseConfig.DB_NAME, DatabaseConfig.NPC_COLLECTION, true);
            Setup.insertAll(Setup.itemRequirements, ItemRequirementDefinition.class, DatabaseConfig.DB_NAME, DatabaseConfig.ITEM_REQUIRMENTS, true);
            Setup.insertAll(Setup.weaponAnimations, WeaponAnimationDefinition.class, DatabaseConfig.DB_NAME, DatabaseConfig.WEAPON_ANIMATIONS, true);
            Setup.insertAll(Setup.weaponInterfaces, WeaponInterfaceDefinition.class, DatabaseConfig.DB_NAME, DatabaseConfig.WEAPON_INTERFACES, true);
            Setup.insertAll(Setup.weaponPoisons, WeaponPoisonDefinition.class, DatabaseConfig.DB_NAME, DatabaseConfig.WEAPON_POISONS, true);
            Setup.insertAll(Setup.npcDrops, NpcDropDefinition.class, DatabaseConfig.DB_NAME, DatabaseConfig.NPC_DROPS, true);
            Setup.insertAll(Setup.shops, ShopDefinition.class, DatabaseConfig.DB_NAME, DatabaseConfig.SHOPS, true);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
