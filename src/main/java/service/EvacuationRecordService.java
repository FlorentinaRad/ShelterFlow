package service;

import model.EvacuationRecord;
import repository.EvacuationRecordRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EvacuationRecordService {
    private final EvacuationRecordRepository evacuationRecordRepository;
    private final PersonService personService;
    private final EmergencyEventService emergencyEventService;

    public EvacuationRecordService(EvacuationRecordRepository evacuationRecordRepository, PersonService personService, EmergencyEventService emergencyEventService) {
        this.evacuationRecordRepository = evacuationRecordRepository;
        this.personService = personService;
        this.emergencyEventService = emergencyEventService;
    }

    private void validateEvacuationRecord(EvacuationRecord evacuationRecord) throws SQLException {
        if (evacuationRecord == null) {
            throw new IllegalArgumentException("Evacuation record cannot be null");
        }

        if (evacuationRecord.getPersonId() == null || evacuationRecord.getPersonId() <= 0) {
            throw new IllegalArgumentException("Person ID must be a positive integer");
        }

        if (evacuationRecord.getEventId() == null || evacuationRecord.getEventId() <= 0) {
            throw new IllegalArgumentException("Emergency event ID must be a positive integer");
        }

        personService.requirePersonById(evacuationRecord.getPersonId());
        emergencyEventService.requireEmergencyEventById(evacuationRecord.getEventId());
    }

    public EvacuationRecord registerEvacuationRecord(EvacuationRecord evacuationRecord) throws SQLException {
        validateEvacuationRecord(evacuationRecord);

        Optional<EvacuationRecord> existingRecord = evacuationRecordRepository.findByPersonIdAndEventId(evacuationRecord.getPersonId(), evacuationRecord.getEventId());
        if (existingRecord.isPresent()) {
            throw new IllegalArgumentException("Person is already registered for this emergency event");
        }

        return evacuationRecordRepository.save(evacuationRecord);
    }

    public EvacuationRecord requireEvacuationRecordById(Integer evacuationRecordId) throws SQLException{
        if(evacuationRecordId == null || evacuationRecordId <= 0) {
            throw new IllegalArgumentException("Evacuation record ID must be a positive integer");
        }

        Optional<EvacuationRecord> evacuationRecord = evacuationRecordRepository.findById(evacuationRecordId);
        if(evacuationRecord.isEmpty()) {
            throw new IllegalArgumentException("Evacuation record with ID " + evacuationRecordId + " does not exist");
        }
        return evacuationRecord.get();
    }

    public List<EvacuationRecord> listAllEvacuationRecords() throws SQLException {
        return evacuationRecordRepository.findAll();
    }

    public EvacuationRecord updateEvacuationRecord(EvacuationRecord evacuationRecord) throws SQLException{
        validateEvacuationRecord(evacuationRecord);

       if(evacuationRecord.getEvacuationId() == null || evacuationRecord.getEvacuationId() <= 0) {
           throw new IllegalArgumentException("Evacuation record ID must be a positive integer");
       }

       requireEvacuationRecordById(evacuationRecord.getEvacuationId());

        Optional<EvacuationRecord> existingRecord = evacuationRecordRepository.findByPersonIdAndEventId(evacuationRecord.getPersonId(), evacuationRecord.getEventId());

        if (existingRecord.isPresent() &&
                !existingRecord.get().getEvacuationId()
                        .equals(evacuationRecord.getEvacuationId())) {

            throw new IllegalArgumentException(
                    "Person is already registered for this emergency event"
            );
        }

       boolean updated = evacuationRecordRepository.update(evacuationRecord);
       if(!updated) {
           throw new SQLException("Failed to update evacuation record with ID " + evacuationRecord.getEvacuationId());
       }

       return evacuationRecord;
    }

    public void deleteEvacuationRecord(Integer evacuationRecordId) throws SQLException{
        requireEvacuationRecordById(evacuationRecordId);

        boolean deleted = evacuationRecordRepository.delete(evacuationRecordId);
        if(!deleted) {
            throw new SQLException("Failed to delete evacuation record with ID " + evacuationRecordId);
        }
    }

}
