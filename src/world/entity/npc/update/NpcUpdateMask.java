package world.entity.npc.update;

public enum NpcUpdateMask {
    /**
     * The Animation.
     */
    ANIMATION(0x10),
    /**
     * The Double hit.
     */
    DOUBLE_HIT(0x8),
    /**
     * The Graphics.
     */
    GRAPHICS(0x80),
    /**
     * The Entity interaction.
     */
    FACE_CHARACTER(0x20),
    /**
     * The Forced chat.
     */
    FORCED_CHAT(0x1),
    /**
     * The Single hit.
     */
    SINGLE_HIT(0x40),
    /**
     * The Transform.
     */
    TRANSFORM(0x2),
    /**
     * The Face coordinate.
     */
    FACE_COORDINATE(0x4);

    private final long mask;

    NpcUpdateMask(int mask) {
        this.mask = mask;
    }

    public long getMask() {
        return mask;
    }
}
