package service;

import model.*;
import model.enums.EmergencyStatus;
import model.enums.EmergencyType;
import model.enums.MissingPersonStatus;
import org.junit.jupiter.api.Test;
import repository.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MissingPersonReportServiceTest {
    private final PersonRepository personRepository = new PersonRepository();
    private final EmergencyEventRepository emergencyEventRepository = new EmergencyEventRepository();
    private final MissingPersonReportRepository missingPersonReportRepository = new MissingPersonReportRepository();

    private final PersonService personService = new PersonService(personRepository);
    private final EmergencyEventService emergencyEventService = new EmergencyEventService(emergencyEventRepository);
    private final MissingPersonReportService missingPersonReportService = new MissingPersonReportService(missingPersonReportRepository, personService, emergencyEventService);

    @Test
    void registerMissingPersonReportShouldSaveValidReport() throws SQLException{
        Person person = new Person(
                "Andrei",
                "Pop",
                LocalDate.of(1990, 5, 14),
                "0711111120",
                "andrei.missing@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 20",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Missing Person Event",
                EmergencyType.FLOOD,
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

        MissingPersonReport report = new MissingPersonReport();
        report.setPersonId(savedPerson.getPersonId());
        report.setEventId(savedEmergencyEvent.getEventId());
        report.setLastSeenDatetime(LocalDateTime.now().minusHours(2));
        report.setLastKnownLocation("Cluj-Napoca city center");
        report.setReportedByName("Maria Pop");
        report.setReportedByPhone("0722222220");
        report.setStatus(MissingPersonStatus.MISSING);
        report.setNotes("Test missing person report");

        MissingPersonReport savedReport = missingPersonReportService.registerMissingPersonReport(report);

        assertNotNull(savedReport.getReportId());
    }

    @Test
    void registerMissingPersonReportShouldRejectNullReport() throws SQLException{
        try {
            missingPersonReportService.registerMissingPersonReport(null);
            fail("Expected IllegalArgumentException for null missing person report");
        } catch (IllegalArgumentException e) {
            assertEquals("Missing person report cannot be null", e.getMessage());
        }
    }

    @Test
    void registerMissingPersonReportShouldRejectDuplicatePersonAndEvent() throws SQLException{
        Person person = new Person(
                "Bianca",
                "Muresan",
                LocalDate.of(1993, 7, 18),
                "0711111121",
                "bianca.missing@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 21",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Fire Missing Event",
                EmergencyType.FIRE,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Central area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test emergency event",
                300
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        MissingPersonReport firstReport = new MissingPersonReport();
        firstReport.setPersonId(savedPerson.getPersonId());
        firstReport.setEventId(savedEmergencyEvent.getEventId());
        firstReport.setLastSeenDatetime(LocalDateTime.now().minusHours(3));
        firstReport.setLastKnownLocation("Central area");
        firstReport.setReportedByName("Daniel Muresan");
        firstReport.setReportedByPhone("0722222221");
        firstReport.setStatus(MissingPersonStatus.MISSING);
        firstReport.setNotes("First report");

        MissingPersonReport secondReport = new MissingPersonReport();
        secondReport.setPersonId(savedPerson.getPersonId());
        secondReport.setEventId(savedEmergencyEvent.getEventId());
        secondReport.setLastSeenDatetime(LocalDateTime.now().minusHours(2));
        secondReport.setLastKnownLocation("Another location");
        secondReport.setReportedByName("Elena Muresan");
        secondReport.setReportedByPhone("0733333321");
        secondReport.setStatus(MissingPersonStatus.MISSING);
        secondReport.setNotes("Second report");

        missingPersonReportService.registerMissingPersonReport(firstReport);

        try {
            missingPersonReportService.registerMissingPersonReport(secondReport);
            fail("Expected IllegalArgumentException for duplicate missing person report");
        } catch (IllegalArgumentException e) {
            assertEquals("Person already has a missing person report for this emergency event", e.getMessage());
        }
    }

    @Test
    void registerMissingPersonReportShouldRejectNonMissingStatus() throws SQLException{
        Person person = new Person(
                "Catalin",
                "Rusu",
                LocalDate.of(1988, 11, 9),
                "0711111122",
                "catalin.missing@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 22",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Earthquake Missing Event",
                EmergencyType.EARTHQUAKE,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test emergency event",
                500
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        MissingPersonReport report = new MissingPersonReport();
        report.setPersonId(savedPerson.getPersonId());
        report.setEventId(savedEmergencyEvent.getEventId());
        report.setLastSeenDatetime(LocalDateTime.now().minusHours(4));
        report.setLastKnownLocation("Cluj-Napoca");
        report.setReportedByName("Mihai Rusu");
        report.setReportedByPhone("0722222222");
        report.setStatus(MissingPersonStatus.LOCATED_SAFE);
        report.setResolvedDatetime(LocalDateTime.now());
        report.setNotes("Invalid new report status");

        try {
            missingPersonReportService.registerMissingPersonReport(report);
            fail("Expected IllegalArgumentException for non-MISSING status");
        } catch (IllegalArgumentException e) {
            assertEquals("New missing person report must have MISSING status", e.getMessage());
        }
    }

    @Test
    void requireMissingPersonReportByIdShouldReturnReportWhenExists() throws SQLException{
        Person person = new Person(
                "Diana",
                "Popescu",
                LocalDate.of(1995, 2, 16),
                "0711111123",
                "diana.missing@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 23",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Landslide Missing Event",
                EmergencyType.LANDSLIDE,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test emergency event",
                180
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        MissingPersonReport report = new MissingPersonReport();
        report.setPersonId(savedPerson.getPersonId());
        report.setEventId(savedEmergencyEvent.getEventId());
        report.setLastSeenDatetime(LocalDateTime.now().minusHours(5));
        report.setLastKnownLocation("Cluj-Napoca");
        report.setReportedByName("Alex Popescu");
        report.setReportedByPhone("0722222223");
        report.setStatus(MissingPersonStatus.MISSING);
        report.setNotes("Report for require test");

        MissingPersonReport savedReport = missingPersonReportService.registerMissingPersonReport(report);

        MissingPersonReport retrievedReport = missingPersonReportService.requireMissingPersonReportById(savedReport.getReportId());

        assertEquals(savedReport.getReportId(), retrievedReport.getReportId());
    }

    @Test
    void listAllMissingPersonReportsShouldReturnReports() throws SQLException{
        int initialSize = missingPersonReportService.listAllMissingPersonReports().size();

        Person person = new Person(
                "Eduard",
                "Marin",
                LocalDate.of(1987, 8, 21),
                "0711111124",
                "eduard.missing@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 24",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Weather Missing Event",
                EmergencyType.EXTREME_WEATHER,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test emergency event",
                260
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        MissingPersonReport report = new MissingPersonReport();
        report.setPersonId(savedPerson.getPersonId());
        report.setEventId(savedEmergencyEvent.getEventId());
        report.setLastSeenDatetime(LocalDateTime.now().minusHours(3));
        report.setLastKnownLocation("Cluj-Napoca");
        report.setReportedByName("Ana Marin");
        report.setReportedByPhone("0722222224");
        report.setStatus(MissingPersonStatus.MISSING);
        report.setNotes("Report for list test");

        missingPersonReportService.registerMissingPersonReport(report);

        List<MissingPersonReport> reports = missingPersonReportService.listAllMissingPersonReports();

        assertEquals(initialSize + 1, reports.size());
    }

    @Test
    void updateMissingPersonReportShouldResolveReport() throws SQLException{
        Person person = new Person(
                "Florin",
                "Matei",
                LocalDate.of(1991, 6, 12),
                "0711111125",
                "florin.missing@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 25",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Flood Resolution Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test emergency event",
                350
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        MissingPersonReport report = new MissingPersonReport();
        report.setPersonId(savedPerson.getPersonId());
        report.setEventId(savedEmergencyEvent.getEventId());
        report.setLastSeenDatetime(LocalDateTime.now().minusHours(6));
        report.setLastKnownLocation("Cluj-Napoca");
        report.setReportedByName("Laura Matei");
        report.setReportedByPhone("0722222225");
        report.setStatus(MissingPersonStatus.MISSING);
        report.setNotes("Initial missing report");

        MissingPersonReport savedReport = missingPersonReportService.registerMissingPersonReport(report);

        MissingPersonReport storedReport = missingPersonReportService.requireMissingPersonReportById(savedReport.getReportId());

        storedReport.setStatus(MissingPersonStatus.LOCATED_SAFE);
        storedReport.setResolvedDatetime(storedReport.getReportedDatetime().plusMinutes(1));
        storedReport.setNotes("Person located safely");

        missingPersonReportService.updateMissingPersonReport(storedReport);

        MissingPersonReport result = missingPersonReportService.requireMissingPersonReportById(savedReport.getReportId());

        assertEquals(MissingPersonStatus.LOCATED_SAFE, result.getStatus());
        assertNotNull(result.getResolvedDatetime());
        assertEquals("Person located safely", result.getNotes());
    }

    @Test
    void updateMissingPersonReportShouldRejectLastSeenAfterReportedDatetime() throws SQLException{
        Person person = new Person(
                "Gabriela",
                "Ionescu",
                LocalDate.of(1994, 9, 4),
                "0711111126",
                "gabriela.missing@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 26",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Fire Datetime Event",
                EmergencyType.FIRE,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test emergency event",
                240
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        MissingPersonReport report = new MissingPersonReport();
        report.setPersonId(savedPerson.getPersonId());
        report.setEventId(savedEmergencyEvent.getEventId());
        report.setLastSeenDatetime(LocalDateTime.now().minusHours(3));
        report.setLastKnownLocation("Cluj-Napoca");
        report.setReportedByName("Adrian Ionescu");
        report.setReportedByPhone("0722222226");
        report.setStatus(MissingPersonStatus.MISSING);
        report.setNotes("Report for invalid last seen test");

        MissingPersonReport savedReport = missingPersonReportService.registerMissingPersonReport(report);

        MissingPersonReport storedReport = missingPersonReportService.requireMissingPersonReportById(savedReport.getReportId());

        storedReport.setLastSeenDatetime(storedReport.getReportedDatetime().plusMinutes(1));

        try {
            missingPersonReportService.updateMissingPersonReport(storedReport);
            fail("Expected IllegalArgumentException for invalid last seen datetime");
        } catch (IllegalArgumentException e) {
            assertEquals("Last seen datetime cannot be after reported datetime", e.getMessage());
        }
    }

    @Test
    void updateMissingPersonReportShouldRejectResolvedDatetimeBeforeReportedDatetime() throws SQLException{
        Person person = new Person(
                "Horia",
                "Dumitru",
                LocalDate.of(1989, 3, 27),
                "0711111127",
                "horia.missing@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 27",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Earthquake Resolution Event",
                EmergencyType.EARTHQUAKE,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test emergency event",
                320
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        MissingPersonReport report = new MissingPersonReport();
        report.setPersonId(savedPerson.getPersonId());
        report.setEventId(savedEmergencyEvent.getEventId());
        report.setLastSeenDatetime(LocalDateTime.now().minusHours(4));
        report.setLastKnownLocation("Cluj-Napoca");
        report.setReportedByName("Elena Dumitru");
        report.setReportedByPhone("0722222227");
        report.setStatus(MissingPersonStatus.MISSING);
        report.setNotes("Report for invalid resolution test");

        MissingPersonReport savedReport = missingPersonReportService.registerMissingPersonReport(report);

        MissingPersonReport storedReport = missingPersonReportService.requireMissingPersonReportById(savedReport.getReportId());

        storedReport.setStatus(MissingPersonStatus.LOCATED_SAFE);
        storedReport.setResolvedDatetime(storedReport.getReportedDatetime().minusMinutes(1));

        try {
            missingPersonReportService.updateMissingPersonReport(storedReport);
            fail("Expected IllegalArgumentException for invalid resolved datetime");
        } catch (IllegalArgumentException e) {
            assertEquals("Resolved datetime cannot be before reported datetime", e.getMessage());
        }
    }

    @Test
    void deleteMissingPersonReportShouldRemoveReport() throws SQLException{
        Person person = new Person(
                "Irina",
                "Moldovan",
                LocalDate.of(1992, 12, 15),
                "0711111128",
                "irina.missing@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 28",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Landslide Delete Event",
                EmergencyType.LANDSLIDE,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test emergency event",
                190
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        MissingPersonReport report = new MissingPersonReport();
        report.setPersonId(savedPerson.getPersonId());
        report.setEventId(savedEmergencyEvent.getEventId());
        report.setLastSeenDatetime(LocalDateTime.now().minusHours(5));
        report.setLastKnownLocation("Cluj-Napoca");
        report.setReportedByName("Victor Moldovan");
        report.setReportedByPhone("0722222228");
        report.setStatus(MissingPersonStatus.MISSING);
        report.setNotes("Report for delete test");

        MissingPersonReport savedReport = missingPersonReportService.registerMissingPersonReport(report);

        missingPersonReportService.deleteMissingPersonReport(savedReport.getReportId());

        try {
            missingPersonReportService.requireMissingPersonReportById(savedReport.getReportId());
            fail("Expected IllegalArgumentException for deleted missing person report");
        } catch (IllegalArgumentException e) {
            assertEquals("Missing person report with ID " + savedReport.getReportId() + " does not exist", e.getMessage());
        }
    }
}
