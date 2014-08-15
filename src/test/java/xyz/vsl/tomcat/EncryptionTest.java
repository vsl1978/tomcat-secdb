package xyz.vsl.tomcat;

import org.junit.Test;
import xyz.vsl.tomcat.secdb.crypto.ContextKey;
import xyz.vsl.tomcat.secdb.crypto.Helper;
import xyz.vsl.tomcat.secdb.crypto.SaltedKey;

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * @author Vladimir Lokhov
 */
public class EncryptionTest {

    private String decrypt(String s) {
        return decrypt(s, "password");
    }

    private String decrypt(String s, String property) {
        Properties properties = new Properties();
        properties.setProperty("name", "TestDS");
        return Helper.decryptAll(s, property, properties, ContextKey.FACTORY);
    }

    @Test
    public void test() {
        assertEquals("test", decrypt("{AES:np}VhkFve4N1x0kQ7N8JAOjeg=="));
    }

    @Test
    public void testFragment() {
        assertEquals("jdbc:something://localhost/test;option=on", decrypt("jdbc:something://localhost/{AES:np}VhkFve4N1x0kQ7N8JAOjeg==;option=on", "url"));
    }

    @Test
    public void testEq() {
        assertEquals("test", decrypt("{AES:np}VhkFve4N1x0kQ7N8JAOjeg====================="));
    }

    @Test
    public void testP() {
        assertEquals("test", decrypt("{AES:nP}KCTVSibADyrS2JewqSQr7w=="));
    }

    @Test
    public void testPFragment() {
        assertEquals("jdbc:something://localhost/test;option=on", decrypt("jdbc:something://localhost/{AES:nP}y2EzZ4qzzvQp+yOGMy8sZg==;option=on", "url"));
    }

    @Test
    public void testN() {
        assertEquals("test", decrypt("{AES:Np}99F2FwaesVK0V5P2oiab0g=="));
    }

    @Test
    public void testNFragment() {
        assertEquals("jdbc:something://localhost/test;option=on", decrypt("jdbc:something://localhost/{AES:Np}99F2FwaesVK0V5P2oiab0g==;option=on", "url"));
    }

    @Test
    public void testNP() {
        assertEquals("test", decrypt("{AES:NP}Rti2tjvpYnEiZljW+ni6VQ=="));
    }

    @Test
    public void testNPFragment() {
        assertEquals("jdbc:something://localhost/test;option=on", decrypt("jdbc:something://localhost/{AES:NP}qE3mlh6rKN27C7fdErecKA==;option=on", "url"));
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidPassword() {
        decrypt("{AES:np}ZhkFve4N1x0kQ7N8JAOjeg==");
    }

    @Test
    public void cycle() {
        String text = "password!";
        String encrypted = Helper.encryptAndFormat(new SaltedKey(), text, false);
        String decrypted = Helper.decryptAll(encrypted, "password", new Properties(), SaltedKey.FACTORY);
        assertEquals("encrypt-decrypt (1)", text, decrypted);
        encrypted = Helper.encryptAndFormat(new SaltedKey(), text, true);
        decrypted = Helper.decryptAll(encrypted, "password", new Properties(), SaltedKey.FACTORY);
        assertEquals("encrypt-decrypt (2)", text, decrypted);
    }
}
