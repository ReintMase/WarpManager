package me.seven.reintmase.warpmanager.Manager;

import me.seven.reintmase.warpmanager.DatabaseManager.DatabaseExample;
import me.seven.reintmase.warpmanager.Main;
import me.seven.reintmase.warpmanager.Manager.Hex.Colorize;
import me.seven.reintmase.warpmanager.Manager.VaultManager.MoneyManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class VisitWarp {

    private final DatabaseExample databaseExample;
    private final MoneyManager moneyManager;

    private final FileConfiguration config;

    private final String playerIsUnlisted, teleportedToWarp;

    public VisitWarp(DatabaseExample databaseExample, MoneyManager moneyManager){
        this.databaseExample = databaseExample;
        this.moneyManager = moneyManager;

        config = Main.getInstance().getConfig();

        playerIsUnlisted = Colorize.hex(config.getString("messages.player-is-unlisted"));
        teleportedToWarp = Colorize.hex(config.getString("messages.teleported-to-warp"));
    }

    public void visitWarp(Player player, String warpName) {
        String worldName = databaseExample.getWorld(warpName);
        int blockX = databaseExample.getBlockX(warpName);
        int blockY = databaseExample.getBlockY(warpName);
        int blockZ = databaseExample.getBlockZ(warpName);

        if(databaseExample.getWarpType(warpName).equals("private")){
            List<String> playersUuids = databaseExample.getAuthorizedPlayers(warpName);

            String senderUuid = player.getUniqueId().toString();

            if(!senderUuid.equals(databaseExample.getUuidByWarpName(warpName))){
                if(!playersUuids.contains(senderUuid)){
                    player.sendMessage(playerIsUnlisted);
                    return;
                }
            }
        }

        if(!moneyManager.playerHasMoney(player, warpName)){
            return;
        }

        moneyManager.takeMoney(player, warpName);
        moneyManager.payMoneyCreator(databaseExample.getUuidByWarpName(warpName), databaseExample.getMoneyByWarpName(warpName));

        if (worldName == null || blockX == 0 || blockY == 0 || blockZ == 0) {
            return;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return;
        }

        Location location = new Location(world, blockX + 0.5, blockY, blockZ + 0.5);
        player.teleport(location);

        player.sendMessage(teleportedToWarp
                .replace("%warp_name%", warpName));
    }

    public void adminVisitWarp(Player player, String warpName) {
        String worldName = databaseExample.getWorld(warpName);
        int blockX = databaseExample.getBlockX(warpName);
        int blockY = databaseExample.getBlockY(warpName);
        int blockZ = databaseExample.getBlockZ(warpName);

        if (worldName == null || blockX == 0 || blockY == 0 || blockZ == 0) {
            return;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return;
        }

        Location location = new Location(world, blockX + 0.5, blockY, blockZ + 0.5);
        player.teleport(location);

        player.sendMessage(teleportedToWarp
                .replace("%warp_name%", warpName));
    }

}
