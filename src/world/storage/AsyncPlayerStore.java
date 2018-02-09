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

public class AsyncPlayerStore implements DataStore<CompletableFuture<Boolean>, Player>, DataAccessor<Player, CompletableFuture<Player>> {
    private final IDBAccessor<Player> playerDb;
    private final PlayerStore playerStore;

    public AsyncPlayerStore(IDBAccessor<Player> db) {
        playerDb = db;
        playerStore = new PlayerStore(playerDb);
    }


    @Override
    public CompletableFuture<Player> load(Player p) {
        return CompletableFuture.supplyAsync(() -> playerStore.load(p));
    }

    @Override
    public CompletableFuture<Boolean> store(Player o) {
        return CompletableFuture.supplyAsync(() -> playerStore.store(o));
    }
}
