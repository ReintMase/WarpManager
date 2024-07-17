package me.seven.reintmase.warpmanager.Manager.VaultManager;

import me.seven.reintmase.warpmanager.DatabaseManager.DatabaseExample;
import me.seven.reintmase.warpmanager.Main;
import me.seven.reintmase.warpmanager.Manager.Hex.Colorize;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

public class MoneyManager {

    private Economy economy;

    private final DatabaseExample databaseExample;
    private final FileConfiguration config;

    private final String takeMoneyMessage, noMoneyMessage;

    public MoneyManager(DatabaseExample databaseExample) {
        this.databaseExample = databaseExample;
        config = Main.getInstance().getConfig();

        takeMoneyMessage = Colorize.hex(config.getString("messages.take-money"));
        noMoneyMessage = Colorize.hex(config.getString("messages.no-money"));
    }

    public boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public void takeMoney(Player player, String warpName) {
        int moneyForTake = databaseExample.getMoneyByWarpName(warpName);

        economy.withdrawPlayer(player, moneyForTake);

        player.sendMessage(takeMoneyMessage
                .replace("%warp_price%", moneyForTake + "")
                .replace("%warp_name%", warpName));
    }

    public void payMoneyCreator(String uuid, int moneyForGive) {
        Player player = Bukkit.getPlayer(uuid);

        economy.depositPlayer(player, moneyForGive);
    }

    public boolean playerHasMoney(Player player, String warpName) {

        double playerMoney = economy.getBalance(player);

        if(playerMoney >= databaseExample.getMoneyByWarpName(warpName)) {
            return true;
        }

        player.sendMessage(noMoneyMessage);
        return false;
    }

}
