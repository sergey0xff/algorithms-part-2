public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        String input = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(input);

        for (int i = 0; i < input.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        
        for (int i = 0; i < input.length(); i++) {
            BinaryStdOut.write(input.charAt((csa.index(i) + input.length() - 1) % input.length()));
        }

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int first = BinaryStdIn.readInt();
        String input = BinaryStdIn.readString();
        int[] count = new int[R + 1];
        int[] next = new int[input.length()];

        for (int i = 0; i < input.length(); i++) {
            ++count[input.charAt(i) + 1];
        }

        for (int i = 0; i < R; i++) {
            count[i + 1] += count[i];
        }

        for (int i = 0; i < input.length(); i++) {
            next[count[input.charAt(i)]++] = i;
        }

        int nxt = next[first];

        BinaryStdOut.write(input.charAt(nxt));

        for (int i = 1; i < input.length(); i++) {
            nxt = next[nxt];
            BinaryStdOut.write(input.charAt(nxt));
        }

        BinaryStdOut.close();
    }



    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
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
