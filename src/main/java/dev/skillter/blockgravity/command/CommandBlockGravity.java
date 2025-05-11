package dev.skillter.blockgravity.command;

import dev.skillter.blockgravity.Reference;
import dev.skillter.blockgravity.permissions.PermissionsEnum;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class CommandBlockGravity implements CommandExecutor, TabCompleter {



    public static final String COMMAND_BASE = "blockgravity";

    public static final String[] FIRST_ARGS = new String[]{"toggle", "info"};





    /**
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase(COMMAND_BASE))
            return false; // exit prematurely if it's a different command

        // /blockgravity
        if (sender.hasPermission(PermissionsEnum.)) {

        } else { // if incorrect usage
            sendUsageMessage(sender);
        }

        return true;
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside of a command block, this will be the player, not
     *                the command block.
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    The arguments passed to the command, including final
     *                partial argument to be completed
     * @return A List of possible completions for the final argument, or null
     * to default to the command executor
     */
    @Override
    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }


    static public void sendUsageMessage(CommandSender sender) {
        sender.sendMessage(Reference.PREFIX + ChatColor.GOLD + "/" + COMMAND_BASE + " " + Arrays.toString(FIRST_ARGS));
    }
}
