package org.sterra.structure;

import java.util.concurrent.LinkedBlockingDeque;

public class BlockingDeque<T> {

    private final LinkedBlockingDeque<T> deque;

    public BlockingDeque(ReverseIterable<T> iterable) {
        this.deque = new LinkedBlockingDeque<>();
        fillDeque(iterable, deque);
    }

    private void fillDeque(Iterable<T> iterable, LinkedBlockingDeque<T> deque) {
        for (T t : iterable) {
            deque.offer(t);
        }
    }

    public T pollFirst() {
        return deque.pollFirst();
    }

    public T pollLast() {
        return deque.pollLast();
    }
}
