package model;

import model.enums.MissingPersonStatus;

import java.time.LocalDateTime;

public class MissingPersonReport {
    private Integer reportId;
    private Integer personId;
    private Integer eventId;

    private LocalDateTime reportedDatetime;
    private LocalDateTime lastSeenDatetime;
    private String lastKnownLocation;

    private String reportedByName;
    private String reportedByPhone;

    private MissingPersonStatus status = MissingPersonStatus.MISSING;

    private LocalDateTime resolvedDatetime;

    private String notes;

    public MissingPersonReport() {
    }

    public MissingPersonReport(Integer personId, Integer eventId, LocalDateTime lastSeenDatetime, String lastKnownLocation, String reportedByName, String reportedByPhone, String notes) {
        this.personId = personId;
        this.eventId = eventId;
        this.lastSeenDatetime = lastSeenDatetime;
        this.lastKnownLocation = lastKnownLocation;
        this.reportedByName = reportedByName;
        this.reportedByPhone = reportedByPhone;
        this.notes = notes;
    }

    public MissingPersonReport(Integer reportId, Integer personId, Integer eventId, LocalDateTime reportedDatetime, LocalDateTime lastSeenDatetime, String lastKnownLocation, String reportedByName, String reportedByPhone, MissingPersonStatus status, LocalDateTime resolvedDatetime, String notes) {
        this.reportId = reportId;
        this.personId = personId;
        this.eventId = eventId;
        this.reportedDatetime = reportedDatetime;
        this.lastSeenDatetime = lastSeenDatetime;
        this.lastKnownLocation = lastKnownLocation;
        this.reportedByName = reportedByName;
        this.reportedByPhone = reportedByPhone;
        this.status = status;
        this.resolvedDatetime = resolvedDatetime;
        this.notes = notes;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getReportedDatetime() {
        return reportedDatetime;
    }

    public void setReportedDatetime(LocalDateTime reportedDatetime) {
        this.reportedDatetime = reportedDatetime;
    }

    public LocalDateTime getLastSeenDatetime() {
        return lastSeenDatetime;
    }

    public void setLastSeenDatetime(LocalDateTime lastSeenDatetime) {
        this.lastSeenDatetime = lastSeenDatetime;
    }

    public String getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(String lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    public String getReportedByName() {
        return reportedByName;
    }

    public void setReportedByName(String reportedByName) {
        this.reportedByName = reportedByName;
    }

    public String getReportedByPhone() {
        return reportedByPhone;
    }

    public void setReportedByPhone(String reportedByPhone) {
        this.reportedByPhone = reportedByPhone;
    }

    public MissingPersonStatus getStatus() {
        return status;
    }

    public void setStatus(MissingPersonStatus status) {
        this.status = status;
    }

    public LocalDateTime getResolvedDatetime() {
        return resolvedDatetime;
    }

    public void setResolvedDatetime(LocalDateTime resolvedDatetime) {
        this.resolvedDatetime = resolvedDatetime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "MissingPersonReport{" +
                "reportId=" + reportId +
                ", personId=" + personId +
                ", eventId=" + eventId +
                ", reportedDatetime=" + reportedDatetime +
                ", lastSeenDatetime=" + lastSeenDatetime +
                ", lastKnownLocation='" + lastKnownLocation + '\'' +
                ", reportedByName='" + reportedByName + '\'' +
                ", reportedByPhone='" + reportedByPhone + '\'' +
                ", status=" + status +
                ", resolvedDatetime=" + resolvedDatetime +
                ", notes='" + notes + '\'' +
                '}';
    }
}
