import java.util.Arrays;

public class BoggleTrieSET {
    private static final int R = 26;

    private TrieNode root;

    /**
     * Returns
     * -1 if not contains
     * 0 if has prefix
     * 1 if has full match
     */
    public TrieNode contains(String key) {
        return contains(root, key, 0);
    }

    public TrieNode contains(TrieNode x, String prefix) {
        return contains(x, prefix, prefix.length() - 1);
    }

    private TrieNode contains(TrieNode x, String key, int d) {
        char c;

        while (x != null) {
            if (d == key.length()) {
                return x;
            }
            c = key.charAt(d);
            d += 1;
            x = x.next[getCharIdx(c)];
        }

        return null;
    }

    private int getCharIdx(char x) {
        return ((int) x) - 65;
    }

    public void add(String key) {
        root = add(root, key, 0);
    }

    private TrieNode add(TrieNode x, String key, int d) {
        if (x == null) x = new TrieNode();
        if (d == key.length()) {
            x.isString = true;
        } else {
            char c = key.charAt(d);
            x.next[getCharIdx(c)] = add(x.next[getCharIdx(c)], key, d + 1);
        }
        return x;
    }


    public static void main(String[] args) {
        In in = new In("inputs/boggle/dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleTrieSET set = new BoggleTrieSET();

        for (String word : dictionary) {
            set.add(word);
        }

        TrieNode hel = set.contains("HELL");
        System.out.println(set.contains(hel, "HELLO"));

    }
}
