package dev.skillter.blockgravity.command;

import dev.skillter.blockgravity.BlockGravity;
import dev.skillter.blockgravity.Reference;
import dev.skillter.blockgravity.config.Config;
import dev.skillter.blockgravity.config.ConfigEntriesEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

import static org.bukkit.ChatColor.*;


public class CommandBlockGravity implements CommandExecutor, TabCompleter {


    public static final String COMMAND_BASE = "blockgravity";

    private static final String[] FIRST_ARGS_ARRAY = Arrays.stream(CommandBlockGravityEnum.values())
            .map(CommandBlockGravityEnum::getArg)
            .toArray(String[]::new);


    /**
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase(COMMAND_BASE)) {
            return false; // exit prematurely if it's a different command
        }
        /// /blockgravity

        if (args.length > 0 && isArgHasPerms(CommandBlockGravityEnum.TOGGLE, args[0], sender)) { // Check if the user has the required permission
            try {
                boolean oldState = Config.get(ConfigEntriesEnum.ENABLED);
                Config.set(ConfigEntriesEnum.ENABLED, !oldState);
                if (oldState)
                    sender.sendMessage(Reference.PREFIX + "The plugin has been " + DARK_RED + "disabled" + RESET + "!");
                else
                    sender.sendMessage(Reference.PREFIX + "The plugin has been " + DARK_GREEN + "enabled" + RESET + "!");
            } catch (IOException e) {
                String msg = "The command '/blockgravity toggle' sent by " + sender.getName() + " has failed. Couldn't read the config.";
                BlockGravity.INSTANCE.getLogger().log(Level.WARNING, msg, e.getCause());
                sender.sendMessage(Reference.PREFIX + "The command has failed. Couldn't read the config.");
            }
        } else if (args.length > 0 && isArgHasPerms(CommandBlockGravityEnum.INFO, args[0], sender)) {
            sendUsageMessage(sender);
        } else {// if incorrect usage
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
        if (args.length == 1) {
            List<String> permittedArgs = new ArrayList<>();
            for (CommandBlockGravityEnum commandBlockGravityEnum : CommandBlockGravityEnum.values()) {
                if (commandBlockGravityEnum.getArg().startsWith(args[0]) && hasPerms(commandBlockGravityEnum, sender))
                    permittedArgs.add(commandBlockGravityEnum.getArg());
            }
            return permittedArgs;
        }

        return List.of();
    }


    private void sendUsageMessage(CommandSender sender) {
        sender.sendMessage(Reference.PREFIX + "/" + COMMAND_BASE + " " + Arrays.toString(FIRST_ARGS_ARRAY));
    }

    private boolean isArgHasPerms(@NotNull CommandBlockGravityEnum referenceCommand, @NotNull String arg, @NotNull CommandSender sender) {
        return referenceCommand.getArg().equalsIgnoreCase(arg) // Check if the argument is the same as the provided reference command's argument
                && (referenceCommand.getRequiredPermission() == null || sender.hasPermission(referenceCommand.getRequiredPermission())); // Check if the user has the required permission
    }

    private boolean hasPerms(@NotNull CommandBlockGravityEnum referenceCommand, @NotNull CommandSender sender) {
        return referenceCommand.getRequiredPermission() == null || sender.hasPermission(referenceCommand.getRequiredPermission()); // Check if the user has the required permission
    }

    private boolean getListOfArgsWithPerms(@NotNull CommandBlockGravityEnum referenceCommand, @NotNull CommandSender sender) {
        return referenceCommand.getRequiredPermission() == null || sender.hasPermission(referenceCommand.getRequiredPermission()); // Check if the user has the required permission
    }

}

