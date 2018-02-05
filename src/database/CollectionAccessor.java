package database;

import com.google.gson.Gson;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class CollectionAccessor<T> implements IDBAccessor<T> {

    private static final Gson gson = new Gson();
    private final String collectionName;
    private final Class<T> mappingClass;
    private String dbName;

    public CollectionAccessor(String collectionName, Class<T> mappingClass) {
        this.collectionName = collectionName;
        this.mappingClass = mappingClass;
    }

    public CollectionAccessor(String dbName, String collectionName, Class<T> mappingClass) {
        this.dbName = DatabaseConfig.DB_NAME;
        this.collectionName = collectionName;
        this.mappingClass = mappingClass;
    }

    @Override
    public boolean update(T obj) {
        return false;
    }

    @Override
    public boolean insert(T obj) {
        Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(collectionName).insertOne(Document.parse(CollectionAccessor.gson.toJson(obj)));
        return true;
    }

    @Override
    public boolean delete(T obj) {
        return false;
    }

    @Override
    public <U> T findOne(U id) {
        Document d = new Document();
        d.put("_id", id);
        Document found = Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(collectionName).find(d).first();
        if (found == null) {
            return null;
        }

        return CollectionAccessor.gson.fromJson(found.toJson(), mappingClass);
    }

    @Override
    public <U> List<T> find(U obj, int limit, int skip) {
        MongoCursor<Document> cursor = Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(collectionName).find().limit(limit).skip(skip).returnKey(true).showRecordId(true).iterator();

        List<T> found = new ArrayList<>();
        while (cursor.hasNext()) {
            Document d = cursor.next();
            found.add(CollectionAccessor.gson.fromJson(d.toJson(), mappingClass));
        }

        return found;
    }
}
