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

package database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import com.sun.javaws.exceptions.InvalidArgumentException;
import database.serialization.AbstractSerializer;
import org.bson.Document;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionAccessor<T> implements IDBAccessor<T> {
    private final String collectionName;
    private final String dbName;
    private final Class<T> mappingClass;
    private final AbstractSerializer<T, String> serializer;
    private final MongoCollection<Document> collection;


    public CollectionAccessor(AbstractSerializer<T, String> serializer, String collectionName) {
        this(serializer, DatabaseConfig.DB_NAME, collectionName, serializer.getSerializationClass());
    }

    public CollectionAccessor(AbstractSerializer<T, String> serializer, String dbName, String collectionName, Class<T> mappingClass) {
        this.serializer = serializer;
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.mappingClass = mappingClass;
        collection = Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(collectionName);
    }

    @Override
    public boolean update(String field, Object id, T obj) {
        UpdateResult r = collection.updateOne(Filters.eq(field, id), Document.parse(serializer.encode(obj)));
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

        collection.insertOne(Document.parse(serializer.encode(obj)));
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
        Document d = findFirstDocumentMatching(field, value);
        if (d == null) {
            return null;
        }
        return serializer.decode(d.toJson(), obj);
    }

    public T findOneMatching(String field, Object value) {
        Document found = findFirstDocumentMatching(field, value);
        if (found == null) {
            return null;
        }

        return serializer.decode(found.toJson());
    }

    public T findOneMatching(HashMap<String, ?> hm) {
        return serializer.decode(findFirstDocumentMatching(fromHashMap(hm)).toJson());
    }

    private Document fromHashMap(HashMap<String, ?> hm) {
        Document d = new Document();
        hm.entrySet().forEach((entry) -> {
            d.put(entry.getKey(), entry.getValue());
        });
        return d;
    }


    private Document findFirstDocumentMatching(String field, Object value) {
        return findFirstDocumentMatching(new Document().append(field, value));
    }

    private Document findFirstDocumentMatching(Document d) {
        return collection.find(d).first();
    }

    public List<T> findAllMatching(HashMap<String, ?> filter) {
        return findDocumentsMatching(fromHashMap(filter), -1, -1);
    }

    public List<T> findAllMatching(HashMap<String, ?> filter, int limit) {
        return findDocumentsMatching(fromHashMap(filter), limit, -1);
    }

    public List<T> findAllMatching(HashMap<String, ?> filter, int limit, int skip) {
        return findDocumentsMatching(fromHashMap(filter), limit, skip);
    }

    public List<T> findAllMatching(String key, String value) {
        return findDocumentsMatching(new Document().append(key, value), -1, -1);
    }

    public List<T> findAllMatching(String key, String value, int limit) {
        return findDocumentsMatching(new Document().append(key, value), limit, -1);
    }

    public List<T> findAllMatching(String key, String value, int limit, int skip) {
        return findDocumentsMatching(new Document().append(key, value), -1, skip);
    }

    private List<T> findDocumentsMatching(Document d, int limit, int skip) {
        FindIterable<Document> fi;

        if (d == null) {
            fi = collection.find();
        } else {
            fi = collection.find(d);
        }

        if (limit != -1) {
            fi = fi.limit(limit);
        }

        if (skip != -1) {
            fi = fi.skip(skip);
        }

        MongoCursor<Document> cursor = fi.returnKey(true).showRecordId(true).iterator();

        List<T> found = new ArrayList<>();
        while (cursor.hasNext()) {
            Document record = cursor.next();
            found.add(serializer.decode(record.toJson()));
        }

        return found;
    }

    @Override
    public List<T> findAll() {
        return findDocumentsMatching(null, -1, -1);
    }

    @Override
    public List<T> find(int limit, int skip) {
        MongoCursor<Document> cursor = Database.getClient().getDatabase(DatabaseConfig.DB_NAME).getCollection(collectionName).find().limit(limit).skip(skip).returnKey(true).showRecordId(true).iterator();

        List<T> found = new ArrayList<>();
        while (cursor.hasNext()) {
            Document d = cursor.next();
            found.add(serializer.decode(d.toJson()));
        }

        return found;
    }
}
