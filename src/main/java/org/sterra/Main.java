package org.sterra;

import org.sterra.structure.ConcurrentDequeIterator;
import org.sterra.structure.DoubleLinkedList;
import org.sterra.worker.CountingBitWorker;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        var linkedList = new DoubleLinkedList<Integer>();

        var random = new Random();
        var listSize = random.nextInt(20, 50);
        System.out.println("Generating list with size " + listSize);
        while (listSize > 0) {
            linkedList.add(random.nextInt(1000));
            listSize--;
        }

        ConcurrentDequeIterator iterator = new ConcurrentDequeIterator(linkedList);
        var countZeros = new CountingBitWorker(true,
                () -> iterator.hasNext() ? iterator.next() : null);
        var countOnes = new CountingBitWorker(false,
                () -> iterator.hasPrevious() ? iterator.previous() : null);
        try (var executorService = Executors.newFixedThreadPool(2)) {
            CompletableFuture.allOf(
                    CompletableFuture.runAsync(countZeros, executorService),
                    CompletableFuture.runAsync(countOnes, executorService)
            ).get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("List: " + linkedList);
        System.out.println(countZeros);
        System.out.println(countOnes);
    }
}