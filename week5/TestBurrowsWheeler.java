import java.io.*;

public class TestBurrowsWheeler {
    private static final InputStream sysIn = System.in;
    private static final PrintStream sysOut = System.out;

    private static String testEncode(String fileName) {
        In in = new In("inputs/burrows/" + fileName);
        String s = in.readAll();
        System.setIn(new ByteArrayInputStream(s.getBytes()));
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bous);
        System.setOut(out);
        BurrowsWheeler.encode();
        System.setOut(sysOut);
        System.setIn(sysIn);
        return bous.toString();
    }

    private static void testDecode(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        BurrowsWheeler.decode();
    }

    public static void main(String[] args) {
        testDecode("\u0000\u0000\u0000\u0003ARD!RCAAAABB");
    }
}
