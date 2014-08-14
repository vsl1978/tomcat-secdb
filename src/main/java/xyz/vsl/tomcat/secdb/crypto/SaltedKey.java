package xyz.vsl.tomcat.secdb.crypto;

import java.util.Properties;

/**
 * @author Vladimir Lokhov
 */
public class SaltedKey implements KeyProducer {
    private static String KEY = "fkjw2oiuw6ybucwe6tp";

    private String key;
    private boolean saltedByName;
    private boolean saltedByProperty;

    public SaltedKey(String targetProperty, Properties properties) {
        this(targetProperty, properties, true, true);
    }

    public SaltedKey(String targetProperty, Properties properties, boolean saltedByName, boolean saltedByProperty) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        if (saltedByName)
            if (this.saltedByName = (properties.getProperty("name") != null))
                sb.append(';').append(properties.getProperty("name"));
        if (saltedByProperty)
            if (this.saltedByProperty = (targetProperty != null))
                sb.append(';').append(targetProperty);
        key = sb.toString();
    }

    @Override
    public String getText() {
        return key;
    }

    @Override
    public String getOptions() {
        return (saltedByName ? "N" : "n") + (saltedByProperty ? "P" : "p");
    }
}
