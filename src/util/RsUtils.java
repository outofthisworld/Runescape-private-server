package util;

import net.buffers.InputBuffer;

public final class RsUtils {

    public static String readRSString(InputBuffer in) {
        byte[] passwordBytes = in.readUntil(b -> b.byteValue() == 10);

        if (passwordBytes == null) {
            return "";
        }

        return new String(passwordBytes);
    }

    public static String bytesToString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }

        return RsUtils.bytesToString(bytes, 0, bytes.length);
    }

    public static String bytesToString(byte[] bytes, int offset, int len) {
        if (bytes == null) {
            return "";
        }

        return new String(bytes, offset, len);
    }
}
