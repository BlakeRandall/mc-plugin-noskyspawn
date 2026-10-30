package dev.example.noskyspawns;

/**
 * Determines whether a natural hostile mob spawn
 * should be blocked based on sky exposure.
 */
final class SpawnPolicy {

    private SpawnPolicy() {
    }

    /**
     * Blocks spawning if either:
     *
     * 1. The location receives any sky light.
     * 2. There is a direct vertical path to the sky.
     *
     * Both conditions must be false for spawning to be allowed.
     */
    static boolean shouldBlockSpawn(
            int skyLight,
            boolean hasDirectSkyAccess
    ) {
        return skyLight > 0 || hasDirectSkyAccess;
    }
}
