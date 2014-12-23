public class Outcast {
    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int[] distances = new int[nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++) {
                distances[i] += wordnet.distance(nouns[i], nouns[j]);
            }
        }
        int maxI = 0;
        for (int i = 1; i < distances.length; i++) {
            if (distances[i] > distances[maxI]) {
                maxI = i;
            }
        }
        return nouns[maxI];
    }

    public static void main(String[] args) {
        WordNet wd = new WordNet("inputs/wordnet/synsets.txt", "inputs/wordnet/hypernyms.txt");
        Outcast outcast = new Outcast(wd);
        System.out.println(outcast.outcast("apple pear peach banana lime lemon blueberry strawberry mango watermelon potato".split(" ")));;
    }
}