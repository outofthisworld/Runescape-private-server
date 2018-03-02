package world.entity.player;

import util.Preconditions;
import world.entity.update.player.PlayerUpdateMask;

/**
 * A class which encapsulates everything to do with a players appearance
 * that is subsequently used in player updating during the player appearance update block.
 * <p>
 * The following are usages of this class:
 * Setting player head icon (prayers)
 * Setting player skull icon
 * Setting default player appearance
 * Setting player gender
 * Updating player clothing/colors
 */
public class Appearance {
    /**
     * The appearance indices.
     */
    private final int[] appearance = new int[AppearanceSlot.values().length];

    /**
     * The player this apperaence belongs to.
     */
    private final Player p;
    /**
     * The players gender 0 = male, 1 = female.
     */
    private Gender gender = Gender.MALE;
    /**
     * The head icon over the player, used for prayers.
     */
    private HeadIcon headIcon = HeadIcon.NONE;
    /**
     * The skull icon, used to indicate if this player has been fighting in wilderness.
     */
    private SkullIcon skullIcon = SkullIcon.NONE;

    private int hairColor = 7;
    private int torsoColor = 8;
    private int legColor = 9;
    private int feetColor = 5;
    private int skinColor = 0;


    public Appearance(Player p) {
        Preconditions.notNull(p);
        this.p = p;
        appearance[AppearanceSlot.HEAD.getId()] = 0;
        appearance[AppearanceSlot.CHEST.getId()] = 18;
        appearance[AppearanceSlot.ARMS.getId()] = 26;
        appearance[AppearanceSlot.HANDS.getId()] = 33;
        appearance[AppearanceSlot.LEGS.getId()] = 36;
        appearance[AppearanceSlot.FEET.getId()] = 42;
        appearance[AppearanceSlot.BEARD.getId()] = 10;
    }

    public Gender getGender() {
        return gender;
    }

    /**
     * Changes players gender and sets appearance update flag.
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public HeadIcon getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(HeadIcon headIcon) {
        this.headIcon = headIcon;
    }

    public SkullIcon getSkullIcon() {
        return skullIcon;
    }

    public void setSkullIcon(SkullIcon skullIcon) {
        this.skullIcon = skullIcon;
    }

    public int getAppearance(AppearanceSlot slot) {
        Preconditions.notNull(slot);
        return appearance[slot.getId()];
    }

    public void setHairColor(int hairColor) {
        this.hairColor = hairColor;
    }

    public void setTorsoColor(int torsoColor) {
        this.torsoColor = torsoColor;
    }

    public void setLegColor(int legColor) {
        this.legColor = legColor;
    }

    public void setFeetColor(int feetColor) {
        this.feetColor = feetColor;
    }

    public void setSkinColor(int skinColor) {
        this.skinColor = skinColor;
    }

    public int getHairColor() {
        return hairColor;
    }

    public int getTorsoColor() {
        return torsoColor;
    }

    public int getLegColor() {
        return legColor;
    }

    public int getFeetColor() {
        return feetColor;
    }

    public int getSkinColor() {
        return skinColor;
    }

    /**
        Called when players appearance has finished being modified to append
        the appearance update flag, which will sync the appearance with
        the client on the next update.
     */
    public void finishUpdateAppearance(){
        p.getUpdateFlags().setFlag(PlayerUpdateMask.APPEARANCE);
    }


    public enum Gender {
        MALE(0),
        FEMALE(1);

        private int id;

        Gender(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public enum SkullIcon {
        NONE(-1),
        WHITE_SKULL(0),
        RED_SKULL(1);

        private int id;

        SkullIcon(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }


    public enum HeadIcon {
        NONE(-1),
        PROTECT_FROM_MAGIC(2),
        PROTECT_FROM_MISSILES(1),
        PROTECT_FROM_MELEE(0),
        RETRIBUTION(3),
        REDEMPTION(5),
        SMITE(4);

        private int headIconId;

        HeadIcon(int headIconId) {
            this.headIconId = headIconId;
        }

        public int getId() {
            return headIconId;
        }
    }
}
