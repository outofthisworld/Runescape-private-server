package world.entity.npc.drop;

import util.random.Chance;
import world.WorldConfig;

public enum DropChance {
    /**
     * Represents a drop chance that always drops.
     * 100% drop rate.
     */
    ALWAYS() {
        @Override
        public boolean shouldDrop() {
            return true;
        }

        @Override
        public double getDropRate() {
            return WorldConfig.DROP_ALWAYS;
        }
    },
    /**
     * Represents a drop chance that always drops.
     * 30% drop rate.
     */
    UNCOMMON() {
        @Override
        public boolean shouldDrop() {
            return Chance.chanceWithin(WorldConfig.DROP_UNCOMMON);
        }

        @Override
        public double getDropRate() {
            return WorldConfig.DROP_UNCOMMON;
        }
    },
    /**
     * Represents a drop chance that always drops.
     * 70% drop rate.
     */
    COMMON() {
        @Override
        public boolean shouldDrop() {
            return Chance.chanceWithin(WorldConfig.DROP_COMMON);
        }

        @Override
        public double getDropRate() {
            return WorldConfig.DROP_COMMON;
        }
    },
    /**
     * Represents a drop chance that always drops.
     * 70% drop rate.
     */
    RARE() {
        @Override
        public boolean shouldDrop() {
            return Chance.chanceWithin(WorldConfig.DROP_RARE);
        }

        @Override
        public double getDropRate() {
            return WorldConfig.DROP_RARE;
        }
    },
    NEAR_IMPOSSIBLE() {
        @Override
        public boolean shouldDrop() {
            return Chance.chanceWithin(WorldConfig.DROP_NEAR_IMPOSSIBLE);
        }

        @Override
        public double getDropRate() {
            return WorldConfig.DROP_NEAR_IMPOSSIBLE;
        }
    };

    public abstract boolean shouldDrop();

    public abstract double getDropRate();
}
