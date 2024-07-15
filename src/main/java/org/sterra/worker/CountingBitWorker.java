package org.sterra.worker;

import org.sterra.action.BitsOnIntCounter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CountingBitWorker implements Runnable {

    private final Supplier<Integer> integerSupplier;
    private final BitsOnIntCounter counter;
    private final List<Integer> processed = new ArrayList<>();

    private int matchedCount = 0;

    public CountingBitWorker(Supplier<Integer> integerSupplier, BitsOnIntCounter counter) {
        this.integerSupplier = integerSupplier;
        this.counter = counter;
    }

    @Override
    public void run() {
        Integer val;
        int total = 0;
        do {
            val = integerSupplier.get();
            if (val != null) {
                total += counter.count(val);
                processed.add(val);
            }
        } while (val != null);
        matchedCount = total;
    }

    @Override
    public String toString() {
        return String.format("Processed %d elements and totally counted %d '%s'. Elements: %s",
                processed.size(), matchedCount, counter, processed);
    }

    public List<Integer> getProcessed() {
        return List.copyOf(processed);
    }
}
