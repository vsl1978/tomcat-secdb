package xyz.vsl.tomcat.secdb;

import xyz.vsl.tomcat.secdb.crypto.ContextKey;
import xyz.vsl.tomcat.secdb.crypto.Helper;
import xyz.vsl.tomcat.secdb.crypto.SaltedKey;

import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author Vladimir Lokhov
 */
public class Shell {

    private static void printHelp(ResourceBundle rb) {
        for (int i = 1, missed = 0; missed < 100 ; i++) {
            try {
                String s = rb.getString("help." + i);
                if (s != null) {
                    System.out.println(s);
                    missed = 0;
                } else missed++;
            } catch (MissingResourceException e) {
                missed++;
            }
        }
    }

    public static void main(String[] args) {
        ResourceBundle rb = ResourceBundle.getBundle(Shell.class.getPackage().getName()+".messages");

        Properties properties = new Properties();
        String target = null;

        if (args.length == 0) {
            printHelp(rb);
            return;
        }

        boolean encrypted = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            String next = i < args.length - 1 ? args[i + 1] : null;
            if ("--help".equals(arg) || "-help".equals(arg) || "-h".equals(arg) || "-?".equals(arg)) {
                printHelp(rb);
                return;
            }
            if ("--name".equals(arg) && next != null) {
                /*
                if (encrypted)
                    System.err.println(rb.getString("name.should.be.first"));
                */
                properties.setProperty("name", next);
                i++;
                continue;
            }

            if ("--username".equals(arg) || "--password".equals(arg)) {
                if (next == null) {
                    // error
                    continue;
                }
                target = arg.substring(2);
                System.out.println(target+": "+ Helper.encryptAndFormat(/*new ContextKey(target, properties)*/new SaltedKey(), next, false));
                encrypted = true;
                target = null;
                i++;
                continue;
            }

            if ("--target".equals(arg) || "--attribute".equals(arg) || "--param".equals(arg) || "--parameter".equals(arg)) {
                target = next;
                i++;
                continue;
            }
            if ("--property".equals(arg)) {
                if (next != null) {
                    String value = i < args.length - 2 ? args[i + 2] : null;
                    if (value != null) {
                        properties.setProperty(next, value);
                        i++;
                        i++;
                    }
                }
                continue;
            }

            System.out.println((target != null ? target : arg) +": " + Helper.encryptAndFormat(/*new ContextKey(target, properties)*/new SaltedKey(), arg, true));
            encrypted = true;
            target = null;
        }
    }
}
