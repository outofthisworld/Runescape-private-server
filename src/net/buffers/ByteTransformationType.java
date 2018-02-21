package net.buffers;
/**
 * The enum Byte transformation type.
 */
public enum ByteTransformationType {
    /**
     * A byte transformation type.
     */
    A {
        @Override
        byte transformValue(byte value) {
            return (byte) (value  + 128);
        }
    },
    /**
     * C byte transformation type.
     */
    C {
        @Override
        byte transformValue(byte value) {
            return (byte) -value;
        }
    },
    /**
     * S byte transformation type.
     */
    S {
        @Override
        byte transformValue(byte value) {
            return (byte) (128 - value);
        }
    },
    /**
     * None byte transformation type.
     */
    NONE {
        @Override
        byte transformValue(byte value) {
            return value;
        }
    };

    abstract byte transformValue(byte value);
}