package org.sterra.structure;

import java.util.Iterator;

public interface ReverseIterable<T> extends Iterable<T> {
    Iterator<T> reverse();
}
