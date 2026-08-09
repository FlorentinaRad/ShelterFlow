package repository;

import config.DatabaseConnection;
import model.MissingPersonReport;
import model.enums.MissingPersonStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MissingPersonReportRepository {
    private static final String INSERT_MISSING_PERSON_REPORT_SQL = """
            INSERT INTO missing_person_reports (
                person_id,
                event_id,
                last_seen_datetime,
                last_known_location,
                reported_by_name,
                reported_by_phone,
                status,
                notes
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public MissingPersonReport save(MissingPersonReport report) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        INSERT_MISSING_PERSON_REPORT_SQL,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            statement.setInt(1, report.getPersonId());
            statement.setInt(2, report.getEventId());

            if (report.getLastSeenDatetime() != null) {
                statement.setTimestamp(3, Timestamp.valueOf(report.getLastSeenDatetime())
                );
            } else {
                statement.setNull(3, Types.TIMESTAMP);
            }

            statement.setString(4, report.getLastKnownLocation());
            statement.setString(5, report.getReportedByName());
            statement.setString(6, report.getReportedByPhone());
            statement.setString(7, report.getStatus().name());
            statement.setString(8, report.getNotes());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating missing person report failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    report.setReportId(generatedKeys.getInt(1));
                    return report;
                }

                throw new SQLException("Creating missing person report failed, no ID obtained.");
            }
        }
    }

    private static final String FIND_BY_ID_SQL = """
        SELECT
            report_id,
            person_id,
            event_id,
            reported_datetime,
            last_seen_datetime,
            last_known_location,
            reported_by_name,
            reported_by_phone,
            status,
            resolved_datetime,
            notes
        FROM missing_person_reports
        WHERE report_id = ?
        """;

    public Optional<MissingPersonReport> findById(int reportId) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)
        ) {
            statement.setInt(1, reportId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    MissingPersonReport report = new MissingPersonReport(
                            resultSet.getInt("report_id"),
                            resultSet.getInt("person_id"),
                            resultSet.getInt("event_id"),
                            resultSet.getTimestamp("reported_datetime").toLocalDateTime(),
                            resultSet.getTimestamp("last_seen_datetime") != null ? resultSet.getTimestamp("last_seen_datetime").toLocalDateTime() : null,
                            resultSet.getString("last_known_location"),
                            resultSet.getString("reported_by_name"),
                            resultSet.getString("reported_by_phone"),
                            MissingPersonStatus.valueOf(resultSet.getString("status")),
                            resultSet.getTimestamp("resolved_datetime") != null ? resultSet.getTimestamp("resolved_datetime").toLocalDateTime() : null,
                            resultSet.getString("notes")
                    );

                    return Optional.of(report);
                }

                return Optional.empty();
            }
        }
    }

    private static final String FIND_ALL_SQL = """
            SELECT
            report_id,
            person_id,
            event_id,
            reported_datetime,
            last_seen_datetime,
            last_known_location,
            reported_by_name,
            reported_by_phone,
            status,
            resolved_datetime,
            notes
        FROM missing_person_reports
        """;

    public List<MissingPersonReport> findAll() throws SQLException {
        List<MissingPersonReport> reports = new ArrayList<>();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                MissingPersonReport report = new MissingPersonReport(
                        resultSet.getInt("report_id"),
                        resultSet.getInt("person_id"),
                        resultSet.getInt("event_id"),
                        resultSet.getTimestamp("reported_datetime").toLocalDateTime(),
                        resultSet.getTimestamp("last_seen_datetime") != null ? resultSet.getTimestamp("last_seen_datetime").toLocalDateTime() : null,
                        resultSet.getString("last_known_location"),
                        resultSet.getString("reported_by_name"),
                        resultSet.getString("reported_by_phone"),
                        MissingPersonStatus.valueOf(resultSet.getString("status")),
                        resultSet.getTimestamp("resolved_datetime") != null ? resultSet.getTimestamp("resolved_datetime").toLocalDateTime() : null,
                        resultSet.getString("notes")
                );
                reports.add(report);
            }
            return reports;
        }
    }

    private static final String UPDATE_MISSING_PERSON_REPORT_SQL = """
        UPDATE missing_person_reports
        SET
            person_id = ?,
            event_id = ?,
            last_seen_datetime = ?,
            last_known_location = ?,
            reported_by_name = ?,
            reported_by_phone = ?,
            status = ?,
            resolved_datetime = ?,
            notes = ?
        WHERE report_id = ?
        """;

    public boolean update(MissingPersonReport report) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_MISSING_PERSON_REPORT_SQL)
        ) {
            statement.setInt(1, report.getPersonId());
            statement.setInt(2, report.getEventId());

            if (report.getLastSeenDatetime() != null) {
                statement.setTimestamp(3, Timestamp.valueOf(report.getLastSeenDatetime())
                );
            } else {
                statement.setNull(3, Types.TIMESTAMP);
            }

            statement.setString(4, report.getLastKnownLocation());
            statement.setString(5, report.getReportedByName());
            statement.setString(6, report.getReportedByPhone());
            statement.setString(7, report.getStatus().name());

            if (report.getResolvedDatetime() != null) {
                statement.setTimestamp(
                        8,
                        Timestamp.valueOf(report.getResolvedDatetime())
                );
            } else {
                statement.setNull(8, Types.TIMESTAMP);
            }

            statement.setString(9, report.getNotes());
            statement.setInt(10, report.getReportId());

            int affectedRows = statement.executeUpdate();
            return affectedRows == 1;
        }
    }

    private static final String DELETE_MISSING_PERSON_REPORT_SQL = """
        DELETE FROM missing_person_reports
        WHERE report_id = ?
        """;

    public boolean delete(Integer reportId) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_MISSING_PERSON_REPORT_SQL)
        ) {
            statement.setInt(1, reportId);

            int affectedRows = statement.executeUpdate();
            return affectedRows == 1;
        }
    }

}
