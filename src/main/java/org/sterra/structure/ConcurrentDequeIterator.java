package org.sterra.structure;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentDequeIterator implements ListIterator<Integer> {

    private final Iterator<Integer> forwardIterator;
    private final Iterator<Integer> reverseIterator;
    private final AtomicInteger leftIdx;
    private final AtomicInteger rightIdx;

    public ConcurrentDequeIterator(DoubleLinkedList<Integer> linkedList) {
        this.forwardIterator = linkedList.iterator();
        this.reverseIterator = linkedList.reverse();
        this.leftIdx = new AtomicInteger(0);
        this.rightIdx = new AtomicInteger(linkedList.size()-1);
    }

    @Override
    public boolean hasNext() {
        return leftIdx.get() <= rightIdx.get() && forwardIterator.hasNext();
    }

    @Override
    public Integer next() {
        if (!hasNext()) {
            log("No Next => From start %d. From finish %d", leftIdx.get(), rightIdx.get());
            return null;
        }
        int nextCount = leftIdx.incrementAndGet();
        log("Next idx %d", nextCount);
        return forwardIterator.next();
    }

    @Override
    public boolean hasPrevious() {
        return leftIdx.get() <= rightIdx.get() && reverseIterator.hasNext();
    }

    @Override
    public Integer previous() {
        if (!hasPrevious()) {
            log("No Prev => From start %d. From finish %d", leftIdx.get(), rightIdx.get());
            return null;
        }
        int prevIdx = rightIdx.decrementAndGet();
        var prev = reverseIterator.next();
        log("Prev idx %d", prevIdx);
        return prev;
    }

    @Override
    public int nextIndex() {
        return hasNext() ? leftIdx.get() + 1 : leftIdx.get();
    }

    @Override
    public int previousIndex() {
        return hasPrevious() ? rightIdx.get() - 1 : rightIdx.get();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove() not supported");
    }

    @Override
    public void set(Integer integer) {
        throw new UnsupportedOperationException("set() not supported");
    }

    @Override
    public void add(Integer integer) {
        throw new UnsupportedOperationException("add() not supported");
    }

    @Override
    public String toString() {
        return String.format("Current start pos '%d' and finish pos '%d'", leftIdx.get(), rightIdx.get());
    }

    private void log(String template, Object... args) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + String.format(template, args));
    }
}
