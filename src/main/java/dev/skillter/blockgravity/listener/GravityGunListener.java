package dev.skillter.blockgravity.listener;

import dev.skillter.blockgravity.BlockGravity;
import dev.skillter.blockgravity.handler.GravityGunHandler;
import dev.skillter.blockgravity.permission.PermissionsEnum;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.StringWriter;
import java.util.StringJoiner;

public class GravityGunListener implements Listener {

    @EventHandler
    public void playerUsedGravityGun(PlayerInteractEvent event) {

        if (event.getHand() != null && (event.getHand() != EquipmentSlot.OFF_HAND)
                && event.getItem() != null && event.getItem().getType().equals(Material.STICK)
                && event.getPlayer().hasPermission(PermissionsEnum.GRAVITY_GUN.getPermission())) {
//            Bukkit.broadcastMessage(event.getItem().toString() + event.getItem().getType().name() + event.getHand().name() + event.getAction().name() + event.getHandlers().toString() + event.getMaterial());
//            Bukkit.broadcastMessage(event.getHand().name());
            Bukkit.getScheduler().scheduleSyncDelayedTask(BlockGravity.INSTANCE, () -> {
                GravityGunHandler.handle(event);
            }, 1);


        }
    }

}
