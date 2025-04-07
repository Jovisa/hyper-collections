package org.hyperskill.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class RangeTest {

    @Test
    public void usualCasesTest() {
        Assertions.assertEquals("(1, 10)", Range.open(1, 10).toString());
        Assertions.assertEquals("[1, 10]", Range.closed(1, 10).toString());
        Assertions.assertEquals("(1, 10]", Range.openClosed(1, 10).toString());
        Assertions.assertEquals("[1, 10)", Range.closedOpen(1, 10).toString());
        Assertions.assertEquals("(1, INF)", Range.greaterThan(1).toString());
        Assertions.assertEquals("[1, INF)", Range.atLeast(1).toString());
        Assertions.assertEquals("(-INF, 10)", Range.lessThan(10).toString());
        Assertions.assertEquals("(-INF, 10]", Range.atMost(10).toString());
        Assertions.assertEquals("(-INF, INF)", Range.all().toString());
    }

    @Test
    public void cornerCases() {
        Assertions.assertEquals("[1, 1]", Range.closed(1, 1).toString());
        Assertions.assertEquals("EMPTY", Range.openClosed(1, 1).toString());
        Assertions.assertEquals("EMPTY", Range.closedOpen(1, 1).toString());
    }

    @Test
    public void methodsTest() {
        assertTrue(Range.open(1, 10).contains(5));
        assertFalse(Range.open(1, 10).contains(10));
        assertTrue(Range.openClosed(1, 10).contains(10));
        assertTrue(Range.closed(1, 10).encloses(Range.closed(5, 7)));
        assertFalse(Range.open(1, 10).encloses(Range.closedOpen(1, 10)));
        assertTrue(Range.closed(1, 10).encloses(Range.open(1, 10)));
        assertTrue(Range.all().encloses(Range.all()));

        Assertions.assertEquals("(-INF, 10]",
                Range.atMost(10).span(Range.closed(1, 5)).toString());
        Assertions.assertEquals("(-INF, INF)",
                Range.atMost(10).span(Range.atLeast(11)).toString());
        Assertions.assertEquals("EMPTY", Range.closedOpen(1, 1)
                .intersection(Range.openClosed(1, 1))
                .toString());
        Assertions.assertEquals("EMPTY",
                Range.atMost(10).intersection(Range.greaterThan(10)).toString());

        Range<Integer> range = Range.all();
        Assertions.assertEquals("[5, INF)",
                range.intersection(Range.atLeast(5)).toString());
        Assertions.assertEquals("(-INF, INF)",
                range.span(Range.atMost(10)).toString());



        assertTrue(Range.closed(0, 0).encloses(Range.closed(0, 0)));
    }

    @Test
    public void unusualMethodCases() {
        assertFalse(Range.openClosed(1, 1).contains(1));
        assertFalse(Range.lessThan(10).contains(10));
        assertTrue(Range.atMost(10).contains(10));
        assertFalse(Range.greaterThan(10).contains(10));
        assertTrue(Range.atLeast(10).contains(10));
        assertTrue(Range.closed(0, 0).encloses(Range.closed(0, 0)));
        assertTrue(Range.openClosed(0, 5).encloses(Range.closedOpen(0, 0)));
        assertFalse(Range.closedOpen(0, 5).encloses(Range.openClosed(0, 5)));
        assertFalse(Range.openClosed(0, 0).encloses(Range.closedOpen(0, 5)));
        assertFalse(Range.openClosed(0, 0).encloses(Range.closedOpen(0, 0)));

        Assertions.assertEquals("[10, 10]", Range.closed(10,10).span(Range.closed(10,10)).toString());
        Assertions.assertEquals("[10, 10]", Range.closed(10,10).span(Range.openClosed(10,10)).toString());
        Assertions.assertEquals("[10, 10]", Range.closed(10,10).span(Range.closedOpen(10,10)).toString());
        Assertions.assertEquals("[10, 10]", Range.openClosed(10,10).span(Range.closed(10,10)).toString());
        Assertions.assertEquals("EMPTY", Range.openClosed(10,10).span(Range.openClosed(10,10)).toString());
        Assertions.assertEquals("EMPTY", Range.openClosed(10,10).span(Range.closedOpen(10,10)).toString());
        Assertions.assertEquals("[10, 10]", Range.closedOpen(10,10).span(Range.closed(10,10)).toString());
        Assertions.assertEquals("[10, 10]", Range.closedOpen(10,10).span(Range.closed(10,10)).toString());
        Assertions.assertEquals("EMPTY",
                Range.closedOpen(10,10).span(Range.openClosed(10,10)).toString());
        Assertions.assertEquals("EMPTY",
                Range.closedOpen(10,10).span(Range.closedOpen(10,10)).toString());
        Assertions.assertEquals("[10, 10]",
                Range.closed(10,10).intersection(Range.closed(10,10)).toString());
        Assertions.assertEquals("EMPTY",
                Range.closed(10,10).intersection(Range.openClosed(10,10)).toString());
        Assertions.assertEquals("EMPTY",
                Range.closed(10,10).intersection(Range.closedOpen(10,10)).toString());
        Assertions.assertEquals("EMPTY",
                Range.openClosed(10,10).intersection(Range.closed(10,10)).toString());
        Assertions.assertEquals("EMPTY",
                Range.openClosed(10,10).intersection(Range.openClosed(10,10)).toString());
        Assertions.assertEquals("EMPTY",
                Range.openClosed(10,10).intersection(Range.closedOpen(10,10)).toString());
        Assertions.assertEquals("EMPTY",
                Range.closedOpen(10,10).intersection(Range.closed(10,10)).toString());
        Assertions.assertEquals("EMPTY",
                Range.closedOpen(10,10).intersection(Range.openClosed(10,10)).toString());
        Assertions.assertEquals("EMPTY",
                Range.closedOpen(10,10).intersection(Range.closedOpen(10,10)).toString());
    }


}