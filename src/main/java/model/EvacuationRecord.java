package model;

import java.time.LocalDateTime;

public class EvacuationRecord {
    private Integer evacuationId;
    private Integer personId;
    private Integer eventId;

    private LocalDateTime registrationDatetime;
    private String evacuationLocation;

    private boolean needsAssistance;
    private String assistanceDetails;

    private String notes;

    public EvacuationRecord() {
    }

    public EvacuationRecord(Integer personId, Integer eventId, String evacuationLocation, boolean needsAssistance, String assistanceDetails, String notes) {
        this.personId = personId;
        this.eventId = eventId;
        this.evacuationLocation = evacuationLocation;
        this.needsAssistance = needsAssistance;
        this.assistanceDetails = assistanceDetails;
        this.notes = notes;
    }

    public EvacuationRecord(Integer evacuationId, Integer personId, Integer eventId, LocalDateTime registrationDatetime, String evacuationLocation, boolean needsAssistance, String assistanceDetails, String notes) {
        this.evacuationId = evacuationId;
        this.personId = personId;
        this.eventId = eventId;
        this.registrationDatetime = registrationDatetime;
        this.evacuationLocation = evacuationLocation;
        this.needsAssistance = needsAssistance;
        this.assistanceDetails = assistanceDetails;
        this.notes = notes;
    }

    public Integer getEvacuationId() {
        return evacuationId;
    }

    public void setEvacuationId(Integer evacuationId) {
        this.evacuationId = evacuationId;
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

    public LocalDateTime getRegistrationDatetime() {
        return registrationDatetime;
    }

    public void setRegistrationDatetime(LocalDateTime registrationDatetime) {
        this.registrationDatetime = registrationDatetime;
    }

    public String getEvacuationLocation() {
        return evacuationLocation;
    }

    public void setEvacuationLocation(String evacuationLocation) {
        this.evacuationLocation = evacuationLocation;
    }

    public boolean isNeedsAssistance() {
        return needsAssistance;
    }

    public void setNeedsAssistance(boolean needsAssistance) {
        this.needsAssistance = needsAssistance;
    }

    public String getAssistanceDetails() {
        return assistanceDetails;
    }

    public void setAssistanceDetails(String assistanceDetails) {
        this.assistanceDetails = assistanceDetails;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "EvacuationRecord{" +
                "evacuationId=" + evacuationId +
                ", personId=" + personId +
                ", eventId=" + eventId +
                ", registrationDatetime=" + registrationDatetime +
                ", evacuationLocation='" + evacuationLocation + '\'' +
                ", needsAssistance=" + needsAssistance +
                ", assistanceDetails='" + assistanceDetails + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
