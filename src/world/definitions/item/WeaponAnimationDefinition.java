package world.definitions.item;



import world.definitions.IDefinition;
import world.entity.player.WeaponAnimation;


/**
 * The type Weapon animation definition.
 */
public class WeaponAnimationDefinition implements IDefinition {
    private int id;
    private WeaponAnimation animation;


    @Override
    public int getId() {
        return id;
    }

    /**
     * Gets weapon anim.
     *
     * @return the weapon anim
     */
    public WeaponAnimation getWeaponAnim() {
        return animation;
    }
}
