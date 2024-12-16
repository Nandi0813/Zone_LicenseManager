package dev.nandi0813.license.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hwid {
    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    private Hwid() {
    }

    public static String getCurrentHwid() {
        try {
            MessageDigest hash = MessageDigest.getInstance("MD5");
            String str = System.getProperty("os.name") + System.getProperty("os.arch") + System.getProperty("os.version") + Runtime.getRuntime().availableProcessors() + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("NUMBER_OF_PROCESSORS");
            return bytesToHex(hash.digest(str.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Invalid algorithm: ", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; ++j) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = HEX[v >>> 4];
            hexChars[j * 2 + 1] = HEX[v & 15];
        }

        return new String(hexChars);
    }
}
