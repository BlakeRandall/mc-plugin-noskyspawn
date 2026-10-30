package dev.example.noskyspawns;

/** Pure policy helper, kept separate so the rule can be unit-tested. */
final class SpawnPolicy {

    private SpawnPolicy() {
    }

    /**
     * A spawn point is sky-exposed when its feet block is above the highest
     * motion-blocking block in the same X/Z column.
     */
    static boolean isSkyExposed(int spawnY, int highestMotionBlockingY) {
        return spawnY > highestMotionBlockingY;
    }
}
