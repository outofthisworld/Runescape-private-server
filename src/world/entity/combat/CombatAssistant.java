package world.entity.combat;

import world.definitions.DefinitionLoader;
import world.definitions.item.WeaponInterfaceDefinition;
import world.entity.player.EquipmentSlot;
import world.entity.player.Player;
import world.interfaces.WeaponInterfaceType;
import world.item.Item;

public final class CombatAssistant {
    /**
     * Determines a players current attack type.
     * @param player
     * @return
     */
    public static AttackType determinePlayerAttackType(Player player){
        AttackType attackType;

        if(player.isCastingASpell() || autoCastIsOn){
            attackType = AttackType.MAGIC;
        }else if(isUsingRangeWeapon(player)){
            attackType = AttackType.RANGE;
        }else{
            attackType = AttackType.MELEE;
        }

        return attackType;
    }

    /**
     * Determines if a player is using a ranged weapon.
     * @param player
     * @return
     */
    public static boolean isUsingRangeWeapon(Player player){
        Item weapon = player.getEquipment().get(EquipmentSlot.WEAPON.getSlotId());
        if(weapon == null) return false;
        WeaponInterfaceDefinition def = DefinitionLoader.getDefinition(DefinitionLoader.WEAPON_INTERFACES,weapon.getId());
        if(def == null) return false;
        return def.getInterfaceType() == WeaponInterfaceType.JAVELIN
                || def.getInterfaceType() == WeaponInterfaceType.CROSSBOW
                || def.getInterfaceType() == WeaponInterfaceType.SHORTBOW
                || def.getInterfaceType() == WeaponInterfaceType.LONGBOW
                || def.getInterfaceType() == WeaponInterfaceType.KNIFE
                || def.getInterfaceType() == WeaponInterfaceType.DART
                || def.getInterfaceType() == WeaponInterfaceType.THROWNAXE;
    }

    public static boolean isUsingMeleeWeapon(Player player){
        Item weapon = player.getEquipment().get(EquipmentSlot.WEAPON.getSlotId());
        if(weapon == null) return false;
        return !isUsingRangeWeapon(player);
    }

    public static boolean isUnarmed(Player player){
        return player.getEquipment().getContainer().isEmpty(EquipmentSlot.WEAPON.getSlotId());
    }
}
