package world.entity.combat;

import world.definitions.DefinitionLoader;
import world.definitions.item.ItemDefinition;
import world.definitions.item.WeaponInterfaceDefinition;
import world.definitions.item.WeaponPoisonDefinition;
import world.entity.Entity;
import world.entity.combat.CombatHandler;
import world.entity.combat.CombatStyle;
import world.entity.player.EquipmentSlot;
import world.entity.player.Player;
import world.interfaces.WeaponInterfaceType;
import world.item.Item;

public class MeleeAttackHandler implements CombatHandler.AttackHandler {
    @Override
    public boolean handleAttack(Entity attacking, Entity underAttack) {

        //Check is underattack is dead

        if(underAttack.getCombatHandler().isUnderAttack() && underAttack.getCombatHandler().getAttackingEntity() != attacking){
            return false;
        }

        if(!attacking.getPosition().isInViewingDistance(underAttack.getPosition()) ||
                attacking.getPosition().manhattanDistanceTo(underAttack.getPosition()) > 1){
            return false;
        }

        underAttack.getCombatHandler().setUnderAttack(true);

        if(underAttack.getCombatHandler().shouldAutoRetaliate()) {
            //Determine correct combat type?
            underAttack.getCombatHandler().attack(attacking);
        }

        if(!attacking.isPlayer()){
            return false;
        }

        Player p = (Player) attacking;
        Item weapon = p.getEquipment().get(EquipmentSlot.WEAPON.getSlotId());


        ItemDefinition itemDefinition;
        WeaponInterfaceDefinition weaponInterfaceDefinition;
        WeaponPoisonDefinition weaponPoisonDefinition;

        if(weapon != null) {
            itemDefinition = weapon.getItemDefinition();
            weaponInterfaceDefinition = DefinitionLoader.getDefinition(DefinitionLoader.WEAPON_INTERFACES, weapon.getId());
            weaponPoisonDefinition = DefinitionLoader.getDefinition(DefinitionLoader.WEAPON_POISONS, weapon.getId());
        }else{
            weaponInterfaceDefinition = DefinitionLoader.getDefinition(DefinitionLoader.WEAPON_INTERFACES, WeaponInterfaceType.UNARMED.getId());
        }


        if(weaponInterfaceDefinition == null){
            return false;
        }

        //int equipmentBonus = p.getEquipment().getEquipmentBonus();

        //Equipment should total weight and bonuses of equipped items for each player.

        CombatStyle c = p.getCombatStyle();
        int combatStyleAnimId = c.getAnimationId();
        int bonusModifer = c.getBonusModifier().getBonus();
        int weaponAttackSpeed = weaponInterfaceDefinition.getAttackSpeed();

       // weaponInterfaceDefinition.
        //Weapon poison

        //Weapon animations

            return true;
    }
}
