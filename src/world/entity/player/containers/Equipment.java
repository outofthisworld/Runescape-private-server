package world.entity.player.containers;

import net.packets.outgoing.OutgoingPacketBuilder;
import util.Debug;
import util.FormatStrings;
import util.Preconditions;
import world.definitions.DefinitionLoader;
import world.definitions.item.ItemRequirementDefinition;
import world.definitions.item.WeaponInterfaceDefinition;
import world.definitions.types.ItemRequirement;
import world.entity.player.EquipmentSlot;
import world.entity.player.Player;
import world.entity.player.Skill;
import world.item.Item;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Equipment extends AbstractGameContainer<Item> {

    public Equipment(Player p) {
        super(p, EquipmentSlot.values().length, 1688, Item.class);
    }


    @Override
    public boolean add(int itemId, int amount) {
        Item i = new Item(itemId, amount);
        return set(i.getItemDefinition().getEquipmentType(), i, i::equals);
    }

    /**
     * Adds an item present from a users inventory into the specfied equipment slot.
     *
     * @param item
     * @return
     */
    @Override
    public boolean add(Item item) {
        Preconditions.notNull(item);
        return set(item.getItemDefinition().getEquipmentType(), item, (i) -> i == item);
    }

    @Override
    public boolean remove(int slotId) {
        return remove(slotId, get(slotId) == null ? 0 : get(slotId).getAmount());
    }

    @Override
    public boolean remove(int slotId, int amount) {
        if (slotId < 0 || slotId >= capacity() || amount <= 0)
            return false;

        Item item = getContainer().get(slotId);

        if (item == null) {
            return false;
        }

        sync(slotId, null);

        if (item.getItemDefinition().isStackable()) {
            Inventory inv = getOwner().getInventory();

            OptionalInt slot = IntStream.range(0, capacity()).filter(i -> {
                //Filter the ones that aren't this item
                if (inv.getContainer().get(i) == null || inv.get(i).getId() != item.getId()) {
                    return false;
                }

                long newAmount = item.getAmount() + inv.get(i).getAmount();
                return newAmount <= Integer.MAX_VALUE;
            }).findFirst();

            if (slot.isPresent()) {
                //Append to the found stackable item
                inv.set(slot.getAsInt(), new Item(item.getId(),
                        item.getAmount() + inv.getContainer().get(slot.getAsInt()).getAmount()));
            } else {
                inv.add(item);
            }
        } else {
            getOwner().getInventory().add(item);
        }

        return false;
    }

    @Override
    public boolean removeEqual(Item item) {
        return remove(indexOfEquals(item), item.getAmount());
    }

    @Override
    public boolean removeEqual(Item item, int amount) {
        return remove(indexOfEquals(item), amount);
    }

    @Override
    public boolean removeRef(Item item) {
        return remove(indexOfRef(item), item.getAmount());
    }

    @Override
    public boolean removeRef(Item item, int amount) {
        return remove(indexOfRef(item), amount);
    }

    @Override
    public boolean set(int slotId, int itemId, int amount) {
        Item i = new Item(itemId, amount);
        return set(slotId, new Item(itemId, amount), i::equals);
    }

    private boolean set(int slotId, Item item, Predicate<Item> pred) {
        Preconditions.notNull(item, pred);

        if (slotId < 0 || slotId >= capacity()) {
            System.out.println("slot id wrong " + slotId);
            return false;
        }

        int inventoryItemSlot = -1;
        for (int i = 0; i < getOwner().getInventory().getContainer().capacity(); i++) {
            if (pred.test(getOwner().getInventory().getContainer().get(i))) {
                inventoryItemSlot = i;
                break;
            }
        }

        if (inventoryItemSlot == -1) {
            System.out.println("failed to find inventoryItemSlot");
            return false;
        }

        System.out.println("slot:" + slotId);
        //Check to see its a valid slot
        EquipmentSlot slot = EquipmentSlot.fromIndex(slotId);

        if (slot == null) {
            System.out.println("slot was null");
            return false;
        }

        Item equipped = getContainer().get(slotId);
        Item inventoryItem = getOwner().getInventory().get(inventoryItemSlot);

        if (inventoryItem == null) {
            System.out.println("inventoryItem was null");
            return false;
        }

        ItemRequirementDefinition itemReqDef = DefinitionLoader.getDefinition(DefinitionLoader.ITEM_REQUIREMENTS, inventoryItem.getId());

        if (itemReqDef != null) {
            Debug.writeLine("Found item requirement for item " + inventoryItem.getItemDefinition().getName() + "(" + inventoryItem.getId() + ")");
            Optional<ItemRequirement> requirement = itemReqDef.getRequirementsImmutable().stream().filter(e -> {
                Skill skill = Skill.valueOf(e.getSkill());
                int level = getOwner().getSkills().getSkillLevel(skill);
                int requiredLevel = e.getLevel();
                return level < requiredLevel;
            }).findFirst();

            if (requirement.isPresent()) {
                ItemRequirement iReq = requirement.get();
                getOwner().getClient().getOutgoingPacketBuilder().sendMessage(
                        FormatStrings.itemRequirement(iReq.getSkill().toLowerCase(), iReq.getLevel(), inventoryItem.getItemDefinition().getName()))
                        .send();

                return false;
            }
        } else {
            Debug.writeLine("Did not find item requirement for item " + inventoryItem.getItemDefinition().getName() + "(" + inventoryItem.getId() + ")");
        }

        if (slot == EquipmentSlot.WEAPON) {
            WeaponInterfaceDefinition def = DefinitionLoader.getDefinition(DefinitionLoader.WEAPON_INTERFACES, inventoryItem.getId());
            OutgoingPacketBuilder out = getOwner().getClient().getOutgoingPacketBuilder();
            if (def != null) {
                out.setSidebarInterface(0, def.getInterfaceId());
            } else {
                //Unarmed.
                out.setSidebarInterface(0, 5855);
            }
        }


        if (equipped == null) {
            getOwner().getInventory().removeRef(inventoryItem);
            sync(slotId, inventoryItem);
            return true;
        }

        //If this item is the same... add to it
        //If we cant add to it because it will overflow add as much as possible
        //If the item is different, remove the item in the slot to the inventory position it is being replaced by
        //and the inventory item into the slot.

        if (inventoryItem.getId() == equipped.getId() && inventoryItem.getItemDefinition().isStackable()) {

            if (equipped.canAddAmount(inventoryItem.getAmount())) {
                getOwner().getInventory().removeRef(inventoryItem);
                sync(slotId, equipped);
            } else {
                int remaining = Integer.MAX_VALUE - equipped.getAmount();
                if (remaining > 0) {
                    getOwner().getInventory().removeRef(inventoryItem, remaining);
                    equipped.canAddAmount(remaining);
                    sync(slotId, equipped);
                }
            }

        } else {
            getContainer().set(slotId, inventoryItem);
            getOwner().getInventory().set(inventoryItemSlot, equipped);
        }
        return true;
    }

    @Override
    public boolean set(int slotId, Item item) {
        return set(slotId, item, (i) -> i == item);
    }
}
