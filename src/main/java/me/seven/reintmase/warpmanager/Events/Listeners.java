package me.seven.reintmase.warpmanager.Events;

import me.seven.reintmase.warpmanager.Arrays.Containers;
import me.seven.reintmase.warpmanager.Main;
import me.seven.reintmase.warpmanager.Manager.VisitWarp;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Listeners implements Listener {

    private final FileConfiguration config;

    private final VisitWarp visitWarp;
    private final Containers containers;

    public Listeners(Containers containers, VisitWarp visitWarp) {
        this.containers = containers;
        this.visitWarp = visitWarp;

        config = Main.getInstance().getConfig();
    }

    @EventHandler
    public void onPlayerCloseMenu(InventoryCloseEvent event) {
        Inventory inventory = event.getPlayer().getInventory();

        if(!containers.getInventories().contains(inventory)) {
            return;
        }

        containers.getInventories().remove(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (!containers.getInventories().contains(inventory)) {
            return;
        }

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) {
            return;
        }

        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        if (meta.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "warp_name"), PersistentDataType.STRING)) {
            String warpName = meta.getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "warp_name"), PersistentDataType.STRING);

            visitWarp.visitWarp(player, warpName);
        }
    }
}
