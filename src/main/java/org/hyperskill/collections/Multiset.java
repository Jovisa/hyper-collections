package org.hyperskill.collections;

import java.util.*;
import java.util.stream.Stream;

public class Multiset<E> {

    private final Map<E, Integer> elementCounts = new LinkedHashMap<>();

    public void add(E element) {
        add(element, 1);
    }

    public void add(E element, int occurrences) {
        if (occurrences > 0) {
            elementCounts.merge(element, occurrences, Integer::sum);
        }
    }

    public boolean contains(E element) {
        return elementCounts.containsKey(element);
    }

    public int count(E element) {
        return elementCounts.getOrDefault(element, 0);
    }

    public Set<E> elementSet() {
        return new HashSet<>(elementCounts.keySet());
    }

    public void remove(E element) {
       remove(element, 1);
    }

    public void remove(E element, int count) {
        if (count > 0) {
            elementCounts.computeIfPresent(element, (key, oldCount) ->
                    (oldCount <= count) ? null : oldCount - count);
        }
    }

    public void setCount(E element, int count) {
        if (!elementCounts.containsKey(element)) return;
        if (count < 0) return;

        if (count == 0) {
            elementCounts.remove(element);
        } else {
            elementCounts.put(element, count);
        }
    }

    public void setCount(E element, int oldCount, int newCount) {
        if (oldCount == elementCounts.getOrDefault(element, 0)) {
            setCount(element, newCount);
        }
    }

    @Override
    public String toString() {
        return elementCounts.entrySet()
                .stream()
                .flatMap(entry ->
                        Stream.generate(entry::getKey)
                                .limit(entry.getValue()))
                .toList()
                .toString();
    }

}
