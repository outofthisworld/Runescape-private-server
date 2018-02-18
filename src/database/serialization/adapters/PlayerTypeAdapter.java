package database.serialization.adapters;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import world.entity.player.Player;

import java.io.IOException;
import java.lang.reflect.Type;



public class PlayerTypeAdapter  implements JsonDeserializer<Player> {


    @Override
    public Player deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        //context.deserialize()
        return null;
    }
}
