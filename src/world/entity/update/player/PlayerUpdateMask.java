package world.entity.update.player;


public enum PlayerUpdateMask {
    /**
     * The Graphics.
     */
    GRAPHICS(0x100),
    /**
     * The Animation.
     */
    ANIMATION(0x8),
    /**
     * The Forced chat.
     */
    FORCED_CHAT(0x4),
    /**
     * The Chat.
     */
    CHAT(0x80),
    /**
     * The Entity interaction.
     */
    ENTITY_INTERACTION(0x1),
    /**
     * The constant APPEARANCE.
     */
    APPEARANCE(0x10),
    /**
     * The Force movement.
     */
    FORCE_MOVEMENT(0x400),
    /**
     * The Face coordinate.
     */
    FACE_COORDINATE(0x2),
    /**
     * The Single hit.
     */
    SINGLE_HIT(0x20),
    /**
     * The Double hit.
     */
    DOUBLE_HIT(0x200);

    private final long mask;

    PlayerUpdateMask(long mask) {
        this.mask = mask;
    }

    public long getMask() {
        return mask;
    }
}

