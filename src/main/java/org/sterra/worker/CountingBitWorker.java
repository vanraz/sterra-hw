package org.sterra.worker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CountingBitWorker implements Runnable {

    private final Supplier<Integer> integerSupplier;
    private final int target;
    private int matchedCount = 0;
    private final List<Integer> processed = new ArrayList<>();

    public CountingBitWorker(boolean countZero, Supplier<Integer> integerSupplier) {
        this.integerSupplier = integerSupplier;
        this.target = countZero ? 0 : 1;
    }

    @Override
    public void run() {
        Integer val;
        int total = 0;
        do {
            val = integerSupplier.get();
            if (val != null) {
                total+=countBit(val, target);
                processed.add(val);
                System.out.println("Processed " + processed.size() + " elements with " + target + " bit");

            }
        } while (val != null);
        matchedCount = total;
    }

    @Override
    public String toString() {
        return String.format("Thread for counting %s bits. Processed %d elements and totally counted %d '%s' bits. Elements: %s",
                target, processed.size(), matchedCount, target, processed);
    }

    private int countBit(int value, int bitToCount) {
        int counter = 0;
        while (value > 0) {
            int remainder = value % 2;
            if (remainder == bitToCount) {
                counter++;
            }
            value /= 2;
        }
        return counter;
    }
}
