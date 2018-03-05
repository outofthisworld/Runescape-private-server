package util;

import world.WorldConfig;

import java.io.*;

public final class Debug {
    private static PrintWriter out;
    private static StringBuilder sb = new StringBuilder();

    static {
        if (WorldConfig.APP_MODE == AppMode.DEVELOPMENT) {
            try {
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(java.io.FileDescriptor.out), "UTF-8"), 512));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private Debug() {
    }

    public static void writeLine(String s) {
        if (out == null) return;
        out.println(createDebugMessage(s));
        out.flush();
        sb.delete(0, sb.length());
    }

    public static void write(String s, Object... args) {
        if (out == null) return;
        out.printf(createDebugMessage(s), args);
        out.flush();
    }

    public static void write(String s) {
        if (out == null) return;
        out.print(createDebugMessage(s));
        out.flush();
    }

    private static String createDebugMessage(String s) {
        sb.append("DEBUG [").append(Thread.currentThread().getStackTrace()[3]).append("] [").append(Thread.currentThread().getStackTrace()[4]).append("] : ").append(s);
        String debugString = sb.toString();
        sb.delete(0, sb.length());
        return debugString;
    }
}
