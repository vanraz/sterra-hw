package org.sterra.structure;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoubleLinkedList<T> implements Iterable<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    private static class Node<T> {
        private Node<T> next;
        private Node<T> prev;

        private final T val;

        public Node(T val) {
            this.val = val;
        }
    }

    public void add(T t) {
        var node = new Node<>(t);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new ForwardIterator();
    }

    public Iterator<T> reverse() {
        return new ReverseIterator();
    }

    private class ForwardIterator implements java.util.Iterator<T> {

        private Node<T> next = head;

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            if (next == null) {
                throw new NoSuchElementException("no next element");
            }
            Node<T> current = next;
            next = next.next;
            return current.val;
        }
    }

    private class ReverseIterator implements Iterator<T> {

        private Node<T> prev = tail;

        @Override
        public boolean hasNext() {
            return prev != null;
        }

        @Override
        public T next() {
            if (prev == null) {
                throw new NoSuchElementException("no previous element");
            }
            Node<T> current = prev;
            prev = prev.prev;
            return current.val;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DoubleLinkedList").append("(").append(size()).append(") {");
        Node<?> node = head;
        while (node != null) {
            sb.append(node.val);
            node = node.next;
            if (node != null) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
