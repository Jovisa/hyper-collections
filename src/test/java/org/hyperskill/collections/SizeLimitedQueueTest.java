package org.hyperskill.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SizeLimitedQueueTest {

    @Test
    public void sizeLimitedQueueTest() {
        SizeLimitedQueue<Integer> collection = new SizeLimitedQueue<>(3); // limit is 3

        assertEquals("[]", collection.toString());
        assertEquals(3, collection.maxSize());
        assertEquals(0, collection.size());
        assertTrue(collection.isEmpty());

        collection.add(1);
        collection.add(2);

        assertFalse(collection.isEmpty());
        assertEquals("[1, 2]", collection.toString());
        assertFalse(collection.isAtFullCapacity());

        collection.add(3);

        assertEquals("[1, 2, 3]", collection.toString());
        assertTrue(collection.isAtFullCapacity());

        collection.add(4);

        assertEquals("[2, 3, 4]", collection.toString());
        assertEquals(2, collection.peek());
        assertEquals("[2, 3, 4]", collection.toString());
        assertEquals(2, collection.remove());
        assertEquals("[3, 4]", collection.toString());

        assertEquals(Object[].class, collection.toArray().getClass());
        assertEquals(Integer[].class, collection.toArray(new Integer[0]).getClass());
    }

}