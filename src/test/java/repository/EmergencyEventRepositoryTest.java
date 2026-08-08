package repository;

import model.EmergencyEvent;
import model.Person;
import model.enums.EmergencyStatus;
import model.enums.EmergencyType;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class EmergencyEventRepositoryTest {
    private final EmergencyEventRepository repository = new EmergencyEventRepository();

    @Test
    void saveShouldGenerateId() throws SQLException {
        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj-Napoca metropolitan area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test for save()",
                1000
        );

        EmergencyEvent savedEvent = repository.save(emergencyEvent);
        assertNotNull(savedEvent.getEventId());
    }

    @Test
    void findByIdShouldReturnEmergencyEventWhenExists() throws SQLException {
        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj-Napoca metropolitan area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test for findById()",
                1000
        );
        EmergencyEvent savedEvent = repository.save(emergencyEvent);
        Optional<EmergencyEvent> result = repository.findById(savedEvent.getEventId());
        assertTrue(result.isPresent());
        assertEquals(savedEvent.getEventId(), result.get().getEventId());
    }

    @Test
    void findByIdShouldReturnEmptyWhenEmergencyEventDoesNotExist() throws SQLException {
        Optional<EmergencyEvent> result = repository.findById(99999999);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllShouldReturnEmergencyEvents() throws SQLException {
        List<EmergencyEvent> events = repository.findAll();
        assertFalse(events.isEmpty());
    }

    @Test
    void updateShouldModifyEmergencyEvent() throws SQLException {
        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj-Napoca metropolitan area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test for update()",
                1000
        );
        EmergencyEvent savedEvent = repository.save(emergencyEvent);
        savedEvent.setName("Updated Test Event");
        boolean update = repository.update(savedEvent);

        Optional<EmergencyEvent> result = repository.findById(savedEvent.getEventId());
        assertTrue(result.isPresent());
        assertEquals("Updated Test Event", result.get().getName());
    }

    @Test
    void deleteShouldRemoveEmergencyEvent() throws SQLException {
        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj-Napoca metropolitan area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test for delete()",
                1000
        );

        EmergencyEvent savedEvent = repository.save(emergencyEvent);

        boolean deleted = repository.delete(savedEvent.getEventId());

        Optional<EmergencyEvent> result =
                repository.findById(savedEvent.getEventId());

        assertTrue(deleted);
        assertTrue(result.isEmpty());
    }
}
