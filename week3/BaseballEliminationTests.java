import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

public class BaseballEliminationTests {
    private BaseballElimination elimination = new BaseballElimination("inputs/baseball/teams4.txt");;

    @Test
    public void testTeams() {
        HashSet<String> expected = new HashSet<>();
        HashSet<String> actual = new HashSet<>();

        expected.add("Atlanta");
        expected.add("Philadelphia");
        expected.add("New_York");
        expected.add("Montreal");

        for (String team : elimination.teams()) {
            actual.add(team);
        }

        assertTrue(expected.equals(actual));
    }

    @Test
    public void testWins() {
        assertEquals(83, elimination.wins("Atlanta"));
        assertEquals(80, elimination.wins("Philadelphia"));
        assertEquals(78, elimination.wins("New_York"));
        assertEquals(77, elimination.wins("Montreal"));
    }

    @Test
    public void testLosses() {
        assertEquals(71, elimination.losses("Atlanta"));
        assertEquals(79, elimination.losses("Philadelphia"));
        assertEquals(78, elimination.losses("New_York"));
        assertEquals(82, elimination.losses("Montreal"));
    }

    @Test
    public void testRemaining() {
        assertEquals(8, elimination.remaining("Atlanta"));
        assertEquals(3, elimination.remaining("Philadelphia"));
        assertEquals(6, elimination.remaining("New_York"));
        assertEquals(3, elimination.remaining("Montreal"));
    }

    @Test
    public void testAgainst() {
        assertEquals(1, elimination.against("Atlanta", "Philadelphia"));
        assertEquals(6, elimination.against("Atlanta", "New_York"));
        assertEquals(1, elimination.against("Montreal", "Atlanta"));
        assertEquals(0, elimination.against("Philadelphia", "New_York"));
    }

    @Test
    public void testNumberOfTeams() {
        assertEquals(4, elimination.numberOfTeams());
    }
}
