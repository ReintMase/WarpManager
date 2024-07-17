package me.seven.reintmase.warpmanager.Commands;

import me.seven.reintmase.warpmanager.DatabaseManager.DatabaseExample;
import me.seven.reintmase.warpmanager.Main;
import me.seven.reintmase.warpmanager.Manager.Hex.Colorize;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class RemoveWarpCommands implements CommandExecutor {

    private final FileConfiguration config;

    private final DatabaseExample databaseExample;

    private final String onlyPlayer, usageRemoveWarp, noWarp,noCreator, removedWarp;

    public RemoveWarpCommands(DatabaseExample databaseExample) {
        this.databaseExample = databaseExample;

        config = Main.getInstance().getConfig();

        onlyPlayer = Colorize.hex(config.getString("messages.only-player"));
        usageRemoveWarp = Colorize.hex(config.getString("messages.usage-remove-warp"));
        noWarp = Colorize.hex(config.getString("messages.no-warp"));
        noCreator = Colorize.hex(config.getString("messages.no-creator"));
        removedWarp = Colorize.hex(config.getString("messages.removed-warp"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(onlyPlayer);
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(usageRemoveWarp);
            return true;
        }

        String warpName = args[0];
        String playerUuid = player.getUniqueId().toString();
        String uuid = databaseExample.getUuidByWarpName(warpName);

        if(!databaseExample.containsWarp(warpName)){
            player.sendMessage(noWarp);
            return true;
        }

        if(!playerUuid.equals(uuid)) {
            player.sendMessage(noCreator);
            return true;
        }

        databaseExample.removeWarp(warpName);

        player.sendMessage(removedWarp
                .replace("%warp_name%", warpName));

        return true;
    }
}
