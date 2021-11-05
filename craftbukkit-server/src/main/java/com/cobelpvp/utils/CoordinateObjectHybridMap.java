package com.cobelpvp.utils;

import java.util.Collection;
import java.util.Collections;

import org.bukkit.craftbukkit.util.LongHash;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class CoordinateObjectHybridMap<T> {

    private Long2ObjectOpenHashMap<T> backingMap = new Long2ObjectOpenHashMap<T>();
    private Collection<T> values = Collections.unmodifiableCollection(backingMap.values()); // values() is uncached and is simply a view, so we can cache it and make it externally unmodifiable

    protected FlatMap<T> flatMap;

    public CoordinateObjectHybridMap() {
        this.flatMap = new FlatMap<T>();
    }

    public boolean contains(int x, int z) {
        return get(x, z) != null;
    }

    public T get(int x, int z) {
        if (x * x < FlatMap.HALF_DIAMETER_SQUARED && z * z < FlatMap.HALF_DIAMETER_SQUARED) {
            return flatMap.get(x, z);
        }

        return backingMap.get(LongHash.toLong(x, z));
    }

    public void remove(int x, int z) {
        if (x * x < FlatMap.HALF_DIAMETER_SQUARED && z * z < FlatMap.HALF_DIAMETER_SQUARED) {
            flatMap.put(x, z, null);
        }

        backingMap.remove(LongHash.toLong(x, z));
    }

    public void put(int x, int z, T value) {
        if (x * x < FlatMap.HALF_DIAMETER_SQUARED && z * z < FlatMap.HALF_DIAMETER_SQUARED) {
            flatMap.put(x, z, value);
        }

        backingMap.put(LongHash.toLong(x, z), value);
    }

    public int size() {
        return backingMap.size();
    }

    public Collection<T> values() {
        return values;
    }
}
