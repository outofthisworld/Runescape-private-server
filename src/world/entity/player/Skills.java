package world.entity.player;

import util.Preconditions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

/**
 * The type Skills.
 */
public class Skills {
    private final int[] skills = new int[world.entity.player.Skill.values().length];
    private final int[] skillExp = new int[world.entity.player.Skill.values().length];
    private final Player p;
    private HashSet<Integer> changedSkills = new HashSet<>();

    /**
     * Instantiates a new Skills.
     *
     * @param p the p
     */
    public Skills(Player p) {
        this.p = p;
        Arrays.fill(skills, 1);
    }

    /**
     * Gets skill level.
     *
     * @param skillId the skill id
     * @return the skill level
     */
    public int getSkillLevel(int skillId) {
        if (skillId < 0 || skillId >= skills.length) {
            throw new IllegalArgumentException("Invalid skill id");
        }

        return skills[skillId];
    }


    /**
     * Gets skill level.
     *
     * @param skill the skill
     * @return the skill level
     */
    public int getSkillLevel(Skill skill) {
        return skills[skill.ordinal()];
    }


    /**
     * Gets skill exp.
     *
     * @param skill the skill
     * @return the skill exp
     */
    public int getSkillExp(Skill skill) {
        return skillExp[skill.ordinal()];
    }


    /**
     * Gets skill exp.
     *
     * @param skillId the skill id
     * @return the skill exp
     */
    public int getSkillExp(int skillId) {
        if (skillId < 0 || skillId >= skillExp.length) {
            throw new IllegalArgumentException("Invalid skill id");
        }


        return skillExp[skillId];
    }

    /**
     * Sets skill level.
     *
     * @param skill      the skill
     * @param skillLevel the skill level
     */
    public void setSkillLevel(Skill skill, int skillLevel) {
        Preconditions.notNull(skill);
        Preconditions.greaterThan(skillLevel, 0);
        Preconditions.lessThanOrEqualTo(skillLevel, 99);

        skills[skill.ordinal()] = skillLevel;
        skillExp[skill.ordinal()] = world.entity.player.Skill.getExpFromLevel(skillLevel);
        changedSkills.add(skill.ordinal());
    }

    /**
     * Sets skill level.
     *
     * @param skillId    the skill id
     * @param skillLevel the skill level
     */
    public void setSkillLevel(int skillId, int skillLevel) {
        setSkillLevel(Skill.fromIndex(skillId), skillLevel);
    }

    /**
     * Sets skill exp.
     *
     * @param skill the skill
     * @param exp   the exp
     */
    public void setSkillExp(Skill skill, int exp) {
        Preconditions.notNull(skill);
        Preconditions.greaterThan(exp, 0);
        skills[skill.ordinal()] = world.entity.player.Skill.getLevelFromExp(exp);
        skillExp[skill.ordinal()] = exp;
        changedSkills.add(skill.ordinal());
    }

    /**
     * Finish updating.
     * Instead of sending a packet for every skill change, this method can be called
     * to update a set of skills in bulk.
     */
    public void finishUpdating() {
        Iterator<Integer> it = changedSkills.iterator();
        for (; it.hasNext(); ) {
            Integer i = it.next();
            updateSkill(i);
            it.remove();
        }
        p.getClient().getOutgoingPacketBuilder().send();
    }

    /**
     * Sets skill exp.
     *
     * @param skillId the skill id
     * @param exp     the exp
     */
    public void setSkillExp(int skillId, int exp) {
        setSkillExp(Skill.fromIndex(skillId), exp);
    }

    /**
     * Sync all.
     */
    public void syncAll() {
        Skill[] skills = Skill.values();
        for (int i = 0; i < skills.length; i++) {
            updateSkill(i);
        }
    }

    private void updateSkill(int skillId) {
        Preconditions.notNull(Skill.fromIndex(skillId));
        p.getClient().getOutgoingPacketBuilder().setSkillLevel(skillId, getSkillLevel(skillId), getSkillExp(skillId));
    }

    private void updateSkill(int skillId, int level, int exp) {
        p.getClient().getOutgoingPacketBuilder().setSkillLevel(skillId, level, exp);
    }
}
