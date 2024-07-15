package org.sterra.structure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ListIterator;

public class LoggingListIteratorProxy<T> implements ListIterator<T> {

    private static final Logger log = LogManager.getLogger();

    private final ListIterator<T> target;

    public LoggingListIteratorProxy(ListIterator<T> target) {
        this.target = target;
    }

    @Override
    public boolean hasNext() {
        var hasNext = target.hasNext();
        if (!hasNext && log.isDebugEnabled()) {
            log.debug("No Next => next pos {} & prev pos {}", target.nextIndex(), target.previousIndex());
        }
        return hasNext;
    }

    @Override
    public T next() {
        if (log.isDebugEnabled()) {
            log.debug("Next pos {}", target.nextIndex());
        }
        return target.next();
    }

    @Override
    public boolean hasPrevious() {
        var hasPrev = target.hasPrevious();
        if (!hasPrev && log.isDebugEnabled()) {
            log.debug("No Prev => next pos {} & prev pos {}", target.nextIndex(), target.previousIndex());
        }
        return hasPrev;
    }

    @Override
    public T previous() {
        if (log.isDebugEnabled()) {
            log.debug("Prev pos {}", target.previousIndex());
        }
        return target.previous();
    }

    @Override
    public int nextIndex() {
        return target.nextIndex();
    }

    @Override
    public int previousIndex() {
        return target.previousIndex();
    }

    @Override
    public void remove() {
        target.remove();
    }

    @Override
    public void set(T t) {
        target.set(t);
    }

    @Override
    public void add(T t) {
        target.add(t);
    }
}
