package org.hyperskill.collections;

import java.util.Objects;
import java.util.stream.Stream;


public class Range<C extends Comparable> {

    private static final String FROM_OPEN = "(";
    private static final String FROM_CLOSED = "[";
    private static final String TO_OPEN = ")";
    private static final String TO_CLOSED = "]";

    private static final String INFINITY = "INF";
    private static final String MINUS_INFINITY = "-INF";

    private static final String EMPTY_RANGE = "EMPTY";


    private final Endpoint<C> from;
    private final Endpoint<C> to;


    public static <C extends Comparable<C>> Range<C> open(C fromValue, C toValue) {
        if (fromValue.equals(toValue)) {
            throw new IllegalArgumentException();
        }
        validateArguments(fromValue, toValue);
        return new Range<>(
                new Endpoint<>(FROM_OPEN, fromValue),
                new Endpoint<>(TO_OPEN, toValue));
    }

    public static <C extends Comparable<C>> Range<C> closed(C fromValue, C toValue) {
        validateArguments(fromValue, toValue);
        return new Range<>(
                new Endpoint<>(FROM_CLOSED, fromValue),
                new Endpoint<>(TO_CLOSED, toValue));
    }

    public static <C extends Comparable<C>> Range<C> openClosed(C fromValue, C toValue) {
        validateArguments(fromValue, toValue);
        return new Range<>(
                new Endpoint<>(FROM_OPEN, fromValue),
                new Endpoint<>(TO_CLOSED, toValue));
    }

    public static <C extends Comparable<C>> Range<C> closedOpen(C fromValue, C toValue) {
        validateArguments(fromValue, toValue);
        return new Range<>(
                new Endpoint<>(FROM_CLOSED, fromValue),
                new Endpoint<>(TO_OPEN, toValue));
    }

    public static <C extends Comparable<C>> Range<C> greaterThan(C fromValue) {
        Objects.requireNonNull(fromValue);
        return new Range<>(
                new Endpoint<>(FROM_OPEN, fromValue),
                new Endpoint<>(TO_OPEN, null));
    }

    public static <C extends Comparable<C>> Range<C> atLeast(C fromValue) {
        Objects.requireNonNull(fromValue);
        return new Range<>(
                new Endpoint<>(FROM_CLOSED, fromValue),
                new Endpoint<>(TO_OPEN, null));
    }

    public static <C extends Comparable<C>> Range<C> lessThan(C toValue) {
        Objects.requireNonNull(toValue);
        return new Range<>(
                new Endpoint<>(FROM_OPEN, null),
                new Endpoint<>(TO_OPEN, toValue));
    }

    public static <C extends Comparable<C>> Range<C> atMost(C toValue) {
        Objects.requireNonNull(toValue);
        return new Range<>(
                new Endpoint<>(FROM_OPEN, null),
                new Endpoint<>(TO_CLOSED, toValue));
    }

    public static <C extends Comparable<C>> Range<C> all() {
        return new Range<C>(
                new Endpoint<C>(FROM_OPEN, null),
                new Endpoint<C>(TO_OPEN, null));
    }

    public boolean contains(C value) {
        Objects.requireNonNull(value);
        if (isAbsolutelyInfinite()) return true;
        if (isEmpty()) return false;

        return containsLowerBound(value)
                && containsUpperBound(value);
    }

    private boolean containsLowerBound(C value) {
        if (isFromInfinite()) return true;

        if (FROM_OPEN.equals(from.type)
                && value.compareTo(from.value) > 0) {
            return true;
        }

        return FROM_CLOSED.equals(from.type)
                && value.compareTo(from.value) >= 0;
    }

    private boolean containsUpperBound(C value) {
        if (isToInfinite()) return true;

        if (TO_OPEN.equals(to.type)
                && value.compareTo(to.value) < 0) {
            return true;
        }

        return TO_CLOSED.equals(to.type)
                && value.compareTo(to.value) <= 0;
    }

    public boolean encloses(Range<C> other) {
        if (isAbsolutelyInfinite()) return true;
        if (isEmpty()) return false;
        if (isEmpty(other)) return true;

        return enclosesLowerBound(other) && enclosesUpperBound(other);
    }

