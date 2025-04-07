package org.hyperskill.collections;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BiMapTest {

    @Test
    public void biMapTest() {
        BiMap<Character, Integer> biMap = new BiMap<>();

        biMap.put('a', 3);
        biMap.putAll(Map.of('b', 4, 'c', 5));

        assertEquals("{a=3, b=4, c=5}", biMap.toString());
        assertEquals("[3, 4, 5]", biMap.values().toString());

        assertThrows(IllegalArgumentException.class,() -> biMap.put('a', 6));
        assertThrows(IllegalArgumentException.class,() -> biMap.put('d', 3));
        assertThrows(IllegalArgumentException.class,() -> biMap.putAll(Map.of('d', 6, 'e', 4)));
        assertThrows(IllegalArgumentException.class,() -> biMap.putAll(Map.of('d', 6, 'c', 7)));

        biMap.putAll(Map.of('d', 6, 'e', 7));
        assertEquals("{a=3, b=4, c=5, d=6, e=7}", biMap.toString());
        assertEquals("{3=a, 4=b, 5=c, 6=d, 7=e}", biMap.inverse().toString());


        biMap.forcePut('f', 8);
        assertEquals("{a=3, b=4, c=5, d=6, e=7, f=8}", biMap.toString());
        assertEquals("{3=a, 4=b, 5=c, 6=d, 7=e, 8=f}", biMap.inverse().toString());

        biMap.forcePut('a', 9);
        assertEquals("{a=9, b=4, c=5, d=6, e=7, f=8}", biMap.toString());
        assertEquals("{4=b, 5=c, 6=d, 7=e, 8=f, 9=a}", biMap.inverse().toString());

        biMap.forcePut('g', 4);
        assertEquals("{a=9, c=5, d=6, e=7, f=8, g=4}", biMap.toString());
        assertEquals("{4=g, 5=c, 6=d, 7=e, 8=f, 9=a}", biMap.inverse().toString());

        biMap.forcePut('c', 6);
        assertEquals("{a=9, c=6, e=7, f=8, g=4}", biMap.toString());
        assertEquals("{4=g, 6=c, 7=e, 8=f, 9=a}", biMap.inverse().toString());
    }

}