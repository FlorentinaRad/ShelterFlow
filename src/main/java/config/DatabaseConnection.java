package config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {
    private static final Properties PROPERTIES = loadProperties();
    private static final String URL = buildDatabaseUrl();
    private DatabaseConnection() {
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream input = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new IllegalStateException("database.properties file not found.");
            }

            properties.load(input);

            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Could not load database configuration.", e);
        }
    }

    private static String buildDatabaseUrl() {
        String server = PROPERTIES.getProperty("db.server");
        String port = PROPERTIES.getProperty("db.port");
        String databaseName = PROPERTIES.getProperty("db.name");
        String authentication = PROPERTIES.getProperty("db.authentication");

        String url =
                "jdbc:sqlserver://" + server + ":" + port + ";" +
                        "databaseName=" + databaseName + ";" +
                        "encrypt=true;" +
                        "trustServerCertificate=true;";

        if ("windows".equalsIgnoreCase(authentication)) {
            url += "integratedSecurity=true;";
        }

        return url;
    }

    // Creeaza si returneaza conexiunea la baza de date
    public static Connection getConnection() throws SQLException {

        String authentication =
                PROPERTIES.getProperty("db.authentication");

        // Windows Authentication
        if ("windows".equalsIgnoreCase(authentication)) {
            return DriverManager.getConnection(URL);
        }

        // SQL Server Authentication
        if ("sql".equalsIgnoreCase(authentication)) {
            String user = PROPERTIES.getProperty("db.user");
            String password = PROPERTIES.getProperty("db.password");

            return DriverManager.getConnection(URL, user, password);
        }

        throw new IllegalStateException("Unsupported database authentication type: " + authentication);
    }
}