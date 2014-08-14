package xyz.vsl.tomcat;

import org.junit.Test;
import xyz.vsl.tomcat.secdb.crypto.Base64;

import static org.junit.Assert.*;

/**
 * @author Vladimir Lokhov
 */
public class Base64Test {

    @Test
    public void zero() {
        assertEquals("encoded", "AA==", Base64.encode(new byte[]{0,}));
        assertEquals("encoded", "AAA=", Base64.encode(new byte[] {0,0}));
        assertEquals("encoded", "AAAA", Base64.encode(new byte[] {0,0,0}));
        assertArrayEquals("decoded", new byte[]{0,}, Base64.decode("AA=="));
        assertArrayEquals("decoded", new byte[]{0,0,}, Base64.decode("AAA="));
        assertArrayEquals("decoded", new byte[]{0,0,0,}, Base64.decode("AAAA"));
        //assertArrayEquals();
    }

    @Test
    public void doubleTransform() {
        byte[] data = new byte[] {1,2,3,4,5,6,7,8,9,0};
        String enc = Base64.encode(data);
        byte[] dec = Base64.decode(enc);
        assertArrayEquals(data, dec);
    }
}
