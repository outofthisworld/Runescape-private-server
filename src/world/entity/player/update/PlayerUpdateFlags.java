package world.entity.player.update;


import world.entity.update.Flags;
import world.entity.update.IFlag;

import java.util.Objects;

public class PlayerUpdateFlags implements IFlag<PlayerUpdateMask> {
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
        if (flags.getMask() >= 0x100) {
            /*
                Indicates to the client that this flag is two bytes.
             */
            flags.setFlag(0x40L);
        }
        return flags.getMask();
    }


}
