package database.serialization.impl;

import database.serialization.NamingStrategy;

import java.lang.reflect.Field;

public class FieldToIdPolicy implements NamingStrategy {
    private final String fieldName;

    public FieldToIdPolicy(String fieldName){
        this.fieldName = fieldName;
    }

    @Override
    public String translateName(Field field) {
        if(field.getName().equals(fieldName)){
            return "_id";
        }
        return field.getName();
    }
}
