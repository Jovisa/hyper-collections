package org.hyperskill.collections;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

public class SizeLimitedQueue<E> {

    private final int MAX_SIZE;
    private final Deque<E> queue;

    public SizeLimitedQueue(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException();
        }

        MAX_SIZE = maxSize;
        queue = new LinkedList<>();
    }


    public void add(E element) {
        Objects.requireNonNull(element);

        if (isAtFullCapacity()) {
            remove();
        }
        queue.addLast(element);
    }

    public void clear() {
        queue.clear();
    }

    public boolean isAtFullCapacity() {
        return queue.size() == MAX_SIZE;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int maxSize() {
        return MAX_SIZE;
    }

    public E peek() {
        return queue.peekFirst();
    }

    public E remove() {
        return queue.removeFirst();
    }

    public int size() {
        return queue.size();
    }

    public E[] toArray(E[] e) {
        return  queue.toArray(e);
    }

    public Object[] toArray() {
        return queue.toArray();
    }

    @Override
    public String toString() {
        return queue.toString();
    }

}
