package repository;

import model.Person;
import org.junit.jupiter.api.Test;

import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PersonRepositoryTest {

    private final PersonRepository repository = new PersonRepository();

    @Test
    void saveShouldGenerateId() throws SQLException {
        Person person = new Person(
                "Test",
                "Save",
                LocalDate.of(1995, 5, 20),
                "0700000000",
                "save.test@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                "Test for save()"
        );
        Person savedPerson = repository.save(person);
        assertNotNull(savedPerson.getPersonId());
    }

    @Test
    void findByIdShouldReturnPersonWhenExists() throws SQLException{
        Person person = new Person(
                "Test",
                "Find",
                LocalDate.of(1990, 1, 1),
                "0711111111",
                "find.test@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                "Test for findById()"
                );
        Person savedPerson = repository.save(person);

        Optional<Person> result = repository.findById(savedPerson.getPersonId());
        assertTrue(result.isPresent());
        assertEquals(savedPerson.getPersonId(), result.get().getPersonId());
    }

    @Test
    void findByIdShouldReturnEmptyWhenPersonDoesNotExist() throws SQLException {
        Optional<Person> result = repository.findById(99999999);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllShouldReturnPersons() throws SQLException {
        List<Person> persons = repository.findAll();
        assertFalse(persons.isEmpty());
    }

    @Test
    void updateShouldModifyPerson() throws SQLException {
        Person person = new Person(
                "Test",
                "Update",
                LocalDate.of(1990, 1, 10),
                "0722222222",
                "update.test@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Old address",
                "Before update"
        );

        Person savedPerson = repository.save(person);

        savedPerson.setFirstName("Updated");
        savedPerson.setHomeAddress("New address");
        savedPerson.setNotes("After update");

        boolean update = repository.update(savedPerson);

        Optional<Person> updatedPerson = repository.findById(savedPerson.getPersonId());
        assertTrue(update);
        assertEquals("Updated", updatedPerson.get().getFirstName());
        assertEquals("New address", updatedPerson.get().getHomeAddress());
        assertEquals("After update", updatedPerson.get().getNotes());
    }

    @Test
    void deleteShouldRemovePerson() throws SQLException {
        Person person = new Person(
                "Test",
                "Delete",
                LocalDate.of(1990, 1, 15),
                "0733333333",
                "delete.test@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                "Test for delete()"
    );

        Person savedPerson = repository.save(person);
        boolean delete = repository.delete(savedPerson.getPersonId());
        Optional<Person> deletedPerson = repository.findById(savedPerson.getPersonId());
        assertTrue(delete);
        assertTrue(deletedPerson.isEmpty());
    }
}