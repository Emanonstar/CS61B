public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque result = new LinkedListDeque();
        for (int i = 0, l = word.length(); i < l; i++) {
            result.addLast(word.charAt(i));
        }
        return result;
    }
}
