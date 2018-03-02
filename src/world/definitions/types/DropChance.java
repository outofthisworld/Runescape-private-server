package world.definitions.types;

import util.Chance;
import world.WorldConfig;

public enum DropChance {
    ALWAYS() {
        @Override
        boolean shouldDrop() {
            return Chance.chanceWithin(WorldConfig.DROP_ALWAYS);
        }

        @Override
        double getDropRate() {
            return WorldConfig.DROP_ALWAYS;
        }
    },
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
