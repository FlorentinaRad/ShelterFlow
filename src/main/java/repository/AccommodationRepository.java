package repository;

import config.DatabaseConnection;
import model.Accommodation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccommodationRepository {

    private static final String INSERT_ACCOMMODATION_SQL = """
            INSERT INTO accommodations (
                evacuation_id,
                shelter_id,
                notes
            )
            VALUES (?, ?, ?)
            """;

    public Accommodation save(Accommodation accommodation) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        INSERT_ACCOMMODATION_SQL,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            statement.setInt(1, accommodation.getEvacuationId());
            statement.setInt(2, accommodation.getShelterId());
            statement.setString(3, accommodation.getNotes());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(
                        "Creating accommodation failed, no rows affected."
                );
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    accommodation.setAccommodationId(
                            generatedKeys.getInt(1)
                    );

                    return accommodation;
                }

                throw new SQLException(
                        "Creating accommodation failed, no ID obtained."
                );
            }
        }
    }

    private static final String FIND_BY_ID_SQL = """
        SELECT
            accommodation_id,
            evacuation_id,
            shelter_id,
            check_in_datetime,
            check_out_datetime,
            notes
        FROM accommodations
        WHERE accommodation_id = ?
        """;

    public Optional<Accommodation> findById(Integer accommodationId) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        FIND_BY_ID_SQL
                )
        ) {
            statement.setInt(1, accommodationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Accommodation accommodation = new Accommodation(
                            resultSet.getInt("accommodation_id"),
                            resultSet.getInt("evacuation_id"),
                            resultSet.getInt("shelter_id"),
                            resultSet.getTimestamp("check_in_datetime").toLocalDateTime(),
                            resultSet.getTimestamp("check_out_datetime") != null ? resultSet.getTimestamp("check_out_datetime").toLocalDateTime() : null,
                            resultSet.getString("notes")
                    );
                    return Optional.of(accommodation);
                }
                return Optional.empty();
            }
        }
    }

    private static final String FIND_ALL_SQL = """
        SELECT
            accommodation_id,
            evacuation_id,
            shelter_id,
            check_in_datetime,
            check_out_datetime,
            notes
        FROM accommodations
        """;

    public List<Accommodation> findAll() throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        FIND_ALL_SQL
                );
                ResultSet resultSet = statement.executeQuery()
        ) {
            List<Accommodation> accommodations = new ArrayList<>();
            while (resultSet.next()) {
                Accommodation accommodation = new Accommodation(
                        resultSet.getInt("accommodation_id"),
                        resultSet.getInt("evacuation_id"),
                        resultSet.getInt("shelter_id"),
                        resultSet.getTimestamp("check_in_datetime").toLocalDateTime(),
                        resultSet.getTimestamp("check_out_datetime") != null ? resultSet.getTimestamp("check_out_datetime").toLocalDateTime() : null,
                        resultSet.getString("notes")
                );
                accommodations.add(accommodation);
            }
            return accommodations;
        }
    }

    private static final String UPDATE_ACCOMMODATION_SQL = """
        UPDATE accommodations
        SET
            evacuation_id = ?,
            shelter_id = ?,
            check_out_datetime = ?,
            notes = ?
        WHERE accommodation_id = ?
        """;

    public boolean update(Accommodation accommodation) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        UPDATE_ACCOMMODATION_SQL
                )
        ) {
            statement.setInt(1, accommodation.getEvacuationId());
            statement.setInt(2, accommodation.getShelterId());

            if (accommodation.getCheckOutDatetime() != null) {
                statement.setTimestamp(3, Timestamp.valueOf(accommodation.getCheckOutDatetime())
                );
            } else {
                statement.setNull(3, Types.TIMESTAMP);
            }

            statement.setString(4, accommodation.getNotes());
            statement.setInt(5, accommodation.getAccommodationId());

            int affectedRows = statement.executeUpdate();
            return affectedRows == 1;
        }
    }

    private static final String DELETE_ACCOMMODATION_SQL = """
        DELETE FROM accommodations
        WHERE accommodation_id = ?
        """;

    public boolean delete(Integer accommodationId) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        DELETE_ACCOMMODATION_SQL)
        ) {
            statement.setInt(1, accommodationId);

            int affectedRows = statement.executeUpdate();
            return affectedRows == 1;
        }
    }
}
