package me.seven.reintmase.warpmanager.DatabaseManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.seven.reintmase.warpmanager.Main;
import org.bukkit.configuration.file.FileConfiguration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceConfig {

    private final FileConfiguration config;
    private final DataSource dataSource;

    public DataSourceConfig(Main main) {
        this.config = main.getConfig();
        this.dataSource = createDataSource();
    }

    private DataSource createDataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        String jdbcUrl = config.getString("database.jdbcUrl");
        String username = config.getString("database.username");
        String password = config.getString("database.password");
        int maximumPoolSize = config.getInt("database.maximumPoolSize", 10);
        int minimumIdle = config.getInt("database.minimumIdle", 2);
        long idleTimeout = config.getLong("database.idleTimeout", 600000);
        long connectionTimeout = config.getLong("database.connectionTimeout", 30000);

        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setIdleTimeout(idleTimeout);
        hikariConfig.setConnectionTimeout(connectionTimeout);

        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}
