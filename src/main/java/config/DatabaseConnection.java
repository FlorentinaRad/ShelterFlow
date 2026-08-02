package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public final class DatabaseConnection {
    //URL-ul JDBC, unde si cum se conecteaza
    private static final String URL =
    "jdbc:sqlserver://localhost:1433;" + //conectarea la server
            "databaseName=ShelterFlow;"+ //selectarea bazei de date
            "integratedSecurity=true;"+ //folosire contul meu Windows
            "encrypt=true;"+ //criptarea conexiunii
            "trustServerCertificate=true;"; //acceptare server local

    //constructor privat
    private DatabaseConnection() {
    }

    //metoda care creeaza conexiunea
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
