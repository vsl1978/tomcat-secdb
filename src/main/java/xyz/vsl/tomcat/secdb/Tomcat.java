package xyz.vsl.tomcat.secdb;

import org.apache.catalina.util.ServerInfo;

/**
 * @author Vladimir Lokhov
 */
class Tomcat {

    public static int getMajorVersion() {
        try {
            String ver = ServerInfo.getServerNumber();
            String major = ver.substring(0, ver.indexOf('.'));
            return Integer.parseInt(major);
        } catch (Exception e) {
            return -1;
        }
    }

    public static <T> Either<T> getInstance(String className, Class<T> superClass) {
        Class<?> klazz = null;
        try {
            if (Thread.currentThread().getContextClassLoader() != null)
                klazz = Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            try {
                klazz = Class.forName(className);
            } catch (ClassNotFoundException e2) {
                return new Either<T>(e2);
            }
        }
        try {
            return new Either<T>(superClass.cast(klazz.newInstance()));
        } catch (Exception e) {
            return new Either<T>(e);
        }
    }

}
