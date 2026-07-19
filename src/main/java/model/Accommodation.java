package model;

import java.time.LocalDateTime;

public class Accommodation {
    private Integer accommodationId;
    private Integer evacuationId;
    private Integer shelterId;

    private LocalDateTime checkInDatetime;
    private LocalDateTime checkOutDatetime;

    private String notes;

    public Accommodation() {
    }

    //pentru o cazare noua SQL genereaza ID-ul si momentul check-in-ului,iar check-out initial null
    public Accommodation(Integer evacuationId, Integer shelterId, String notes) {
        this.evacuationId = evacuationId;
        this.shelterId = shelterId;
        this.notes = notes;
    }

    public Accommodation(Integer accommodationId, Integer evacuationId, Integer shelterId, LocalDateTime checkInDatetime, LocalDateTime checkOutDatetime, String notes) {
        this.accommodationId = accommodationId;
        this.evacuationId = evacuationId;
        this.shelterId = shelterId;
        this.checkInDatetime = checkInDatetime;
        this.checkOutDatetime = checkOutDatetime;
        this.notes = notes;
    }

    public Integer getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Integer accommodationId) {
        this.accommodationId = accommodationId;
    }

    public Integer getEvacuationId() {
        return evacuationId;
    }

    public void setEvacuationId(Integer evacuationId) {
        this.evacuationId = evacuationId;
    }

    public Integer getShelterId() {
        return shelterId;
    }

    public void setShelterId(Integer shelterId) {
        this.shelterId = shelterId;
    }

    public LocalDateTime getCheckInDatetime() {
        return checkInDatetime;
    }

    public void setCheckInDatetime(LocalDateTime checkInDatetime) {
        this.checkInDatetime = checkInDatetime;
    }

    public LocalDateTime getCheckOutDatetime() {
        return checkOutDatetime;
    }

    public void setCheckOutDatetime(LocalDateTime checkOutDatetime) {
        this.checkOutDatetime = checkOutDatetime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Accommodation{" +
                "accommodationId=" + accommodationId +
                ", evacuationId=" + evacuationId +
                ", shelterId=" + shelterId +
                ", checkInDatetime=" + checkInDatetime +
                ", checkOutDatetime=" + checkOutDatetime +
                ", notes='" + notes + '\'' +
                '}';
    }
}
