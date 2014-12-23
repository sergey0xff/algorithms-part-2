import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class MoveToFrontTest {
    private static final InputStream sysIn = System.in;
    private static final PrintStream sysOut = System.out;

    private static String testEncode(String fileName) {
        In in = new In("inputs/burrows/" + fileName);
        String s = in.readAll();
        System.setIn(new ByteArrayInputStream(s.getBytes()));
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bous);
        System.setOut(out);
        MoveToFront.encode();
        System.setOut(sysOut);
        System.setIn(sysIn);
        return bous.toString();
    }

    private static void testDecode(String fileName) {
        In in = new In("inputs/burrows/" + fileName);
        String s = in.readAll();
        System.setIn(new ByteArrayInputStream(s.getBytes()));
        MoveToFront.decode();
        System.setIn(sysIn);
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(testEncode("abra.txt").getBytes()));
    }
}
