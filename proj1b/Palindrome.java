public class Palindrome {

    /** Returns a Deque where the characters appear in the same order as in the String. */
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> result = new LinkedListDeque<>();
        for (int i = 0, l = word.length(); i < l; i++) {
            result.addLast(word.charAt(i));
        }
        return result;
    }

    /** Returns true if the given word is a palindrome, and false otherwise.
     * A palindrome is defined as a word that is the same whether it is read
     * forwards or backwards. */
    public boolean isPalindrome(String word) {
        Deque<Character> wordInDeque = wordToDeque(word);
        return isPalindromeHelper(wordInDeque);
    }

    private boolean isPalindromeHelper(Deque<Character> deque) {
        if (deque.size() == 0 || deque.size() == 1) {
            return true;
        }
        char first = deque.removeFirst();
        char last = deque.removeLast();
        if (first != last) {
            return false;
        }
        return isPalindromeHelper(deque);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> wordInDeque = wordToDeque(word);
        return isPalindromeHelper(wordInDeque, cc);
    }

    private boolean isPalindromeHelper(Deque<Character> deque, CharacterComparator cc) {
        if (deque.size() == 0 || deque.size() == 1) {
            return true;
        }
        char first = deque.removeFirst();
        char last = deque.removeLast();
        if (!cc.equalChars(first, last)) {
            return false;
        }
        return isPalindromeHelper(deque, cc);
    }
}
