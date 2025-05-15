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

import java.util.HashMap;
import java.util.UUID;

public class GravityGunHandler {

    public static HashMap<UUID, FallingBlock> assignedGravityBlocks = new HashMap<>();

    public static void handle(@NotNull PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            BlockGravity.INSTANCE.getLogger().warning("clickedBlock is null in GravityGunHandler");
            return;
        }
        event.getPlayer().sendMessage("poof! goes " + clickedBlock.getType().name());

        Location location = clickedBlock.getLocation().clone().subtract(-0.5D, 0D, -0.5D);
        BlockData blockData = event.getClickedBlock().getBlockData().clone();

        clickedBlock.setType(Material.AIR);
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, blockData);
        fallingBlock.setGravity(false);

        // change the lived ticks to 1 every 500 ticks to prevent the falling block from de-spawning (despawns on the 600th tick)
        long currentTime = System.currentTimeMillis();
        Bukkit.getScheduler().runTaskTimer(BlockGravity.INSTANCE, () -> {
//            Bukkit.broadcastMessage("scheduler loop has passed: " + ((System.currentTimeMillis() - currentTime) / 1000));
            fallingBlock.setTicksLived(1);
        }, 500, 500);


        Location eyeLocation = event.getPlayer().getEyeLocation();
        Location targetLocation = eyeLocation.subtract(0, 0.5, 0).add(eyeLocation.getDirection().multiply(2));
        Location startingLocation = fallingBlock.getLocation();


        // Let's spend next 10 ticks transitioning the falling block to the right location


//        fallingBlock.teleport(targetLocation);



        // Set the falling block's velocity towards the target location
//        Vector direction = targetLocation.toVector().subtract(fallingBlock.getLocation().toVector()).normalize();
//        double distance = fallingBlock.getLocation().distance(targetLocation);

        // Calculate speed based on distance (you can adjust this)
//        double speed = 0.5; // Adjust as needed


        Vector direction = targetLocation.toVector().subtract(startingLocation.toVector()).normalize();
        double speed = 0.5; // Adjust this value to change the speed of movement.


        fallingBlock.setVelocity(direction.multiply(speed));












        final int runEveryTicks = 2;
        final int howManyTimes = 10;
        new BukkitRunnable() {


            int loop = 0;

            @Override
            public void run() {
                loop++;


                Bukkit.broadcastMessage("test run() " + loop);

                if (loop >= howManyTimes) this.cancel();
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
}
