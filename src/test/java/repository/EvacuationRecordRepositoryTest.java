package repository;

import model.EmergencyEvent;
import model.EvacuationRecord;
import model.Person;
import model.enums.EmergencyStatus;
import model.enums.EmergencyType;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EvacuationRecordRepositoryTest {
    private final EvacuationRecordRepository repository =
            new EvacuationRecordRepository();

    private final PersonRepository personRepository =
            new PersonRepository();

    private final EmergencyEventRepository eventRepository =
            new EmergencyEventRepository();

    @Test
    void saveShouldGenerateId() throws SQLException {
        Person person = new Person(
                "Test",
                "Evacuation",
                null,
                "0700000000",
                "evacuation.test@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                "Test person for evacuation"
        );
        Person savedPerson = personRepository.save(person);

        EmergencyEvent event = new EmergencyEvent (
                "Test Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj-Napoca metropolitan area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test for evacuation record save()",
                1000
        );
        EmergencyEvent savedEvent = eventRepository.save(event);

        EvacuationRecord record = new EvacuationRecord(
                savedPerson.getPersonId(),
                savedEvent.getEventId(),
                "Cluj-Napoca",
                true,
                "Needs wheelchair access",
                "Test for save()"
        );
        EvacuationRecord savedRecord = repository.save(record);
        assertNotNull(savedRecord.getEvacuationId());
    }

    @Test
    void findByIdShouldReturnEvacuationRecordWhenExists() throws SQLException {
        Person person = new Person(
                "Test",
                "Evacuation",
                LocalDate.of(1995, 5, 20),
                "0700000000",
                "evacuation.find@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                "Test person"
        );

        Person savedPerson = personRepository.save(person);

        EmergencyEvent event = new EmergencyEvent(
                "Test Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj-Napoca metropolitan area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test event",
                1000
        );

        EmergencyEvent savedEvent = eventRepository.save(event);

        EvacuationRecord record = new EvacuationRecord(
                savedPerson.getPersonId(),
                savedEvent.getEventId(),
                "Cluj-Napoca",
                true,
                "Needs assistance",
                "Test for findById()"
        );

        EvacuationRecord savedRecord = repository.save(record);

        Optional<EvacuationRecord> result =
                repository.findById(savedRecord.getEvacuationId());

        assertTrue(result.isPresent());
        assertEquals(
                savedRecord.getEvacuationId(),
                result.get().getEvacuationId()
        );

        assertNotNull(result.get().getRegistrationDatetime());
    }

    @Test
    void findByIdShouldReturnEmptyWhenEvacuationRecordDoesNotExist() throws SQLException {
        Optional<EvacuationRecord> result = repository.findById(99999999);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllShouldReturnEvacuationRecords() throws SQLException {
        List<EvacuationRecord> records = repository.findAll();

        assertFalse(records.isEmpty());
    }

    @Test
    void updateShouldModifyEvacuationRecord() throws SQLException {
        Person person = new Person(
                "Test",
                "Update Evacuation",
                LocalDate.of(1995, 5, 20),
                "0700000000",
                "evacuation.update@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                "Test person"
        );

        Person savedPerson = personRepository.save(person);

        EmergencyEvent event = new EmergencyEvent(
                "Test Update Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj-Napoca metropolitan area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test event",
                1000
        );

        EmergencyEvent savedEvent = eventRepository.save(event);

        EvacuationRecord record = new EvacuationRecord(
                savedPerson.getPersonId(),
                savedEvent.getEventId(),
                "Old location",
                false,
                null,
                "Before update"
        );

        EvacuationRecord savedRecord = repository.save(record);

        savedRecord.setEvacuationLocation("Updated location");
        savedRecord.setNeedsAssistance(true);
        savedRecord.setAssistanceDetails("Needs medical assistance");
        savedRecord.setNotes("After update");

        boolean updated = repository.update(savedRecord);

        Optional<EvacuationRecord> result =
                repository.findById(savedRecord.getEvacuationId());

        assertTrue(updated);
        assertTrue(result.isPresent());
        assertEquals(
                "Updated location",
                result.get().getEvacuationLocation()
        );
        assertTrue(result.get().isNeedsAssistance());
        assertEquals(
                "Needs medical assistance",
                result.get().getAssistanceDetails()
        );
        assertEquals("After update", result.get().getNotes());
    }

    @Test
    void deleteShouldRemoveEvacuationRecord() throws SQLException {
        Person person = new Person(
                "Test",
                "Delete Evacuation",
                LocalDate.of(1995, 5, 20),
                "0700000000",
                "evacuation.delete@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                "Test person"
        );

        Person savedPerson = personRepository.save(person);

        EmergencyEvent event = new EmergencyEvent(
                "Test Delete Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj-Napoca metropolitan area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test event",
                1000
        );

        EmergencyEvent savedEvent = eventRepository.save(event);

        EvacuationRecord record = new EvacuationRecord(
                savedPerson.getPersonId(),
                savedEvent.getEventId(),
                "Cluj-Napoca",
                false,
                null,
                "Test for delete()"
        );

        EvacuationRecord savedRecord = repository.save(record);

        boolean deleted =
                repository.delete(savedRecord.getEvacuationId());

        Optional<EvacuationRecord> result =
                repository.findById(savedRecord.getEvacuationId());

        assertTrue(deleted);
        assertTrue(result.isEmpty());
    }
}
