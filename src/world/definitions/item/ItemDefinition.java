package world.definitions.item;

import world.definitions.IDefinition;
import world.entity.player.equipment.EquipmentSlot;


/**
 * The type Item definition.
 */
public final class ItemDefinition implements IDefinition {
    private final int[] bonuses = new int[18];
    private int id;
    private String name;
    private String examine;
    private int equipmentType;
    private boolean noted;
    private boolean notable;
    private boolean stackable;
    private int parentId;
    private int notedId;
    private boolean members;
    private int specifialStorePrice;
    private int generalStorePrice;
    private int lowAlchValue;
    private int highAlchValue;
    private int weight;
    private boolean twoHanded;
    private boolean platebody;
    private boolean fullHelm;
    private boolean tradeable;

    @Override
    public int getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets examine.
     *
     * @return the examine
     */
    public String getExamine() {
        return examine;
    }

    /**
     * Gets equipment type.
     *
     * @return the equipment type
     */
    public int getEquipmentType() {
        return equipmentType;
    }

    /**
     * Get equipment slot equipment slot.
     *
     * @return the equipment slot
     */
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.fromIndex(equipmentType);
    }

    /**
     * Is noted boolean.
     *
     * @return the boolean
     */
    public boolean isNoted() {
        return noted;
    }

    /**
     * Is notable boolean.
     *
     * @return the boolean
     */
    public boolean isNotable() {
        return notable;
    }

    /**
     * Is stackable boolean.
     *
     * @return the boolean
     */
    public boolean isStackable() {
        return stackable;
    }

    /**
     * Gets parent id.
     *
     * @return the parent id
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * Gets noted id.
     *
     * @return the noted id
     */
    public int getNotedId() {
        return notedId;
    }

    /**
     * Is members boolean.
     *
     * @return the boolean
     */
    public boolean isMembers() {
        return members;
    }

    /**
     * Gets specifial store price.
     *
     * @return the specifial store price
     */
    public int getSpecifialStorePrice() {
        return specifialStorePrice;
    }

    /**
     * Gets general store price.
     *
     * @return the general store price
     */
    public int getGeneralStorePrice() {
        return generalStorePrice;
    }

    /**
     * Gets low alch value.
     *
     * @return the low alch value
     */
    public int getLowAlchValue() {
        return lowAlchValue;
    }

    /**
     * Gets high alch value.
     *
     * @return the high alch value
     */
    public int getHighAlchValue() {
        return highAlchValue;
    }

    /**
     * Gets weight.
     *
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Get bonuses int [ ].
     *
     * @return the int [ ]
     */
    public int[] getBonuses() {
        return bonuses;
    }

    /**
     * Is two handed boolean.
     *
     * @return the boolean
     */
    public boolean isTwoHanded() {
        return twoHanded;
    }

    /**
     * Is platebody boolean.
     *
     * @return the boolean
     */
    public boolean isPlatebody() {
        return platebody;
    }

    /**
     * Is full helm boolean.
     *
     * @return the boolean
     */
    public boolean isFullHelm() {
        return fullHelm;
    }

    /**
     * Is tradeable boolean.
     *
     * @return the boolean
     */
    public boolean isTradeable() {
        return tradeable;
    }
}