package world.event.impl;

import util.Preconditions;
import world.entity.movement.Movement;
import world.entity.player.Player;

public class PlayerMoveEvent extends Event {
    private final Player e;
    private final Movement movement;

    public PlayerMoveEvent(Player e, Movement movement) {
        Preconditions.notNull(e, movement);
        this.e = e;
        this.movement = movement;
    }

    public Player getPlayer() {
        return e;
    }

    @Override
    public Movement getSender() {
        return movement;
    }
}
