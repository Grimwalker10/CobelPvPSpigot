package com.cobelpvp.utils;

import net.minecraft.server.Chunk;

public class ChunkFlatMap extends FlatMap<Chunk> {

    private Chunk lastChunk;

    @Override
    public Chunk get(int x, int z) {
        Chunk last = lastChunk; // have to do this to be somewhat thread-safe-ish

        if (last != null && last.locX == x && last.locZ == z) {
            return last;
        }

        return lastChunk = super.get(x, z);
    }

    @Override
    public void remove(int x, int z) {
        if (lastChunk != null && lastChunk.locX == x && lastChunk.locZ == z) {
            lastChunk = null; // we don't really care for thread safety here, we'd just lose a few cache hits
        }

        super.remove(x, z);
    }
}
