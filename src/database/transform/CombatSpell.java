package database.transform;

import world.entity.player.Player;

import java.util.Optional;

public abstract class CombatSpell {
    public Optional<Animation> castAnimation() {
        return Optional.of(new Animation(711));
    }


    public Optional<Projectile> projectile(CharacterNode cast, CharacterNode castOn) {
        return Optional.of(new Projectile(cast, castOn, 91, 44, 3, 43, 31, 0));
    }


    public Optional<Graphic> endGraphic() {
        return Optional.of(new Graphic(92));
    }


    public int maximumHit() {
        return 2;
    }


    public Optional<Graphic> startGraphic() {
        return Optional.of(new Graphic(90, 6553600));
    }


    public double baseExperience() {
        return 5;
    }


    public Optional<Item[]> equipmentRequired(Player player) {
        return Optional.empty();
    }


    public Optional<Item[]> itemsRequired(Player player) {
        return Optional.of(new Item[]{new Item(556), new Item(558)});
    }


    public int levelRequired() {
        return 1;
    }

    public int spellId() {
        return 1152;
    }
}
