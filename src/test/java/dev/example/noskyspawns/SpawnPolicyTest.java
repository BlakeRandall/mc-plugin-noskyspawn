package dev.example.noskyspawns;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SpawnPolicyTest {

    @Test
    void blocksSpawnWhenSkyLightIsPresent() {
        assertTrue(
                SpawnPolicy.shouldBlockSpawn(1, false)
        );
    }

    @Test
    void blocksSpawnWhenDirectSkyAccessExists() {
        assertTrue(
                SpawnPolicy.shouldBlockSpawn(0, true)
        );
    }

    @Test
    void blocksSpawnWhenBothConditionsAreTrue() {
        assertTrue(
                SpawnPolicy.shouldBlockSpawn(15, true)
        );
    }

    @Test
    void allowsSpawnWhenThereIsNoSkyLightOrDirectSkyAccess() {
        assertFalse(
                SpawnPolicy.shouldBlockSpawn(0, false)
        );
    }

    @Test
    void allowsCaveSpawningAtAnyHeight() {
        // Height is intentionally not part of the policy.
        assertFalse(
                SpawnPolicy.shouldBlockSpawn(0, false)
        );
    }
}
