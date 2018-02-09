package database.serialization;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.GsonBuilder;

public class GsonSerializer<T> extends AbstractSerializer<T, String> {

    private final Class<T> mappingClass;
    private final GsonBuilder gsonBuilder;
    private Encoder<T, String> encoder = new JsonEncoder();
    private Encoder<String, T> decoder = new JsonDecoder();

    public GsonSerializer(Class<T> mappingClass) {
        gsonBuilder = new GsonBuilder();
        this.mappingClass = mappingClass;
    }


    public GsonSerializer<T> setNamingStategy(NamingStrategy strat) {
        gsonBuilder.setFieldNamingStrategy(createFieldNamingStrategy(strat));
        return this;
    }

    public GsonSerializer<T> setFieldSkipPolicy(SkipFieldPolicy skip) {
        gsonBuilder.setExclusionStrategies(createExclusionStategy(skip));
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

    public Encoder<T, String> getEncoder() {
        return encoder;
    }

    public GsonSerializer setEncoder(Encoder<T, String> encoder) {
        this.encoder = encoder;
        return this;
    }

    public Encoder<String, T> getDecoder() {
        return decoder;
    }

    public GsonSerializer setDecoder(Encoder<String, T> decoder) {
        this.decoder = decoder;
        return this;
    }

    @Override
    public String encode(T t) {
        return encoder.encode(t);
    }

    @Override
    public T decode(String s) {
        return decoder.encode(s);
    }

    @Override
    public Class<T> getSerializationClass() {
        return mappingClass;
    }

    private class JsonEncoder implements Encoder<T, String> {

        @Override
        public String encode(T t) {
            return gsonBuilder.create().toJson(t);
        }
    }

    private class JsonDecoder implements Encoder<String, T> {

        @Override
        public T encode(String s) {
            return gsonBuilder.create().fromJson(s, mappingClass);
        }
    }
}
