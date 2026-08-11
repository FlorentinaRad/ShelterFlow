package service;

import model.EmergencyEvent;
import model.enums.EmergencyStatus;
import model.enums.EmergencyType;
import org.junit.jupiter.api.Test;
import repository.EmergencyEventRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EmergencyEventServiceTest {
    private final EmergencyEventRepository emergencyEventRepository = new EmergencyEventRepository();
    private final EmergencyEventService emergencyEventService = new EmergencyEventService(emergencyEventRepository);

    @Test
    void registerEmergencyEventShouldSaveValidEvent() throws SQLException {
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
        assertNotNull(savedEmergencyEvent.getEventId());
    }

    @Test
    void registerEmergencyEventShouldRejectNullEvent() throws SQLException {
        try {
            emergencyEventService.registerEmergencyEvent(null);
            fail("Expected IllegalArgumentException for null emergency event");
        } catch (IllegalArgumentException e) {
            assertEquals("Emergency event cannot be null", e.getMessage());
        }
    }

    @Test
    void registerEmergencyEventShouldRejectInvalidDates() throws SQLException{
        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Emergency Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj-Napoca metropolitan area",
                LocalDateTime.now(),
                LocalDateTime.now().minusDays(1),
                EmergencyStatus.CLOSED,
                "Invalid dates",
                1000
        );
        try {
            emergencyEventService.registerEmergencyEvent(emergencyEvent);
            fail("Expected IllegalArgumentException for invalid dates");
        } catch (IllegalArgumentException e) {
            assertEquals("Emergency event end datetime cannot be before start datetime", e.getMessage());
        }
    }

    @Test
    void registerEmergencyEventShouldRejectActiveEventWithEndDatetime() throws SQLException {
        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Emergency Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj-Napoca metropolitan area",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                EmergencyStatus.ACTIVE,
                "Invalid active event",
                1000
        );
        try {
            emergencyEventService.registerEmergencyEvent(emergencyEvent);
            fail("Expected IllegalArgumentException for active event with end datetime");
        } catch (IllegalArgumentException e) {
            assertEquals("Active emergency event cannot have an end datetime", e.getMessage());
        }
    }

    @Test
    void registerEmergencyEventShouldRejectClosedEventWithoutEndDate() throws SQLException {
        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Emergency Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj-Napoca metropolitan area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.CLOSED,
                "Invalid closed event",
                1000
        );
        try {
            emergencyEventService.registerEmergencyEvent(emergencyEvent);
            fail("Expected IllegalArgumentException for closed event without end date");
        } catch (IllegalArgumentException e) {
            assertEquals("Closed emergency event must have an end datetime", e.getMessage());
        }
    }

    @Test
    void requireEmergencyEventByIdShouldReturnEventWhenExists() throws SQLException {
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
        EmergencyEvent retrievedEmergencyEvent = emergencyEventService.requireEmergencyEventById(savedEmergencyEvent.getEventId());
        assertEquals(savedEmergencyEvent.getEventId(),  retrievedEmergencyEvent.getEventId());  }

    @Test
    void updateEmergencyEventShouldModifyEvent() throws SQLException {
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
        savedEmergencyEvent.setDescription("Updated description");
        emergencyEventService.updateEmergencyEvent(savedEmergencyEvent);
        EmergencyEvent result = emergencyEventService.requireEmergencyEventById(savedEmergencyEvent.getEventId());
        assertEquals("Updated description", result.getDescription());
    }

    @Test
    void deleteEmergencyEventShouldRemoveEvent() throws SQLException {
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
        emergencyEventService.deleteEmergencyEvent(savedEmergencyEvent.getEventId());
        try {
            emergencyEventService.requireEmergencyEventById(savedEmergencyEvent.getEventId());
            fail("Expected IllegalArgumentException for deleted emergency event");
        } catch (IllegalArgumentException e) {
            assertEquals("Emergency event with ID " + savedEmergencyEvent.getEventId() + " does not exist", e.getMessage());
        }
    }

}
