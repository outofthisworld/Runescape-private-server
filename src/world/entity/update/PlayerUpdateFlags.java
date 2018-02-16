package world.entity.update;

import java.util.Objects;

public class PlayerUpdateFlags implements IFlag<PlayerUpdateFlags.PlayerUpdateMask> {
    private final Flags flags = new Flags();

    public PlayerUpdateFlags() {

    }

    @Override
    public void setFlag(PlayerUpdateMask flag) {
        Objects.requireNonNull(flag);
        flags.setFlag(flag.getMask());
    }

    @Override
    public void toggleFlag(PlayerUpdateMask flag) {
        Objects.requireNonNull(flag);
        flags.toggleFlag(flag.getMask());
    }

    @Override
    public void unsetFlag(PlayerUpdateMask flag) {
        Objects.requireNonNull(flag);
        flags.unsetFlag(flag.getMask());
    }

    @Override
    public boolean isSet(PlayerUpdateMask flag) {
        Objects.requireNonNull(flag);
        return flags.isSet(flag.getMask());
    }

    @Override
    public boolean anySet() {
        return flags.anySet();
    }

    @Override
    public void clear() {
        flags.clear();
    }

    @Override
    public long getMask() {
        return flags.getMask();
    }


    public enum PlayerUpdateMask {
        /**
         * The constant APPEARANCE.
         */
        APPEARANCE(0x10),
        /**
         * The Chat.
         */
        CHAT(0x80),
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
         * The Entity interaction.
         */
        ENTITY_INTERACTION(0x1),
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
        DOUBLE_HIT(0x200),
        /**
         * The Force movement.
         */
        FORCE_MOVEMENT(0x400);

        private final long mask;

        PlayerUpdateMask(long mask) {
            this.mask = mask;
        }

        public long getMask() {
            return mask;
        }
    }
}
