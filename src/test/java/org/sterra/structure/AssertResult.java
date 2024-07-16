package org.sterra.structure;

import org.junit.jupiter.api.Assertions;

import java.util.Iterator;
import java.util.List;

public class AssertResult {

    static void assertProcessedElements(DoubleLinkedList<Integer> source, List<Integer> processedByZeroThread,
                                 List<Integer> processedByOneThread) {
        Assertions.assertEquals(source.size(), processedByZeroThread.size() + processedByOneThread.size(),
                String.format("Processed by first thread %d, processed by second thread %d", processedByZeroThread.size(),
                        processedByOneThread.size()));
        var listIterator = source.iterator();
        assertElements(listIterator, processedByZeroThread);
        assertElements(listIterator, processedByOneThread.reversed());
    }

    private static void assertElements(Iterator<Integer> listIterator, Iterable<Integer> subList) {
        var subListIterator = subList.iterator();
        Assertions.assertEquals(listIterator.hasNext(), subListIterator.hasNext());
        while (subListIterator.hasNext()) {
            if (!listIterator.hasNext()) {
                throw new AssertionError("has no elements on source collection");
            }
            var source = listIterator.next();
            var processed = subListIterator.next();
            Assertions.assertEquals(source, processed);
        }
    }
}
