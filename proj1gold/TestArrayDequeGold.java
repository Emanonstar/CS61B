import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {

    @Test
    public void testStudentArrayDeque() {

        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();
        String log = "\n";
        while (true) {
            int intBetweenZeroAndEight = StdRandom.uniform(5);
            switch (intBetweenZeroAndEight) {
                case 0:
                    int item = StdRandom.uniform(10000);
                    sad.addFirst(item);
                    ads.addFirst(item);
                    log += "addFirst(" + item + ")\n";
                    break;
                case 1:
                    item = StdRandom.uniform(10000);
                    sad.addLast(item);
                    ads.addLast(item);
                    log += "addLast(" + item + ")\n";
                    break;
                case 2:
                    assertEquals(log + "isEmpty()\n", ads.isEmpty(), sad.isEmpty());
                    if (!ads.isEmpty()) {
                        Integer exp = ads.removeFirst();
                        Integer act = sad.removeFirst();
                        log += "removeFirst()\n";
                        assertEquals(exp, act);
                    }
                    break;
                case 3:
                    assertEquals(log + "isEmpty()\n", ads.isEmpty(), sad.isEmpty());
                    if (!ads.isEmpty()) {
                        Integer exp = ads.removeLast();
                        Integer act = sad.removeLast();
                        log += "removeLast()\n";
                        assertEquals(log, exp, act);
                    }
                    break;
                default:
                    assertEquals(log + "size()\n", ads.size(), sad.size());
                    if (ads.size() != 0) {
                        int index = StdRandom.uniform(ads.size());
                        Integer exp = ads.get(index);
                        Integer act = sad.get(index);
                        log += "get(" + index + ")\n";
                        assertEquals(exp, act);
                    }
            }
        }
    }
}
