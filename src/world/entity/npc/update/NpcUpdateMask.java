package world.entity.npc.update;

public enum NpcUpdateMask {
    /**
     * The Graphics.
     */
    GRAPHICS(0x80),
    /**
     * The Animation.
     */
    ANIMATION(0x10),
    /**
     * The Forced chat.
     */
    FORCED_CHAT(0x1),
    /**
     * The Entity interaction.
     */
    ENTITY_INTERACTION(0x20),
    /**
     * The Face coordinate.
     */
    FACE_COORDINATE(0x4),
    /**
     * The Transform.
     */
    TRANSFORM(0x2),
    /**
     * The Single hit.
     */
    SINGLE_HIT(0x40),
    /**
     * The Double hit.
     */
    DOUBLE_HIT(0x8);

    private final int mask;

    NpcUpdateMask(int mask) {
        this.mask = mask;
    }

    public int getMask() {
        return mask;
    }
}
