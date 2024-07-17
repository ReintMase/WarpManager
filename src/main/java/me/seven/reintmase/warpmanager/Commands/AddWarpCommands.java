package me.seven.reintmase.warpmanager.Commands;

import me.seven.reintmase.warpmanager.DatabaseManager.DatabaseExample;
import me.seven.reintmase.warpmanager.Main;
import me.seven.reintmase.warpmanager.Manager.Hex.Colorize;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class AddWarpCommands implements CommandExecutor {

    private final DatabaseExample databaseExample;
    private final FileConfiguration config;

    private final String onlyPlayer, usageToCreate, warpAlreadyExist, moneyFormatException, usePrivateOrPublic, createWarp;

    public AddWarpCommands(DatabaseExample databaseExample) {
        this.databaseExample = databaseExample;
        config = Main.getInstance().getConfig();

        onlyPlayer = Colorize.hex(config.getString("messages.only-player"));
        usageToCreate = Colorize.hex(config.getString("messages.usage-to-create"));
        warpAlreadyExist = Colorize.hex(config.getString("messages.warp-already-exist"));
        moneyFormatException = Colorize.hex(config.getString("messages.money-format-exception"));
        usePrivateOrPublic = Colorize.hex(config.getString("messages.private-or-public"));
        createWarp = Colorize.hex(config.getString("messages.create-warp"));

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Colorize.hex(onlyPlayer));
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 3) {
            player.sendMessage(usageToCreate);
            return true;
        }

        String warpName = args[0];

        if (databaseExample.containsWarp(warpName)) {
            player.sendMessage(warpAlreadyExist);
            return true;
        }

        String uuid = player.getUniqueId().toString();
        int money;
        try {
            money = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(moneyFormatException);
            return true;
        }

        String type = args[2];

        if (!type.equals("public") && !type.equals("private")) {
            player.sendMessage(usePrivateOrPublic);
            return true;
        }

        int blockX = player.getLocation().getBlockX();
        int blockY = player.getLocation().getBlockY();
        int blockZ = player.getLocation().getBlockZ();
        String world = player.getWorld().getName();

        databaseExample.addWarp(warpName, uuid, player.getName(), money, blockX, blockY, blockZ, world, type);

        player.sendMessage(createWarp
                .replace("%warp_name%", warpName)
                .replace("%warp_price%", money + ""));

        return true;
    }
}
