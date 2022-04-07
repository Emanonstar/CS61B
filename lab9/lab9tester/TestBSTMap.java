package lab9tester;

import static org.junit.Assert.*;

import org.junit.Test;
import lab9.BSTMap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Tests by Brendan Hu, Spring 2015, revised for 2018 by Josh Hug
 */
public class TestBSTMap {

    @Test
    public void sanityGenericsTest() {
        try {
            BSTMap<String, String> a = new BSTMap<String, String>();
            BSTMap<String, Integer> b = new BSTMap<String, Integer>();
            BSTMap<Integer, String> c = new BSTMap<Integer, String>();
            BSTMap<Boolean, Integer> e = new BSTMap<Boolean, Integer>();
        } catch (Exception e) {
            fail();
        }
    }

    //assumes put/size/containsKey/get work
    @Test
    public void sanityClearTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1 + i);
            //make sure put is working via containsKey and get
            assertTrue(null != b.get("hi" + i));
            assertTrue(b.get("hi" + i).equals(1 + i));
            assertTrue(b.containsKey("hi" + i));
        }
        assertEquals(455, b.size());
        b.clear();
        assertEquals(0, b.size());
        for (int i = 0; i < 455; i++) {
            assertTrue(null == b.get("hi" + i) && !b.containsKey("hi" + i));
        }
    }

    // assumes put works
    @Test
    public void sanityContainsKeyTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertFalse(b.containsKey("waterYouDoingHere"));
        b.put("waterYouDoingHere", 0);
        assertTrue(b.containsKey("waterYouDoingHere"));
    }

    // assumes put works
    @Test
    public void sanityGetTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertEquals(null, b.get("starChild"));
        assertEquals(0, b.size());
        b.put("starChild", 5);
        assertTrue(((Integer) b.get("starChild")).equals(5));
        b.put("KISS", 5);
        assertTrue(((Integer) b.get("KISS")).equals(5));
        assertNotEquals(null, b.get("starChild"));
        assertEquals(2, b.size());
    }

    // assumes put works
    @Test
    public void sanitySizeTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertEquals(0, b.size());
        b.put("hi", 1);
        assertEquals(1, b.size());
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1);
        }
        assertEquals(456, b.size());
    }

    //assumes get/containskey work
    @Test
    public void sanityPutTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("hi", 1);
        assertTrue(b.containsKey("hi"));
        assertTrue(b.get("hi") != null);
    }

    @Test
    public void keySetTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("i", 1);
        b.put("am", 2);
        b.put("iron", 3);
        b.put("man", 4);
        Set<String> exp = new HashSet<>();
        exp.add("am");
        exp.add("i");
        exp.add("iron");
        exp.add("man");
        assertEquals(exp, b.keySet());
    }

    @Test
    public void removeTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("i", 1);
        int actual = b.remove("i");
        assertEquals(1, actual);
        assertEquals(0, b.size());
        b.put("d", 4);
        assertEquals(1, b.size());
        b.put("b", 2);
        b.put("f", 9);
        b.put("a", 1);
        b.put("c", 3);
        b.put("elf", 5);
        b.put("eye", 6);
        b.put("g", 7);

        Integer a = b.remove("g", 99);
        assertEquals(null, a);
        assertEquals(8, b.size());
        actual = b.remove("g", 7);
        assertEquals(7, actual);
        a = b.remove("h");
        assertEquals(null, a);
        a = b.remove("f", 66);
        assertEquals(null, a);
        actual = b.remove("f", 9);
        assertEquals(9, actual);
        actual = b.remove("d", 4);
        assertEquals(4, actual);
        assertEquals(5, b.size());
    }

    @Test
    public void iteratorTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("d", 4);
        assertEquals(1, b.size());
        b.put("b", 2);
        b.put("f", 9);
        b.put("a", 1);
        b.put("c", 3);
        b.put("elf", 5);
        b.put("eye", 6);
        b.put("g", 7);

        Iterator<String> i = b.iterator();
        while (i.hasNext()) {
            System.out.println(i.next());
        }
    }


    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestBSTMap.class);
    }
}
