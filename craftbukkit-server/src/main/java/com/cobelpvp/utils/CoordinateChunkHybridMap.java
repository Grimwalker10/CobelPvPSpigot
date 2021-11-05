package com.cobelpvp.utils;

import net.minecraft.server.Chunk;

public class CoordinateChunkHybridMap extends CoordinateObjectHybridMap<Chunk> {

    public CoordinateChunkHybridMap() {
        flatMap = new ChunkFlatMap();
    }
}
