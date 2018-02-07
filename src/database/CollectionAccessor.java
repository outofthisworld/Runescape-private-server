package database;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.bson.Document;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CollectionAccessor<T> implements IDBAccessor<T> {

    private final Gson gson;
    private final String collectionName;
    private final Class<T> mappingClass;
    private String dbName;

    public CollectionAccessor(String collectionName, Class<T> mappingClass) {
        this.collectionName = collectionName;
        this.mappingClass = mappingClass;
        gson = new Gson();
    }

    public CollectionAccessor(String dbName, String collectionName, Class<T> mappingClass) {
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.mappingClass = mappingClass;
        gson = new Gson();
    }

    public CollectionAccessor(String dbName, String collectionName, Class<T> mappingClass, SkipFieldPolicy policy) {
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.mappingClass = mappingClass;
        gson = new GsonBuilder().addSerializationExclusionStrategy(policy).create();
    }

    @Override
    public boolean update(Object id, T obj) {
        UpdateResult r = Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(collectionName).updateOne(Filters.eq("_id", id), Document.parse(gson.toJson(obj)));
        return r.wasAcknowledged();
    }

    public boolean update(T obj) throws InvalidArgumentException, IllegalAccessException {
        Field f = null;
        Object id;

        if (f == null) {
            try {
                f = mappingClass.getDeclaredField("_id");
            } catch (NoSuchFieldException e) {
            }
        }

        if (f == null) {
            try {
                f = mappingClass.getDeclaredField("id");

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        if (f == null) {
            throw new InvalidArgumentException(new String[]{"Missing _id or id fields on obect"});
        }

        f.setAccessible(true);
        id = f.get(obj);


        UpdateResult r = Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(collectionName).updateOne(Filters.eq("_id", id), Document.parse(gson.toJson(obj)));
        return r.wasAcknowledged();
    }

    @Override
    public boolean insert(T obj) {

        Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(collectionName).insertOne(Document.parse(gson.toJson(obj)));
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

        return gson.fromJson(found.toJson(), mappingClass);
    }

    @Override
    public List<T> findAll() {
        MongoCursor<Document> cursor = Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(collectionName).find().returnKey(true).showRecordId(true).iterator();

        List<T> found = new ArrayList<>();
        while (cursor.hasNext()) {
            Document d = cursor.next();
            found.add(gson.fromJson(d.toJson(), mappingClass));
        }

        return found;
    }

    @Override
    public List<T> find(int limit, int skip) {
        MongoCursor<Document> cursor = Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(collectionName).find().limit(limit).skip(skip).returnKey(true).showRecordId(true).iterator();

        List<T> found = new ArrayList<>();
        while (cursor.hasNext()) {
            Document d = cursor.next();
            found.add(gson.fromJson(d.toJson(), mappingClass));
        }

        return found;
    }

    public interface SkipFieldPolicy extends ExclusionStrategy {

        @Override
        default boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    }
}
