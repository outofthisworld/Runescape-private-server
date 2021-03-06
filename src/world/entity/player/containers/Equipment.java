package world.entity.player.containers;

import net.packets.outgoing.OutgoingPacketBuilder;
import util.integrity.Debug;
import util.integrity.Preconditions;
import util.strings.FormatStrings;
import world.definitions.DefinitionLoader;
import world.definitions.item.ItemRequirementDefinition;
import world.definitions.item.WeaponInterfaceDefinition;
import world.entity.player.Player;
import world.entity.player.EquipmentSlot;
import world.entity.player.Skill;
import world.entity.player.update.PlayerUpdateMask;
import world.interfaces.SidebarInterface;
import world.interfaces.WeaponInterfaceType;
import world.item.Item;
import world.item.ItemRequirement;

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

        getOwner().getUpdateFlags().setFlag(PlayerUpdateMask.APPEARANCE);

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

        if (slot == EquipmentSlot.WEAPON) {
            setWeaponInterface(inventoryItem);
        }
        return true;
    }

    @Override
    public boolean set(int slotId, Item item) {
        return set(slotId, item, (i) -> i == item);
    }

    private void setWeaponInterface(Item item) {
        if (item != null) {
            WeaponInterfaceDefinition def = DefinitionLoader.getDefinition(DefinitionLoader.WEAPON_INTERFACES, item.getId());
            if (def != null) {
                OutgoingPacketBuilder out = getOwner().getClient().getOutgoingPacketBuilder();
                out.setSidebarInterface(SidebarInterface.ATTACK_TYPE.ordinal(), def.getInterfaceId());
                out.sendInterfaceText(item.getItemDefinition().getName(), def.getNameLineId());
                return;
            }
        }

        //Unarmed.
        OutgoingPacketBuilder out = getOwner().getClient().getOutgoingPacketBuilder();
        WeaponInterfaceDefinition weaponIDef = DefinitionLoader.getDefinition(DefinitionLoader.WEAPON_INTERFACES, WeaponInterfaceType.UNARMED.getId());
        out.sendInterfaceText(Character.toUpperCase(weaponIDef.getInterfaceName().toCharArray()[0])
                + weaponIDef.getInterfaceName().toLowerCase().substring(1, weaponIDef.getInterfaceName().length()), weaponIDef.getNameLineId());
        out.setSidebarInterface(SidebarInterface.ATTACK_TYPE.ordinal(), weaponIDef.getId());
    }

    @Override
    public void syncAll() {
        super.syncAll();
        setWeaponInterface(get(EquipmentSlot.WEAPON.getSlotId()));
    }
}
