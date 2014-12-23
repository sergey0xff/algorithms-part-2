import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WordNetTest {
    private WordNet testWordNet = new WordNet("inputs/wordnet/synsets.txt", "inputs/wordnet/hypernyms.txt");


    @Test(expected = NullPointerException.class)
    public void testConstructorThrowsNullPointerException() {
        new WordNet(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testIsNounThrowsNullPointerException() {
        testWordNet.isNoun(null);
    }

    @Test(expected = NullPointerException.class)
    public void testDistanceThrowsNullPointerException() {
        testWordNet.distance(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testSapThrowsNullPointerException() {
        testWordNet.sap(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCycledGraphInvalid() {
        new WordNet("inputs/wordnet/synsets3.txt", "inputs/wordnet/hypernymsInvalidCycle.txt");
    }

    @Test
    public void testIsNoun() {
        String[] nouns = {"word", "bird", "fowl", "worm", "louse", "dirt_ball"};
        String[] notNouns = {"", "123456", "adsf90j", "randomSTRING123"};
        for (String noun : nouns) {
            assertTrue(testWordNet.isNoun(noun));
        }
        for (String notNoun : notNouns) {
            assertFalse(testWordNet.isNoun(notNoun));
        }
    }
}
