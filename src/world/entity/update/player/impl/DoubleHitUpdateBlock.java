package world.entity.update.player.impl;

import net.buffers.OutputBuffer;
import world.entity.player.Player;

import java.util.function.BiConsumer;

public class DoubleHitUpdateBlock implements BiConsumer<Player, OutputBuffer> {
    @Override
    public void accept(Player player, OutputBuffer buffer) {
           /*builder.put(entity.getSecondaryHit().getDamage())
                                .put(entity.getSecondaryHit().getType().getId(), ByteValue.SUBTRACTION)
                                .put(entity.getSkill().getLevel(Skill.HITPOINTS))
                                .put(entity.getSkill().getMaxLevel(Skill.HITPOINTS), ByteValue.NEGATION);*/
    }
}
