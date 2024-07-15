package org.sterra.action;

public class ZeroBitsCounter implements BitsOnIntCounter {

    public static final BitsOnIntCounter nonZeroBitsCounter = new BitsOnIntCounter() {
        @Override
        public int count(Integer val) {
            return val == null ? 0 : Integer.bitCount(val);
        }

        @Override
        public String toString() {
            return "1 bit";
        }
    };

    @Override
    public int count(Integer val) {
        return val == null ? 0 : Integer.SIZE - Integer.numberOfLeadingZeros(val) - Integer.bitCount(val);
    }

    @Override
    public String toString() {
        return "0 bit";
    }
}
