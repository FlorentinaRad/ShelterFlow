package service;

import model.MissingPersonReport;
import model.enums.MissingPersonStatus;
import repository.MissingPersonReportRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MissingPersonReportService {
    private final MissingPersonReportRepository missingPersonReportRepository;
    private final PersonService personService;
    private final EmergencyEventService emergencyEventService;

    public MissingPersonReportService(MissingPersonReportRepository missingPersonReportRepository, PersonService personService, EmergencyEventService emergencyEventService) {
        this.missingPersonReportRepository = missingPersonReportRepository;
        this.personService = personService;
        this.emergencyEventService = emergencyEventService;
    }

    private void validateMissingPersonReport(MissingPersonReport report) throws SQLException{
        if(report == null) {
            throw new IllegalArgumentException("Missing person report cannot be null");
        }

        if (report.getPersonId() == null || report.getPersonId() <= 0) {
            throw new IllegalArgumentException("Person ID must be a positive integer");
        }
        personService.requirePersonById(report.getPersonId());

        if (report.getEventId() == null || report.getEventId() <= 0) {
            throw new IllegalArgumentException("Emergency event ID must be a positive integer");
        }
        emergencyEventService.requireEmergencyEventById(report.getEventId());

        if(report.getReportedByName() == null || report.getReportedByName().isBlank()) {
            throw new IllegalArgumentException("Reported by name cannot be null or empty");
        }

        if(report.getStatus() == null) {
            throw new IllegalArgumentException("Missing person report status cannot be null");
        }

        if(report.getStatus() == MissingPersonStatus.MISSING && report.getResolvedDatetime() != null) {
            throw new IllegalArgumentException("Missing report cannot have a resolved datetime");
        }

        if(report.getStatus() != MissingPersonStatus.MISSING && report.getResolvedDatetime() == null) {
            throw new IllegalArgumentException("Resolved report must have a resolved datetime");
        }

        if(report.getReportedDatetime() != null && report.getLastSeenDatetime() != null && report.getLastSeenDatetime().isAfter(report.getReportedDatetime())) {
            throw new IllegalArgumentException("Last seen datetime cannot be after reported datetime");
        }

        if(report.getReportedDatetime() != null && report.getResolvedDatetime() != null && report.getResolvedDatetime().isBefore(report.getReportedDatetime())) {
            throw new IllegalArgumentException("Resolved datetime cannot be before reported datetime");
        }
    }

    public MissingPersonReport registerMissingPersonReport(MissingPersonReport report) throws SQLException{
        validateMissingPersonReport(report);

        if (report.getStatus() != MissingPersonStatus.MISSING) {
            throw new IllegalArgumentException("New missing person report must have MISSING status");
        }
        
        Optional<MissingPersonReport> existingReport = missingPersonReportRepository.findByPersonIdAndEventId(report.getPersonId(), report.getEventId());

        if(existingReport.isPresent()){
            throw  new IllegalArgumentException("Person already has a missing person report for this emergency event");
        }
        return missingPersonReportRepository.save(report);
    }

    public MissingPersonReport requireMissingPersonReportById(Integer reportId) throws SQLException{
        if(reportId == null || reportId <= 0) {
            throw new IllegalArgumentException("Missing person report ID must be a positive integer");
        }

        Optional<MissingPersonReport> missingPersonReport = missingPersonReportRepository.findById(reportId);
        if(missingPersonReport.isEmpty()) {
            throw new IllegalArgumentException("Missing person report with ID " + reportId + " does not exist");
        }

        return missingPersonReport.get();
    }

    public List<MissingPersonReport> listAllMissingPersonReports() throws SQLException{
        return missingPersonReportRepository.findAll();
    }

    public MissingPersonReport updateMissingPersonReport(MissingPersonReport report) throws SQLException{
        validateMissingPersonReport(report);

        if(report.getReportId() == null || report.getReportId() <= 0) {
            throw new  IllegalArgumentException("Missing person report ID must be a positive integer");
        }

        MissingPersonReport existingReport = requireMissingPersonReportById(report.getReportId());

        if(!existingReport.getPersonId().equals(report.getPersonId())){
            throw new IllegalArgumentException("Person cannot be changed for an existing missing person report");
        }

        if(!existingReport.getEventId().equals(report.getEventId())){
            throw new IllegalArgumentException("Event cannot be changed for an existing missing person report");
        }

        if(report.getLastSeenDatetime() != null && report.getLastSeenDatetime().isAfter(existingReport.getReportedDatetime())) {
            throw new IllegalArgumentException("Last seen datetime cannot be after reported datetime");
        }

        if(report.getResolvedDatetime() != null && report.getResolvedDatetime().isBefore(existingReport.getReportedDatetime())) {
            throw new IllegalArgumentException("Resolved datetime cannot be before reported datetime");
        }

        boolean updated = missingPersonReportRepository.update(report);
        if(!updated) {
            throw new SQLException("Failed to update missing person report with ID " + report.getReportId());
        }

        return report;
    }

    public void deleteMissingPersonReport (Integer reportId) throws SQLException{
        requireMissingPersonReportById(reportId);

        boolean deleted = missingPersonReportRepository.delete(reportId);
        if (!deleted) {
            throw new SQLException("Failed to delete missing person report with ID " + reportId);
        }
    }

}
