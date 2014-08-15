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
        System.out.println(obj);
        ResourceRef resourceRef = decryptResourceRef((ResourceRef) obj, name);
        return new org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory().getObjectInstance(resourceRef, name, nameCtx, environment);
    }

}
