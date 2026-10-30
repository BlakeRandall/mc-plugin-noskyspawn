package dev.example.noskyspawns;

import org.bukkit.HeightMap;
import org.bukkit.World;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Cancels natural hostile-mob spawns when the spawn column has direct access
 * to the sky. It does not affect spawners, spawn eggs, commands, raids, etc.
 */
public final class NoSkySpawnsPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("NoSkySpawns enabled. Surface natural hostile spawns are blocked.");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) {
            return;
        }

        if (event.getEntity().getSpawnCategory() != SpawnCategory.MONSTER) {
            return;
        }

        World world = event.getLocation().getWorld();
        if (world == null || !world.hasSkyLight()) {
            return;
        }

        if (!isWorldEnabled(world)) {
            return;
        }

        int x = event.getLocation().getBlockX();
        int y = event.getLocation().getBlockY();
        int z = event.getLocation().getBlockZ();
        int highestBlockingY = world.getHighestBlockYAt(x, z, HeightMap.MOTION_BLOCKING);

        if (SpawnPolicy.isSkyExposed(y, highestBlockingY)) {
            event.setCancelled(true);
        }
    }

    private boolean isWorldEnabled(World world) {
        var ignoredWorlds = getConfig().getStringList("ignored-worlds");
        return !ignoredWorlds.contains(world.getName());
    }
}
