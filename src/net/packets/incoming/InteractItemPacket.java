package net.packets.incoming;

import net.buffers.InputBuffer;
import net.impl.session.Client;
import world.item.Item;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static net.packets.incoming.IncomingPacket.Opcodes.*;

public class InteractItemPacket extends IncomingPacket {
    private static final Logger logger = Logger.getLogger(BankPacket.class.getName());

    private final Set<Integer> opcodes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(ITEM_ACTION_1, ITEM_ACTION_3, EQUIP_ITEM, ALTERNATE_ITEM_OPTION, UNEQUIP_ITEM, MOVE_ITEM, DROP_ITEM_GROUND, LIGHT_ITEM)));

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {
        System.out.println("handling packet interact item");
        switch (packetOpcode) {
            case ITEM_ACTION_1:
                System.out.println("Item action 1");
                break;
            case ITEM_ACTION_3:
                System.out.println("Item action 3");
                break;
            case EQUIP_ITEM:
                if (in.remaining() < 6) return;
                equipItem(c, in.readBigUnsignedWord(), in.readBigUnsignedWordA(), in.readBigUnsignedWordA());
                break;
            case ALTERNATE_ITEM_OPTION:
                System.out.println("alternate item opt");
                break;
            case UNEQUIP_ITEM:
                if (in.remaining() < 6) return;
                unequipItem(c, in.readBigUnsignedWordA(), in.readBigUnsignedWordA(), in.readBigUnsignedWordA());
                break;
            case MOVE_ITEM:
                System.out.println("moveItem");
                break;
            case DROP_ITEM_GROUND:
                System.out.println("drop item");
                break;
        }
    }

    private void equipItem(Client c, int itemId, int itemSlot, int interfaceId) {
        if (interfaceId != 3214) {
            return;
        }
        System.out.println(itemId);
        System.out.println(itemSlot);
        System.out.println(interfaceId);

        Item item = c.getPlayer().getInventory().get(itemSlot);

        if (item == null) {
            System.out.println("item was null");
            return;
        }

        if (itemId != item.getId()) {
            System.out.println(item.getId());
            System.out.println("invalid item id");
            return;
        }

        System.out.println("adding item to equip");
        if (c.getPlayer().getEquipment().add(item)) {
            System.out.println("added item");
        } else {
            System.out.println("failed to add item");
        }
    }

    private void unequipItem(Client c, int interfaceId, int itemSlot, int itemId) {
        System.out.println(interfaceId);
        System.out.println(itemSlot);
        System.out.println(itemId);
        Item item = c.getPlayer().getEquipment().get(itemSlot);
        System.out.println(item == null);
        if (item == null) return;

        if (item.getId() != itemId) return;

        c.getPlayer().getEquipment().removeRef(item);
    }

    @Override
    public boolean handlesOpcode(int opcode) {
        return opcodes.contains(opcode);
    }

    @Override
    public Set<Integer> getOpcodes() {
        return opcodes;
    }
}
