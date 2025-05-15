package dev.skillter.blockgravity.permission;

import dev.skillter.blockgravity.Reference;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum PermissionsEnum {

    TOGGLE(".toggle", null),
    GRAVITY_GUN(".gravitygun", "Pickup blocks with the gravity gun"),
    EXAMPLE(".examplepermission", null);


    private final Permission permission;

    PermissionsEnum(@NotNull String name, @Nullable String description) {
        this.permission = new Permission(Reference.PERMISSION_BASE + name, description);
    }

    PermissionsEnum(@NotNull String permissionString) {
        this.permission = new Permission(Reference.PERMISSION_BASE + permissionString);
    }

    public Permission getPermission() {
        return permission;
    }

    public String getName() {
        return permission.getName();
    }

    public String getDescription() {
        return permission.getDescription();
    }

    @Override
    public String toString() {
        return this.permission.toString();
    }
}
