package world.entity.npc.update;

import util.integrity.Preconditions;
import world.entity.update.Flags;
import world.entity.update.IFlag;

public class NpcUpdateFlags implements IFlag<NpcUpdateMask> {

    private final Flags flags = new Flags();

    @Override
    public void setFlag(NpcUpdateMask flag) {
        Preconditions.notNull(flag);
        flags.setFlag(flag.getMask());
    }

    @Override
    public void toggleFlag(NpcUpdateMask flag) {
        Preconditions.notNull(flag);
        flags.toggleFlag(flag.getMask());
    }

    @Override
    public void unsetFlag(NpcUpdateMask flag) {
        Preconditions.notNull(flag);
        flags.unsetFlag(flag.getMask());
    }

    @Override
    public boolean isSet(NpcUpdateMask flag) {
        Preconditions.notNull(flag);
        return  flags.isSet(flag.getMask());
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
