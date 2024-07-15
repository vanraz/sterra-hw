package org.sterra.structure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sterra.action.BitsOnIntCounter;
import org.sterra.action.ZeroBitsCounter;

public class BitsCounterTest {

    private final BitsOnIntCounter zeroBitsCounter = new ZeroBitsCounter();
    private final BitsOnIntCounter nonZeroBitsCounter = ZeroBitsCounter.nonZeroBitsCounter;

    @ParameterizedTest
    @CsvSource(value = {
            "100, 4",
            "1001, 3",
            "150801, 11",
            "-15, 3"
    })
    void testCountZeroBits(int number, int expectedZeroBits) {
        Assertions.assertEquals(expectedZeroBits, zeroBitsCounter.count(number), String.format("Binary string is %s", Integer.toBinaryString(number)));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "100, 3",
            "1001, 7",
            "150801, 7",
            "-15, 29"
    })
    void testCountNonZeroBits(int number, int expectedNonZeroBits) {
        Assertions.assertEquals(expectedNonZeroBits, nonZeroBitsCounter.count(number), String.format("Binary string is %s", Integer.toBinaryString(number)));
    }
}