    private boolean enclosesLowerBound(Range<C> other) {
        if (isFromInfinite()) return true;
        if (isFromInfinite(other)) return false;

        if (other.from.value.equals(this.from.value)) {
            if (FROM_CLOSED.equals(this.from.type)) {
                return true;
            }
            if (FROM_OPEN.equals(this.from.type)
                    && FROM_OPEN.equals(other.from.type)) {
                return true;
            }
        }

        return other.from.value.compareTo(this.from.value) > 0;
    }

    private boolean enclosesUpperBound(Range<C> other) {
        if (isToInfinite()) return true;
        if (isToInfinite(other)) return false;

        if (other.to.value.equals(this.to.value)) {
            if (TO_CLOSED.equals(this.to.type)) {
                return true;
            }
            if (TO_OPEN.equals(this.to.type)
                    && TO_OPEN.equals(other.to.type)) {
                return true;
            }
        }

        return other.to.value.compareTo(this.to.value) < 0;
    }


    public Range<C> intersection(Range<C> connectedRange) {
        if (isAbsolutelyInfinite()) {
            return connectedRange;
        }
        if (isAbsolutelyInfinite(connectedRange)) {
            return this;
        }

        if (isEmpty()) {
            return new Range<>(this.from, this.to);
        }
        if (isEmpty(connectedRange)) {
            return new Range<>(connectedRange.from, connectedRange.to);
        }

        Endpoint<C> intersectionFrom = getLowerIntersectionEndpoint(connectedRange);
        Endpoint<C> intersectionTo = getUpperIntersectionEndpoint(connectedRange);

        if (isEmptyIntersection(intersectionFrom, intersectionTo)) {
            return getEmptyRange(intersectionFrom.value);
        }

        return new Range<>(intersectionFrom, intersectionTo);
    }

    private boolean isEmptyIntersection(Endpoint<C> intersectionFrom, Endpoint<C> intersectionTo) {
        if (Objects.equals(intersectionFrom.value, intersectionTo.value)
                && FROM_OPEN.equals(intersectionFrom.type)
                && TO_OPEN.equals(intersectionTo.type)) {
            return true;
        }
        if (Objects.isNull(intersectionFrom.value)
                || Objects.isNull(intersectionTo.value)) {
            return false;
        }
        return intersectionFrom.value.compareTo(intersectionTo.value) > 0;
    }


    private Endpoint<C> getLowerIntersectionEndpoint(Range<C> connectedRange) {

        if (isFromInfinite() && isFromInfinite(connectedRange)) {
            return new Endpoint<>(this.from.type, this.from.value);
        }

        if (isFromInfinite()) {
            return new Endpoint<>(connectedRange.from.type, connectedRange.from.value);
        }

        if (isFromInfinite(connectedRange)) {
            return new Endpoint<>(this.from.type, this.from.value);
        }

        if (connectedRange.from.value.equals(this.from.value)) {
            String endpointType = anyOpenBound(connectedRange.from.type, this.from.type)
                    ? FROM_OPEN
                    : FROM_CLOSED;
            return new Endpoint<>(endpointType, this.from.value);
        }

        return connectedRange.from.value.compareTo(this.from.value) > 0
                ? new Endpoint<>(connectedRange.from.type, connectedRange.from.value)
                : new Endpoint<>(this.from.type, this.from.value);
    }

    private Endpoint<C> getUpperIntersectionEndpoint(Range<C> connectedRange) {

        if (isToInfinite() && isToInfinite(connectedRange)) {
            return new Endpoint<>(this.to.type, this.to.value);
        }

        if (isToInfinite()) {
            return new Endpoint<>(connectedRange.to.type, connectedRange.to.value);
        }

        if (isToInfinite(connectedRange)) {
            return new Endpoint<>(this.to.type, this.to.value);
        }

        if (connectedRange.to.value.equals(this.to.value)) {
            String endpointType = anyOpenBound(connectedRange.to.type, this.to.type)
                    ? TO_OPEN
                    : TO_CLOSED;
            return new Endpoint<>(endpointType, this.to.value);
        }

        return connectedRange.to.value.compareTo(this.to.value) < 0
                ? new Endpoint<>(connectedRange.to.type, connectedRange.to.value)
                : new Endpoint<>(this.to.type, this.to.value);
    }


    public Range<C> span(Range<C> other) {
        if (isEmpty()) {
            return new Range<>(other.from, other.to);
        }
        if (isEmpty(other)) {
            return new Range<>(this.from, this.to);
        }

        if (isAbsolutelyInfinite()) {
            return new Range<>(this.from, this.to);
        }
        if (isAbsolutelyInfinite(other)) {
            return new Range<>(other.from, other.to);
        }

        Endpoint<C> lowerEndpoint = getLowerSpanEndpoint(other);
        Endpoint<C> upperEndpoint = getUpperSpanEndpoint(other);

        return new Range<>(lowerEndpoint, upperEndpoint);
    }

