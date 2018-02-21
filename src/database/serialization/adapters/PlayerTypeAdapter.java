package database.serialization.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import world.entity.player.Player;

import java.lang.reflect.Type;


public class PlayerTypeAdapter implements JsonDeserializer<Player> {


    @Override
    public Player deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        //context.deserialize()
        return null;
    }
}
