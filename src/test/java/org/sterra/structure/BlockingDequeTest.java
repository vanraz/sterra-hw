package org.sterra.structure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sterra.action.ZeroBitsCounter;
import org.sterra.worker.CountingBitWorker;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BlockingDequeTest {

    private final Logger logger = LogManager.getLogger();

    private DoubleLinkedList<Integer> linkedList;

    @BeforeEach
    void setUp() {
        linkedList = new DoubleLinkedList<>();

        var random = new Random();
        var listSize = random.nextInt(200, 500);
        if (logger.isDebugEnabled()) {
            logger.debug("Generating list with size {}", listSize);
        }
        while (listSize > 0) {
            linkedList.add(random.nextInt(1000));
            listSize--;
        }
    }

    @Test
    void testBlockingDeque() {
        var deque = new BlockingDeque<>(linkedList);

        var countZeros = new CountingBitWorker(deque::pollFirst, new ZeroBitsCounter());
        var countOnes = new CountingBitWorker(
                deque::pollLast, ZeroBitsCounter.nonZeroBitsCounter);
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

        AssertResult.assertProcessedElements(linkedList, countZeros.getProcessed(), countOnes.getProcessed());
    }
}
