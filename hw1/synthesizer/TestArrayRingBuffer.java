package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(10);
        assertTrue(arb.isEmpty());
        assertEquals(10, arb.capacity());
        assertEquals(0, arb.fillCount());
        //arb.dequeue();
        //arb.peek();
        for (int i = 0; i < 10; i++) {
            arb.enqueue(i);
        }
        //arb.enqueue(42);
        assertEquals((Integer) 0, arb.peek());
        assertEquals(10, arb.fillCount());
        assertEquals((Integer) 0, arb.dequeue());
        assertEquals(9, arb.fillCount());
        assertEquals((Integer) 1, arb.dequeue());
        assertEquals(8, arb.fillCount());

    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
