import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class WordNet {
    // synset on id mapping
    private Digraph g;
    private int graphSize;
    private HashMap<String, List<Integer>> nouns = new HashMap<>();
    private HashMap<Integer, String> synsetById = new HashMap<>();
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new NullPointerException();

        readSynsets(synsets);
        readHypernyms(hypernyms);

        checkRootedDAG();

        sap = new SAP(g);
    }

    private void checkRootedDAG() {
        if (new DirectedCycle(g).hasCycle())
            throw new IllegalArgumentException();

        int roots = 0;

        for (int i = 0; i < g.V(); i++) {
            int length = 0;

            for (Integer ignored : g.adj(i)) {
                length++;
            }

            if (length == 0) {
                roots++;
            }
        }

        if (roots != 1)
            throw new IllegalArgumentException();
    }

    private void readHypernyms(String hypernyms) {
        In in = new In(hypernyms);
        g = new Digraph(graphSize);

        while (in.hasNextLine()) {
            String[] chunks = in.readLine().trim().split(",");
            int v = Integer.parseInt(chunks[0]);
            for (int i = 1; i < chunks.length; i++) {
                g.addEdge(v, Integer.parseInt(chunks[i]));
            }
        }
    }

    private void readSynsets(String synsets) {
        In in = new In(synsets);

        while (in.hasNextLine()) {
            String[] chunks = in.readLine().trim().split(",");
            int id = Integer.parseInt(chunks[0]);
            String[] synsetItems = chunks[1].trim().split(" ");
            synsetById.put(id, chunks[1].trim());
            for (String synset : synsetItems) {
                if (!nouns.containsKey(synset)) {
                    nouns.put(synset, new LinkedList<Integer>());
                }
                nouns.get(synset).add(id);
            }
            graphSize++;
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new NullPointerException();
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new NullPointerException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        List<Integer> vList = nouns.get(nounA);
        List<Integer> wList = nouns.get(nounB);
        return sap.length(vList, wList);
    }

    // a synset (second field of synsets.txt) that is
    // the common ancestor of nounA and nounB in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new NullPointerException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        List<Integer> v = nouns.get(nounA);
        List<Integer> w = nouns.get(nounB);
        return synsetById.get(sap.ancestor(v, w));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wd = new WordNet("inputs/wordnet/synsets.txt", "inputs/wordnet/hypernyms.txt");

        assert wd.distance("white_marlin", "mileage") == 23;
        assert wd.distance("Black_Plague", "black_marlin") == 33;
        assert wd.distance("American_water_spaniel", "histology") == 27;
        assert wd.distance("Brown_Swiss", "barrel_roll") == 29;
        assert wd.distance("worm", "bird") == 5;
    }
}