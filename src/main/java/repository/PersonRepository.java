package repository;

import config.DatabaseConnection;
import model.Person;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonRepository {

    private static final String INSERT_PERSON_SQL = """
            INSERT INTO persons (
                first_name,
                last_name,
                birth_date,
                phone_number,
                email,
                home_country,
                home_county,
                home_locality,
                home_address,
                notes
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public Person save(Person person) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();

                PreparedStatement statement = connection.prepareStatement(
                        INSERT_PERSON_SQL,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            statement.setString(1, person.getFirstName());
            statement.setString(2, person.getLastName());

            if (person.getBirthDate() != null) {
                statement.setDate(
                        3,
                        Date.valueOf(person.getBirthDate())
                );
            } else {
                statement.setNull(3, Types.DATE);
            }

            statement.setString(4, person.getPhoneNumber());
            statement.setString(5, person.getEmail());
            statement.setString(6, person.getHomeCountry());
            statement.setString(7, person.getHomeCounty());
            statement.setString(8, person.getHomeLocality());
            statement.setString(9, person.getHomeAddress());
            statement.setString(10, person.getNotes());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(
                        "Creating person failed, no rows affected."
                );
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    person.setPersonId(generatedKeys.getInt(1));
                    return person;
                }

                throw new SQLException(
                        "Creating person failed, no ID obtained."
                );
            }
        }
    }

    private static final String FIND_BY_ID_SQL = """
            SELECT 
                person_id,
                first_name,
                last_name,
                birth_date,
                phone_number,
                email,
                home_country,
                home_county,
                home_locality,
                home_address,
                notes
            FROM persons
            WHERE person_id = ?
            """;

    public Optional<Person> findById(int personId) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)
        ) {
            statement.setInt(1, personId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Person person = new Person(
                            resultSet.getInt("person_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getDate("birth_date") != null ? resultSet.getDate("birth_date").toLocalDate() : null,
                            resultSet.getString("phone_number"),
                            resultSet.getString("email"),
                            resultSet.getString("home_country"),
                            resultSet.getString("home_county"),
                            resultSet.getString("home_locality"),
                            resultSet.getString("home_address"),
                            resultSet.getString("notes")
                    );

                    return Optional.of(person);
                }
                return Optional.empty();
            }
        }
    }

    private static final String FIND_ALL_SQL = """
            SELECT 
                person_id,
                first_name,
                last_name,
                birth_date,
                phone_number,
                email,
                home_country,
                home_county,
                home_locality,
                home_address,
                notes
            FROM persons
            """;

    public List<Person> findAll() throws SQLException {
        List<Person> persons = new ArrayList<>();

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL);
            ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Person person = new Person(
                    resultSet.getInt("person_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getDate("birth_date") != null ? resultSet.getDate("birth_date").toLocalDate() : null,
                    resultSet.getString("phone_number"),
                    resultSet.getString("email"),
                    resultSet.getString("home_country"),
                    resultSet.getString("home_county"),
                    resultSet.getString("home_locality"),
                    resultSet.getString("home_address"),
                    resultSet.getString("notes")
                );
                persons.add(person);
            }
            return persons;
        }
    }

    private static final String UPDATE_PERSON__SQL = """
            UPDATE persons
            SET
                first_name = ?,
                last_name = ?,
                birth_date = ?,
                phone_number = ?,
                email = ?,
                home_country = ?,
                home_county = ?,
                home_locality = ?,
                home_address = ?,
                notes = ?
            WHERE person_id = ?
            """;

    public boolean update(Person person) throws SQLException {
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_PERSON__SQL)
        ) {
            statement.setString(1, person.getFirstName());
            statement.setString(2, person.getLastName());

            if (person.getBirthDate() != null) {
                statement.setDate(3, Date.valueOf(person.getBirthDate()));
            } else {
                statement.setNull(3, Types.DATE);
            }

            statement.setString(4, person.getPhoneNumber());
            statement.setString(5, person.getEmail());
            statement.setString(6, person.getHomeCountry());
            statement.setString(7, person.getHomeCounty());
            statement.setString(8, person.getHomeLocality());
            statement.setString(9, person.getHomeAddress());
            statement.setString(10, person.getNotes());

            statement.setInt(11, person.getPersonId());

            int affectedRows = statement.executeUpdate();
            return affectedRows == 1;
        }
    }

    private static final String DELETE_PERSON_SQL = """
        DELETE FROM persons
        WHERE person_id = ?
        """;

    public boolean delete(int personId) throws SQLException {
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_PERSON_SQL)
        ) {
            statement.setInt(1, personId);

            int affectedRows = statement.executeUpdate();
            return affectedRows == 1;
        }
    }
}