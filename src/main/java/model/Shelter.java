package model;

import model.enums.ShelterStatus;

public class Shelter {
    private Integer shelterId;
    private String name;

    private String country;
    private String county;
    private String locality;
    private String address;

    private int totalCapacity;

    private ShelterStatus status;

    private String phoneNumber;
    private String email;

    private boolean accessRamp;
    private boolean childrenArea;
    private boolean medicalRoom;
    private boolean acceptsPets;

    private String publicInformation;

    public Shelter() {
    }

    public Shelter(String name, String country, String county, String locality, String address, int totalCapacity, ShelterStatus status, String phoneNumber, String email, boolean accessRamp, boolean childrenArea, boolean medicalRoom, boolean acceptsPets, String publicInformation) {
        this.name = name;
        this.country = country;
        this.county = county;
        this.locality = locality;
        this.address = address;
        this.totalCapacity = totalCapacity;
        this.status = status;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.accessRamp = accessRamp;
        this.childrenArea = childrenArea;
        this.medicalRoom = medicalRoom;
        this.acceptsPets = acceptsPets;
        this.publicInformation = publicInformation;
    }

    public Shelter(Integer shelterId, String name, String country, String county, String locality, String address, int totalCapacity, ShelterStatus status, String phoneNumber, String email, boolean accessRamp, boolean childrenArea, boolean medicalRoom, boolean acceptsPets, String publicInformation) {
        this.shelterId = shelterId;
        this.name = name;
        this.country = country;
        this.county = county;
        this.locality = locality;
        this.address = address;
        this.totalCapacity = totalCapacity;
        this.status = status;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.accessRamp = accessRamp;
        this.childrenArea = childrenArea;
        this.medicalRoom = medicalRoom;
        this.acceptsPets = acceptsPets;
        this.publicInformation = publicInformation;
    }

    public Integer getShelterId() {
        return shelterId;
    }

    public void setShelterId(Integer shelterId) {
        this.shelterId = shelterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public ShelterStatus getStatus() {
        return status;
    }

    public void setStatus(ShelterStatus status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAccessRamp() {
        return accessRamp;
    }

    public void setAccessRamp(boolean accessRamp) {
        this.accessRamp = accessRamp;
    }

    public boolean isChildrenArea() {
        return childrenArea;
    }

    public void setChildrenArea(boolean childrenArea) {
        this.childrenArea = childrenArea;
    }

    public boolean isMedicalRoom() {
        return medicalRoom;
    }

    public void setMedicalRoom(boolean medicalRoom) {
        this.medicalRoom = medicalRoom;
    }

    public boolean isAcceptsPets() {
        return acceptsPets;
    }

    public void setAcceptsPets(boolean acceptsPets) {
        this.acceptsPets = acceptsPets;
    }

    public String getPublicInformation() {
        return publicInformation;
    }

    public void setPublicInformation(String publicInformation) {
        this.publicInformation = publicInformation;
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "shelterId=" + shelterId +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", county='" + county + '\'' +
                ", locality='" + locality + '\'' +
                ", address='" + address + '\'' +
                ", totalCapacity=" + totalCapacity +
                ", status=" + status +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", accessRamp=" + accessRamp +
                ", childrenArea=" + childrenArea +
                ", medicalRoom=" + medicalRoom +
                ", acceptsPets=" + acceptsPets +
                ", publicInformation='" + publicInformation + '\'' +
                '}';
    }
}
