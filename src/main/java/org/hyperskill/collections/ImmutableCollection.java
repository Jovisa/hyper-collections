package org.hyperskill.collections;

import java.util.*;

public final class ImmutableCollection<T> {
    private final List<T> data;

    public static <T> ImmutableCollection<T> of() {
        return new ImmutableCollection<>(List.of());
    }

    public static <T> ImmutableCollection<T> of(T... elements) {
        List<T> data = Arrays.stream(elements)
                .map(Objects::requireNonNull)
                .toList();

        return new ImmutableCollection<>(data);
    }

    private ImmutableCollection(List<T> data) {
        this.data = data;
    }


    public boolean contains(T element) {
        return data.contains(element);
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

}
