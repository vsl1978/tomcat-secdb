package xyz.vsl.tomcat.secdb;

import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.XADataSource;
import xyz.vsl.tomcat.secdb.crypto.Helper;
import xyz.vsl.tomcat.secdb.crypto.SaltedKey;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

/**
 * @author Vladimir Lokhov
 */
public class Tomcat7 extends DataSourceFactory {
    private String name;

    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        this.name = name != null ? name.toString() : null;
        return super.getObjectInstance(obj, name, nameCtx, environment);
    }

    public DataSource createDataSource(Properties properties, Context context, boolean XA) throws SQLException {
        Properties props = new Properties();
        boolean skipName = !properties.containsKey("name");
        if (skipName && name != null)
            properties.setProperty("name", name);
        for (Enumeration e = properties.propertyNames(); e.hasMoreElements(); ) {
            String property = (String)e.nextElement();
            if (skipName && "name".equals(property))
                continue;
            Object o = properties.get(property);
            if (o instanceof String) {
                String value = Helper.decryptAll((String)o, property, properties, SaltedKey.FACTORY);
                props.setProperty(property, value);
            } else {
                props.put(property, o);
            }
        }
        if (skipName)
            properties.remove("name");

        PoolConfiguration poolProperties = parsePoolProperties(props);

        if (poolProperties.getDataSourceJNDI() != null && poolProperties.getDataSource() == null) {
            performJNDILookup(context, poolProperties);
        }
        org.apache.tomcat.jdbc.pool.DataSource dataSource = XA ? new XADataSource(poolProperties) : new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);
        dataSource.createPool();

        return dataSource;
    }
}
