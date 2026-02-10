package utils;

import config.DatabaseConfigManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * JDBC connection helper that uses the DatabaseConfigManager Singleton.
 */
public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {
        DatabaseConfigManager config = DatabaseConfigManager.getInstance();
        return DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword()
        );
    }
}

