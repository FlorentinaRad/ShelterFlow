package service;

import model.EmergencyEvent;
import model.enums.EmergencyStatus;
import repository.EmergencyEventRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EmergencyEventService {
    private final EmergencyEventRepository emergencyEventRepository;

    public EmergencyEventService(EmergencyEventRepository emergencyEventRepository) {
        this.emergencyEventRepository = emergencyEventRepository;
    }

    private void validateEmergencyEvent(EmergencyEvent emergencyEvent) {
        if (emergencyEvent == null) {
            throw new IllegalArgumentException("Emergency event cannot be null");
        }

        if(emergencyEvent.getName() == null || emergencyEvent.getName().isBlank()) {
            throw new IllegalArgumentException("Emergency event name cannot be null or empty");
        }

        if(emergencyEvent.getType() == null) {
            throw new IllegalArgumentException("Emergency event type cannot be null");
        }

        if(emergencyEvent.getCountry() == null || emergencyEvent.getCountry().isBlank()) {
            throw new IllegalArgumentException("Emergency event country cannot be null or empty");
        }

        if(emergencyEvent.getCounty() == null || emergencyEvent.getCounty().isBlank()) {
            throw new IllegalArgumentException("Emergency event county cannot be null or empty");
        }

        if(emergencyEvent.getStartDatetime() == null) {
            throw new IllegalArgumentException("Emergency event start datetime cannot be null");
        }

        if(emergencyEvent.getEndDatetime() != null && emergencyEvent.getEndDatetime().isBefore(emergencyEvent.getStartDatetime())) {
            throw new IllegalArgumentException("Emergency event end datetime cannot be before start datetime");
        }

        if(emergencyEvent.getStatus() == null) {
            throw new IllegalArgumentException("Emergency event status cannot be null");
        }

        if(emergencyEvent.getStatus() == EmergencyStatus.CLOSED && emergencyEvent.getEndDatetime() == null) {
            throw new IllegalArgumentException("Closed emergency event must have an end datetime");
        }

        if(emergencyEvent.getStatus() == EmergencyStatus.ACTIVE && emergencyEvent.getEndDatetime() != null) {
            throw new IllegalArgumentException("Active emergency event cannot have an end datetime");
        }

        if(emergencyEvent.getEstimatedAffectedPeople()!= null && emergencyEvent.getEstimatedAffectedPeople() < 0) {
            throw new IllegalArgumentException("Estimated affected people cannot be negative");
        }
    }

    public EmergencyEvent registerEmergencyEvent(EmergencyEvent emergencyEvent) throws SQLException {
        validateEmergencyEvent(emergencyEvent);
        return emergencyEventRepository.save(emergencyEvent);
    }

    public EmergencyEvent requireEmergencyEventById(Integer emergencyEventId) throws SQLException {
        if (emergencyEventId == null || emergencyEventId <= 0) {
            throw new IllegalArgumentException("Emergency event ID must be a positive integer");
        }

        Optional<EmergencyEvent> emergencyEvent = emergencyEventRepository.findById(emergencyEventId);

        if (emergencyEvent.isEmpty()) {
            throw new IllegalArgumentException("Emergency event with ID " + emergencyEventId + " does not exist");
        }

        return emergencyEvent.get();
    }

    public List<EmergencyEvent> listAllEmergencyEvents() throws SQLException {
        return emergencyEventRepository.findAll();
    }

    public EmergencyEvent updateEmergencyEvent(EmergencyEvent emergencyEvent) throws SQLException {
        validateEmergencyEvent(emergencyEvent);

        if (emergencyEvent.getEventId() == null || emergencyEvent.getEventId() <= 0) {
            throw new IllegalArgumentException("Emergency event ID must be a positive integer");
        }

        requireEmergencyEventById(emergencyEvent.getEventId());

        boolean updated = emergencyEventRepository.update(emergencyEvent);
        if (!updated) {
            throw new SQLException("Failed to update emergency event with ID " + emergencyEvent.getEventId());
        }

        return emergencyEvent;
    }

    public void deleteEmergencyEvent(Integer eventId) throws SQLException {
        requireEmergencyEventById(eventId);

        boolean deleted = emergencyEventRepository.delete(eventId);
        if (!deleted) {
            throw new SQLException("Failed to delete emergency event with ID " + eventId);
        }
    }

}
