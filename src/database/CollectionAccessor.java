package database;

import com.google.gson.*;
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
    private final GsonBuilder builder;
    private final String dbName;


    public CollectionAccessor(String collectionName, Class<T> mappingClass) {
        this(DatabaseConfig.DB_NAME, collectionName, mappingClass);
    }

    public CollectionAccessor(String dbName, String collectionName, Class<T> mappingClass) {
        this(dbName, collectionName, mappingClass, null, null);
    }

    public CollectionAccessor(String dbName, String collectionName, Class<T> mappingClass, SkipFieldPolicy policy) {
        this(dbName, collectionName, mappingClass, policy, null);
    }

    public CollectionAccessor(String dbName, String collectionName, Class<T> mappingClass, NamingStategy strat) {
        this(dbName, collectionName, mappingClass, null, strat);
    }

    public CollectionAccessor(String dbName, String collectionName, Class<T> mappingClass, SkipFieldPolicy policy, NamingStategy strat) {
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.mappingClass = mappingClass;
        builder = new GsonBuilder();

        if (policy != null) {
            builder.addSerializationExclusionStrategy(createExclusionStategy(policy));
        }

        if (strat != null) {
            builder.setFieldNamingStrategy(createFieldNamingStrategy(strat));
        }

        gson = builder.create();
    }

    private FieldNamingStrategy createFieldNamingStrategy(NamingStategy strat){
        return field -> strat.translateName(field);
    }

    private ExclusionStrategy createExclusionStategy(SkipFieldPolicy policy) {
        return new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return policy.shouldSkipField(fieldAttributes);
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return policy.shouldSkipClass(aClass);
            }
        }
    }


    @Override
    public boolean update(String field, Object id, T obj) {
        UpdateResult r = Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(collectionName).updateOne(Filters.eq(field, id), Document.parse(gson.toJson(obj)));
        return r.wasAcknowledged();
    }

    public boolean update(String field, Object id, T obj) {
        UpdateResult r = Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(collectionName).updateOne(Filters.eq(field, id), Document.parse(gson.toJson(obj)));
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
    public boolean update(Object id, T obj) {
        return false;
    }

    @Override
    public boolean insert(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Tried to insert null object into database");
        }

        Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(collectionName).insertOne(Document.parse(gson.toJson(obj)));
        return true;
    }

    public boolean insert(String idField, T obj) {

        Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(collectionName).insertOne(Document.parse(gson.toJson(obj)));
        return true;
    }

    @Override
    public boolean delete(T obj) {
        return false;
    }

    @Override
    public <U> T findOne(U id) {
        return findOneMatching("_id", id);
    }

    public T findOneAndPopulate(String field, Object value, T obj) {
        Gson gson = new GsonBuilder().registerTypeAdapter(mappingClass, (InstanceCreator) type -> obj).create();
        Document d = findDocumentMatching(field, value);
        if (d == null) {
            return null;
        }

        return gson.fromJson(d.toJson(), mappingClass);
    }

    public T findOneMatching(String field, Object value) {
        Document found = findDocumentMatching(field, value);
        if (found == null) {
            return null;
        }

        return gson.fromJson(found.toJson(), mappingClass);
    }


    private Document findDocumentMatching(String field, Object value) {
        Document d = new Document();
        d.put(field, value);
        return findDocumentMatching(d);
    }

    private Document findDocumentMatching(Document d) {
        Document found = Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(collectionName).find(d).first();
        if (found == null) {
            return null;
        }

        return found;
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

    public interface NamingStategy {
        String translateName(Field field);
    }


    public interface SkipFieldPolicy {
        boolean shouldSkipField(FieldAttributes fieldAttributes);

        default boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    }
}
