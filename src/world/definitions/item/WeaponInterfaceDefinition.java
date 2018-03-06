package world.definitions.item;

import world.definitions.IDefinition;
import world.entity.player.combat.CombatStyle;
import world.interfaces.WeaponInterfaceType;

import java.util.ArrayList;
import java.util.Objects;


/**
 * The type Weapon interface definition.
 */
public class WeaponInterfaceDefinition implements IDefinition {
    private int interfaceId;
    private WeaponInterfaceType interfaceType;
    private int attackSpeed;
    private int nameLineId;
    private int specialBarId;
    private int specialMeterId;
    private ArrayList<Integer> weaponIds = new ArrayList<>();
    private CombatStyle[] combatStyles = new CombatStyle[4];


    public int getId() {
        return Objects.hash(interfaceType.name());
    }

    /**
     * Gets interface id.
     *
     * @return the interface id
     */
    public int getInterfaceId() {
        return interfaceId;
    }

    /**
     * Gets interface type.
     *
     * @return the interface type
     */
    public WeaponInterfaceType getInterfaceType() {
        return interfaceType;
    }

    /**
     * Gets attack speed.
     *
     * @return the attack speed
     */
    public int getAttackSpeed() {
        return attackSpeed;
    }

    /**
     * Gets name line id.
     *
     * @return the name line id
     */
    public int getNameLineId() {
        return nameLineId;
    }

    /**
     * Gets special bar id.
     *
     * @return the special bar id
     */
    public int getSpecialBarId() {
        return specialBarId;
    }

    /**
     * Gets special meter id.
     *
     * @return the special meter id
     */
    public int getSpecialMeterId() {
        return specialMeterId;
    }

    /**
     * Gets weapon ids.
     *
     * @return the weapon ids
     */
    public ArrayList<Integer> getWeaponIds() {
        return weaponIds;
    }

    /**
     * Get combat stlyes combat style [ ].
     *
     * @return the combat style [ ]
     */
    public CombatStyle[] getCombatStlyes() {
        return combatStyles;
    }

    /**
     * Gets interface name.
     *
     * @return the interface name
     */
    public String getInterfaceName() {
        return interfaceType.name();
    }
}
