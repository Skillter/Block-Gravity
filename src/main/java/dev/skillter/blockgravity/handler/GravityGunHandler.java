package dev.skillter.blockgravity.handler;

import dev.skillter.blockgravity.BlockGravity;
import dev.skillter.blockgravity.Reference;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GravityGunHandler {

    public static HashMap<UUID, FallingBlock> assignedGravityBlocks = new HashMap<>();
    public static HashMap<UUID, ArrayList<Integer>> assignedTasks = new HashMap<>();

    public static void useGravityGun(@NotNull PlayerInteractEvent event) {
        // Toggle mechanic
        UUID finalPlayerUUID = event.getPlayer().getUniqueId();
        if ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR))
                && (assignedGravityBlocks.containsKey(finalPlayerUUID) || assignedTasks.containsKey(finalPlayerUUID))) {
            Bukkit.broadcastMessage("cancelling");
            cancelHandling(finalPlayerUUID);
            return;
        }

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                || event.getClickedBlock().getType().isAir() || !event.getClickedBlock().getType().isBlock()) { // Shouldn't happen, but just in case
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
        ArrayList<Integer> taskIDs = new ArrayList<>();
        taskIDs.add(Bukkit.getScheduler().runTaskTimer(BlockGravity.INSTANCE, () -> {
//            Bukkit.broadcastMessage("scheduler loop has passed: " + ((System.currentTimeMillis() - currentTime) / 1000));
            fallingBlock.setTicksLived(1);
        }, 500, 500).getTaskId());


        // Let's spend next 10 ticks transitioning the falling block to the right location

        final int runEveryTicks = 2;
        final int howManyTicks = 20 * 10;
        final int transitionInTicks = 10;
        taskIDs.add(new BukkitRunnable() {
            int loop = 0;

            double startingSpeed = 0.3; // Adjust this value to change the speed of movement.
            Location eyeLocation;
            Location targetLocation;
            Location startingLocation;

            @Override
            public void run() {
                loop++;
                fallingBlock.setVelocity(new Vector(0, 0, 0));
//                fallingBlock.teleport(fallingBlock.getLocation());
                eyeLocation = event.getPlayer().getEyeLocation();
                startingLocation = fallingBlock.getLocation();
                targetLocation = eyeLocation.clone().subtract(0, 0.5, 0).add(eyeLocation.clone().getDirection().multiply(2));
                Vector direction = targetLocation.toVector().subtract(startingLocation.toVector()).normalize();
                Bukkit.broadcastMessage(String.valueOf(calculateDistanceInBlocks(startingLocation, targetLocation)));
                fallingBlock.setVelocity(direction.clone().multiply(calculateDistanceInBlocks(startingLocation, targetLocation)).multiply(startingSpeed));

//                Bukkit.broadcastMessage(loop + ": " + direction.toVector3d());

                if (loop == (transitionInTicks / runEveryTicks)) startingSpeed = startingSpeed - 0.1;
//                if (loop >= howManyTicks / runEveryTicks) this.cancel();
            }
        }.runTaskTimer(BlockGravity.INSTANCE, 0L, runEveryTicks).getTaskId());

        assignedTasks.putIfAbsent(finalPlayerUUID, taskIDs);

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

    public static void throwBlock(@NotNull EntityDamageByEntityEvent event) {
        Player player;
        if (event.getDamager() instanceof Player){
            player = (Player) event.getDamager();
        } else {
            event.getDamager().sendMessage(Reference.PREFIX + ChatColor.DARK_RED + "There was an error throwing the block.");
            Bukkit.getLogger().warning(event.getDamager().getName() + " tried to throw a block using the gravity gun, but something didn't work.");
            return;
        }
        throwBlock(player);
    }
    public static void throwBlock(@NotNull PlayerInteractEvent event) {
        throwBlock(event.getPlayer());
    }


    private static void throwBlock(@NotNull Player player) {

        double throwSpeed = 10;
        final boolean keepOldVelocity = true;
        UUID finalPlayerUUID = player.getUniqueId();

        // Check if the gravity gun is in use
        if (assignedGravityBlocks.containsKey(finalPlayerUUID)) {
            Bukkit.broadcastMessage("Throwing the block");
            FallingBlock fallingBlock = assignedGravityBlocks.get(finalPlayerUUID);
            cancelHandling(finalPlayerUUID);

            Location playerEyeLocation = player.getEyeLocation();
            Vector playerDirection = playerEyeLocation.getDirection();
            Vector velocityToApply = playerDirection.multiply(throwSpeed);
            if (keepOldVelocity)
                velocityToApply.add(fallingBlock.getVelocity());
            fallingBlock.setVelocity(velocityToApply);


        }
    }

//    static private void makeGravityBlock(BlockData blockData, Location location) {
//
//    }


        private static double calculateDistanceInBlocks (Location loc1, Location loc2){
            double dx = Math.abs(loc1.getX() - loc2.getX());
            double dy = Math.abs(loc1.getY() - loc2.getY());
            double dz = Math.abs(loc1.getZ() - loc2.getZ());

            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }

        private static void cancelHandling (@NotNull UUID playerUUID){
            if (assignedGravityBlocks.containsKey(playerUUID)) {
                FallingBlock fallingBlock = assignedGravityBlocks.get(playerUUID);
                fallingBlock.setTicksLived(1);
                fallingBlock.setGravity(true);
                assignedGravityBlocks.remove(playerUUID);
            }

            if (assignedTasks.containsKey(playerUUID)) {
                for (Integer taskID : assignedTasks.get(playerUUID)) {
                    Bukkit.getScheduler().cancelTask(taskID);
                }
                assignedTasks.remove(playerUUID);
            }
        }


    }
