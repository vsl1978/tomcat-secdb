package xyz.vsl.tomcat.secdb;

import org.apache.naming.ResourceRef;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.StringRefAddr;
import javax.naming.spi.ObjectFactory;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author Vladimir Lokhov
 */
public class C3P0 extends BaseObjectFactory implements ObjectFactory {

    public static final String C3P0_DS = "com.mchange.v2.c3p0.ComboPooledDataSource";

    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        if (!(obj instanceof ResourceRef))
            return null;
        ResourceRef resourceRef = decryptResourceRef((ResourceRef) obj, name);

        ResourceRef c3p0 = new ResourceRef(
                C3P0_DS,
                getString(resourceRef, ResourceRef.DESCRIPTION),
                getString(resourceRef, ResourceRef.SCOPE),
                getString(resourceRef, ResourceRef.AUTH),
                getBool(resourceRef, ResourceRef.SINGLETON),
                resourceRef.getFactoryClassName(),
                resourceRef.getFactoryClassLocation()
        );
        for (Enumeration en = resourceRef.getAll(); en.hasMoreElements(); ) {
            RefAddr ra = (RefAddr) en.nextElement();
            String type = ra.getType();
            if ("type".equals(type))
                c3p0.add(new StringRefAddr(type, C3P0_DS));
            else if ("factory".equals(type))
                c3p0.add(new StringRefAddr(type, "org.apache.naming.factory.BeanFactory"));
            else if (ResourceRef.DESCRIPTION.equals(type))
                continue;
            else if (ResourceRef.SCOPE.equals(type))
                continue;
            else if (ResourceRef.AUTH.equals(type))
                continue;
            else if (ResourceRef.SINGLETON.equals(type))
                continue;
            else
                c3p0.add(ra);
        }

        return new org.apache.naming.factory.BeanFactory().getObjectInstance(c3p0, name, nameCtx, environment);
    }

    private String getString(ResourceRef resourceRef, String name) {
        RefAddr ra = resourceRef.get(name);
        return ra != null && ra.getContent() != null ? String.valueOf(ra.getContent()) : null;
    }

    private boolean getBool(ResourceRef resourceRef, String name) {
        String s = getString(resourceRef, name);
        return s != null && "true".equals(s.toLowerCase());
    }

}
