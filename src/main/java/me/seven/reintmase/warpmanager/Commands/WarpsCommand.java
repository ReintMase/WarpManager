package me.seven.reintmase.warpmanager.Commands;

import lombok.SneakyThrows;
import me.seven.reintmase.warpmanager.Main;
import me.seven.reintmase.warpmanager.Manager.Hex.Colorize;
import me.seven.reintmase.warpmanager.MenuManager.WarpsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpsCommand implements CommandExecutor {

    private final WarpsMenu warpsMenu;

    private final String onlyPlayer;

    public WarpsCommand(WarpsMenu warpsMenu){
        this.warpsMenu = warpsMenu;

        onlyPlayer = Colorize.hex(Main.getInstance().getConfig().getString("messages.only-player"));
    }

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(onlyPlayer);
            return true;
        }

        Player player = (Player) sender;

        warpsMenu.createMenu(player);

        return true;
    }
}
