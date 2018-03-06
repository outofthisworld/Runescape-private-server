package world.entity.player.update.impl;

import net.buffers.OutputBuffer;
import world.entity.player.Player;

import java.util.function.BiConsumer;

public class SingleHitUpdateBlock implements BiConsumer<Player, OutputBuffer> {
    @Override
    public void accept(Player player, OutputBuffer buffer) {
                  /*builder.put(entity.getPrimaryHit().getDamage())
                                .put(entity.getPrimaryHit().getType().getId(), ByteValue.ADDITION)
                                .put(entity.getSkill().getLevel(Skill.HITPOINTS), ByteValue.NEGATION)
                                .put(entity.getSkill().getMaxLevel(Skill.HITPOINTS));*/
    }
}
