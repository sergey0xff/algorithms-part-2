import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CircularSuffixArrayTest {
    private CircularSuffixArray abra;
    private CircularSuffixArray abc;

    @Before
    public void setUp() {
        abra = new CircularSuffixArray("ABRACADABRA!");
        abc = new CircularSuffixArray("abc");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorThrowsNullPointerException() {
        new CircularSuffixArray(null);
    }

    @Test
    public void testLength() {
        String[] strings = {"a", "abc", "abcdef"};
        for (String string : strings) {
            CircularSuffixArray arr = new CircularSuffixArray(string);
            assertEquals(string.length(), arr.length());
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexIndexOutOfBoundsException() {
        abc.index(5);
        abc.index(-1);
    }

    @Test
    public void testIndex() {
        assertEquals(2, abra.index(11));
        assertEquals(0, abra.index(3));
        assertEquals(3, abra.index(4));
        assertEquals(1, abra.index(7));
    }
}