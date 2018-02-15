package world.entity.player;

import util.Preconditions;

import java.util.*;

/**
 * The type Update flags.
 */
public abstract class UpdateFlags {

    private static final Map<UpdateFlagsType, Set<Integer>> flags;

    /*
        Constructs the static flags map
        Used for checking that the flags are valid at run-time.
     */
    static {
        flags = Collections.unmodifiableMap(new HashMap<UpdateFlagsType, Set<Integer>>() {
            {
                put(UpdateFlagsType.PLAYER, Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                        PlayerUpdateFlags.ANIMATION,
                        PlayerUpdateFlags.APPEARANCE,
                        PlayerUpdateFlags.CHAT,
                        PlayerUpdateFlags.DOUBLE_HIT,
                        PlayerUpdateFlags.SINGLE_HIT,
                        PlayerUpdateFlags.ENTITY_INTERACTION,
                        PlayerUpdateFlags.FACE_COORDINATE,
                        PlayerUpdateFlags.FORCE_MOVEMENT,
                        PlayerUpdateFlags.FORCED_CHAT,
                        PlayerUpdateFlags.GRAPHICS
                ))));

                put(UpdateFlagsType.NPC, Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                        NpcUpdateFlags.ANIMATION,
                        NpcUpdateFlags.DOUBLE_HIT,
                        NpcUpdateFlags.SINGLE_HIT,
                        NpcUpdateFlags.ENTITY_INTERACTION,
                        NpcUpdateFlags.FACE_COORDINATE,
                        NpcUpdateFlags.TRANSFORM,
                        NpcUpdateFlags.FORCED_CHAT,
                        NpcUpdateFlags.GRAPHICS
                ))));
            }
        });
    }

    private final UpdateFlagsType type;
    /**
     * The Mask.
     */
    protected int mask;

    /**
     * Instantiates a new Update flags.
     *
     * @param type the type
     */
    public UpdateFlags(UpdateFlagsType type) {
        Preconditions.notNull(type);
        this.type = type;
    }

    /**
     * Sets flag.
     *
     * @param flag the flag
     */
    public void setFlag(int flag) {
        Preconditions.setContains(flag, UpdateFlags.flags.get(type));
        mask |= flag;
    }

    /**
     * Toggle flag.
     *
     * @param flag the flag
     */
    public void toggleFlag(int flag) {
        Preconditions.setContains(flag, UpdateFlags.flags.get(type));
        mask ^= flag;
    }

    /**
     * Unset flag.
     *
     * @param flag the flag
     */
    public void unsetFlag(int flag) {
        Preconditions.setContains(flag, UpdateFlags.flags.get(type));
        mask &= (~flag);
    }

    /**
     * Is set boolean.
     *
     * @param flag the flag
     * @return the boolean
     */
    public boolean isSet(int flag) {
        Preconditions.setContains(flag, UpdateFlags.flags.get(type));
        return (mask & flag) == flag;
    }

    /**
     * Any set boolean.
     *
     * @return the boolean
     */
    public boolean anySet() {
        return mask > 0;
    }

    /**
     * Clear.
     */
    public void clear() {
        mask = 0;
    }

    /**
     * Gets mask.
     *
     * @return the mask
     */
    int getMask() {
        if (mask >= 0x100) {
            mask |= 0x40;
        }
        return mask;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public UpdateFlagsType getType() {
        return type;
    }


    /**
     * The enum Update flags type.
     */
    public enum UpdateFlagsType {
        /**
         * Player update flags type.
         */
        PLAYER,
        /**
         * Npc update flags type.
         */
        NPC
    }

    /**
     * The type Npc update flags.
     */
    public static class NpcUpdateFlags {
        /**
         * The constant APPEARANCE.
         */
        public static final int
                /**
                 * The Graphics.
                 */
                GRAPHICS = 0x80,
        /**
         * The Animation.
         */
        ANIMATION = 0x10,
        /**
         * The Forced chat.
         */
        FORCED_CHAT = 0x1,
        /**
         * The Entity interaction.
         */
        ENTITY_INTERACTION = 0x20,
        /**
         * The Face coordinate.
         */
        FACE_COORDINATE = 0x4,
        /**
         * The Transform.
         */
        TRANSFORM = 0x2,
        /**
         * The Single hit.
         */
        SINGLE_HIT = 0x40,
        /**
         * The Double hit.
         */
        DOUBLE_HIT = 0x8;
    }

    /**
     * The type Player update flags.
     */
    public static class PlayerUpdateFlags {
        /**
         * The constant APPEARANCE.
         */
        public static final int APPEARANCE = 0x10,
        /**
         * The Chat.
         */
        CHAT = 0x80,
        /**
         * The Graphics.
         */
        GRAPHICS = 0x100,
        /**
         * The Animation.
         */
        ANIMATION = 0x8,
        /**
         * The Forced chat.
         */
        FORCED_CHAT = 0x4,
        /**
         * The Entity interaction.
         */
        ENTITY_INTERACTION = 0x1,
        /**
         * The Face coordinate.
         */
        FACE_COORDINATE = 0x2,
        /**
         * The Single hit.
         */
        SINGLE_HIT = 0x20,
        /**
         * The Double hit.
         */
        DOUBLE_HIT = 0x200,
        /**
         * The Force movement.
         */
        FORCE_MOVEMENT = 0x400;
    }
}
