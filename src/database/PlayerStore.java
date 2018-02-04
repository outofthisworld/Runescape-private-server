package database;

import world.player.Player;

public class PlayerStore implements DataAccessor<Player>, DataStore<Boolean, Player> {
    private final IDBAccessor<Player> playerDb;

    public PlayerStore(IDBAccessor<Player> playerStore) {
        playerDb = playerStore;
    }

    @Override
    public Player load(String key) {
        return playerDb.findOne(key);
    }

    @Override
    public Boolean store(String key, Player o) {
        return null;
    }
}
