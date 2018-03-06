package net.packets.incoming;

import net.buffers.InputBuffer;
import net.impl.session.Client;
import util.integrity.Debug;
import world.definitions.DefinitionLoader;
import world.definitions.item.WeaponInterfaceDefinition;
import world.entity.player.equipment.EquipmentSlot;
import world.entity.player.combat.CombatStyle;
import world.interfaces.WeaponInterfaceType;
import world.item.Item;

import java.util.*;
import java.util.logging.Logger;

import static net.packets.incoming.IncomingPacket.Opcodes.*;

public class InteractInterfacePacket extends IncomingPacket {
    public static final int AUTO_RETALIATE = 22845;
    public static final int LOG_OUT = 2458;
    public static final int ACCEPT_AID_ON = 12591;
    public static final int ACCEPT_AID_OFF = 12590;
    public static final int PRICE_CHECKER = 27651;
    public static final int ITEMS_KEPT_ON_DEATH = 27654;
    public static final int EMOTE_YES = 168;
    public static final int EMOTE_NO = 169;
    public static final int EMOTE_BOW = 164;
    public static final int EMOTE_ANGRY = 167;
    private static final Logger logger = Logger.getLogger(BankPacket.class.getName());
    private final Set<Integer> opcodes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(INTERFACE_BUTTON_CLICK, CLOSE_INTERFACE, TYPING_INTO_INTERFACE)));

    @Override
    public void handle(Client c, int packetOpcode, InputBuffer in) throws Exception {
        switch (packetOpcode) {
            case INTERFACE_BUTTON_CLICK:

                int interfaceButtonId = in.readBigSignedWord();
                Debug.writeLine("Interface button click : " + interfaceButtonId);

                /*Handle weapon interface buttons*/
                Item weapon = c.getPlayer().getEquipment().get(EquipmentSlot.WEAPON.getSlotId());
                if (weapon != null) {
                    int weaponId = weapon.getId();
                    WeaponInterfaceDefinition def = DefinitionLoader.getDefinition(DefinitionLoader.WEAPON_INTERFACES, weaponId);

                    CombatStyle[] styles = def.getCombatStlyes();
                    for (CombatStyle s : styles) {
                        if (s.getGameButtonId() == interfaceButtonId) {
                            Debug.writeLine("Setting player combat style to : " + s.getStyle().name() + " " + s.getBonusModifier().name());
                            c.getPlayer().setCombatStyle(s);
                            return;
                        }
                    }
                } else {
                    WeaponInterfaceDefinition def = DefinitionLoader.getDefinition(DefinitionLoader.WEAPON_INTERFACES, WeaponInterfaceType.UNARMED.getId());
                    Optional<CombatStyle> opt = Arrays.stream(def.getCombatStlyes()).filter(e -> e.getGameButtonId() == interfaceButtonId).findFirst();

                    if (opt.isPresent()) {
                        CombatStyle s = opt.get();
                        Debug.writeLine("Setting player combat style to : " + s.getStyle().name() + " " + s.getBonusModifier().name());
                        c.getPlayer().setCombatStyle(s);
                        return;
                    }
                }
                /* End handling weapon interface buttons */

                /*Handle prayers*/


                /*End handling prayers*/

                /*Handle magic*/

                /*End handling magic*/


                /* Handle location e.g anything that's not weapon,prayer and magic. */
                switch (interfaceButtonId) {

                }


                break;
        }
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
