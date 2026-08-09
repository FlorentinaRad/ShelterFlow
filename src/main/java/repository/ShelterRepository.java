package repository;

import config.DatabaseConnection;
import model.Shelter;
import model.enums.ShelterStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShelterRepository {
    private static final String INSERT_SHELTER_SQL = """
            INSERT INTO shelters (
                name,
                country,
                county,
                locality,
                address,
                total_capacity,
                status,
                phone_number,
                email,
                access_ramp,
                children_area,
                medical_room,
                accepts_pets,
                public_information
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public Shelter save(Shelter shelter) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();

                PreparedStatement statement = connection.prepareStatement(
                        INSERT_SHELTER_SQL,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            statement.setString(1, shelter.getName());
            statement.setString(2, shelter.getCountry());
            statement.setString(3, shelter.getCounty());
            statement.setString(4, shelter.getLocality());
            statement.setString(5, shelter.getAddress());
            statement.setInt(6, shelter.getTotalCapacity());
            statement.setString(7, shelter.getStatus().name());
            statement.setString(8, shelter.getPhoneNumber());
            statement.setString(9, shelter.getEmail());
            statement.setBoolean(10, shelter.isAccessRamp());
            statement.setBoolean(11, shelter.isChildrenArea());
            statement.setBoolean(12, shelter.isMedicalRoom());
            statement.setBoolean(13, shelter.isAcceptsPets());
            statement.setString(14, shelter.getPublicInformation());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating shelter failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    shelter.setShelterId(generatedKeys.getInt(1));
                    return shelter;
                }
                throw new SQLException("Creating shelter failed, no ID obtained.");
            }
        }

    }

    private static final String FIND_BY_ID_SQL = """
        SELECT
            shelter_id,
            name,
            country,
            county,
            locality,
            address,
            total_capacity,
            status,
            phone_number,
            email,
            access_ramp,
            children_area,
            medical_room,
            accepts_pets,
            public_information
        FROM shelters
        WHERE shelter_id = ?
        """;

    public Optional<Shelter> findById(int shelterId) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)
        ) {
            statement.setInt(1, shelterId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Shelter shelter = new Shelter(
                            resultSet.getInt("shelter_id"),
                            resultSet.getString("name"),
                            resultSet.getString("country"),
                            resultSet.getString("county"),
                            resultSet.getString("locality"),
                            resultSet.getString("address"),
                            resultSet.getInt("total_capacity"),
                            ShelterStatus.valueOf(resultSet.getString("status")),
                            resultSet.getString("phone_number"),
                            resultSet.getString("email"),
                            resultSet.getBoolean("access_ramp"),
                            resultSet.getBoolean("children_area"),
                            resultSet.getBoolean("medical_room"),
                            resultSet.getBoolean("accepts_pets"),
                            resultSet.getString("public_information")
                    );
                    return Optional.of(shelter);
                }
                return Optional.empty();
            }
        }
    }

    private static final String FIND_ALL_SQL = """
           SELECT
            shelter_id,
            name,
            country,
            county,
            locality,
            address,
            total_capacity,
            status,
            phone_number,
            email,
            access_ramp,
            children_area,
            medical_room,
            accepts_pets,
            public_information
        FROM shelters
        """;

    public List<Shelter> findAll() throws SQLException {
        List<Shelter> shelters = new ArrayList<>();
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Shelter shelter = new Shelter(
                        resultSet.getInt("shelter_id"),
                        resultSet.getString("name"),
                        resultSet.getString("country"),
                        resultSet.getString("county"),
                        resultSet.getString("locality"),
                        resultSet.getString("address"),
                        resultSet.getInt("total_capacity"),
                        ShelterStatus.valueOf(resultSet.getString("status")),
                        resultSet.getString("phone_number"),
                        resultSet.getString("email"),
                        resultSet.getBoolean("access_ramp"),
                        resultSet.getBoolean("children_area"),
                        resultSet.getBoolean("medical_room"),
                        resultSet.getBoolean("accepts_pets"),
                        resultSet.getString("public_information")
                );
                shelters.add(shelter);
            }
            return shelters;
        }
    }

    private static final String UPDATE_SHELTER_SQL = """
            UPDATE shelters
            SET
                name = ?,
                country = ?,
                county = ?,
                locality = ?,
                address = ?,
                total_capacity = ?,
                status = ?,
                phone_number = ?,
                email = ?,
                access_ramp = ?,
                children_area = ?,
                medical_room = ?,
                accepts_pets = ?,
                public_information = ?
            WHERE shelter_id = ?
            """;

    public boolean update(Shelter shelter) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_SHELTER_SQL)
        ) {
            statement.setString(1, shelter.getName());
            statement.setString(2, shelter.getCountry());
            statement.setString(3, shelter.getCounty());
            statement.setString(4, shelter.getLocality());
            statement.setString(5, shelter.getAddress());
            statement.setInt(6, shelter.getTotalCapacity());
            statement.setString(7, shelter.getStatus().name());
            statement.setString(8, shelter.getPhoneNumber());
            statement.setString(9, shelter.getEmail());
            statement.setBoolean(10, shelter.isAccessRamp());
            statement.setBoolean(11, shelter.isChildrenArea());
            statement.setBoolean(12, shelter.isMedicalRoom());
            statement.setBoolean(13, shelter.isAcceptsPets());
            statement.setString(14, shelter.getPublicInformation());
            statement.setInt(15, shelter.getShelterId());

            int affectedRows = statement.executeUpdate();
            return affectedRows == 1;
        }
    }

    private static final String DELETE_SHELTER_SQL = """
            DELETE FROM shelters
            WHERE shelter_id = ?
            """;

    public boolean delete(Integer shelterId) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_SHELTER_SQL)
        ) {
            statement.setInt(1, shelterId);
            int affectedRows = statement.executeUpdate();
            return affectedRows == 1;
        }
    }
}


