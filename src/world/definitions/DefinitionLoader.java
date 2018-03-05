package world.definitions;

import database.CollectionAccessor;
import database.DatabaseConfig;
import database.IDBAccessor;
import database.serialization.GsonSerializer;
import world.definitions.item.*;
import world.definitions.npc.NpcDefinition;
import world.definitions.npc.NpcDropDefinition;
import world.definitions.npc.ShopDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class DefinitionLoader {
    public static final IDBAccessor<ItemDefinition> ITEM_DEFINITIONS = new CollectionAccessor<>(new GsonSerializer<>(ItemDefinition.class), DatabaseConfig.ITEMS_COLLECTION);
    public static final IDBAccessor<NpcDefinition> NPC_DEFINITIONS = new CollectionAccessor<>(new GsonSerializer<>(NpcDefinition.class), DatabaseConfig.NPC_COLLECTION);
    public static final IDBAccessor<ItemRequirementDefinition> ITEM_REQUIREMENTS = new CollectionAccessor<>(new GsonSerializer<>(ItemRequirementDefinition.class), DatabaseConfig.ITEM_REQUIRMENTS);
    public static final IDBAccessor<WeaponAnimationDefinition> WEAPON_ANIMATIONS = new CollectionAccessor<>(new GsonSerializer<>(WeaponAnimationDefinition.class), DatabaseConfig.WEAPON_ANIMATIONS);
    public static final IDBAccessor<WeaponInterfaceDefinition> WEAPON_INTERFACES = new CollectionAccessor<>(new GsonSerializer<>(WeaponInterfaceDefinition.class), DatabaseConfig.WEAPON_INTERFACES);
    public static final IDBAccessor<WeaponPoisonDefinition> WEAPON_POISONS = new CollectionAccessor<>(new GsonSerializer<>(WeaponPoisonDefinition.class), DatabaseConfig.WEAPON_POISONS);
    public static final IDBAccessor<NpcDropDefinition> NPC_DROPS = new CollectionAccessor<>(new GsonSerializer<>(NpcDropDefinition.class), DatabaseConfig.NPC_DROPS);
    public static final IDBAccessor<ShopDefinition> SHOPS = new CollectionAccessor<>(new GsonSerializer<>(ShopDefinition.class), DatabaseConfig.SHOPS);


    private static int DEFINITIONS_COUNT = 8;
    private static CompletableFuture<Void>[] futures = new CompletableFuture[DEFINITIONS_COUNT];
    private static Map<IDBAccessor<?>, Map<Integer, ? extends IDefinition>> definitions = new HashMap<>();
    private static Map<Integer, ItemDefinition> itemDefinitions = new HashMap<>();
    private static Map<Integer, NpcDefinition> npcDefinitions = new HashMap<>();
    private static Map<Integer, ItemRequirementDefinition> itemRequirements = new HashMap<>();
    private static Map<Integer, WeaponAnimationDefinition> weaponAnimations = new HashMap<>();
    private static Map<Integer, WeaponInterfaceDefinition> weaponInterfaces = new HashMap<>();
    private static Map<Integer, WeaponPoisonDefinition> weaponPoisons = new HashMap<>();
    private static Map<Integer, NpcDropDefinition> npcDrops = new HashMap<>();
    private static Map<Integer, ShopDefinition> shops = new HashMap<>();
    private static int cursor = 0;

    public static CompletableFuture<Void> load() {
        loadDefinition(ITEM_DEFINITIONS, itemDefinitions);
        loadDefinition(NPC_DEFINITIONS, npcDefinitions);
        loadDefinition(ITEM_REQUIREMENTS, itemRequirements);
        loadDefinition(WEAPON_ANIMATIONS, weaponAnimations);

        //Load WeaponInterfaces slightly differently
        buildDefinitionsMap(WEAPON_INTERFACES, weaponInterfaces);
        futures[cursor++] = CompletableFuture.runAsync(() -> {
            WEAPON_INTERFACES.findAll().forEach(def -> {
                def.getWeaponIds().forEach(wepId -> {
                    if (weaponInterfaces.containsKey(wepId)) {
                        throw new IllegalStateException("Duplicate key for " + wepId + " found when loading weapon interfaces");
                    }
                    weaponInterfaces.put(wepId, def);
                });
                int defId = def.getId();
                if (weaponInterfaces.containsKey(defId)) {
                    throw new IllegalStateException("Duplicate key for " + def + " found when loading weapon interfaces");
                }
                weaponInterfaces.put(defId, def);
            });
        });

        loadDefinition(WEAPON_POISONS, weaponPoisons);
        loadDefinition(NPC_DROPS, npcDrops);
        loadDefinition(SHOPS, shops);
        return CompletableFuture.allOf(futures);
    }

    private static <T extends IDefinition> void loadDefinition(IDBAccessor<T> accessor, Map<Integer, T> map) {
        buildDefinitionsMap(accessor, map);
        futures[cursor++] = CompletableFuture.runAsync(() -> {
            accessor.findAll().forEach(def -> {
                map.put(def.getId(), def);
            });
        });
    }

    private static <T extends IDefinition> void buildDefinitionsMap(IDBAccessor<T> accessor, Map<Integer, T> map) {
        if (definitions.containsKey(accessor)) {
            System.out.println("dup key in definitions");
            throw new IllegalStateException("Definitions contains duplicate key.");
        }
        definitions.put(accessor, map);
    }

    public static <T extends IDefinition> Map<Integer, T> getDefinitionMap(IDBAccessor<T> accessor) {
        if (definitions.get(accessor) == null) {
            System.out.println("get was null");
            System.out.println(accessor == ITEM_DEFINITIONS);
        }
        return (Map<Integer, T>) definitions.get(accessor);
    }

    public static <T extends IDefinition> T getDefinition(IDBAccessor<?> accessor, int id) {
        return (T) definitions.get(accessor).get(id);
    }
}
