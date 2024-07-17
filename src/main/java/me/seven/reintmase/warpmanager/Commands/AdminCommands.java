package me.seven.reintmase.warpmanager.Commands;

import me.seven.reintmase.warpmanager.DatabaseManager.DatabaseExample;
import me.seven.reintmase.warpmanager.Main;
import me.seven.reintmase.warpmanager.Manager.Hex.Colorize;
import me.seven.reintmase.warpmanager.Manager.VisitWarp;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminCommands implements CommandExecutor {

    private final String usageRemoveWarpAdmin, noWarp, wmUsage, removedWarp, usageSetPrice,onlyPlayer, moneyFormatException, setMoneyMessage, usageVisitAdmin, noPermission;

    private final DatabaseExample databaseExample;
    private final VisitWarp visitWarp;

    public AdminCommands(DatabaseExample databaseExample, VisitWarp visitWarp) {
        FileConfiguration config = Main.getInstance().getConfig();
        this.databaseExample = databaseExample;
        this.visitWarp = visitWarp;

        usageRemoveWarpAdmin = Colorize.hex(config.getString("messages.usage-remove-warp-admin"));
        onlyPlayer = Colorize.hex(config.getString("messages.only-player"));
        noWarp = Colorize.hex(config.getString("messages.no-warp"));
        wmUsage = Colorize.hex(config.getString("messages.admin-commands-usage"));
        removedWarp = Colorize.hex(config.getString("messages.removed-warp"));
        usageSetPrice = Colorize.hex(config.getString("messages.usage-set-price"));
        moneyFormatException = Colorize.hex(config.getString("messages.money-format-exception"));
        setMoneyMessage = Colorize.hex(config.getString("messages.set-money-message"));
        usageVisitAdmin = Colorize.hex(config.getString("messages.usage-visit-admin"));
        noPermission = Colorize.hex(config.getString("messages.no-permission"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(onlyPlayer);
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("wm.admin")) {
            player.sendMessage(noPermission);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(wmUsage);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "help":
                player.sendMessage("");
                player.sendMessage(Colorize.hex("&a/wm remwarp <warp_name> - &fremoves warp"));
                player.sendMessage(Colorize.hex("&a/wm setprice <warp_name> <price> - &fsets the price for warp"));
                player.sendMessage(Colorize.hex("&a/wm visit <warp_name> - &fteleports to warp"));
                player.sendMessage(Colorize.hex(""));
                return true;
            case "remwarp":
                if (args.length < 2) {
                    player.sendMessage(usageRemoveWarpAdmin);
                    return true;
                }

                String warpNameToRemove = args[1];

                if (!databaseExample.containsWarp(warpNameToRemove)) {
                    player.sendMessage(noWarp);
                    return true;
                }

                databaseExample.removeWarp(warpNameToRemove);

                player.sendMessage(removedWarp.replace("%warp_name%", warpNameToRemove));
                break;

            case "setprice":
                if (args.length < 3) {
                    player.sendMessage(usageSetPrice);
                    return true;
                }

                String warpNameToSetPrice = args[1];
                int price;
                try {
                    price = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage(moneyFormatException);
                    return true;
                }

                if (!databaseExample.containsWarp(warpNameToSetPrice)) {
                    player.sendMessage(noWarp);
                    return true;
                }

                databaseExample.setMoney(warpNameToSetPrice, price);

                player.sendMessage(setMoneyMessage
                        .replace("%warp_name%", warpNameToSetPrice)
                        .replace("%price%", String.valueOf(price)));
                break;

            case "visit":
                if (args.length < 2) {
                    player.sendMessage(usageVisitAdmin);
                    return true;
                }

                String warpNameToVisit = args[1];

                if (!databaseExample.containsWarp(warpNameToVisit)) {
                    player.sendMessage(noWarp);
                    return true;
                }

                visitWarp.adminVisitWarp(player, warpNameToVisit);
                break;

            default:
                player.sendMessage(wmUsage);
                break;
        }

        return true;
    }
}