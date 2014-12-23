import java.util.HashMap;

public class SAP {
    private Digraph g;
    private HashMap<String, SearchResult> cache = new HashMap<>();
    private HashMap<String, SearchResult> cacheIters = new HashMap<>();

    private static class SearchResult {
        private int ancestor;
        private int length;

        private SearchResult(int ancestor, int length) {
            this.ancestor = ancestor;
            this.length = length;
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new NullPointerException();
        this.g = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v > g.V() - 1 || w < 0 || w > g.V() - 1)
            throw new IndexOutOfBoundsException();
        return search(v, w).length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v > g.V() - 1 || w < 0 || w > g.V() - 1)
            throw new IndexOutOfBoundsException();
        return search(v, w).ancestor;
    }

    private SearchResult search(int v, int w) {
        String key;

        if (v < w) {
            key = v + ":" + w;
        } else {
            key = w + ":" + v;
        }

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        if (v == w) {
            SearchResult result = new SearchResult(v, 0);
            cache.put(key, result);
            return result;
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(g, w);

        int length = Integer.MAX_VALUE;
        int ancestor = -1;

        for (int i = 0; i < g.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int dist = bfsV.distTo(i) + bfsW.distTo(i);

                if (dist < length) {
                    length = dist;
                    ancestor = i;
                }
            }
        }

        if (length == Integer.MAX_VALUE) {
            length = -1;
        }

        SearchResult result = new SearchResult(ancestor, length);
        cache.put(key, result);
        return result;
    }

    private SearchResult search(Iterable<Integer> v, Iterable<Integer> w) {
        StringBuilder builder = new StringBuilder();
        for (Integer integer : v) {
            builder.append(integer).append(",");
        }
        builder.append(":");
        for (Integer integer : w) {
            builder.append(integer).append(",");
        }
        String key = builder.toString();

        if (cacheIters.containsKey(key)) {
            return cacheIters.get(key);
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(g, w);

        SearchResult result = null;

        for (int i = 0; i < g.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int dist = bfsV.distTo(i) + bfsW.distTo(i);
                if (result == null) {
                    result = new SearchResult(i, dist);
                } else if (dist < result.length) {
                    result.length = dist;
                    result.ancestor = i;
                }
            }
        }

        if (result == null) {
            return new SearchResult(-1, -1);
        }
        cacheIters.put(key, result);
        return result;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new NullPointerException();
        return search(v, w).length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new NullPointerException();
        return search(v, w).ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String filename = "inputs/wordnet/digraph-wordnet.txt";
        if (args.length > 0) {
            filename = args[0];
        }

        In in = new In(filename);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}