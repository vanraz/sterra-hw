package org.sterra.structure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sterra.action.ZeroBitsCounter;
import org.sterra.worker.CountingBitWorker;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ConcurrentDequeIteratorTest {

    private final Logger logger = LogManager.getLogger();

    private DoubleLinkedList<Integer> linkedList;

    @BeforeEach
    void setUp() {
        linkedList = new DoubleLinkedList<>();

        var random = new Random();
        var listSize = random.nextInt(20, 50);
        if (logger.isDebugEnabled()) {
            logger.debug("Generating list with size {}", listSize);
        }
        while (listSize > 0) {
            linkedList.add(random.nextInt(1000));
            listSize--;
        }
    }

    @Test
    void testIterateByTwoThreads() {
        var iterator = new LoggingListIteratorProxy<>(new ConcurrentDequeIterator(linkedList));
        var countZeros = new CountingBitWorker(() -> iterator.hasNext() ? iterator.next() : null, new ZeroBitsCounter());
        var countOnes = new CountingBitWorker(
                () -> iterator.hasPrevious() ? iterator.previous() : null, ZeroBitsCounter.nonZeroBitsCounter);
        try (var executorService = Executors.newFixedThreadPool(2)) {
            CompletableFuture.allOf(
                    CompletableFuture.runAsync(countZeros, executorService),
                    CompletableFuture.runAsync(countOnes, executorService)
            ).get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        logger.info("List: {}", linkedList);
        logger.info(countZeros);
        logger.info(countOnes);

        var processedByZeroThread = countZeros.getProcessed();
        var processedByOneThread = countOnes.getProcessed();
        Assertions.assertEquals(linkedList.size(), processedByZeroThread.size() + processedByOneThread.size());
        var listIterator = linkedList.iterator();
        assertElements(listIterator, processedByZeroThread);
        assertElements(listIterator, processedByOneThread.reversed());
    }

    private void assertElements(Iterator<Integer> listIterator, Iterable<Integer> subList) {
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
