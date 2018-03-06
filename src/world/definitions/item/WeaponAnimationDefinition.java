package world.definitions.item;


import javafx.animation.Animation;
import world.definitions.IDefinition;


/**
 * The type Weapon animation definition.
 */
public class WeaponAnimationDefinition implements IDefinition {
    private int id;
    private Animation anim;


    @Override
    public int getId() {
        return id;
    }

    /**
     * Gets weapon anim.
     *
     * @return the weapon anim
     */
    public Animation getWeaponAnim() {
        return anim;
    }
}
