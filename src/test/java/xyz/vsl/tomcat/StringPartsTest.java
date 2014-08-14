package xyz.vsl.tomcat;

import org.junit.Test;
import static org.junit.Assert.*;
import xyz.vsl.tomcat.secdb.crypto.Helper;
import xyz.vsl.tomcat.secdb.crypto.StringParts;

/**
 * @author Vladimir Lokhov
 */
public class StringPartsTest {

    @Test
    public void alone() {
        String s = "{AES:NP}4AAuuH+O2MaQb0MIhztI6w==";
        StringParts sp = Helper.extract(s);
        assertEquals("prefix", sp.getHead(), "");
        assertEquals("text", sp.getText(), s);
        assertEquals("suffix", sp.getTail(), "");
    }

    @Test
    public void noEq1() {
        String s = "{AES:NP}4AAuuH+O2MaQb0MIhztI6w";
        StringParts sp = Helper.extract(s);
        assertEquals("prefix", sp.getHead(), "");
        assertEquals("text", sp.getText(), s);
        assertEquals("suffix", sp.getTail(), "");
    }

    @Test
    public void noEq2() {
        String s = "{AES:NP}4AAuuH+O2MaQb0MIhztI6w";
        StringParts sp = Helper.extract("head"+s);
        assertEquals("prefix", sp.getHead(), "head");
        assertEquals("text", sp.getText(), s);
        assertEquals("suffix", sp.getTail(), "");
    }

    @Test
    public void empty() {
        String s = "";
        StringParts sp = Helper.extract(s);
        assertNull("prefix", sp.getHead());
        assertNull("text", sp.getText());
        assertNull("suffix", sp.getTail());
    }

    @Test
    public void notMatches() {
        String s = "1234567890";
        StringParts sp = Helper.extract(s);
        assertNull("prefix", sp.getHead());
        assertNull("text", sp.getText());
        assertNull("suffix", sp.getTail());
    }

    @Test
    public void atStart() {
        String s = "{AES:NP}4AAuuH+O2MaQb0MIhztI6w==";
        StringParts sp = Helper.extract(s+"tail");
        assertEquals("prefix", sp.getHead(), "");
        assertEquals("text", sp.getText(), s);
        assertEquals("suffix", sp.getTail(), "tail");
    }

    @Test
    public void inMiddle() {
        String s = "{AES:NP}4AAuuH+O2MaQb0MIhztI6w==";
        StringParts sp = Helper.extract("head"+s+"tail");
        assertEquals("prefix", sp.getHead(), "head");
        assertEquals("text", sp.getText(), s);
        assertEquals("suffix", sp.getTail(), "tail");
    }

    @Test
    public void atEnd() {
        String s = "{AES:NP}4AAuuH+O2MaQb0MIhztI6w==";
        StringParts sp = Helper.extract("head"+s);
        assertEquals("prefix", sp.getHead(), "head");
        assertEquals("text", sp.getText(), s);
        assertEquals("suffix", sp.getTail(), "");
    }
}
