package world.entity.update;

import world.entity.update.player.PlayerUpdateBlock;
import world.storage.SimpleCache;

public class UpdateBlockCache extends SimpleCache<String, PlayerUpdateBlock> {
    @Override
    public PlayerUpdateBlock load(String key) {
        return cache.get(key);
    }

    @Override
    public Boolean store(String key, PlayerUpdateBlock o) {
        cache.put(key, o);
        return true;
    }
}
