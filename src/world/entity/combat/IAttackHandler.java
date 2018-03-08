package world.entity.combat;



import java.util.function.Function;
public interface IAttackHandler extends Function<AttackType,CombatHandler.AttackHandler> {
}
