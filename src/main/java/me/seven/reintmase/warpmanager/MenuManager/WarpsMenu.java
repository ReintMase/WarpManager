package me.seven.reintmase.warpmanager.MenuManager;

import me.seven.reintmase.warpmanager.Arrays.Containers;
import me.seven.reintmase.warpmanager.DatabaseManager.DatabaseExample;
import me.seven.reintmase.warpmanager.Main;
import me.seven.reintmase.warpmanager.Manager.Hex.Colorize;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WarpsMenu {

    private Inventory inventory;

    private final Containers containers;
    private final DatabaseExample databaseExample;

    private final FileConfiguration config;

    public WarpsMenu(Containers containers, DatabaseExample databaseExample) {
        config = Main.getInstance().getConfig();
        this.containers = containers;
        this.databaseExample = databaseExample;
    }

    public void createMenu(Player player) {
        String title = Colorize.hex(config.getString("menu-settings.title", "Warps"));
        inventory = Bukkit.createInventory(null, 54, title);
        containers.getInventories().add(inventory);

        addItems();
        addWarpsToMenu();

        player.openInventory(inventory);
    }

    public void addWarpsToMenu() {
        Set<String> warps = databaseExample.getAllWarps();
        for (String warpName : warps) {
            int money = databaseExample.getMoneyByWarpName(warpName);
            String type = databaseExample.getWarpType(warpName);

            ItemStack item = createWarpItem(warpName, money, type);
            inventory.addItem(item);
        }
    }

    private ItemStack createWarpItem(String warpName, int money, String type) {
        String path = "menu-settings.items.warps-example.";

        Material material = Material.valueOf(config.getString(path + "item-type", "STONE"));
        List<String> itemLore = config.getStringList(path + "lore");
        String itemName = Colorize.hex(config.getString(path + "item-name"));

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            String ownerName = databaseExample.getPlayerNameByWarpName(warpName);
            meta.setDisplayName(itemName
                    .replace("%warp_owner%", ownerName)
                    .replace("%warp_name%", warpName)
                    .replace("%warp_state%", type));

            List<String> lore = new ArrayList<>();
            for (String loreItem : itemLore) {
                lore.add(Colorize.hex(loreItem.replace("%warp_price%", String.valueOf(money))
                        .replace("%warp_state%", type)
                        .replace("%warp_name%", warpName)
                        .replace("%warp_owner%", ownerName)));
            }

            meta.setLore(lore);

            meta.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "warp_name"), PersistentDataType.STRING, warpName);
            item.setItemMeta(meta);
        }

        return item;
    }


    private void addItems() {
        Set<String> itemKeys = config.getConfigurationSection("menu-settings.items").getKeys(false);
        for (String itemKey : itemKeys) {
            String path = "menu-settings.items." + itemKey + ".";
            String itemName = Colorize.hex(config.getString(path + "item-name"));
            String itemType = config.getString(path + "item-type");
            List<String> itemLore = config.getStringList(path + "lore");
            List<Integer> itemSlots = config.getIntegerList(path + "slots");

            Material material = Material.valueOf(itemType);
            ItemStack item = new ItemStack(material);
            ItemMeta itemMeta = item.getItemMeta();

            if (itemMeta == null) {
                continue;
            }

            itemMeta.setDisplayName(itemName);

            List<String> lore = new ArrayList<>();

            for(String loreItem : itemLore) {
                lore.add(Colorize.hex(loreItem));
            }

            itemMeta.setLore(lore);

            item.setItemMeta(itemMeta);

            for (int slot : itemSlots) {
                inventory.setItem(slot, item);
            }
        }
    }
}
