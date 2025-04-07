package org.hyperskill.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MultisetTest {

    @Test
    public void multisetTest() {
        Multiset<Character> multiset = new Multiset<>();
        multiset.add('a');
        multiset.add('b', 6);

        assertEquals("[a, b, b, b, b, b, b]", multiset.toString());
        assertFalse(multiset.contains('c'));
        assertEquals(6, multiset.count('b'));
        assertEquals("[a, b]", multiset.elementSet().toString());

        multiset.remove('a');
        multiset.remove('b', 3);
        assertEquals("[b, b, b]", multiset.toString());

        multiset.add('c');
        multiset.setCount('c', 2);
        multiset.setCount('b', 3, 4);
        assertEquals("[b, b, b, b, c, c]", multiset.toString());
    }

}