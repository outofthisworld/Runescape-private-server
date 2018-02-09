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

package world.storage;


import database.IDBAccessor;
import world.entity.player.Player;

import java.util.concurrent.CompletableFuture;

/**
 * The type Async player store.
 */
public class AsyncPlayerStore implements DataStore<CompletableFuture<Boolean>, Player>, DataAccessor<Player, CompletableFuture<Player>> {
    private final IDBAccessor<Player> playerDb;
    private final PlayerStore playerStore;

    /**
     * Instantiates a new Async player store.
     *
     * @param db the db
     */
    public AsyncPlayerStore(IDBAccessor<Player> db) {
        playerDb = db;
        playerStore = new PlayerStore(playerDb);
    }


    @Override
    public CompletableFuture<Player> load(Player p) {
        return CompletableFuture.supplyAsync(() -> playerStore.load(p));
    }

    @Override
    public CompletableFuture<Player> load(String key) {
        return CompletableFuture.supplyAsync(() -> playerStore.load(key));
    }

    @Override
    public CompletableFuture<Boolean> store(Player o) {
        return CompletableFuture.supplyAsync(() -> playerStore.store(o));
    }
}
