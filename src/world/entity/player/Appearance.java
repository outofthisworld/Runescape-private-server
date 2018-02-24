package world.entity.player;

import util.Preconditions;

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
    private final Player p;
    private int gender = 0;


    public Appearance(Player p) {
        this.p = p;
    }

    public int getGender() {
        return gender;
    }


    public int getAppearence(AppearanceSlot slot) {
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

		/*
         * The default gender.
		 */
        gender = 0;
    }
}
