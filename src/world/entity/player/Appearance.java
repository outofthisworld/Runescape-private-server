package world.entity.player;

public class Appearance {
    /**
     * The default appearance indices.
     */
    public static final int[] DEFAULT_APPEARANCE = {18, 26, 36, 0, 33, 42, 10};

    /**
     * The default clothes color indices.
     */
    public static final int[] DEFAULT_COLORS = {7, 8, 9, 5, 0};

    /**
     * The appearance indices.
     */
    private final int[] appearance = new int[13];

    /**
     * The clothes color indicates.
     */
    private final int[] colors = new int[5];
    private final Player p;
    private int gender = 0;


    public Appearance(Player p) {
        this.p = p;
    }

    public int getGender() {
        return gender;
    }


    public int getAppearenceIndice(int i) {
        return Appearance.DEFAULT_APPEARANCE[i];
    }

    public int getColorIndice(int i) {
        return Appearance.DEFAULT_APPEARANCE[i];
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
