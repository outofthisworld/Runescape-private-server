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

package database.serialization;


import com.google.gson.*;

/**
 * The type Gson serializer.
 *
 * @param <T> the type parameter
 */
public class GsonSerializer<T> extends AbstractSerializer<T, String> {
    private final Class<T> mappingClass;
    private final GsonBuilder gsonBuilder;
    private FieldNamingStrategy fStat;
    private ExclusionStrategy eStat;

    /**
     * Instantiates a new Gson serializer.
     *
     * @param serializationClass the serialization class
     */
    public GsonSerializer(Class<T> serializationClass) {
        gsonBuilder = new GsonBuilder();
        mappingClass = serializationClass;
    }

    /**
     * Sets naming stategy.
     *
     * @param strat the strat
     * @return the naming stategy
     */
    public GsonSerializer<T> setNamingStategy(NamingStrategy strat) {
        gsonBuilder.setFieldNamingStrategy(fStat = createFieldNamingStrategy(strat));
        return this;
    }

    /**
     * Sets field skip policy.
     *
     * @param skip the skip
     * @return the field skip policy
     */
    public GsonSerializer<T> setFieldSkipPolicy(SkipFieldPolicy skip) {
        gsonBuilder.setExclusionStrategies(eStat = createExclusionStategy(skip));
        return this;
    }

    private FieldNamingStrategy createFieldNamingStrategy(NamingStrategy strat) {
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
        };
    }

    @Override
    public String encode(T t) {
        return gsonBuilder.create().toJson(t, mappingClass);
    }

    @Override
    public T decode(String s) {
        return decode(gsonBuilder.create(), s);
    }

    private T decode(Gson g, String json) {
        return g.fromJson(json, mappingClass);
    }

    @Override
    public T decode(String json, T t) {
        GsonBuilder b = new GsonBuilder().registerTypeAdapter(mappingClass, (InstanceCreator) type -> t);
        if (eStat != null) {
            b.setExclusionStrategies(eStat);
        }
        if (fStat != null) {
            b.setFieldNamingStrategy(fStat);
        }
        return decode(b.create(), json);
    }

    @Override
    public Class<T> getSerializationClass() {
        return mappingClass;
    }
}
