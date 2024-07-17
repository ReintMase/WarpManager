package me.seven.reintmase.warpmanager.Commands;

import me.seven.reintmase.warpmanager.DatabaseManager.DatabaseExample;
import me.seven.reintmase.warpmanager.Main;
import me.seven.reintmase.warpmanager.Manager.Hex.Colorize;
import me.seven.reintmase.warpmanager.Manager.ZoneManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PluginCommands implements CommandExecutor {

    private final DatabaseExample databaseExample;
    private final ZoneManager zoneManager;

    private final FileConfiguration config;

    private final String onlyPlayer, usageAddAndRemovePlayer, playerIsNotOnline,useToAddPlayer, noWarp, noCreator, noPrivate, playerAlreadyInWarp, youCreator,addedPlayerToWarp, useToRemovePlayer, playerAlreadyIsNotInWarp, removedPlayerToWarp;

    public PluginCommands(DatabaseExample databaseExample, ZoneManager zoneManager) {
        this.databaseExample = databaseExample;
        this.zoneManager = zoneManager;

        config = Main.getInstance().getConfig();

        onlyPlayer = Colorize.hex(config.getString("messages.only-player"));
        usageAddAndRemovePlayer = Colorize.hex(config.getString("messages.add-and-remove-player"));
        useToAddPlayer = Colorize.hex(config.getString("messages.use-to-add-player"));
        noWarp = Colorize.hex(config.getString("messages.no-warp"));
        noCreator = Colorize.hex(config.getString("messages.no-creator"));
        noPrivate = Colorize.hex(config.getString("messages.no-private"));
        playerAlreadyInWarp = Colorize.hex(config.getString("messages.player-already-in-warp"));
        addedPlayerToWarp = Colorize.hex(config.getString("messages.added-player-to-warp"));
        useToRemovePlayer = Colorize.hex(config.getString("messages.use-to-remove-player"));
        playerAlreadyIsNotInWarp = Colorize.hex(config.getString("messages.player-already-is-not-in-warp"));
        removedPlayerToWarp = Colorize.hex(config.getString("messages.removed-player-in-warp"));
        youCreator = Colorize.hex(config.getString("messages.you-creator"));
        playerIsNotOnline = Colorize.hex(config.getString("messages.player-is-not-online"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(onlyPlayer);
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(usageAddAndRemovePlayer);
            return true;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (args.length < 4) {
                player.sendMessage(useToAddPlayer);
                return true;
            }

            if (args[1].equalsIgnoreCase("player")) {
                String warpName = args[2];

                if (!databaseExample.containsWarp(warpName)) {
                    player.sendMessage(noWarp);
                    return true;
                }

                String uuid = player.getUniqueId().toString();

                if (!uuid.contains(databaseExample.getUuidByWarpName(warpName))) {
                    player.sendMessage(noCreator);
                    return true;
                }

                if(!databaseExample.getWarpType(warpName).equals("private")){
                    player.sendMessage(noPrivate);
                    return true;
                }

                String target = args[3];
                Player targetPlayer = Bukkit.getPlayer(target);

                if(targetPlayer == null){
                    player.sendMessage(playerIsNotOnline);
                    return true;
                }

                String targetPlayerUUID = targetPlayer.getUniqueId().toString();

                String reqUuid = databaseExample.getUuidByWarpName(warpName);

                if(reqUuid.equals(targetPlayerUUID)){
                    player.sendMessage(youCreator);
                    return true;
                }

                if(databaseExample.getAuthorizedPlayers(warpName).contains(targetPlayerUUID)) {
                    player.sendMessage(playerAlreadyInWarp);
                    return true;
                }

                if (!targetPlayer.isOnline()) {
                    targetPlayerUUID = Bukkit.getOfflinePlayer(target).getUniqueId().toString();
                    databaseExample.addAuthorizedPlayer(warpName, targetPlayerUUID);

                    player.sendMessage(addedPlayerToWarp
                            .replace("%player%", targetPlayer.getName())
                            .replace("%warp_name%", warpName));
                } else {
                    targetPlayerUUID = Bukkit.getPlayer(target).getUniqueId().toString();
                    databaseExample.addAuthorizedPlayer(warpName, targetPlayerUUID);

                    player.sendMessage(addedPlayerToWarp
                            .replace("%player%", targetPlayer.getName())
                            .replace("%warp_name%", warpName));
                }
            } else {
                player.sendMessage(useToAddPlayer);
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length < 4) {
                player.sendMessage(useToRemovePlayer);
                return true;
            }

            if (args[1].equalsIgnoreCase("player")) {
                String warpName = args[2];

                if (!databaseExample.containsWarp(warpName)) {
                    player.sendMessage(noWarp);
                    return true;
                }

                String uuid = player.getUniqueId().toString();

                if (!uuid.contains(databaseExample.getUuidByWarpName(warpName))) {
                    player.sendMessage(noCreator);
                    return true;
                }

                if(!databaseExample.getWarpType(warpName).equals("private")){
                    player.sendMessage(noPrivate);
                    return true;
                }

                String target = args[3];
                Player targetPlayer = Bukkit.getPlayer(target);
                String targetPlayerUUID = targetPlayer.getUniqueId().toString();

                if(!targetPlayer.isOnline()){
                    player.sendMessage(playerIsNotOnline);
                }

                String reqUuid = databaseExample.getUuidByWarpName(warpName);

                if(reqUuid.equals(targetPlayerUUID)){
                    player.sendMessage(youCreator);
                    return true;
                }

                if(!databaseExample.getAuthorizedPlayers(warpName).contains(targetPlayerUUID)) {
                    player.sendMessage(playerAlreadyIsNotInWarp);
                    return true;
                }

                if (!targetPlayer.isOnline()) {
                    targetPlayerUUID = Bukkit.getOfflinePlayer(target).getUniqueId().toString();
                    databaseExample.removeAuthorizedPlayer(warpName, targetPlayerUUID);

                    player.sendMessage(removedPlayerToWarp
                            .replace("%player%", targetPlayer.getName())
                            .replace("%warp_name%", warpName));

                    int blockX = targetPlayer.getLocation().getBlockX();
                    int blockY = targetPlayer.getLocation().getBlockX();
                    int blockZ = targetPlayer.getLocation().getBlockX();

                    if(zoneManager.isPlayerNearBlock(player, blockX, blockY, blockZ, Main.getInstance().getConfig().getInt("zone-settings.radius"))){
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn %player%".replace("%player%", targetPlayer.getName()));
                    }
                }
            } else {
                player.sendMessage(useToRemovePlayer);
                return true;
            }
        }

        return true;
    }
}
