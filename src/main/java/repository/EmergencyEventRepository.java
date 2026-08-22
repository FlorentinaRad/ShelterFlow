package repository;

import config.DatabaseConnection;
import model.EmergencyEvent;
import model.enums.EmergencyStatus;
import model.enums.EmergencyType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmergencyEventRepository {
    private static final String INSERT_EMERGENCY_EVENT_SQL = """
            INSERT INTO emergency_events (
                name,
                type,
                country,
                county,
                locality,
                affected_area,
                start_datetime,
                end_datetime,
                status,
                description,
                estimated_affected_people
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public EmergencyEvent save(EmergencyEvent event) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_EMERGENCY_EVENT_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, event.getName());
            statement.setString(2, event.getType().name());
            statement.setString(3, event.getCountry());
            statement.setString(4, event.getCounty());
            statement.setString(5, event.getLocality());
            statement.setString(6, event.getAffectedArea());

            statement.setTimestamp(7, Timestamp.valueOf(event.getStartDatetime()));

            if (event.getEndDatetime() != null) {
                statement.setTimestamp(8, Timestamp.valueOf(event.getEndDatetime()));
            } else {
                statement.setNull(8, Types.TIMESTAMP);
            }

            statement.setString(9, event.getStatus().name());
            statement.setString(10, event.getDescription());

            if (event.getEstimatedAffectedPeople() != null) {
                statement.setInt(11, event.getEstimatedAffectedPeople());
            } else {
                statement.setNull(11, Types.INTEGER);
            }

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating emergency event failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    event.setEventId(generatedKeys.getInt(1));
                    return event;
                }
                throw new SQLException("Creating emergency event failed, no ID obtained.");
            }
        }
    }

    private static final String FIND_BY_ID_SQL = """
            SELECT
                event_id,
                name,
                type,
                country,
                county,
                locality,
                affected_area,
                start_datetime,
                end_datetime,
                status,
                description,
                estimated_affected_people
            FROM emergency_events
            WHERE event_id = ?
            """;

    public Optional<EmergencyEvent> findById(Integer eventId) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)
        ) {
            statement.setInt(1, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Integer estimatedAffectedPeople = resultSet.getInt("estimated_affected_people");

                    if (resultSet.wasNull()) {
                        estimatedAffectedPeople = null;
                    }

                    EmergencyEvent event = new EmergencyEvent(
                            resultSet.getInt("event_id"),
                            resultSet.getString("name"),
                            EmergencyType.valueOf(resultSet.getString("type")),
                            resultSet.getString("country"),
                            resultSet.getString("county"),
                            resultSet.getString("locality"),
                            resultSet.getString("affected_area"),
                            resultSet.getTimestamp("start_datetime").toLocalDateTime(),
                            resultSet.getTimestamp("end_datetime") != null ? resultSet.getTimestamp("end_datetime").toLocalDateTime() : null,
                            EmergencyStatus.valueOf(resultSet.getString("status")),
                            resultSet.getString("description"),
                            estimatedAffectedPeople
                    );
                    return Optional.of(event);
                }
                return Optional.empty();
            }
        }
    }

    private static final String FIND_ALL_SQL = """
            SELECT
                event_id,
                name,
                type,
                country,
                county,
                locality,
                affected_area,
                start_datetime,
                end_datetime,
                status,
                description,
                estimated_affected_people
            FROM emergency_events
            """;

    public List<EmergencyEvent> findAll() throws SQLException {
        List<EmergencyEvent> events = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Integer estimatedAffectedPeople = resultSet.getInt("estimated_affected_people");

                if (resultSet.wasNull()) {
                    estimatedAffectedPeople = null;
                }

                EmergencyEvent event = new EmergencyEvent(
                        resultSet.getInt("event_id"),
                        resultSet.getString("name"),
                        EmergencyType.valueOf(resultSet.getString("type")),
                        resultSet.getString("country"),
                        resultSet.getString("county"),
                        resultSet.getString("locality"),
                        resultSet.getString("affected_area"),
                        resultSet.getTimestamp("start_datetime").toLocalDateTime(),
                        resultSet.getTimestamp("end_datetime") != null ? resultSet.getTimestamp("end_datetime").toLocalDateTime() : null,
                        EmergencyStatus.valueOf(resultSet.getString("status")),
                        resultSet.getString("description"),
                        estimatedAffectedPeople
                );
                events.add(event);
            }
            return events;
        }
    }

    private static final String UPDATE_EMERGENCY_EVENT_SQL = """
            UPDATE emergency_events
            SET
                name = ?,
                type = ?,
                country = ?,
                county = ?,
                locality = ?,
                affected_area = ?,
                start_datetime = ?,
                end_datetime = ?,
                status = ?,
                description = ?,
                estimated_affected_people = ?
            WHERE event_id = ?
            """;

    public boolean update(EmergencyEvent event) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_EMERGENCY_EVENT_SQL)
        ) {
            statement.setString(1, event.getName());
            statement.setString(2, event.getType().name());
            statement.setString(3, event.getCountry());
            statement.setString(4, event.getCounty());
            statement.setString(5, event.getLocality());
            statement.setString(6, event.getAffectedArea());
            statement.setTimestamp(7, Timestamp.valueOf(event.getStartDatetime()));

            if (event.getEndDatetime() != null) {
                statement.setTimestamp(8, Timestamp.valueOf(event.getEndDatetime()));
            } else {
                statement.setNull(8, Types.TIMESTAMP);
            }

            statement.setString(9, event.getStatus().name());
            statement.setString(10, event.getDescription());
            if (event.getEstimatedAffectedPeople() != null) {
                statement.setInt(11, event.getEstimatedAffectedPeople());
            } else {
                statement.setNull(11, Types.INTEGER);
            }
            statement.setInt(12, event.getEventId());

            int affectedRows = statement.executeUpdate();
            return affectedRows == 1;
        }
    }

    private static final String DELETE_EMERGENCY_EVENT_SQL = """
            DELETE FROM emergency_events
            WHERE event_id = ?
            """;

    public boolean delete(Integer eventId) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_EMERGENCY_EVENT_SQL)
        ) {
            statement.setInt(1, eventId);

            int affectedRows = statement.executeUpdate();
            return affectedRows == 1;
        }
    }
}
