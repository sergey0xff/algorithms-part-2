public class CircularSuffixArray {
    private int[] indexes;
    private char[] chars;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new NullPointerException();
        indexes = new int[s.length()];
        chars = s.toCharArray();

        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }

        sort(0, indexes.length - 1, 0);
    }

    // length of s
    public int length() {
        return indexes.length;
    }

    // returns indexes of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= indexes.length) throw new IndexOutOfBoundsException();
        return indexes[i];
    }

    private void sort(int lo, int hi, int offset) {
        if (lo >= hi || offset >= chars.length) return;

        int lt = lo, gt = hi;
        char a = charAt(lo, offset);
        int i = lo + 1;

        while (i <= gt) {
            char b = charAt(i, offset);

            if (b < a) {
                exch(lt++, i++);
            } else if (b > a) {
                exch(i, gt--);
            } else {
                i++;
            }
        }

        sort(lo, lt - 1, offset);

        if (lt != gt) {
            sort(lt, gt, offset + 1);
        }

        sort(gt + 1, hi, offset);
    }

    private void exch(int i, int j) {
        int tmp = indexes[i];
        indexes[i] = indexes[j];
        indexes[j] = tmp;
    }

    private char charAt(int i, int offset) {
        return chars[(indexes[i] + offset) % chars.length];
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
