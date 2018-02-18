package database.serialization.impl;

import com.google.gson.FieldAttributes;
import database.serialization.SkipFieldPolicy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlayerSkipFieldPolicy implements SkipFieldPolicy {
    private static final Set<String> skipFields = new HashSet<>(Arrays.asList(
            "c",
            "player",
            "p",
            "world",
            "e",
            "playerUpdateBlock",
            "entity",
            "client",
            "updateFlags",
            "movement",
            "slotId",
            "worldId",
            "isInitialized",
            "localPlayers"
    ));

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return skipFields.contains(fieldAttributes.getName());
    }
}
