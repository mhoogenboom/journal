package com.robinfinch.journal.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Collection of utility methods.
 *
 * @author Mark Hoogenboom
 */
public class Utils {

    public static final String LOG_TAG = "com.robinfinch.journal.log";

    public static <T> int compare(Comparable<T> c1, T c2) {
        if (c1 == null) {
            return (c2 == null) ? 0 : -1;
        } else {
            return (c2 == null) ? 1 : c1.compareTo(c2);
        }
    }

    public static String hash(String s, String algorithm) {
        byte[] digest = hash(s.getBytes(), algorithm);

        StringBuilder digestHex = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            String h = Integer.toHexString(0xff & digest[i]);
            if (h.length() == 1) {
                digestHex.append("0");
            }
            digestHex.append(h);
        }

        return digestHex.toString();
    }

    public static byte[] hash(byte[] b, String algorithm) {
        byte[] result;
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(b);
            result = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger(LOG_TAG).log(Level.SEVERE, "Failed to hash", e);
            result = null;
        }
        return result;
    }
}
