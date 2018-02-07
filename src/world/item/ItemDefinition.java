package world.item;

public class ItemDefinition {

    private final int[] bonuses = new int[14];
    private int id;
    private String name;
    private String examine;
    private int buyPrice;
    private int sellPrice;
    private boolean stackable;
    private boolean noted;
    private int noteId;
    private boolean doubleHanded;
    private String equipmentType;
    private boolean tradeable;
    private boolean sellable;
    private boolean dropable;
    private boolean membersOnly;
    private boolean questItem;
    private int highAlchValue;
    private int lowAlchValue;
    private int weight;
    private boolean equipable;
    private int slotId;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExamine() {
        return examine;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public boolean isStackable() {
        return stackable;
    }

    public boolean isNoted() {
        return noted;
    }

    public int getNoteId() {
        return noteId;
    }

    public boolean isDoubleHanded() {
        return doubleHanded;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public boolean isTradeable() {
        return tradeable;
    }

    public boolean isSellable() {
        return sellable;
    }

    public boolean isDropable() {
        return dropable;
    }

    public int[] getBonuses() {
        return bonuses;
    }
}