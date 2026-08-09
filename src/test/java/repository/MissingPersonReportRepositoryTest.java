package repository;

import model.EmergencyEvent;
import model.MissingPersonReport;
import model.Person;
import model.enums.EmergencyStatus;
import model.enums.EmergencyType;
import model.enums.MissingPersonStatus;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MissingPersonReportRepositoryTest {

    private final MissingPersonReportRepository repository =
            new MissingPersonReportRepository();

    private final PersonRepository personRepository =
            new PersonRepository();

    private final EmergencyEventRepository eventRepository =
            new EmergencyEventRepository();


    @Test
    void saveShouldGenerateId() throws SQLException {
        MissingPersonReport report =
                createMissingPersonReport();

        MissingPersonReport savedReport =
                repository.save(report);

        assertNotNull(savedReport.getReportId());
    }


    @Test
    void findByIdShouldReturnMissingPersonReportWhenExists()
            throws SQLException {

        MissingPersonReport savedReport =
                repository.save(createMissingPersonReport());

        Optional<MissingPersonReport> result =
                repository.findById(savedReport.getReportId());

        assertTrue(result.isPresent());

        assertEquals(
                savedReport.getReportId(),
                result.get().getReportId()
        );

        // reported_datetime este generat automat de SQL Server
        assertNotNull(result.get().getReportedDatetime());

        assertEquals(
                MissingPersonStatus.MISSING,
                result.get().getStatus()
        );
    }


    @Test
    void findByIdShouldReturnEmptyWhenReportDoesNotExist()
            throws SQLException {

        Optional<MissingPersonReport> result =
                repository.findById(99999999);

        assertTrue(result.isEmpty());
    }


    @Test
    void findAllShouldReturnMissingPersonReports() throws SQLException {
        MissingPersonReport savedReport =
                repository.save(createMissingPersonReport());

        List<MissingPersonReport> reports =
                repository.findAll();

        assertFalse(reports.isEmpty());

        assertTrue(
                reports.stream().anyMatch(
                        report ->
                                report.getReportId()
                                        .equals(savedReport.getReportId())
                )
        );
    }


    @Test
    void updateShouldModifyMissingPersonReport() throws SQLException {
        MissingPersonReport savedReport =
                repository.save(createMissingPersonReport());

        // Citim raportul complet din baza
        MissingPersonReport report =
                repository.findById(savedReport.getReportId())
                        .orElseThrow();

        report.setStatus(MissingPersonStatus.LOCATED_SAFE);
        report.setResolvedDatetime(LocalDateTime.now());
        report.setNotes("Person located safely");

        boolean updated =
                repository.update(report);

        Optional<MissingPersonReport> result =
                repository.findById(report.getReportId());

        assertTrue(updated);
        assertTrue(result.isPresent());

        assertEquals(
                MissingPersonStatus.LOCATED_SAFE,
                result.get().getStatus()
        );

        assertNotNull(result.get().getResolvedDatetime());

        assertEquals(
                "Person located safely",
                result.get().getNotes()
        );
    }


    @Test
    void deleteShouldRemoveMissingPersonReport() throws SQLException {
        MissingPersonReport savedReport =
                repository.save(createMissingPersonReport());

        boolean deleted =
                repository.delete(savedReport.getReportId());

        Optional<MissingPersonReport> result =
                repository.findById(savedReport.getReportId());

        assertTrue(deleted);
        assertTrue(result.isEmpty());
    }


    // Creeaza Person si EmergencyEvent necesare pentru foreign keys
    private MissingPersonReport createMissingPersonReport()
            throws SQLException {

        Person person = new Person(
                "Test",
                "Missing",
                LocalDate.of(1998, 4, 15),
                "0700000000",
                "missing.person@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                "Test person for missing report"
        );

        Person savedPerson =
                personRepository.save(person);


        EmergencyEvent event = new EmergencyEvent(
                "Test Missing Person Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj-Napoca metropolitan area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test event for missing person report",
                1000
        );

        EmergencyEvent savedEvent =
                eventRepository.save(event);


        return new MissingPersonReport(
                savedPerson.getPersonId(),
                savedEvent.getEventId(),
                LocalDateTime.now().minusHours(2),
                "Cluj-Napoca",
                "Test Reporter",
                "0711111111",
                "Test missing person report"
        );
    }
}
