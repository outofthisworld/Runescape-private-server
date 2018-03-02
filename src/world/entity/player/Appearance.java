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
     * The default appearance indices.
     */
    public static final int[] DEFAULT_APPEARANCE = {3, 19, 29, 35, 39, 44};

    /**
     * pHead = 3;
     * pTorso = 19;
     * pArms = 29;
     * pHands = 35;
     * pLegs = 39;
     * pFeet = 44;
     * The default clothes color indices.
     */
    public static final int[] DEFAULT_COLORS = {0, 5, 6, 4, 7, 10};

    /**
     * The appearance indices.
     */
    private final int[] appearance = new int[DEFAULT_APPEARANCE.length];

    /**
     * The clothes color indicates.
     */
    private final int[] colors = new int[DEFAULT_COLORS.length];

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


    public Appearance(Player p) {
        this.p = p;
    }

    public Gender getGender() {
        return gender;
    }

    /**
     * Changes players gender and sets appearance update flag.
     */
    public void setGender(Gender gender) {
        this.gender = gender;
        p.getUpdateFlags().setFlag(PlayerUpdateMask.APPEARANCE);
    }

    public HeadIcon getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(HeadIcon headIcon) {
        this.headIcon = headIcon;
        p.getUpdateFlags().setFlag(PlayerUpdateMask.APPEARANCE);
    }

    public SkullIcon getSkullIcon() {
        return skullIcon;
    }

    public void setSkullIcon(SkullIcon skullIcon) {
        this.skullIcon = skullIcon;
        p.getUpdateFlags().setFlag(PlayerUpdateMask.APPEARANCE);
    }

    public int getAppearance(AppearanceSlot slot) {
        Preconditions.notNull(slot);
        return appearance[slot.getId()];
    }

    public int getColor(AppearanceSlot slot) {
        Preconditions.notNull(slot);
        return colors[slot.getId()];
    }

    public void setDefault() {
        /*
         * The default appearance indices.
		 */


        for (int i = 0; i < Appearance.DEFAULT_APPEARANCE.length; i++) {

            appearance[i] = Appearance.DEFAULT_APPEARANCE[i];
        }

		/*
         * The default clothes color indices.
		 */
        for (int i = 0; i < Appearance.DEFAULT_COLORS.length; i++) {

            colors[i] = Appearance.DEFAULT_COLORS[i];
        }
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

        public int getHeadIconId() {
            return headIconId;
        }
    }
}
