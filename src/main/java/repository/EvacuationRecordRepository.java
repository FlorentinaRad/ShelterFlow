package repository;

import config.DatabaseConnection;
import model.EvacuationRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EvacuationRecordRepository {
    private static final String INSERT_EVACUATION_RECORD_SQL = """
            INSERT INTO evacuation_records (
                person_id,
                event_id,
                evacuation_location,
                needs_assistance,
                assistance_details,
                notes
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    public EvacuationRecord save(EvacuationRecord record) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_EVACUATION_RECORD_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1, record.getPersonId());
            statement.setInt(2, record.getEventId());
            statement.setString(3, record.getEvacuationLocation());
            statement.setBoolean(4, record.isNeedsAssistance());
            statement.setString(5, record.getAssistanceDetails());
            statement.setString(6, record.getNotes());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating evacuation record failed, no rows affected.");
            }

            try (
                    ResultSet generatedKeys = statement.getGeneratedKeys()
            ) {
                if (generatedKeys.next()) {
                    record.setEvacuationId(generatedKeys.getInt(1));
                    return record;
                }
                throw new SQLException("Creating evacuation record failed, no ID obtained.");
            }
        }
    }

    private static final String FIND_BY_ID_SQL = """
            SELECT
                evacuation_id,
                person_id,
                event_id,
                registration_datetime,
                evacuation_location,
                needs_assistance,
                assistance_details,
                notes
            FROM evacuation_records
            WHERE evacuation_id = ?
            """;

    public Optional<EvacuationRecord> findById(int evacuationId) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)
        ) {
            statement.setInt(1, evacuationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    EvacuationRecord record = new EvacuationRecord(
                            resultSet.getInt("evacuation_id"),
                            resultSet.getInt("person_id"),
                            resultSet.getInt("event_id"),
                            resultSet.getTimestamp("registration_datetime").toLocalDateTime(),
                            resultSet.getString("evacuation_location"),
                            resultSet.getBoolean("needs_assistance"),
                            resultSet.getString("assistance_details"),
                            resultSet.getString("notes")
                    );
                    return Optional.of(record);
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    private static final String FIND_ALL_SQL = """
            SELECT
                evacuation_id,
                person_id,
                event_id,
                registration_datetime,
                evacuation_location,
                needs_assistance,
                assistance_details,
                notes
            FROM evacuation_records
            """;

    public List<EvacuationRecord> findAll() throws SQLException {
        List<EvacuationRecord> records = new ArrayList<>();

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL);
            ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                EvacuationRecord record = new EvacuationRecord(
                        resultSet.getInt("evacuation_id"),
                        resultSet.getInt("person_id"),
                        resultSet.getInt("event_id"),
                        resultSet.getTimestamp("registration_datetime").toLocalDateTime(),
                        resultSet.getString("evacuation_location"),
                        resultSet.getBoolean("needs_assistance"),
                        resultSet.getString("assistance_details"),
                        resultSet.getString("notes")
                );
                records.add(record);
            }
            return records;
        }
    }

    private static final String UPDATE_EVACUATION_RECORD_SQL = """
            UPDATE evacuation_records
            SET
                person_id = ?,
                event_id = ?,
                evacuation_location = ?,
                needs_assistance = ?,
                assistance_details = ?,
                notes = ?
            WHERE evacuation_id = ?
            """;

    public boolean update(EvacuationRecord record) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_EVACUATION_RECORD_SQL)
        ) {
            statement.setInt(1, record.getPersonId());
            statement.setInt(2, record.getEventId());
            statement.setString(3, record.getEvacuationLocation());
            statement.setBoolean(4, record.isNeedsAssistance());
            statement.setString(5, record.getAssistanceDetails());
            statement.setString(6, record.getNotes());
            statement.setInt(7, record.getEvacuationId());

            int affectedRows = statement.executeUpdate();
            return affectedRows == 1;
        }
    }

    private static final String DELETE_EVACUATION_RECORD_SQL = """
            DELETE FROM evacuation_records
            WHERE evacuation_id = ?
            """;

    public boolean delete(Integer evacuationId) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_EVACUATION_RECORD_SQL)
        ) {
            statement.setInt(1, evacuationId);

            int affectedRows = statement.executeUpdate();
            return affectedRows == 1;
        }
    }

    private static final String FIND_BY_PERSON_AND_EVENT_SQL = """
            SELECT 
                evacuation_id,
                person_id,
                event_id,
                registration_datetime,
                evacuation_location,
                needs_assistance,
                assistance_details,
                notes
            FROM evacuation_records
            WHERE person_id = ?
            AND event_id = ?
            """;

    public Optional<EvacuationRecord> findByPersonIdAndEventId(Integer personId, Integer eventId) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_BY_PERSON_AND_EVENT_SQL)
        ) {
            statement.setInt(1, personId);
            statement.setInt(2, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    EvacuationRecord evacuationRecord = new EvacuationRecord(
                            resultSet.getInt("evacuation_id"),
                            resultSet.getInt("person_id"),
                            resultSet.getInt("event_id"),
                            resultSet.getTimestamp("registration_datetime").toLocalDateTime(),
                            resultSet.getString("evacuation_location"),
                            resultSet.getBoolean("needs_assistance"),
                            resultSet.getString("assistance_details"),
                            resultSet.getString("notes")
                    );
                    return Optional.of(evacuationRecord);
                }
                return Optional.empty();
            }
        }
    }
}
