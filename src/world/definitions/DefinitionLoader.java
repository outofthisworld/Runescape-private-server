package world.definitions;

import database.CollectionAccessor;
import database.DatabaseConfig;
import database.IDBAccessor;
import database.serialization.GsonSerializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DefinitionLoader {
    public static final IDBAccessor<ItemDefinition> ITEM_DEFINITIONS = new CollectionAccessor<>(new GsonSerializer<>(ItemDefinition.class), DatabaseConfig.ITEMS_COLLECTION);
    public static final IDBAccessor<NpcDefinition> NPC_DEFINITIONS = new CollectionAccessor<>(new GsonSerializer<>(NpcDefinition.class), DatabaseConfig.NPC_COLLECTION);
    private static final int DEFINITIONS_COUNT = 2;
    private static final Map<IDBAccessor<?>, Map<Integer, ? extends IDefinition>> definitions = new HashMap<>();
    private static Map<Integer, ItemDefinition> itemDefinitions = new HashMap<>();
    private static Map<Integer, NpcDefinition> npcDefinitions = new HashMap<>();

    public static CompletableFuture<Void> load() {

        int increment = 0;
        CompletableFuture<Void>[] futures = new CompletableFuture[DEFINITIONS_COUNT];

        futures[increment++] = CompletableFuture.runAsync(() -> {
            ITEM_DEFINITIONS.findAll().forEach(def -> {
                itemDefinitions.put(def.getId(), def);
            });
            itemDefinitions = Collections.unmodifiableMap(itemDefinitions);
        });

        futures[increment++] = CompletableFuture.runAsync(() -> {
            NPC_DEFINITIONS.findAll().forEach(def -> {
                npcDefinitions.put(def.getId(), def);
            });
            npcDefinitions = Collections.unmodifiableMap(npcDefinitions);
        });

        definitions.put(ITEM_DEFINITIONS, itemDefinitions);
        definitions.put(NPC_DEFINITIONS, npcDefinitions);

        return CompletableFuture.allOf(futures);
    }

    public static <T extends IDefinition> Map<Integer, T> getDefinitionMap(IDBAccessor<?> accessor) {
        return (Map<Integer, T>) definitions.get(accessor);
    }
    public static <T extends IDefinition> T getDefinition(IDBAccessor<?> accessor, int id) {
        return (T) definitions.get(accessor).get(id);
    }
}
