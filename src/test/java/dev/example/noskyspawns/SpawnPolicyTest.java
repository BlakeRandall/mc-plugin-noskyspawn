package dev.example.noskyspawns;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SpawnPolicyTest {

    @Test
    void blocksSpawnAboveSurface() {
        assertTrue(SpawnPolicy.isSkyExposed(64, 63));
    }

    @Test
    void allowsSpawnBelowSurfaceEvenAtHighYLevel() {
        assertFalse(SpawnPolicy.isSkyExposed(70, 90));
    }

    @Test
    void allowsSpawnAtOrBelowHighestBlockingBlock() {
        assertFalse(SpawnPolicy.isSkyExposed(63, 63));
    }
}
