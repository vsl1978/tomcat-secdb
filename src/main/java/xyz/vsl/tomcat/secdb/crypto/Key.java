package xyz.vsl.tomcat.secdb.crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author Vladimir Lokhov
 */
public class Key {

    public static SecretKey generate(String algorithm, KeyProducer key) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] keydata = key.getText().getBytes("UTF-8");
            byte[] hash = md.digest(keydata);
            byte[] key128 = new byte[16];
            System.arraycopy(hash, 0, key128, 0, key128.length);
            return new SecretKeySpec(key128, algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            // impossible
            return null;
        }
    }
}
