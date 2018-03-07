package world.entity.npc.update;

import world.entity.update.Flags;
import world.entity.update.IFlag;

public class NpcUpdateFlags implements IFlag<NpcUpdateFlags> {

    private final Flags flags = new Flags();

    @Override
    public void setFlag(NpcUpdateFlags flag) {
        flags.setFlag(flag.getMask());
    }

    @Override
    public void toggleFlag(NpcUpdateFlags flag) {
        flags.toggleFlag(flag.getMask());
    }

    @Override
    public void unsetFlag(NpcUpdateFlags flag) {
        flags.unsetFlag(flag.getMask());
    }

    @Override
    public boolean isSet(NpcUpdateFlags flag) {
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
        return flags.getMask();
    }
}
