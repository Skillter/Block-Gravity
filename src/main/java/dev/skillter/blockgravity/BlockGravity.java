package dev.skillter.blockgravity;

import dev.skillter.blockgravity.command.CommandBlockGravity;
import dev.skillter.blockgravity.config.Config;
import dev.skillter.blockgravity.listener.GravityGunListener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockGravity extends JavaPlugin {

    public static BlockGravity INSTANCE = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        Config.initConfig();
        registerCommand(CommandBlockGravity.COMMAND_BASE, new CommandBlockGravity());
        this.getServer().getPluginManager().registerEvents(new GravityGunListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        INSTANCE = null;
    }


    private void registerCommand(String commandBase, CommandExecutor commandClass) {
        PluginCommand command = this.getCommand(commandBase);
        if (command != null) {
            command.setExecutor(commandClass);
        } else {
            getLogger().severe("Failed to register command: " + CommandBlockGravity.COMMAND_BASE);
        }
    }
}
