package dev.example.noskyspawns;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Restricts natural hostile mob spawning to locations
 * that have no sky light and no direct vertical sky access.
 *
 * Spawners, spawn eggs, commands and other spawn reasons
 * are unaffected.
 */
public final class NoSkySpawnsPlugin extends JavaPlugin
        implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info(
                "NoSkySpawns enabled. "
                        + "Natural hostile spawning is restricted "
                        + "to locations without sky exposure."
        );
    }

    @EventHandler(
            priority = EventPriority.HIGH,
            ignoreCancelled = true
    )
    public void onCreatureSpawn(CreatureSpawnEvent event) {

        // Only affect naturally spawned mobs.
        if (event.getSpawnReason()
                != CreatureSpawnEvent.SpawnReason.NATURAL) {
            return;
        }

        // Only affect hostile mobs.
        if (event.getEntity().getSpawnCategory()
                != SpawnCategory.MONSTER) {
            return;
        }

        if (event.getEntity().getType() == EntityType.PILLAGER) {
            return;
        }

        Location location = event.getLocation();
        World world = location.getWorld();

        if (world == null || !world.hasSkyLight()) {
            return;
        }

        // Check whether this world is ignored.
        if (!isWorldEnabled(world)) {
            return;
        }

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        // Check 1: Get the sky light level at the spawn location.
        int skyLight = world.getBlockAt(x, y, z).getLightFromSky();

        // Check 2: Look for a direct vertical path to the sky.
        boolean hasDirectSkyAccess =
                hasDirectSkyAccess(world, x, y, z);

        // Cancel spawning if either check detects sky exposure.
        if (SpawnPolicy.shouldBlockSpawn(
                skyLight,
                hasDirectSkyAccess
        )) {
            event.setCancelled(true);
        }
    }

    /**
     * Checks whether there is an unobstructed vertical path
     * from the spawn location to the top of the world.
     *
     * An occluding block stops the sky-access check.
     */
    private boolean hasDirectSkyAccess(
            World world,
            int x,
            int y,
            int z
    ) {

        for (int checkY = y + 1;
             checkY < world.getMaxHeight();
             checkY++) {

            Material material =
                    world.getBlockAt(x, checkY, z).getType();

            if (material.isOccluding()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns false if the world is listed in ignored-worlds.
     *
     * An empty list means all worlds are enabled.
     */
    private boolean isWorldEnabled(World world) {

        var ignoredWorlds =
                getConfig().getStringList("ignored-worlds");

        return !ignoredWorlds.contains(world.getName());
    }
}
