package database;

import world.entity.player.Player;

public class PlayerStore implements DataAccessor<Player>, DataStore<Boolean, Player> {
    private final IDBAccessor<Player> playerDb;

    public PlayerStore(IDBAccessor<Player> playerStore) {
        playerDb = playerStore;
    }

    @Override
    public Boolean store(Player o) {
        return playerDb.insert(o);
    }


    @Override
    public Player load(Player obj) {
        return playerDb.findOne(obj.getUsername());
    }
}
