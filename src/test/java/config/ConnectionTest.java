package config;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionTest {

    @Test
    void getConnectionShouldConnectToDatabase() throws SQLException {
        String sql = "SELECT DB_NAME()";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            assertNotNull(connection);
            assertFalse(connection.isClosed());

            assertTrue(resultSet.next());
            assertNotNull(resultSet.getString(1));
        }
    }
}
