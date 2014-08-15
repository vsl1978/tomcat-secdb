package xyz.vsl.tomcat.secdb;

import org.apache.naming.ResourceRef;
import xyz.vsl.tomcat.secdb.crypto.Helper;
import xyz.vsl.tomcat.secdb.crypto.SaltedKey;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.StringRefAddr;
import java.util.*;

/**
 * @author Vladimir Lokhov
 */
class BaseObjectFactory {

    public ResourceRef decryptResourceRef(ResourceRef resourceRef, Name name) throws Exception {
        Properties properties = new Properties();
        for (Enumeration en = resourceRef.getAll(); en.hasMoreElements(); ) {
            RefAddr ra = (RefAddr) en.nextElement();
            String type = ra.getType();
            Object content = ra.getContent();
            String value = content instanceof String ? (String)content : null;
            if (value != null)
                properties.setProperty(type, value);
        }
        if (!properties.containsKey("name") && name != null)
            properties.setProperty("name", name.toString());
        List<Integer> toRemove = new ArrayList<Integer>();
        List<RefAddr> toAdd = new ArrayList<RefAddr>();

        int idx = 0;
        for (Enumeration en = resourceRef.getAll(); en.hasMoreElements(); idx++) {
            RefAddr ra = (RefAddr) en.nextElement();
            String type = ra.getType();
            Object content = ra.getContent();
            if (!(content instanceof String))
                continue;
            String value = (String)content;
            String decrypted = Helper.decryptAll(value, type, properties, SaltedKey.FACTORY);
            if (value.equals(decrypted))
                continue;
            toRemove.add(idx - toRemove.size());
            toAdd.add(new StringRefAddr(type, decrypted));
            properties.setProperty(type, decrypted);
        }

        for (Integer i : toRemove)
            resourceRef.remove(i);
        for (RefAddr a : toAdd)
            resourceRef.add(a);

        return resourceRef;
    }

}
