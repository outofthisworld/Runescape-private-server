package world.combat;

import world.definitions.DefinitionLoader;
import world.definitions.item.ItemDefinition;
import world.definitions.item.WeaponInterfaceDefinition;
import world.definitions.item.WeaponPoisonDefinition;
import world.entity.Entity;
import world.entity.player.Player;
import world.entity.player.combat.AttackStyle;
import world.entity.player.combat.CombatStyle;
import world.entity.player.containers.Equipment;
import world.entity.player.equipment.EquipmentSlot;
import world.interfaces.WeaponInterfaceType;
import world.item.Item;

public enum AttackType implements CombatHandler.AttackHandler{
    MELEE {
        private int x;

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
                attacking.getCombatHandler().setUnderAttack(true);
                underAttack.getCombatHandler().attack(attacking, MELEE);
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

            int equipmentBonus = p.getEquipment().getEquipmentBonus();

            //Equipment should total weight and bonuses of equipped items for each player.
            int[] itemBonuses = itemDefinition.getBonuses();

            CombatStyle c = p.getCombatStyle();
            int combatStyleAnimId = c.getAnimationId();
            int bonusModifer = c.getBonusModifier().getBonus();
            int weaponAttackSpeed = weaponInterfaceDefinition.getAttackSpeed();

            weaponInterfaceDefinition.
            //Weapon poison
            if(weapon.getItemDefinition().)
            //Weapon animations

            return true;
        }
    },
    RANGE {
        @Override
        public boolean handleAttack(Entity attacking, Entity underAttack) {
            return false;
        }
    },
    MAGIC {
        @Override
        public boolean handleAttack(Entity attacking, Entity underAttack) {
            return false;
        }
    }
}