    private Endpoint<C> getUpperSpanEndpoint(Range<C> other) {

        if (anyInfiniteBound(other.to.value, this.to.value)) {
            return new Endpoint<>(TO_OPEN, null);
        }

        if (other.to.value.equals(this.to.value)) {
            String endpointType = anyClosedBound(other.to.type, this.to.type)
                    ? TO_CLOSED
                    : TO_OPEN;
            return new Endpoint<>(endpointType, this.to.value);
        }

        return other.to.value.compareTo(this.to.value) > 0
                ? new Endpoint<>(other.to.type, other.to.value)
                : new Endpoint<>(this.to.type, this.to.value);
    }

    private Endpoint<C> getLowerSpanEndpoint(Range<C> other) {

        if (anyInfiniteBound(other.from.value, this.from.value)) {
            return new Endpoint<>(FROM_OPEN, null);
        }

        if (other.from.value.equals(this.from.value)) {
            String endpointType = anyClosedBound(other.from.type, this.from.type)
                    ? FROM_CLOSED
                    : FROM_OPEN;
            return new Endpoint<>(endpointType, this.from.value);
        }

        return other.from.value.compareTo(this.from.value) < 0
                ? new Endpoint<>(other.from.type, other.from.value)
                : new Endpoint<>(this.from.type, this.from.value);
    }

    private boolean anyInfiniteBound(C endpointValue, C otherEndpointValue) {
        return Stream.of(endpointValue, otherEndpointValue)
                .anyMatch(Objects::isNull);
    }

    private boolean anyClosedBound(String endpointType, String otherEndpointType) {
        return Stream.of(endpointType, otherEndpointType)
                .anyMatch(t -> FROM_CLOSED.equals(t) || TO_CLOSED.equals(t));
    }

    private boolean anyOpenBound(String endpointType, String otherEndpointType) {
        return Stream.of(endpointType, otherEndpointType)
                .anyMatch(t -> FROM_OPEN.equals(t) || TO_OPEN.equals(t));
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return EMPTY_RANGE;
        }
        final String fromValue = from.value == null
                ? MINUS_INFINITY
                : from.value.toString();
        final String toValue = to.value == null
                ? INFINITY
                : to.value.toString();

        return String.format("%s%s, %s%s", from.type, fromValue, toValue, to.type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Range<?> other)) return false;
        return Objects.equals(from, other.from)
                && Objects.equals(to, other.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    private static <C extends Comparable<C>> void validateArguments(C fromValue, C toValue) {
        if (fromValue.compareTo(toValue) > 0) {
            throw new IllegalArgumentException();
        }
        Objects.requireNonNull(fromValue);
        Objects.requireNonNull(toValue);
    }

    public boolean isEmpty() {
        return isEmpty(this);
    }

    private boolean isEmpty(Range<C> range) {
        if (isFromInfinite(range) || isToInfinite(range)) {
            return false;
        }
        Endpoint<C> from = range.from;
        Endpoint<C> to = range.to;
        return from.value.equals(to.value)
                && (FROM_OPEN.equals(from.type) && TO_CLOSED.equals(to.type)
                || FROM_CLOSED.equals(from.type) && TO_OPEN.equals(to.type));
    }

    private Range<C> getEmptyRange(C value) {
        return new Range<>(new Endpoint<>(FROM_OPEN, value), new Endpoint<>(TO_CLOSED, value));
    }

    private boolean isAbsolutelyInfinite() {
        return from.value == null
                && to.value == null;
    }

    private boolean isAbsolutelyInfinite(Range<C> range) {
        return range.from.value == null
                && range.to.value == null;
    }

    private boolean isFromInfinite() {
        return from.value == null;
    }

    private boolean isFromInfinite(Range<C> range) {
        return range.from.value == null;
    }

    private boolean isToInfinite() {
        return to.value == null;
    }

    private boolean isToInfinite(Range<C> range) {
        return range.to.value == null;
    }

    private Range(Endpoint<C> from, Endpoint<C> to) {
        this.from = from;
        this.to = to;
    }

    private record Endpoint<C>(String type, C value) {
    }

}
