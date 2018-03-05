package world.definitions.player;


import world.definitions.types.Graphic;
import world.entity.player.combat.magic.Projectile;
import world.entity.player.combat.magic.SpellType;
import world.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SpellDefinition {
    private String spellName;
    private SpellType spellType;
    private int spellId;
    private int maximumHit;
    private int animation;
    private int baseExperience;
    private Projectile projectile;
    private Graphic startGraphic;
    private Graphic endGraphic;
    private List<Item> itemsRequired = new ArrayList<>();
    private List<Item> equipmentRequired = new ArrayList<>();

    public String getSpellName() {
        return spellName;
    }

    public SpellType getSpellType() {
        return spellType;
    }

    public int getSpellId() {
        return spellId;
    }

    public int getMaximumHit() {
        return maximumHit;
    }

    public int getAnimation() {
        return animation;
    }

    public int getBaseExperience() {
        return baseExperience;
    }

    public Projectile getProjectile() {
        return projectile;
    }

    public Graphic getStartGraphic() {
        return startGraphic;
    }

    public Graphic getEndGraphic() {
        return endGraphic;
    }

    public List<Item> getItemsRequired() {
        return Collections.unmodifiableList(itemsRequired);
    }

    public List<Item> getEquipmentRequired() {
        return Collections.unmodifiableList(equipmentRequired);
    }
}
