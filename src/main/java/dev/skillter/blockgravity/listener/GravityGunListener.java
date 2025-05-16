package dev.skillter.blockgravity.listener;

import dev.skillter.blockgravity.BlockGravity;
import dev.skillter.blockgravity.handler.GravityGunHandler;
import dev.skillter.blockgravity.permission.PermissionsEnum;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class GravityGunListener implements Listener {

    @EventHandler
    public void playerDelayedUseGravityGun(PlayerInteractEvent event) {
        if (event.getHand() != null && (event.getHand() != EquipmentSlot.OFF_HAND)
                && event.getItem() != null && event.getItem().getType().equals(Material.STICK)
                && event.getPlayer().hasPermission(PermissionsEnum.GRAVITY_GUN.getPermission())) {

//            Bukkit.broadcastMessage(event.getItem().toString() + event.getItem().getType().name() + event.getHand().name() + event.getAction().name() + event.getHandlers().toString() + event.getMaterial());
//            Bukkit.broadcastMessage(event.getHand().name());
            Bukkit.getScheduler().scheduleSyncDelayedTask(BlockGravity.INSTANCE, () -> {
                GravityGunHandler.useGravityGun(event);
            }, 1);
        }
    }

    @EventHandler
    public void playerSecondUseGravityGun(PlayerInteractEvent event) {
        if (event.getHand() != null && (event.getHand() != EquipmentSlot.OFF_HAND)
                && event.getItem() != null && event.getItem().getType().equals(Material.STICK)
                && event.getPlayer().hasPermission(PermissionsEnum.GRAVITY_GUN.getPermission())
                && (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))) {

            GravityGunHandler.throwBlock(event);
        }
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void playerSecondUseOnGravitatedBlockGravityGun(EntityDamageByEntityEvent event) {
//        if (event.getEntity().getType().equals(EntityType.FALLING_BLOCK)
//                && event.getDamager() instanceof Player
//                && ((Player) event.getDamager()).getPlayer().getItemInUse() != null && ((Player) event.getDamager()).getPlayer().getItemInUse().getType().equals(Material.STICK)
//                && ((Player) event.getDamager()).getPlayer().hasPermission(PermissionsEnum.GRAVITY_GUN.getPermission())) {
//        ){
        Bukkit.broadcastMessage("reaches");
        GravityGunHandler.throwBlock(event);
//        }
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void test(PlayerInteractEvent event) {
        String msg = event.getEventName() + " " + event.getClass().getName() + event.getClass().descriptorString();
        Bukkit.getLogger().info(msg);

        HandlerList.getRegisteredListeners(0).
    }


}
