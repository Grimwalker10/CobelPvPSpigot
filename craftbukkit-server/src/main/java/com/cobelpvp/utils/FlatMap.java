package com.cobelpvp.utils;

public class FlatMap<V> {

    private static final int DIAMETER = 4096;
    private static final int HALF_DIAMETER = DIAMETER / 2;
    private final Object[] flatLookup;

    public static final int HALF_DIAMETER_SQUARED = HALF_DIAMETER * HALF_DIAMETER;

    public FlatMap() {
        this.flatLookup = new Object[DIAMETER * DIAMETER];
    }

    public void put(final int msw, final int lsw, final V value) {
        this.flatLookup[(msw + HALF_DIAMETER) * DIAMETER + (lsw + HALF_DIAMETER)] = value;
    }

    public void remove(final int msw, final int lsw) {
        this.put(msw, lsw, null);
    }

    public boolean contains(final int msw, final int lsw) {
        return this.get(msw, lsw) != null;
    }

    public V get(final int msw, final int lsw) {
        return (V) this.flatLookup[(msw + HALF_DIAMETER) * DIAMETER + (lsw + HALF_DIAMETER)];
    }
}
