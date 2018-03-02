package world.definitions.item;


import javafx.animation.Animation;
import world.definitions.IDefinition;


public class WeaponAnimationDefinition implements IDefinition {
    private int id;
    private Animation anim;


    @Override
    public int getId() {
        return id;
    }

    public Animation getWeaponAnim() {
        return anim;
    }
}
