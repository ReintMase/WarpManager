package me.seven.reintmase.warpmanager;

import lombok.Getter;
import me.seven.reintmase.warpmanager.Arrays.Containers;
import me.seven.reintmase.warpmanager.Commands.*;
import me.seven.reintmase.warpmanager.DatabaseManager.DataSourceConfig;
import me.seven.reintmase.warpmanager.DatabaseManager.DatabaseExample;
import me.seven.reintmase.warpmanager.Events.Listeners;
import me.seven.reintmase.warpmanager.Manager.VaultManager.MoneyManager;
import me.seven.reintmase.warpmanager.Manager.VisitWarp;
import me.seven.reintmase.warpmanager.Manager.ZoneManager;
import me.seven.reintmase.warpmanager.MenuManager.WarpsMenu;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;

@Getter
public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;
    private DataSourceConfig dataSourceConfig;
    private DatabaseExample databaseExample;

    private AddWarpCommands addWarpCommands;
    private RemoveWarpCommands removeWarpCommands;
    private VisitWarpCommands visitWarpCommands;
    private PluginCommands pluginCommands;
    private WarpsCommand warpsCommand;
    private AdminCommands adminCommands;

    private VisitWarp visitWarp;
    private ZoneManager zoneManager;
    private WarpsMenu warpsMenu;
    private Containers containers;

    private MoneyManager moneyManager;
    private Listeners listeners;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;

        dataSourceConfig = new DataSourceConfig(this);
        databaseExample = new DatabaseExample(dataSourceConfig);
        databaseExample.executeQuery();
        moneyManager = new MoneyManager(databaseExample);
        containers = new Containers();

        visitWarp = new VisitWarp(databaseExample, moneyManager);
        zoneManager = new ZoneManager();

        listeners = new Listeners(containers, visitWarp);

        moneyManager.setupEconomy();
        warpsMenu = new WarpsMenu(containers, databaseExample);

        addWarpCommands = new AddWarpCommands(databaseExample);
        getCommand("addwarp").setExecutor(addWarpCommands);

        removeWarpCommands = new RemoveWarpCommands(databaseExample);
        getCommand("remwarp").setExecutor(removeWarpCommands);

        visitWarpCommands = new VisitWarpCommands(visitWarp, databaseExample);
        getCommand("visit").setExecutor(visitWarpCommands);

        pluginCommands = new PluginCommands(databaseExample, zoneManager);
        getCommand("warp").setExecutor(pluginCommands);

        warpsCommand = new WarpsCommand(warpsMenu);
        getCommand("warps").setExecutor(warpsCommand);

        adminCommands = new AdminCommands(databaseExample, visitWarp);
        getCommand("wm").setExecutor(adminCommands);

        getServer().getPluginManager().registerEvents(listeners, this);
    }
}
