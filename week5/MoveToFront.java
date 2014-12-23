public class MoveToFront {
    private static final int R = 256;
    private static char[] abc = new char[R];

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        initAbc();

        char i;
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char current = abc[0];

            for (i = 0; c != abc[i]; i++) {
                char tmp = abc[i];
                abc[i] = current;
                current = tmp;
            }
            abc[i] = current;
            BinaryStdOut.write(i);
            abc[0] = c;
        }

        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        initAbc();

        char i;
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            for (i = abc[c]; c > 0; c--) {
                abc[c] = abc[c - 1];
            }
            abc[c] = i;
            BinaryStdOut.write(i);
        }

        BinaryStdOut.close();
    }

    // initialize alphabet
    private static void initAbc() {
        for (int i = 0; i < R; i++) {
            abc[i] = (char) i;
        }
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length != 1) throw new IllegalArgumentException();

        switch (args[0]) {
            case "-":
                encode();
                break;
            case "+":
                decode();
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
