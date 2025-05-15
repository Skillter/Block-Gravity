package dev.skillter.blockgravity.handler;

import dev.skillter.blockgravity.BlockGravity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import java.util.HashMap;
import java.util.UUID;

public class GravityGunHandler {

    public static HashMap<UUID, FallingBlock> assignedGravityBlocks = new HashMap<>();

    public static void handle(@NotNull PlayerInteractEvent event) {
        // Toggle mechanic
        UUID finalPlayerUUID = event.getPlayer().getUniqueId();
        if (assignedGravityBlocks.containsKey(finalPlayerUUID)) {
            assignedGravityBlocks.remove(finalPlayerUUID);
            return;
        }
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            BlockGravity.INSTANCE.getLogger().warning("clickedBlock is null in GravityGunHandler");
            return;
        }
        event.getPlayer().sendMessage("poof! goes " + clickedBlock.getType().name());

        Location location = clickedBlock.getLocation().clone().add(0.5D, 0.05D, 0.5D);
        BlockData blockData = event.getClickedBlock().getBlockData().clone();

        clickedBlock.setType(Material.AIR);
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, blockData);
        assignedGravityBlocks.putIfAbsent(event.getPlayer().getUniqueId(), fallingBlock);
        fallingBlock.setGravity(false);

        // change the lived ticks to 1 every 500 ticks to prevent the falling block from de-spawning (despawns on the 600th tick)
        long currentTime = System.currentTimeMillis();
        Bukkit.getScheduler().runTaskTimer(BlockGravity.INSTANCE, () -> {
//            Bukkit.broadcastMessage("scheduler loop has passed: " + ((System.currentTimeMillis() - currentTime) / 1000));
            fallingBlock.setTicksLived(1);
        }, 500, 500);



        // Let's spend next 10 ticks transitioning the falling block to the right location

        final int runEveryTicks = 2;
        final int howManyTicks = 20 * 10;
        final int transitionInTicks = 10;
        new BukkitRunnable() {
            int loop = 0;

            double startingSpeed = 0.3; // Adjust this value to change the speed of movement.
            Location eyeLocation;
            Location targetLocation;
            Location startingLocation;
            @Override
            public void run() {
                loop++;
                fallingBlock.setVelocity(new Vector(0, 0, 0));
                fallingBlock.teleport(fallingBlock.getLocation());
                eyeLocation = event.getPlayer().getEyeLocation();
                startingLocation = fallingBlock.getLocation();
                targetLocation = eyeLocation.clone().subtract(0, 0.5, 0).add(eyeLocation.clone().getDirection().multiply(2));
                Vector direction = targetLocation.toVector().subtract(startingLocation.toVector()).normalize();
                Bukkit.broadcastMessage(String.valueOf(calculateDistanceInBlocks(startingLocation, targetLocation)));
                fallingBlock.setVelocity(direction.clone().multiply(calculateDistanceInBlocks(startingLocation, targetLocation)).multiply(startingSpeed));

//                Bukkit.broadcastMessage(loop + ": " + direction.toVector3d());

//                if (loop == transitionInTicks / runEveryTicks) startingSpeed=- 0.1;
                if (loop >= howManyTicks / runEveryTicks) this.cancel();
            }
        }.runTaskTimer(BlockGravity.INSTANCE, 0L, runEveryTicks);
















//        AtomicInteger i = new AtomicInteger(0);
//        int id;
//        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(BlockGravity.INSTANCE, () -> {
//            i.getAndIncrement();
//            if (i.get() > 5)
//                Bukkit.getScheduler().cancelTask(id);
//        }, 0, 2);




//        int runEveryTicks = 2;
//        int totalTicks = 10;
//        new BukkitRunnable() {
//            public void run() {
//                if (totalTicks == 0) {
//                    Bukkit.broadcastMessage("test");
//                    cancel();
//                    return;
//                }
//                totalTicks--;
//            }
//        }.runTaskTimer(BlockGravity.INSTANCE, 0L, runEveryTicks);



    }


//    static private void makeGravityBlock(BlockData blockData, Location location) {
//
//    }








    private static double calculateDistanceInBlocks(Location loc1, Location loc2) {
        double dx = Math.abs(loc1.getX() - loc2.getX());
        double dy = Math.abs(loc1.getY() - loc2.getY());
        double dz = Math.abs(loc1.getZ() - loc2.getZ());

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }





}
