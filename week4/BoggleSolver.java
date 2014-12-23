import java.util.*;

public class BoggleSolver {
    private BoggleTrieSET dictionary = new BoggleTrieSET();
    private BoggleBoard board;
    private Set<String> result;
    private Integer[][] neighbours;


    private class Node {
        private int i;
        private int j;
        private String prefix;
        private boolean[] marked;

        public Node(int i, int j, String prefix, boolean[] parentMarked) {
            this.i = i;
            this.j = j;
            this.prefix = prefix;

            marked = new boolean[parentMarked.length];
            System.arraycopy(parentMarked, 0, marked, 0, parentMarked.length);
            marked[get1DIndex(i, j)] = true;
        }
    }
    
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new NullPointerException();

        // init dictionary
        for (String word : dictionary) {
            this.dictionary.add(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) throw new NullPointerException();
        result = new HashSet<>();
        if (board.rows() == 0 || board.cols() == 0) return result;

        this.board = board;
        neighbours = new Integer[board.cols() * board.rows()][8];

        // Pre compile neighbours
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                int idx = get1DIndex(i, j);
                neighbours[idx] = new Integer[8];
                int n = 0;

                // top left
                if (i - 1 >= 0 && j - 1 >= 0) {
                    neighbours[idx][n++] = get1DIndex(i - 1, j - 1);
                }

                // top
                if (i - 1 >= 0) {
                    neighbours[idx][n++] = get1DIndex(i - 1, j);
                }

                // top right
                if (i - 1 >= 0 && j + 1 < board.cols()) {
                    neighbours[idx][n++] = get1DIndex(i - 1, j + 1);
                }

                // left
                if (j - 1 >= 0) {
                    neighbours[idx][n++] = get1DIndex(i, j - 1);
                }

                // right
                if (j + 1 < board.cols()) {
                    neighbours[idx][n++] = get1DIndex(i, j + 1);
                }

                // bottom left
                if (i + 1 < board.rows() && j - 1 >= 0) {
                    neighbours[idx][n++] = get1DIndex(i + 1, j - 1);
                }

                // bottom
                if (i + 1 < board.rows()) {
                    neighbours[idx][n++] = get1DIndex(i + 1, j);
                }

                // bottom right
                if (i + 1 < board.rows() && j + 1 < board.cols()) {
                    neighbours[idx][n] = get1DIndex(i + 1, j + 1);
                }
            }
        }

        // Solve
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                char letter = board.getLetter(i, j);
                String prefix = "" + letter;
                if (letter == 'Q') prefix += 'U';
                Node x = new Node(i, j, prefix, new boolean[board.rows() * board.cols()]);
                solve(x);
            }
        }

        return result;
    }

    private void solve(Node start) {
        LinkedList<Node> stack = new LinkedList<>();
        stack.add(start);

        while (!stack.isEmpty()) {
            Node x = stack.removeLast();
            Integer[] xNeighbours = neighbours[get1DIndex(x.i, x.j)];
            for (int i = 0; i < xNeighbours.length; i++) {
                if (xNeighbours[i] == null) break;
                int[] ij = get2DIndex(xNeighbours[i]);
                addNeighbour(ij[0], ij[1], x, stack);
            }
        }
    }

    private void addNeighbour(int i, int j, Node prev, LinkedList<Node> stack) {
        if (!prev.marked[get1DIndex(i, j)]) {
            char letter = board.getLetter(i, j);
            String prefix = prev.prefix + letter;
            if (letter == 'Q') prefix += 'U';
            TrieNode trieResult = dictionary.contains(prefix);

            if (trieResult == null) return;
            if (trieResult.isString && prefix.length() > 2) result.add(prefix);
            stack.addLast(new Node(i, j, prefix, prev.marked));
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) throw new NullPointerException();
        TrieNode trieNode = dictionary.contains(word);
        if (trieNode == null || !trieNode.isString) return 0;

        int l = word.length();

        if (l <= 2) return 0;
        if (l == 3 || l == 4) return 1;
        if (l == 5) return 2;
        if (l == 6) return 3;
        if (l == 7) return 5;

        return 11;
    }

    private int get1DIndex(int i, int j) {
        return i * board.cols() + j;
    }

    private int[] get2DIndex(int i) {
        int[] result = new int[2];
        result[0] = i / board.cols();
        result[1] = i % board.cols();
        return result;
    }

    public static void main(String[] args) {
        String dictionaryFilename = "inputs/boggle/dictionary-yawl.txt";
        String boardFilename = "inputs/boggle/board-dichlorodiphenyltrichloroethanes.txt";

        if (args.length == 2) {
            dictionaryFilename = args[0];
            boardFilename = args[1];
        }

        In in = new In(dictionaryFilename);
        String[] dictionary = in.readAllStrings();

        long startDictInit = System.currentTimeMillis();
        BoggleSolver solver = new BoggleSolver(dictionary);
        System.out.println("Dictionary init: " + (System.currentTimeMillis() - startDictInit) / 1000.0);

        BoggleBoard board = new BoggleBoard(boardFilename);
        long startScores = System.currentTimeMillis();

        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }

        StdOut.println("Score = " + score);

        System.out.println("Scores count time: " + (System.currentTimeMillis() - startScores) / 1000.0);
    }
}
