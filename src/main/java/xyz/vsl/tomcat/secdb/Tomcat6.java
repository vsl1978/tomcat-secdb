package xyz.vsl.tomcat.secdb;

import org.apache.naming.ResourceRef;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;
import java.util.*;

/**
 * @author Vladimir Lokhov
 */
public class Tomcat6 extends BaseObjectFactory implements ObjectFactory {

    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        if (!(obj instanceof ResourceRef))
            return null;
        ResourceRef resourceRef = decryptResourceRef((ResourceRef) obj, name);
        String[] factoryClasses;
        if (Tomcat.getMajorVersion() < 8)
            factoryClasses = new String[] {"org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory", "org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory"};
        else
            factoryClasses = new String[] {"org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory"};

        Either<ObjectFactory> factory = null;
        for (String className : factoryClasses) {
            factory = Tomcat.getInstance(className, ObjectFactory.class);
            if (factory.getValue() != null)
                break;
        }
        if (factory.getError() != null)
            throw factory.getError();
        return factory.getValue().getObjectInstance(resourceRef, name, nameCtx, environment);
    }

}
