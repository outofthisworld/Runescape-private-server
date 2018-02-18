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

    /**
     * Converts a string into a long value. This is used for the player appearance updating to
     * update a players username.
     *
     * @param string The string to convert.
     */
    public static long convertStringToLong(String string) {
        long l = 0L;
        int i = 0;
        do {
            char c = string.charAt(i);
            l *= 37L;
            if ((c >= 'A') && (c <= 'Z')) {
                l += '\001' + c - 65;
            } else if ((c >= 'a') && (c <= 'z')) {
                l += '\001' + c - 97;
            } else if ((c >= '0') && (c <= '9')) {
                l += '\033' + c - 48;
            }
            i++;
            if (i >= string.length()) {
                break;
            }
        } while (i < 12);
        while ((l % 37L == 0L) && (l != 0L)) {
            l /= 37L;
        }
        return l;
    }
}
