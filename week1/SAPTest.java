import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SAPTest {
    @Test(expected = NullPointerException.class)
    public void testConstructorThrowsNullPointerException() {
        new SAP(null);
    }

    @Test(expected = NullPointerException.class)
    public void testLengthThrowsNullPointerException() {
        SAP sap = new SAP(new Digraph(0));
        sap.length(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testAncestorThrowsNullPointerException() {
        SAP sap = new SAP(new Digraph(0));
        sap.ancestor(null, null);
    }

    @Test
    public void testDigraph2() {
        SAP sap = new SAP(new Digraph(new In("inputs/wordnet/digraph2.txt")));
        assertEquals(2, sap.length(1, 3));
    }

    @Test
    public void testDigraph4() {
        SAP sap = new SAP(new Digraph(new In("inputs/wordnet/digraph4.txt")));
        assertEquals(2, sap.length(4, 1));
    }
}