package world.entity.player.combat.magic;


import world.definitions.IDefinition;
import world.definitions.types.Graphic;
import world.entity.player.combat.magic.Projectile;
import world.entity.player.combat.magic.SpellType;
import world.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * The type CombatSpell.
 */
public class CombatSpell extends AbstractSpell {
    private int maximumHit;
    private Projectile projectile;
    private Graphic startGraphic;
    private Graphic endGraphic;

    public int getMaximumHit() {
        return maximumHit;
    }

    public void setMaximumHit(int maximumHit) {
        this.maximumHit = maximumHit;
    }


    public Projectile getProjectile() {
        return projectile;
    }

    public void setProjectile(Projectile projectile) {
        this.projectile = projectile;
    }

    public Graphic getStartGraphic() {
        return startGraphic;
    }

    public void setStartGraphic(Graphic startGraphic) {
        this.startGraphic = startGraphic;
    }

    public Graphic getEndGraphic() {
        return endGraphic;
    }

    public void setEndGraphic(Graphic endGraphic) {
        this.endGraphic = endGraphic;
    }
}
