package xyz.vsl.tomcat.secdb.crypto;

import java.util.Properties;

/**
 * @author Vladimir Lokhov
 */
public class ContextKey implements KeyProducer {
    private static String KEY = "fkjw2oiuw6ybucwe6tp";

    private String key;
    private boolean saltedByName;
    private boolean saltedByProperty;

    public final static Factory FACTORY = new Factory() {
        @Override
        public KeyProducer keyProducer(String options, String property, Properties properties) {
            boolean saltedByName = false;
            boolean saltedByProperty = false;
            if (options != null) {
                saltedByName = options.indexOf('N') >= 0;
                saltedByProperty = options.indexOf('P') >= 0;
            }
            return new ContextKey(property, properties, saltedByName, saltedByProperty);
        }
    };

    public ContextKey(String targetProperty, Properties properties) {
        this(targetProperty, properties, true, true);
    }

    public ContextKey(String targetProperty, Properties properties, boolean saltedByName, boolean saltedByProperty) {
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
