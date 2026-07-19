package model;

import model.enums.EmergencyStatus;
import model.enums.EmergencyType;

import java.time.LocalDateTime;

public class EmergencyEvent {
    private Integer eventId;
    private String name;
    private EmergencyType type;

    private String country;
    private String county;
    private String locality;
    private String affectedArea;

    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;

    private EmergencyStatus status;
    private String description;
    private Integer estimatedAffectedPeople;

    //constructor fara parametri - pentru construirea obiectului treptat
    public EmergencyEvent() {
    }

    //constructor fara ID - pentru crearea unui eveniment de urgenta nou
    public EmergencyEvent(String name, EmergencyType type, String country, String county, String locality, String affectedArea, LocalDateTime startDatetime, LocalDateTime endDatetime, EmergencyStatus status, String description, Integer estimatedAffectedPeople) {
        this.name = name;
        this.type = type;
        this.country = country;
        this.county = county;
        this.locality = locality;
        this.affectedArea = affectedArea;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.status = status;
        this.description = description;
        this.estimatedAffectedPeople = estimatedAffectedPeople;
    }

    //constructor cu toate campurile - pentru citire
    public EmergencyEvent(Integer eventId, String name, EmergencyType type, String country, String county, String locality, String affectedArea, LocalDateTime startDatetime, LocalDateTime endDatetime, EmergencyStatus status, String description, Integer estimatedAffectedPeople) {
        this.eventId = eventId;
        this.name = name;
        this.type = type;
        this.country = country;
        this.county = county;
        this.locality = locality;
        this.affectedArea = affectedArea;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.status = status;
        this.description = description;
        this.estimatedAffectedPeople = estimatedAffectedPeople;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmergencyType getType() {
        return type;
    }

    public void setType(EmergencyType type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getAffectedArea() {
        return affectedArea;
    }

    public void setAffectedArea(String affectedArea) {
        this.affectedArea = affectedArea;
    }

    public LocalDateTime getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(LocalDateTime startDatetime) {
        this.startDatetime = startDatetime;
    }

    public LocalDateTime getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(LocalDateTime endDatetime) {
        this.endDatetime = endDatetime;
    }

    public EmergencyStatus getStatus() {
        return status;
    }

    public void setStatus(EmergencyStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEstimatedAffectedPeople() {
        return estimatedAffectedPeople;
    }

    public void setEstimatedAffectedPeople(Integer estimatedAffectedPeople) {
        this.estimatedAffectedPeople = estimatedAffectedPeople;
    }

    @Override
    public String toString() {
        return "EmergencyEvent{" +
                "eventId=" + eventId +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", country='" + country + '\'' +
                ", county='" + county + '\'' +
                ", locality='" + locality + '\'' +
                ", affectedArea='" + affectedArea + '\'' +
                ", startDatetime=" + startDatetime +
                ", endDatetime=" + endDatetime +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", estimatedAffectedPeople=" + estimatedAffectedPeople +
                '}';
    }
}
