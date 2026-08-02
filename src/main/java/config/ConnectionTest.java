package config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionTest {

    public static void main(String[] args) {
        String sql = "SELECT DB_NAME()";

        //deschidere resurse
        try (
                Connection connection = DatabaseConnection.getConnection(); //deschide conexiunea catre ShelterFlow
                PreparedStatement statement = connection.prepareStatement(sql); //pregateste comanda de SELECT
                ResultSet resultSet = statement.executeQuery() //comanda este trimisa catre Server; rezultatul este pastrat in ResultSet
        ) {
            if (resultSet.next()) {
                System.out.println(
                        "Connected to database: " + resultSet.getString(1)
                );
            }
         //tratare erori
        } catch (SQLException exception) {
            exception.printStackTrace(); //afiseaza detaliile tehnice ale erorii
        }
    }
}