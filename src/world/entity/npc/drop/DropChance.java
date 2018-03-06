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
        boolean shouldDrop() {
            return true;
        }

        @Override
        double getDropRate() {
            return WorldConfig.DROP_ALWAYS;
        }
    },
    /**
     * Represents a drop chance that always drops.
     * 30% drop rate.
     */
    UNCOMMON() {
        @Override
        boolean shouldDrop() {
            return Chance.chanceWithin(WorldConfig.DROP_UNCOMMON);
        }

        @Override
        double getDropRate() {
            return WorldConfig.DROP_UNCOMMON;
        }
    },
    /**
     * Represents a drop chance that always drops.
     * 70% drop rate.
     */
    COMMON() {
        @Override
        boolean shouldDrop() {
            return Chance.chanceWithin(WorldConfig.DROP_COMMON);
        }

        @Override
        double getDropRate() {
            return WorldConfig.DROP_COMMON;
        }
    },
    /**
     * Represents a drop chance that always drops.
     * 70% drop rate.
     */
    RARE() {
        @Override
        boolean shouldDrop() {
            return Chance.chanceWithin(WorldConfig.DROP_RARE);
        }

        @Override
        double getDropRate() {
            return WorldConfig.DROP_RARE;
        }
    },
    NEAR_IMPOSSIBLE() {
        @Override
        boolean shouldDrop() {
            return Chance.chanceWithin(WorldConfig.DROP_NEAR_IMPOSSIBLE);
        }

        @Override
        double getDropRate() {
            return WorldConfig.DROP_NEAR_IMPOSSIBLE;
        }
    };

    abstract boolean shouldDrop();

    abstract double getDropRate();
}
