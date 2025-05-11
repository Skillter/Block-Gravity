package dev.skillter.blockgravity;

import dev.skillter.blockgravity.config.Config;
import dev.skillter.blockgravity.config.ConfigEntriesEnum;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockGravity extends JavaPlugin {

    public static BlockGravity INSTANCE = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        Config.initConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        INSTANCE = null;
    }
}
