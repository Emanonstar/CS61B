import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();
    static CharacterComparator offByOne = new OffByOne();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        assertTrue(palindrome.isPalindrome(""));
        assertTrue(palindrome.isPalindrome("a"));
        assertTrue(palindrome.isPalindrome("racecar"));
        assertTrue(palindrome.isPalindrome("noon"));
        assertFalse(palindrome.isPalindrome("horse"));
        assertFalse(palindrome.isPalindrome("apple"));
        assertFalse(palindrome.isPalindrome("food"));
        assertTrue(palindrome.isPalindrome("", offByOne));
        assertTrue(palindrome.isPalindrome("a", offByOne));
        assertTrue(palindrome.isPalindrome("nonm", offByOne));
        assertTrue(palindrome.isPalindrome("flake", offByOne));
        assertTrue(palindrome.isPalindrome("noqnm", offByOne));
        assertFalse(palindrome.isPalindrome("noon", offByOne));
        assertFalse(palindrome.isPalindrome("racecar", offByOne));
        assertFalse(palindrome.isPalindrome("apple", offByOne));
        assertFalse(palindrome.isPalindrome("horse", offByOne));
    }
}
