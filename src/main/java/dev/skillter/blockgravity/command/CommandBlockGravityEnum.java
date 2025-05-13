package dev.skillter.blockgravity.command;

import dev.skillter.blockgravity.permissions.PermissionsEnum;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum CommandBlockGravityEnum {

    TOGGLE("toggle", PermissionsEnum.TOGGLE.getPermission()),
    INFO("info", null);

    private final String arg;
    private final Permission requiredPerm;

    CommandBlockGravityEnum(@NotNull String arg,@Nullable Permission requiredPerm) {
        this.arg = arg;
        this.requiredPerm = requiredPerm;
    }

    @NotNull
    public String getArg() {
        return arg;
    }

    @Nullable
    public Permission getRequiredPermission() {
        return requiredPerm;
    }

    @NotNull
    @Override
    public String toString() {
        return arg;
    }
}
