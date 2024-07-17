package me.seven.reintmase.warpmanager.Commands;

import me.seven.reintmase.warpmanager.DatabaseManager.DatabaseExample;
import me.seven.reintmase.warpmanager.Main;
import me.seven.reintmase.warpmanager.Manager.Hex.Colorize;
import me.seven.reintmase.warpmanager.Manager.VisitWarp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.xml.crypto.Data;

public class VisitWarpCommands implements CommandExecutor {

    private final VisitWarp visitWarp;

    private final FileConfiguration config;

    private final String usageVisitWarp, onlyPlayer, noWarp;
    private final DatabaseExample databaseExample;

    public VisitWarpCommands(VisitWarp visitWarp, DatabaseExample databaseExample) {
        this.visitWarp = visitWarp;
        this.databaseExample = databaseExample;

        config = Main.getInstance().getConfig();

        usageVisitWarp = Colorize.hex(config.getString("messages.usage-visit-warp"));
        noWarp = Colorize.hex(config.getString("messages.no-warp"));
        onlyPlayer = Colorize.hex(config.getString("messages.only-player"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(onlyPlayer);
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(usageVisitWarp);
            return true;
        }

        String warpName = args[0];

        if(!databaseExample.containsWarp(warpName)){
            player.sendMessage(noWarp);
            return true;
        }

        visitWarp.visitWarp(player, warpName);
        return true;
    }
}
