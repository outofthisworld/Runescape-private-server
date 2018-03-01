
package world.definitions.item;

import world.definitions.IDefinition;


public final class ItemDefinition implements IDefinition {
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
    private final int[] bonuses = new int[18];
    private boolean twoHanded;
    private boolean platebody;
    private boolean fullHelm;
    private boolean tradeable;

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExamine() {
        return examine;
    }

    public int getEquipmentType() {
        return equipmentType;
    }

    public boolean isNoted() {
        return noted;
    }

    public boolean isNotable() {
        return notable;
    }

    public boolean isStackable() {
        return stackable;
    }

    public int getParentId() {
        return parentId;
    }

    public int getNotedId() {
        return notedId;
    }

    public boolean isMembers() {
        return members;
    }

    public int getSpecifialStorePrice() {
        return specifialStorePrice;
    }

    public int getGeneralStorePrice() {
        return generalStorePrice;
    }

    public int getLowAlchValue() {
        return lowAlchValue;
    }

    public int getHighAlchValue() {
        return highAlchValue;
    }

    public int getWeight() {
        return weight;
    }

    public int[] getBonuses() {
        return bonuses;
    }

    public boolean isTwoHanded() {
        return twoHanded;
    }

    public boolean isPlatebody() {
        return platebody;
    }

    public boolean isFullHelm() {
        return fullHelm;
    }

    public boolean isTradeable() {
        return tradeable;
    }
}