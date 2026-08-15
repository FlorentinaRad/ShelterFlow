package service;

import model.EmergencyEvent;
import model.EvacuationRecord;
import model.Person;
import model.enums.EmergencyStatus;
import model.enums.EmergencyType;
import org.junit.jupiter.api.Test;
import repository.EmergencyEventRepository;
import repository.EvacuationRecordRepository;
import repository.PersonRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EvacuationRecordServiceTest {
    private final PersonRepository personRepository = new PersonRepository();
    private final EmergencyEventRepository emergencyEventRepository = new EmergencyEventRepository();
    private final EvacuationRecordRepository evacuationRecordRepository = new EvacuationRecordRepository();
    private final PersonService personService = new PersonService(personRepository);
    private final EmergencyEventService emergencyEventService = new EmergencyEventService(emergencyEventRepository);
    private final EvacuationRecordService evacuationRecordService = new EvacuationRecordService(
            evacuationRecordRepository,
            personService,
            emergencyEventService
    );

    @Test
    void registerEvacuationRecordShouldSaveValidRecord() throws SQLException {
        Person person = new Person(
                "Test",
                "Person",
                LocalDate.of(1995, 5, 10),
                "0712345678",
                "person@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 1",
                "Test person"
        );

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Emergency Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj-Napoca metropolitan area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test emergency event",
                1000
        );
        Person savedPerson = personService.registerPerson(person);
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        EvacuationRecord evacuationRecord = new EvacuationRecord(
                savedPerson.getPersonId(),
                savedEmergencyEvent.getEventId(),
                "Cluj-Napoca",
                true,
                "Requires transportation assistance",
                "Test evacuation record"
        );

        EvacuationRecord savedEvacuationRecord = evacuationRecordService.registerEvacuationRecord(evacuationRecord);
        assertNotNull(savedEvacuationRecord.getEvacuationId());
    }

    @Test
    void registerEvacuationRecordShouldRejectNullRecord() throws SQLException {
        try {
            evacuationRecordService.registerEvacuationRecord(null);
            fail("Expected IllegalArgumentException for null evacuation record");
        } catch (IllegalArgumentException e) {
            assertEquals("Evacuation record cannot be null", e.getMessage());
        }
    }

    @Test
    void registerEvacuationRecordShouldRejectDuplicatePersonAndEvent() throws SQLException {
        Person person = new Person(
                "Test",
                "Person",
                LocalDate.of(1995, 5, 10),
                "0712345678",
                "person@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 1",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Emergency Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj-Napoca metropolitan area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test emergency event",
                1000
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        EvacuationRecord firstRecord = new EvacuationRecord(
                savedPerson.getPersonId(),
                savedEmergencyEvent.getEventId(),
                "Cluj-Napoca",
                true,
                "Requires transportation assistance",
                "First evacuation record"
        );

        EvacuationRecord duplicateRecord = new EvacuationRecord(
                savedPerson.getPersonId(),
                savedEmergencyEvent.getEventId(),
                "Different location",
                false,
                null,
                "Duplicate evacuation record"
        );

        evacuationRecordService.registerEvacuationRecord(firstRecord);

        try {
            evacuationRecordService.registerEvacuationRecord(duplicateRecord);
            fail("Expected IllegalArgumentException for duplicate evacuation record");
        } catch (IllegalArgumentException e) {
            assertEquals("Person is already registered for this emergency event", e.getMessage());
        }
    }

    @Test
    void requireEvacuationRecordByIdShouldReturnRecordWhenExists() throws SQLException {
        Person person = new Person(
                "Maria",
                "Popescu",
                LocalDate.of(1990, 8, 15),
                "0723456789",
                "maria@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 2",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Fire Event",
                EmergencyType.FIRE,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Central area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test fire emergency",
                500
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        EvacuationRecord evacuationRecord = new EvacuationRecord(
                savedPerson.getPersonId(),
                savedEmergencyEvent.getEventId(),
                "Cluj-Napoca Central Station",
                false,
                null,
                "Test evacuation record"
        );

        EvacuationRecord savedRecord = evacuationRecordService.registerEvacuationRecord(evacuationRecord);
        EvacuationRecord retrievedRecord = evacuationRecordService.requireEvacuationRecordById(savedRecord.getEvacuationId());
        assertEquals(savedRecord.getEvacuationId(), retrievedRecord.getEvacuationId()
        );
    }

    @Test
    void listAllEvacuationRecordsShouldReturnRecords() throws SQLException {
        Person person = new Person(
                "Andrei",
                "Ionescu",
                LocalDate.of(1988, 3, 20),
                "0734567890",
                "andrei@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 3",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Earthquake Event",
                EmergencyType.EARTHQUAKE,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test earthquake emergency",
                750
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        EvacuationRecord evacuationRecord = new EvacuationRecord(
                savedPerson.getPersonId(),
                savedEmergencyEvent.getEventId(),
                "Cluj-Napoca",
                false,
                null,
                "Test evacuation record"
        );
        EvacuationRecord savedRecord = evacuationRecordService.registerEvacuationRecord(evacuationRecord);

        List<EvacuationRecord> evacuationRecords = evacuationRecordService.listAllEvacuationRecords();

        boolean found = false;

        for (EvacuationRecord currentRecord : evacuationRecords) {
            if (currentRecord.getEvacuationId().equals(savedRecord.getEvacuationId())) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void updateEvacuationRecordShouldModifyRecord() throws SQLException {
        Person person = new Person(
                "Elena",
                "Marin",
                LocalDate.of(1992, 11, 5),
                "0745678901",
                "elena@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 4",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Flood Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test flood emergency",
                600
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        EvacuationRecord evacuationRecord = new EvacuationRecord(
                savedPerson.getPersonId(),
                savedEmergencyEvent.getEventId(),
                "Initial evacuation location",
                false,
                null,
                "Initial notes"
        );
        EvacuationRecord savedRecord = evacuationRecordService.registerEvacuationRecord(evacuationRecord);

        savedRecord.setEvacuationLocation("Updated evacuation location");
        savedRecord.setNotes("Updated notes");

        evacuationRecordService.updateEvacuationRecord(savedRecord);
        EvacuationRecord result = evacuationRecordService.requireEvacuationRecordById(savedRecord.getEvacuationId());

        assertEquals("Updated evacuation location", result.getEvacuationLocation());
        assertEquals("Updated notes", result.getNotes());
    }

    @Test
    void updateEvacuationRecordShouldRejectDuplicatePersonAndEvent() throws SQLException {
        Person firstPerson = new Person(
                "Daniel",
                "Pop",
                LocalDate.of(1985, 6, 12),
                "0756789012",
                "daniel@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 5",
                "First test person"
        );
        Person savedFirstPerson = personService.registerPerson(firstPerson);

        Person secondPerson = new Person(
                "Ioana",
                "Muresan",
                LocalDate.of(1993, 2, 18),
                "0767890123",
                "ioana@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 6",
                "Second test person"
        );
        Person savedSecondPerson = personService.registerPerson(secondPerson);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Fire Event",
                EmergencyType.FIRE,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test emergency event",
                400
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        EvacuationRecord firstRecord = new EvacuationRecord(
                savedFirstPerson.getPersonId(),
                savedEmergencyEvent.getEventId(),
                "Location 1",
                false,
                null,
                "First evacuation record"
        );
        EvacuationRecord secondRecord = new EvacuationRecord(
                savedSecondPerson.getPersonId(),
                savedEmergencyEvent.getEventId(),
                "Location 2",
                false,
                null,
                "Second evacuation record"
        );
        evacuationRecordService.registerEvacuationRecord(firstRecord);
        EvacuationRecord savedSecondRecord = evacuationRecordService.registerEvacuationRecord(secondRecord);

        savedSecondRecord.setPersonId(savedFirstPerson.getPersonId());

        try {
            evacuationRecordService.updateEvacuationRecord(savedSecondRecord);
            fail("Expected IllegalArgumentException for duplicate evacuation record");
        } catch (IllegalArgumentException e) {
            assertEquals("Person is already registered for this emergency event", e.getMessage());
        }
    }

    @Test
    void deleteEvacuationRecordShouldRemoveRecord() throws SQLException {
        Person person = new Person(
                "Mihai",
                "Rusu",
                LocalDate.of(1989, 9, 25),
                "0778901234",
                "mihai@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 7",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Landslide Event",
                EmergencyType.LANDSLIDE,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test emergency event",
                300
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        EvacuationRecord evacuationRecord = new EvacuationRecord(
                savedPerson.getPersonId(),
                savedEmergencyEvent.getEventId(),
                "Test evacuation location",
                false,
                null,
                "Record for delete test"
        );
        EvacuationRecord savedRecord = evacuationRecordService.registerEvacuationRecord(evacuationRecord);
        evacuationRecordService.deleteEvacuationRecord(savedRecord.getEvacuationId());

        try {
            evacuationRecordService.requireEvacuationRecordById(savedRecord.getEvacuationId());
            fail("Expected IllegalArgumentException for deleted evacuation record");
        } catch (IllegalArgumentException e) {
            assertEquals("Evacuation record with ID " + savedRecord.getEvacuationId() + " does not exist", e.getMessage());
        }
    }
}
