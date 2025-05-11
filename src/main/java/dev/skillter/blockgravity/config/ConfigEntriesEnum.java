package dev.skillter.blockgravity.config;

import org.jetbrains.annotations.NotNull;

public enum ConfigEntriesEnum {


    ENABLED("enabled", true);

    private final String entry;
    private final boolean defaultValue;

    ConfigEntriesEnum(String entry, boolean defaultValue) {
        this.entry = entry;
        this.defaultValue = defaultValue;
    }

    @NotNull
    @Override
    public String toString() {
        return this.entry;
    }

    public boolean getDefaultValue() {
        return this.defaultValue;
    }


}
