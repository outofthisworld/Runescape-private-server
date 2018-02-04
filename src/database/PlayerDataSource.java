package database;

import world.player.Player;

import java.util.List;

public class PlayerDataSource implements IDBAccessor<Player> {

    @Override
    public boolean update(Player player) {
        return false;
    }

    @Override
    public boolean insert(Player player) {
        return false;
    }

    @Override
    public boolean delete(Player player) {
        return false;
    }

    @Override
    public <U> Player findOne(U id) {
        return null;
    }

    @Override
    public <U> List<Player> findAll(U obj) {
        return null;
    }
}
