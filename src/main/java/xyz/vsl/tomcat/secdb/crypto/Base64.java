package xyz.vsl.tomcat.secdb.crypto;

import java.util.Arrays;

/**
 * @author Vladimir Lokhov
 */
public class Base64 {

    private static final char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
    private static final byte[] codes = new byte[256];


    static {
        Arrays.fill(codes, (byte)-1);
        for (int i = 0; i < chars.length - 1; i++)
            codes[chars[i]] = (byte)i;
    }

    public static String encode(byte[] data) {
        char[] result = new char[((data.length + 2) / 3) * 4];
        boolean has24bit;
        boolean has16bit;

        for (int i = 0, j = 0; i < data.length; j += 4) {
            int val = ((int) data[i] & 255) << 8;
            i++;
            if (has16bit = (i < data.length))
                val = val + ((int) data[i] & 255); // trip = true
            val = val << 8;
            i++;
            if (has24bit = (i < data.length))
                val = val + ((int) data[i] & 255); // quad = true
            i++;

            result[j + 3] = chars[has24bit ? val & 63 : 64];
            val = val >>> 6;
            result[j + 2] = chars[has16bit ? val & 63 : 64];
            val = val >>> 6;
            result[j + 1] = chars[val & 63];
            val = val >>> 6;
            result[j] = chars[val & 63];
        }
        return new String(result);
    }

    public static byte[] decode(String encoded) {
        char[] data = encoded.toCharArray();
        int len = ((data.length + 3) / 4) * 3;
        byte[] out = new byte[len];
        int shift = 0, n = 0, index = 0;
        boolean eq = false;
        for (int i = 0; i < data.length; i++) {
            int value = codes[data[i] & 255];
            if (value >= 0) {
                n = (n << 6) + value;
                shift += 6;
                if (shift >= 8) {
                    shift = shift & 7;
                    out[index++] = (byte) ((n >>> shift) & 255);
                }
            }
            eq = data[i] == '=';
        }
        if (index != out.length) {
            if (eq) {
                byte[] temp = new byte[index];
                System.arraycopy(out, 0, temp, 0, index);
                out = temp;
            }
            else throw new IllegalArgumentException("Invalid data length ("+index+")");
        }
        return out;
    }

}
