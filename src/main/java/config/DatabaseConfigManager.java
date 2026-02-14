package config;

import java.util.Objects;
import java.util.Properties;

public final class DatabaseConfigManager {

    private static volatile DatabaseConfigManager instance;

    private final String url;
    private final String username;
    private final String password;

    private DatabaseConfigManager() {
        Properties sys = System.getProperties();

        this.url = firstNonNull(
                sys.getProperty("db.url"),
                System.getenv("DB_URL"),
                "jdbc:postgresql://localhost:5432/food_delivery"
        );

        this.username = firstNonNull(
                sys.getProperty("db.username"),
                System.getenv("DB_USERNAME"),
                "postgres"
        );

        this.password = firstNonNull(
                sys.getProperty("db.password"),
                System.getenv("DB_PASSWORD"),
                "postgres"
        );
    }

    public static DatabaseConfigManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfigManager.class) {
                if (instance == null) {
                    instance = new DatabaseConfigManager();
                }
            }
        }
        return instance;
    }

    private String firstNonNull(String a, String b, String fallback) {
        if (a != null && !a.isBlank()) return a;
        if (b != null && !b.isBlank()) return b;
        return Objects.requireNonNull(fallback);
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

