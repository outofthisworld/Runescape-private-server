/*
 Project by outofthisworld24
 All rights reserved.
 */

/*
 * Project by outofthisworld24
 * All rights reserved.
 */

/*------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 Project by outofthisworld24
 All rights reserved.
 -----------------------------------------------------------------------------*/

package world.definitions;

import database.CollectionAccessor;
import database.DatabaseConfig;
import database.IDBAccessor;
import database.serialization.GsonSerializer;

import java.util.Collections;
import java.util.List;

public final class ItemDefinition {

    private static final IDBAccessor<ItemDefinition> itemDB = new CollectionAccessor<>(new GsonSerializer<>(ItemDefinition.class), DatabaseConfig.ITEMS_COLLECTION);
    private static List<ItemDefinition> itemDefinitions = null;

    static {
        if (ItemDefinition.itemDefinitions == null) {
            ItemDefinition.itemDefinitions = Collections.unmodifiableList(ItemDefinition.itemDB.findAll());
        }
    }

    private final int[] bonuses = new int[18];
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

    private ItemDefinition() {
    }

    public static ItemDefinition getForId(int id) {
        if (ItemDefinition.itemDefinitions == null || id < 0 || id >= ItemDefinition.itemDefinitions.size()) {
            return null;
        }

        return ItemDefinition.itemDefinitions.get(id);
    }

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

    public boolean isNotable() {
        return noteId != -1;
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

    public int getSellPrice() {
        return sellPrice;
    }

    public boolean isMembersOnly() {
        return membersOnly;
    }

    public boolean isQuestItem() {
        return questItem;
    }

    public int getHighAlchValue() {
        return highAlchValue;
    }

    public int getLowAlchValue() {
        return lowAlchValue;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isEquipable() {
        return equipable;
    }

    public int getSlotId() {
        return slotId;
    }
}