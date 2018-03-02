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
    public static final IDBAccessor<NpcDefinition> ITEM_REQUIREMENTS = new CollectionAccessor<>(new GsonSerializer<>(ItemRequirmentDefinition.class), DatabaseConfig.ITEM_REQUIRMENTS);
    public static final IDBAccessor<NpcDefinition> WEAPON_ANIMATIONS = new CollectionAccessor<>(new GsonSerializer<>(WeaponAnimationDefinition.class), DatabaseConfig.WEAPON_ANIMATIONS);
    public static final IDBAccessor<NpcDefinition> WEAPON_INTERFACES = new CollectionAccessor<>(new GsonSerializer<>(WeaponInterfaceDefinition.class), DatabaseConfig.WEAPON_INTERFACES);
    public static final IDBAccessor<NpcDefinition> WEAPON_POISONS = new CollectionAccessor<>(new GsonSerializer<>(WeaponPoisonDefinition.class), DatabaseConfig.WEAPON_POISONS);
    public static final IDBAccessor<NpcDefinition> NPC_DROPS = new CollectionAccessor<>(new GsonSerializer<>(NpcDropDefinition.class), DatabaseConfig.NPC_DROPS);
    public static final IDBAccessor<NpcDefinition> SHOPS = new CollectionAccessor<>(new GsonSerializer<>(ShopDefinition.class), DatabaseConfig.SHOPS);


    private static int DEFINITIONS_COUNT = 8;
    private static CompletableFuture<Void>[] futures = new CompletableFuture[DEFINITIONS_COUNT];
    private static Map<IDBAccessor<?>, Map<Integer, ? extends IDefinition>> definitions = new HashMap<>();
    private static Map<Integer, ItemDefinition> itemDefinitions = new HashMap<>();
    private static Map<Integer, NpcDefinition> npcDefinitions = new HashMap<>();
    private static Map<Integer, NpcDefinition> itemRequirments = new HashMap<>();
    private static Map<Integer, NpcDefinition> weaponAnimations = new HashMap<>();
    private static Map<Integer, NpcDefinition> weaponInterfaces = new HashMap<>();
    private static Map<Integer, NpcDefinition> weaponPoisons = new HashMap<>();
    private static Map<Integer, NpcDefinition> npcDrops = new HashMap<>();
    private static Map<Integer, NpcDefinition> shops = new HashMap<>();
    private static int cursor = 0;

    public static CompletableFuture<Void> load() {
        loadDefinition(ITEM_DEFINITIONS, itemDefinitions);
        loadDefinition(NPC_DEFINITIONS, npcDefinitions);
        loadDefinition(ITEM_REQUIREMENTS, itemRequirments);
        loadDefinition(WEAPON_ANIMATIONS, weaponAnimations);
        loadDefinition(WEAPON_INTERFACES, weaponInterfaces);
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
            throw new IllegalStateException("Definitions contains duplicate key.");
        }
        definitions.put(accessor, map);
    }

    public static <T extends IDefinition> Map<Integer, T> getDefinitionMap(IDBAccessor<?> accessor) {
        return (Map<Integer, T>) definitions.get(accessor);
    }

    public static <T extends IDefinition> T getDefinition(IDBAccessor<?> accessor, int id) {
        return (T) definitions.get(accessor).get(id);
    }
}
