package me.seven.reintmase.warpmanager.Manager;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ZoneManager {

    public boolean isPlayerNearBlock(Player player, int blockX, int blockY, int blockZ, double radius) {
        Location playerLocation = player.getLocation();
        int playerX = playerLocation.getBlockX();
        int playerY = playerLocation.getBlockY();
        int playerZ = playerLocation.getBlockZ();

        double distanceSquared = Math.pow(playerX - blockX, 2) + Math.pow(playerY - blockY, 2) + Math.pow(playerZ - blockZ, 2);

        double radiusSquared = Math.pow(radius, 2);

        return distanceSquared <= radiusSquared;
    }


}
