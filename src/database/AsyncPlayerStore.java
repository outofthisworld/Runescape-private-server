package database;


import world.player.Player;

import java.util.concurrent.CompletableFuture;

public class AsyncPlayerStore implements DataStore<CompletableFuture<Boolean>, Player>, DataAccessor<CompletableFuture<Player>> {
    private final IDBAccessor<Player> playerDb;
    private final PlayerStore playerStore;

    public AsyncPlayerStore(IDBAccessor<Player> db) {
        playerDb = db;
        playerStore = new PlayerStore(playerDb);
    }


    @Override
    public CompletableFuture<Player> load(String key) {
        return CompletableFuture.supplyAsync(() -> playerStore.load(key));
    }

    @Override
    public CompletableFuture<Boolean> store(String key, Player o) {
        return CompletableFuture.supplyAsync(() -> playerStore.store(key, o));
    }
}
