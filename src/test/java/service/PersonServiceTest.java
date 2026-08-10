package service;

import model.Person;
import org.junit.jupiter.api.Test;
import repository.PersonRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonServiceTest {
    private final PersonRepository personRepository = new PersonRepository();
    private final PersonService personService = new PersonService(personRepository);

    @Test
    void registerPersonShouldSaveValidPerson() throws SQLException {
        Person person = new Person(
                "Test",
                "Register",
                LocalDate.of(2000, 5, 20),
                "0700000000",
                "service.register@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                "Test for registerPerson()"
        );
        Person savedPerson = personService.registerPerson(person);
        assertNotNull(savedPerson.getPersonId());
    }

    @Test
    void registerPersonShouldRejectNullPerson() throws SQLException {
        try {
            personService.registerPerson(null);
            fail("Expected IllegalArgumentException for null person");
        } catch (IllegalArgumentException e) {
            assertEquals("Person cannot be null", e.getMessage());
        }
    }

    @Test
    void registerPersonShouldRejectBlankFirstName() throws SQLException {
        Person person = new Person(
                "   ",
                "Test",
                LocalDate.of(2000, 5, 20),
                "0700000000",
                "blank.firstname@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                "Invalid first name"
        );
        try {
            personService.registerPerson(person);
            fail("Expected IllegalArgumentException for blank first name");
        } catch (IllegalArgumentException e) {
            assertEquals("First name cannot be null or empty", e.getMessage());
        }
    }

    @Test
    void registerPersonShouldRejectFutureBirthDate() throws SQLException {
        Person person = new Person(
                "Test",
                "Future",
                LocalDate.now().plusDays(1),
                "0700000000",
                "future.birthdate@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                "Invalid birth date"
        );
        try {
            personService.registerPerson(person);
            fail("Expected IllegalArgumentException for future birth date");
        } catch (IllegalArgumentException e) {
            assertEquals("Birth date cannot be in the future", e.getMessage());
        }
    }

    @Test
    void requirePersonByIdShouldReturnPersonWhenExists() throws SQLException{
        Person person = new Person(
                "Test",
                "Require",
                LocalDate.of(1998, 4, 10),
                "0711111111",
                "service.require@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                "Test for requirePersonById()"
        );
        Person savedPerson = personService.registerPerson(person);
        Person result = personService.requirePersonById(savedPerson.getPersonId());
        assertEquals(savedPerson.getPersonId(), result.getPersonId());
    }

    @Test
    void requirePersonByIdShouldRejectInvalidId() throws SQLException {
        try {
            personService.requirePersonById(0);
            fail("Expected IllegalArgumentException for invalid person ID");
        } catch (IllegalArgumentException e) {
            assertEquals("Person ID must be a positive integer", e.getMessage());
        }
    }

    @Test
    void requirePersonByIdShouldRejectNonExistingPerson() throws SQLException {
        int personId = 9999999;
        try {
            personService.requirePersonById(personId);
            fail("Expected IllegalArgumentException for non-existing person ID");
        } catch (IllegalArgumentException e) {
            assertEquals("Person with ID " + personId + " does not exist", e.getMessage());
        }
    }

    @Test
    void listAllPersonsShouldReturnPersons() throws SQLException {
        Person person = new Person(
                "Test",
                "List",
                LocalDate.of(1997, 8, 12),
                "0722222222",
                "service.list@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                "Test for listAllPersons()"
        );
        Person savedPerson = personService.registerPerson(person);
        List<Person> persons = personService.listAllPersons();

        assertFalse(persons.isEmpty());
        boolean found = false;

        for(Person currentPerson : persons) {
            if(currentPerson.getPersonId().equals(savedPerson.getPersonId())) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    void updatePersonShouldModifyPerson() throws SQLException{
        Person person = new Person(
                "Test",
                "Update",
                LocalDate.of(1995, 3, 15),
                "0733333333",
                "service.update@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Old address",
                "Before update"
        );
        Person savedPerson = personService.registerPerson(person);
        savedPerson.setFirstName("Updated");
        savedPerson.setHomeAddress("New address");
        savedPerson.setNotes("After update");

        Person updatedPerson = personService.updatePerson(savedPerson);

        Person result = personService.requirePersonById(updatedPerson.getPersonId());
        assertEquals("Updated", result.getFirstName());
        assertEquals("New address", result.getHomeAddress());
        assertEquals("After update", result.getNotes());
    }

    @Test
    void updatePersonShouldRejectInvalidData() throws SQLException{
        Person person = new Person(
                "Test",
                "Invalid Update",
                LocalDate.of(1995, 3, 15),
                "0744444444",
                "service.invalid.update@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                "Before invalid update"
        );
        Person savedPerson = personService.registerPerson(person);
        savedPerson.setFirstName(" ");

        try {
            personService.updatePerson(savedPerson);
            fail("Expected IllegalArgumentException for invalid first name");
        } catch(IllegalArgumentException e) {
            assertEquals("First name cannot be null or empty", e.getMessage());
        }
    }

    @Test
    void deletePersonShouldRemovePerson() throws SQLException{
        Person person = new Person(
                "Test",
                "Delete",
                LocalDate.of(1996, 6, 20),
                "0755555555",
                "service.delete@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                "Test for deletePerson()"
        );
        Person savedPerson = personService.registerPerson(person);
        personService.deletePerson(savedPerson.getPersonId());

        try {
            personService.requirePersonById(savedPerson.getPersonId());
            fail("Expected IllegalArgumentException for deleted person");
        } catch (IllegalArgumentException e) {
            assertEquals("Person with ID " + savedPerson.getPersonId() + " does not exist", e.getMessage());
        }
    }
}
