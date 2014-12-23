import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BaseballElimination {
    private int n;
    private HashMap<String, Integer> teamToIdx = new HashMap<>();
    private HashMap<Integer, String> idxToTeam = new HashMap<>();
    private HashMap<String, List<String>> eliminated = new HashMap<>();
    private int[] wins; // wins of a team i
    private int maxWin; // max wins of the top team
    private String maxWinTeam; // name of the top team with max wins
    private int[] losses; // losses of a team i
    private int[] remaining; // games remaining of a team i
    private int[][] gridRemaining; // games remaining of team i and team j

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null) throw new NullPointerException();
        parseTeams(filename);
    }

    private void parseTeams(String filename) {
        In in = new In(filename);
        int n = in.readInt();
        this.n = n;

        wins = new int[n];
        losses = new int[n];
        remaining = new int[n];
        gridRemaining = new int[n][n];

        for (int i = 0; i < n; i++) {
            String team = in.readString();
            teamToIdx.put(team, i);
            idxToTeam.put(i, team);

            int win = in.readInt();
            if (win > maxWin) {
                maxWin = win;
                maxWinTeam = team;
            }

            wins[i] = win;
            losses[i] = in.readInt();
            remaining[i] = in.readInt();

            for (int j = 0; j < n; j++) {
                gridRemaining[i][j] = in.readInt();
            }
        }
    }

    private void trivial(String team) {
        int teamIdx = teamToIdx.get(team);
        if (wins[teamIdx] + remaining[teamIdx] < maxWin) {
            LinkedList<String> list = new LinkedList<>();
            list.add(maxWinTeam);
            eliminated.put(team, list);
        }
    }

    private void nonTrivial(String team) {
        int teamIdx = teamToIdx.get(team);

        int uniqueGames = n * (n - 1) / 2;
        int v = uniqueGames + n + 2;
        int s = v - 1;
        int t = v - 2;

        FlowNetwork flowNetwork = new FlowNetwork(v);

        // connect s to each game
        // connect each game to corresponding teamToIdx
        int gameV = 0;
        for (int i = 0; i < n - 1; i++) {
            if (i == teamIdx) continue;

            for (int j = i + 1; j < n; j++) {
                if (j == teamIdx) continue;
                flowNetwork.addEdge(new FlowEdge(s, gameV, gridRemaining[i][j]));
                flowNetwork.addEdge(new FlowEdge(gameV, uniqueGames + i, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(gameV, uniqueGames + j, Double.POSITIVE_INFINITY));
                ++gameV;
            }
        }

        // connect each game to artificial vertex t
        for (int i = 0; i < n; i++) {
            if (i == teamIdx) continue;

            double capacity = Math.max(0, wins[teamIdx] + remaining[teamIdx] - wins[i]);
            flowNetwork.addEdge(new FlowEdge(uniqueGames + i, t, capacity));
        }
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, s, t);

        LinkedList<String> elimination = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            if (fordFulkerson.inCut(uniqueGames + i)) {
                elimination.add(idxToTeam.get(i));
            }
        }

        eliminated.put(team, elimination);
    }

    private List<String> computeElimination(String team) {
        if (eliminated.get(team) == null) trivial(team);
        if (eliminated.get(team) == null) nonTrivial(team);
        return eliminated.get(team);
    }

    // number of teamToIdx
    public int numberOfTeams() {
        return n;
    }

    // all teamToIdx
    public Iterable<String> teams() {
        return teamToIdx.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        checkTeam(team);
        return wins[teamToIdx.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        checkTeam(team);
        return losses[teamToIdx.get(team)];
    }

    // number of remaining gridRemaining for given team
    public int remaining(String team) {
        checkTeam(team);
        return remaining[teamToIdx.get(team)];
    }

    // number of remaining gridRemaining between team1 and team2
    public int against(String team1, String team2) {
        checkTeam(team1);
        checkTeam(team2);
        return gridRemaining[teamToIdx.get(team1)][teamToIdx.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        checkTeam(team);
        List<String> elimination = computeElimination(team);
        return !elimination.isEmpty();
    }

    // subset R of teamToIdx that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        checkTeam(team);
        List<String> elimination = computeElimination(team);
        if (!elimination.isEmpty()) {
            return elimination;
        }
        return null;
    }

    private void checkTeam(String team) {
        if (team == null) throw new NullPointerException();
        if (!teamToIdx.containsKey(team)) throw new IllegalArgumentException("Wrong team: " + team);
    }

    public static void main(String[] args) {
        String filename = "inputs/baseball/teams54.txt";
        if (args.length == 1) filename = args[0];

        BaseballElimination division = new BaseballElimination(filename);

        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}