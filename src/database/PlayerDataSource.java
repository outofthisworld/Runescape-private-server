package database;

import com.google.gson.Gson;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import world.player.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerDataSource implements IDBAccessor<Player> {

    private static final String playerCollection = "Players";
    private static final Gson gson = new Gson();


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
        Document d = new Document();
        d.put("_id", id);
        Document found = Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(PlayerDataSource.playerCollection).find(d).first();
        if (found == null) {
            return null;
        }

        return PlayerDataSource.gson.fromJson(found.toJson(), Player.class);
    }


    @Override
    public <U> List<Player> find(U obj, int limit, int skip) {
        MongoCursor<Document> cursor = Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(PlayerDataSource.playerCollection).find().limit(limit).skip(skip).returnKey(true).showRecordId(true).iterator();

        List<Player> players = new ArrayList<>();
        while (cursor.hasNext()) {
            Document d = cursor.next();
            players.add(PlayerDataSource.gson.fromJson(d.toJson(), Player.class));
        }

        return players;
    }
}
