package model;

import java.time.LocalDate;

public class Person {
    private Integer personId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    private String phoneNumber;
    private String email;

    private String homeCountry;
    private String homeCounty;
    private String homeLocality;
    private String homeAddress;

    private String notes;

    public Person() {
    }

    public Person(String firstName, String lastName, LocalDate birthDate, String phoneNumber, String email, String homeCountry, String homeCounty, String homeLocality, String homeAddress, String notes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.homeCountry = homeCountry;
        this.homeCounty = homeCounty;
        this.homeLocality = homeLocality;
        this.homeAddress = homeAddress;
        this.notes = notes;
    }

    public Person(Integer personId, String firstName, String lastName, LocalDate birthDate, String phoneNumber, String email, String homeCountry, String homeCounty, String homeLocality, String homeAddress, String notes) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.homeCountry = homeCountry;
        this.homeCounty = homeCounty;
        this.homeLocality = homeLocality;
        this.homeAddress = homeAddress;
        this.notes = notes;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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

    public String getHomeCountry() {
        return homeCountry;
    }

    public void setHomeCountry(String homeCountry) {
        this.homeCountry = homeCountry;
    }

    public String getHomeCounty() {
        return homeCounty;
    }

    public void setHomeCounty(String homeCounty) {
        this.homeCounty = homeCounty;
    }

    public String getHomeLocality() {
        return homeLocality;
    }

    public void setHomeLocality(String homeLocality) {
        this.homeLocality = homeLocality;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Person{" + "personId=" + personId + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", birthDate=" + birthDate + ", phoneNumber='" + phoneNumber + '\'' + ", email='" + email + '\'' + ", homeCountry='" + homeCountry + '\'' + ", homeCounty='" + homeCounty + '\'' + ", homeLocality='" + homeLocality + '\'' + ", homeAddress='" + homeAddress + '\'' + ", notes='" + notes + '\'' + '}';
    }
}
