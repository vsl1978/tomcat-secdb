package xyz.vsl.tomcat.secdb.crypto;

import java.util.Properties;

/**
 * @author Vladimir Lokhov
 */
public class SaltedKey implements KeyProducer {
    private static String KEY = "fkjw2oiuw6ybucwe6tp";

    private String salt;

    public final static Factory FACTORY = new Factory() {
        @Override
        public KeyProducer keyProducer(String options, String property, Properties properties) {
            return new SaltedKey(options == null ? "" : options);
        }
    };

    public SaltedKey() {
        int watchdog = 1024;
        do {
            long t = System.nanoTime() % 16777213;
            byte[] b = new byte[3];
            b[0] = (byte) (t & 255);
            t = t >>> 8;
            b[1] = (byte) (t & 255);
            t = t >>> 8;
            b[2] = (byte) (t & 255);
            this.salt = Base64.encode(b);
        } while ((this.salt.charAt(1) == 'u' || this.salt.charAt(1) == 'U') && watchdog-- > 0);
    }

    public SaltedKey(String salt) {
        this.salt = salt;
    }

    @Override
    public String getText() {
        return KEY + ";" + salt;
    }

    @Override
    public String getOptions() {
        return salt;
    }
}
