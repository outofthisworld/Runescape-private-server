package world.entity.player;

import java.util.Arrays;

public class Skills {
    private final int[] skills = new int[world.entity.player.Skill.values().length];
    private final int[] skillExp = new int[world.entity.player.Skill.values().length];
    private final Player p;

    public Skills(Player p) {
        this.p = p;
        Arrays.fill(skills,1);
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
        if (skillLevel < 1 || skillLevel > 99) {
            throw new IllegalArgumentException("Skill level must be between one and 99");
        }

        skills[skill.ordinal()] = skillLevel;
        skillExp[skill.ordinal()] = world.entity.player.Skill.getExpFromLevel(skillLevel);
        //c.getOutgoingPacketBuilder().updateSkill(skill.ordinal(), skills[skill.ordinal()], skillExp[skill.ordinal()]);
    }

    /**
     * Sets skill level.
     *
     * @param skillId    the skill id
     * @param skillLevel the skill level
     */
    public void setSkillLevel(int skillId, int skillLevel) {
        if (skillId < 0 || skillId >= skills.length) {
            throw new IllegalArgumentException("Invalid skill id");
        }

        if (skillLevel < 1 || skillLevel > 99) {
            throw new IllegalArgumentException("Skill level must be between one and 99");
        }


        skills[skillId] = skillLevel;
        skillExp[skillId] = world.entity.player.Skill.getExpFromLevel(skillLevel);
        //c.getOutgoingPacketBuilder().updateSkill(skillId, skills[skillId], skillExp[skillId]);
    }

    /**
     * Sets skill exp.
     *
     * @param skill the skill
     * @param exp   the exp
     */
    public void setSkillExp(Skill skill, int exp) {
        skills[skill.ordinal()] = world.entity.player.Skill.getLevelFromExp(exp);
        skillExp[skill.ordinal()] = exp;
        //c.getOutgoingPacketBuilder().updateSkill(skill.ordinal(), skills[skill.ordinal()], skillExp[skill.ordinal()]);
    }

    /**
     * Sets skill exp.
     *
     * @param skillId the skill id
     * @param exp     the exp
     */
    public void setSkillExp(int skillId, int exp) {
        if (skillId < 0 || skillId >= skillExp.length) {
            throw new IllegalArgumentException("Invalid skill id");
        }

        skills[skillId] = world.entity.player.Skill.getLevelFromExp(exp);
        skillExp[skillId] = exp;
        //c.getOutgoingPacketBuilder().updateSkill(skillId, skills[skillId], skillExp[skillId]);
    }
}
