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

/**
 * The type Player store.
 */
public class PlayerStore implements DataAccessor<Player, Player>, DataStore<Boolean, Player> {
    private final IDBAccessor<Player> playerDb;

    /**
     * Instantiates a new Player store.
     *
     * @param playerStore the player store
     */
    public PlayerStore(IDBAccessor<Player> playerStore) {
        playerDb = playerStore;
    }

    @Override
    public Boolean store(Player o) {
        return playerDb.insert(o);
    }


    @Override
    public Player load(Player obj) {
        return playerDb.findOneAndPopulate(obj.getUsername(), obj);
    }

    @Override
    public Player load(String key) {
        return playerDb.findOne(key);
    }
}
