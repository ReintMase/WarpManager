package me.seven.reintmase.warpmanager.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseExample {

    private final DataSourceConfig dataSourceConfig;

    public DatabaseExample(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    public void executeQuery() {
        try (Connection connection = dataSourceConfig.getConnection()) {
            createTableIfNotExists(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTableIfNotExists(Connection connection) {
        String createWarpsTableSQL = "CREATE TABLE IF NOT EXISTS warps (" +
                "uuid VARCHAR(255) NOT NULL, " +
                "creator_name VARCHAR(255) NOT NULL, " +
                "warp_name VARCHAR(255) NOT NULL, " +
                "money INT NOT NULL, " +
                "blockX INT NOT NULL, " +
                "blockY INT NOT NULL, " +
                "blockZ INT NOT NULL, " +
                "type VARCHAR(255) NOT NULL, " +
                "world VARCHAR(255) NOT NULL, " +
                "PRIMARY KEY (warp_name))";

        String createAuthorizedPlayersTableSQL = "CREATE TABLE IF NOT EXISTS authorized_players (" +
                "warp_name VARCHAR(255) NOT NULL, " +
                "player_uuid VARCHAR(255) NOT NULL, " +
                "FOREIGN KEY (warp_name) REFERENCES warps(warp_name) ON DELETE CASCADE)";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createWarpsTableSQL);
            statement.execute(createAuthorizedPlayersTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean containsWarp(String warpName) {
        String sql = "SELECT 1 FROM warps WHERE warp_name = ?";

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, warpName);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addWarp(String warpName, String uuid, String creatorName, int money, int blockX, int blockY, int blockZ, String world, String type) {
        String sql = "INSERT INTO warps (uuid, creator_name, warp_name, money, blockX, blockY, blockZ, world, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, uuid);
            statement.setString(2, creatorName);
            statement.setString(3, warpName);
            statement.setInt(4, money);
            statement.setInt(5, blockX);
            statement.setInt(6, blockY);
            statement.setInt(7, blockZ);
            statement.setString(8, world);
            statement.setString(9, type);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeWarp(String warpName) {
        String sql = "DELETE FROM warps WHERE warp_name = ?";

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, warpName);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getUuidByWarpName(String warpName) {
        String sql = "SELECT uuid FROM warps WHERE warp_name = ?";
        String uuid = null;

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, warpName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    uuid = resultSet.getString("uuid");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return uuid;
    }

    public int getMoneyByWarpName(String warpName) {
        String sql = "SELECT money FROM warps WHERE warp_name = ?";
        int money = 0;

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, warpName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    money = resultSet.getInt("money");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return money;
    }

    public void setMoney(String warpName, int newMoney) {
        String sql = "UPDATE warps SET money = ? WHERE warp_name = ?";

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, newMoney);
            statement.setString(2, warpName);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPlayerNameByWarpName(String warpName) {
        String sql = "SELECT creator_name FROM warps WHERE warp_name = ?";
        String creatorName = null;

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, warpName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    creatorName = resultSet.getString("creator_name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return creatorName;
    }

    public int getBlockX(String warpName) {
        String sql = "SELECT blockX FROM warps WHERE warp_name = ?";
        int blockX = 0;

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, warpName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    blockX = resultSet.getInt("blockX");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return blockX;
    }

    public int getBlockY(String warpName) {
        String sql = "SELECT blockY FROM warps WHERE warp_name = ?";
        int blockY = 0;

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, warpName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    blockY = resultSet.getInt("blockY");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return blockY;
    }

    public int getBlockZ(String warpName) {
        String sql = "SELECT blockZ FROM warps WHERE warp_name = ?";
        int blockZ = 0;

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, warpName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    blockZ = resultSet.getInt("blockZ");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return blockZ;
    }

    public String getWorld(String warpName) {
        String sql = "SELECT world FROM warps WHERE warp_name = ?";
        String world = null;

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, warpName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    world = resultSet.getString("world");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return world;
    }

    public void addAuthorizedPlayer(String warpName, String playerUuid) {
        String sql = "INSERT INTO authorized_players (warp_name, player_uuid) VALUES (?, ?)";

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, warpName);
            statement.setString(2, playerUuid);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeAuthorizedPlayer(String warpName, String playerUuid) {
        String sql = "DELETE FROM authorized_players WHERE warp_name = ? AND player_uuid = ?";

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, warpName);
            statement.setString(2, playerUuid);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAuthorizedPlayers(String warpName) {
        String sql = "SELECT player_uuid FROM authorized_players WHERE warp_name = ?";
        List<String> players = new ArrayList<>();

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, warpName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    players.add(resultSet.getString("player_uuid"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return players;
    }

    public Set<String> getAllWarps() {
        Set<String> warps = new HashSet<>();
        String sql = "SELECT warp_name FROM warps";

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String warpName = resultSet.getString("warp_name");
                warps.add(warpName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return warps;
    }

    public String getWarpType(String warpName){
        String sql = "SELECT type FROM warps WHERE warp_name = ?";
        String type = null;

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, warpName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    type = resultSet.getString("type");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return type;
    }
}
