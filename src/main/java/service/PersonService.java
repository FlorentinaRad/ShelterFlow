package service;

import model.Person;
import repository.PersonRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private void validatePerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }

        if (person.getFirstName() == null || person.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }

        if (person.getLastName() == null || person.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }

        if (person.getBirthDate() != null && person.getBirthDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }
    }

    public Person registerPerson(Person person) throws SQLException {
        validatePerson(person);
        return personRepository.save(person);
    }

    public Person requirePersonById(Integer personId) throws SQLException {
        if (personId == null || personId <= 0) {
            throw new IllegalArgumentException("Person ID must be a positive integer");
        }

        Optional<Person> person = personRepository.findById(personId);

        if (person.isEmpty()) {
            throw new IllegalArgumentException("Person with ID " + personId + " does not exist");
        }

        return person.get();
    }

    public List<Person> listAllPersons() throws SQLException {
        return personRepository.findAll();
    }

    public Person updatePerson(Person person) throws SQLException {
        validatePerson(person);

        if (person.getPersonId() == null || person.getPersonId() <= 0) {
            throw new IllegalArgumentException("Person ID must be a positive integer");
        }

        requirePersonById(person.getPersonId());

        boolean updated = personRepository.update(person);
        if (!updated) {
            throw new SQLException("Failed to update person with ID " + person.getPersonId());
        }

        return person;
    }

    public void deletePerson(Integer personId) throws SQLException {
        requirePersonById(personId);

        boolean deleted = personRepository.delete(personId);
        if (!deleted) {
            throw new SQLException("Failed to delete person with ID " + personId);
        }
    }
}

