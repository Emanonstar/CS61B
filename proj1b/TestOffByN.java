import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {

    static CharacterComparator offBy5 = new OffByN(5);

    @Test
    public void testEqualChars() {
        assertTrue(offBy5.equalChars('a', 'f'));
        assertTrue(offBy5.equalChars('A', 'F'));
        assertTrue(offBy5.equalChars('f', 'a'));
        assertTrue(offBy5.equalChars('o', 't'));
        assertFalse(offBy5.equalChars('a', 'e'));
        assertFalse(offBy5.equalChars('z', 'a'));
        assertFalse(offBy5.equalChars('a', 'a'));
        assertFalse(offBy5.equalChars('a', 'F'));
        assertFalse(offBy5.equalChars('A', 'f'));
    }
}
