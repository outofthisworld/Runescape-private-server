package world.event.impl;

import world.entity.movement.Movement;
import world.entity.player.Player;

public class RegionUpdateEvent extends AbstractEvent {
    private final Player p;
    private Movement m;

    public RegionUpdateEvent(Player p, Movement m) {
        this.p = p;
    }

    public Player getPlayer() {
        return p;
    }

    @Override
    public Movement getSender() {
        return m;
    }
}
