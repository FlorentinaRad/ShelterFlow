package service;

import model.*;
import model.enums.EmergencyStatus;
import model.enums.EmergencyType;
import model.enums.ShelterStatus;
import org.junit.jupiter.api.Test;
import repository.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AccommodationServiceTest {
    private final PersonRepository personRepository = new PersonRepository();
    private final EmergencyEventRepository emergencyEventRepository = new EmergencyEventRepository();
    private final EvacuationRecordRepository evacuationRecordRepository = new EvacuationRecordRepository();
    private final ShelterRepository shelterRepository = new ShelterRepository();
    private final AccommodationRepository accommodationRepository = new AccommodationRepository();
    private final PersonService personService = new PersonService(personRepository);
    private final EmergencyEventService emergencyEventService = new EmergencyEventService(emergencyEventRepository);
    private final EvacuationRecordService evacuationRecordService = new EvacuationRecordService(evacuationRecordRepository, personService, emergencyEventService);
    private final ShelterService shelterService = new ShelterService(shelterRepository);
    private final AccommodationService accommodationService = new AccommodationService(accommodationRepository, evacuationRecordService, shelterService);

    @Test
    void registerAccommodationShouldSaveValidAccommodation() throws SQLException{
        Person person = new Person(
                "Ana",
                "Pop",
                LocalDate.of(1994, 4, 12),
                "0711111111",
                "ana@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 10",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Flood Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test emergency event",
                500
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        EvacuationRecord evacuationRecord = new EvacuationRecord(
                savedPerson.getPersonId(),
                savedEmergencyEvent.getEventId(),
                "Cluj-Napoca",
                false,
                null,
                "Test evacuation record"
        );
        EvacuationRecord savedEvacuationRecord = evacuationRecordService.registerEvacuationRecord(evacuationRecord);

        Shelter shelter = new Shelter(
                "Test Accommodation Shelter",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Shelter 10",
                100,
                ShelterStatus.OPEN,
                "0722222222",
                "accommodation-shelter@test.com",
                true,
                true,
                true,
                true,
                "Shelter used for accommodation tests"
        );
        Shelter savedShelter = shelterService.registerShelter(shelter);

        Accommodation accommodation = new Accommodation(
                savedEvacuationRecord.getEvacuationId(),
                savedShelter.getShelterId(),
                "Test accommodation"
        );

        Accommodation savedAccommmodation = accommodationService.registerAccommodation(accommodation);
        assertNotNull(savedAccommmodation.getAccommodationId());
    }

    @Test
    void registerAccommodationShouldRejectNullAccommodation() throws SQLException{
        try {
            accommodationService.registerAccommodation(null);
            fail("Expected IllegalArgumentException for null accommodation");
        } catch (IllegalArgumentException e) {
            assertEquals("Accommodation cannot be null", e.getMessage());
        }
    }

    @Test
    void registerAccommodationShouldRejectWhenEvacuationAlreadyHasActiveAccommodation() throws SQLException{
        Person person = new Person(
                "Radu",
                "Ionescu",
                LocalDate.of(1991, 7, 14),
                "0733333333",
                "radu@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 11",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Fire Event",
                EmergencyType.FIRE,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Central area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test emergency event",
                300
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        EvacuationRecord evacuationRecord = new EvacuationRecord(
                savedPerson.getPersonId(),
                savedEmergencyEvent.getEventId(),
                "Cluj-Napoca",
                false,
                null,
                "Test evacuation record"
        );
        EvacuationRecord savedEvacuationRecord = evacuationRecordService.registerEvacuationRecord(evacuationRecord);

        Shelter firstShelter = new Shelter(
                "First Test Shelter",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Shelter 11",
                100,
                ShelterStatus.OPEN,
                "0744444444",
                "first-shelter@test.com",
                true,
                true,
                true,
                true,
                "First shelter"
        );
        Shelter savedFirstShelter = shelterService.registerShelter(firstShelter);

        Shelter secondShelter = new Shelter(
                "Second Test Shelter",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Shelter 12",
                100,
                ShelterStatus.OPEN,
                "0755555555",
                "second-shelter@test.com",
                true,
                true,
                true,
                true,
                "Second shelter"
        );
        Shelter savedSecondShelter = shelterService.registerShelter(secondShelter);

        Accommodation firstAccommodation = new Accommodation(
                savedEvacuationRecord.getEvacuationId(),
                savedFirstShelter.getShelterId(),
                "First active accommodation"
        );

        Accommodation secondAccommodation = new Accommodation(
                savedEvacuationRecord.getEvacuationId(),
                savedSecondShelter.getShelterId(),
                "Second active accommodation"
        );
        accommodationService.registerAccommodation(firstAccommodation);

        try {
            accommodationService.registerAccommodation(secondAccommodation);
            fail("Expected IllegalStateException for duplicate active accommodation");
        } catch (IllegalStateException e) {
            assertEquals(
                    "Evacuation record already has an active accommodation",
                    e.getMessage()
            );
        }
    }

    @Test
    void registerAccommodationShouldRejectClosedShelter() throws SQLException{
        Person person = new Person(
                "Ioana",
                "Marin",
                LocalDate.of(1990, 6, 18),
                "0766666666",
                "ioana.accommodation@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 13",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Earthquake Event",
                EmergencyType.EARTHQUAKE,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test emergency event",
                450
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        EvacuationRecord evacuationRecord = new EvacuationRecord(
                savedPerson.getPersonId(),
                savedEmergencyEvent.getEventId(),
                "Cluj-Napoca",
                false,
                null,
                "Test evacuation record"
        );

        EvacuationRecord savedEvacuationRecord = evacuationRecordService.registerEvacuationRecord(evacuationRecord);

        Shelter shelter = new Shelter(
                "Closed Test Shelter",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Shelter 13",
                100,
                ShelterStatus.CLOSED,
                "0767777777",
                "closed-shelter@test.com",
                true,
                true,
                true,
                false,
                "Closed shelter for test"
        );
        Shelter savedShelter = shelterService.registerShelter(shelter);

        Accommodation accommodation = new Accommodation(
                savedEvacuationRecord.getEvacuationId(),
                savedShelter.getShelterId(),
                "Accommodation in closed shelter"
        );

        try {
            accommodationService.registerAccommodation(accommodation);
            fail("Expected IllegalStateException for closed shelter");
        } catch (IllegalStateException e) {
            assertEquals("Cannot accommodate a person in a closed shelter", e.getMessage());
        }
    }

   @Test
    void requireAccommodationByIdShouldReturnAccommodationWhenExists() throws SQLException{
       Person person = new Person(
               "Marius",
               "Popescu",
               LocalDate.of(1987, 10, 9),
               "0777777777",
               "marius.accommodation@test.com",
               "Romania",
               "Cluj",
               "Cluj-Napoca",
               "Strada Test 14",
               "Test person"
       );
       Person savedPerson = personService.registerPerson(person);

       EmergencyEvent emergencyEvent = new EmergencyEvent(
               "Test Flood Event",
               EmergencyType.FLOOD,
               "Romania",
               "Cluj",
               "Cluj-Napoca",
               "Cluj area",
               LocalDateTime.now(),
               null,
               EmergencyStatus.ACTIVE,
               "Test emergency event",
               350
       );
       EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

       EvacuationRecord evacuationRecord = new EvacuationRecord(
               savedPerson.getPersonId(),
               savedEmergencyEvent.getEventId(),
               "Cluj-Napoca",
               false,
               null,
               "Test evacuation record"
       );
       EvacuationRecord savedEvacuationRecord = evacuationRecordService.registerEvacuationRecord(evacuationRecord);

       Shelter shelter = new Shelter(
               "Accommodation Test Shelter",
               "Romania",
               "Cluj",
               "Cluj-Napoca",
               "Strada Shelter 14",
               80,
               ShelterStatus.OPEN,
               "0788888888",
               "shelter14@test.com",
               true,
               true,
               false,
               true,
               "Shelter for require test"
       );
       Shelter savedShelter = shelterService.registerShelter(shelter);

       Accommodation accommodation = new Accommodation(
               savedEvacuationRecord.getEvacuationId(),
               savedShelter.getShelterId(),
               "Accommodation for require test"
       );
       Accommodation savedAccommodation = accommodationService.registerAccommodation(accommodation);

       Accommodation retrievedAccommodation = accommodationService.requireAccommodationById(savedAccommodation.getAccommodationId());

       assertEquals(savedAccommodation.getAccommodationId(), retrievedAccommodation.getAccommodationId());
   }

    @Test
    void updateAccommodationShouldModifyAccommodation() throws SQLException{
        Person person = new Person(
                "Elena",
                "Rusu",
                LocalDate.of(1992, 3, 22),
                "0799999999",
                "elena.accommodation@test.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Test 15",
                "Test person"
        );
        Person savedPerson = personService.registerPerson(person);

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                "Test Fire Event",
                EmergencyType.FIRE,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test emergency event",
                250
        );
        EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

        EvacuationRecord evacuationRecord = new EvacuationRecord(
                savedPerson.getPersonId(),
                savedEmergencyEvent.getEventId(),
                "Cluj-Napoca",
                false,
                null,
                "Test evacuation record"
        );
        EvacuationRecord savedEvacuationRecord = evacuationRecordService.registerEvacuationRecord(evacuationRecord);

        Shelter shelter = new Shelter(
                "Update Test Shelter",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Shelter 15",
                75,
                ShelterStatus.OPEN,
                "0710101010",
                "update-shelter@test.com",
                true,
                false,
                true,
                true,
                "Shelter for update test"
        );
        Shelter savedShelter = shelterService.registerShelter(shelter);

        Accommodation accommodation = new Accommodation(
                savedEvacuationRecord.getEvacuationId(),
                savedShelter.getShelterId(),
                "Initial accommodation notes"
        );
        Accommodation savedAccommodation = accommodationService.registerAccommodation(accommodation);

        savedAccommodation.setNotes("Updated accommodation notes");

        accommodationService.updateAccommodation(savedAccommodation);
        Accommodation result = accommodationService.requireAccommodationById(savedAccommodation.getAccommodationId());

        assertEquals("Updated accommodation notes", result.getNotes());
    }

   @Test
   void updateAccommodationShouldRejectInvalidCheckOutDatetime() throws SQLException{
       Person person = new Person(
               "Cristian",
               "Moldovan",
               LocalDate.of(1986, 12, 3),
               "0720202020",
               "cristian.accommodation@test.com",
               "Romania",
               "Cluj",
               "Cluj-Napoca",
               "Strada Test 16",
               "Test person"
       );
       Person savedPerson = personService.registerPerson(person);

       EmergencyEvent emergencyEvent = new EmergencyEvent(
               "Test Earthquake Event",
               EmergencyType.EARTHQUAKE,
               "Romania",
               "Cluj",
               "Cluj-Napoca",
               "Cluj area",
               LocalDateTime.now(),
               null,
               EmergencyStatus.ACTIVE,
               "Test emergency event",
               200
       );
       EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

       EvacuationRecord evacuationRecord = new EvacuationRecord(
               savedPerson.getPersonId(),
               savedEmergencyEvent.getEventId(),
               "Cluj-Napoca",
               false,
               null,
               "Test evacuation record"
       );
       EvacuationRecord savedEvacuationRecord = evacuationRecordService.registerEvacuationRecord(evacuationRecord);

       Shelter shelter = new Shelter(
               "Invalid Checkout Test Shelter",
               "Romania",
               "Cluj",
               "Cluj-Napoca",
               "Strada Shelter 16",
               60,
               ShelterStatus.OPEN,
               "0730303030",
               "checkout-shelter@test.com",
               true,
               true,
               true,
               false,
               "Shelter for invalid checkout test"
       );
       Shelter savedShelter = shelterService.registerShelter(shelter);

       Accommodation accommodation = new Accommodation(
               savedEvacuationRecord.getEvacuationId(),
               savedShelter.getShelterId(),
               "Accommodation for invalid checkout test"
       );
       Accommodation savedAccommodation = accommodationService.registerAccommodation(accommodation);

       Accommodation storedAccommodation = accommodationService.requireAccommodationById(savedAccommodation.getAccommodationId());
       storedAccommodation.setCheckOutDatetime(
               storedAccommodation.getCheckInDatetime().minusMinutes(1)
       );

       try {
           accommodationService.updateAccommodation(storedAccommodation);
           fail("Expected IllegalArgumentException for invalid check-out datetime");
       } catch (IllegalArgumentException e) {
           assertEquals("Check-out datetime cannot be before check-in datetime", e.getMessage());
       }
   }

   @Test
     void deleteAccommodationShouldRemoveAccommodation() throws SQLException{
       Person person = new Person(
               "Mihai",
               "Dumitru",
               LocalDate.of(1989, 8, 11),
               "0740404040",
               "mihai.accommodation@test.com",
               "Romania",
               "Cluj",
               "Cluj-Napoca",
               "Strada Test 17",
               "Test person"
       );
       Person savedPerson = personService.registerPerson(person);

       EmergencyEvent emergencyEvent = new EmergencyEvent(
               "Test Landslide Event",
               EmergencyType.LANDSLIDE,
               "Romania",
               "Cluj",
               "Cluj-Napoca",
               "Cluj area",
               LocalDateTime.now(),
               null,
               EmergencyStatus.ACTIVE,
               "Test emergency event",
               180
       );
       EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

       EvacuationRecord evacuationRecord = new EvacuationRecord(
               savedPerson.getPersonId(),
               savedEmergencyEvent.getEventId(),
               "Cluj-Napoca",
               false,
               null,
               "Test evacuation record"
       );
       EvacuationRecord savedEvacuationRecord = evacuationRecordService.registerEvacuationRecord(evacuationRecord);

       Shelter shelter = new Shelter(
               "Delete Test Shelter",
               "Romania",
               "Cluj",
               "Cluj-Napoca",
               "Strada Shelter 17",
               50,
               ShelterStatus.OPEN,
               "0750505050",
               "delete-shelter@test.com",
               true,
               false,
               true,
               true,
               "Shelter for delete test"
       );
       Shelter savedShelter = shelterService.registerShelter(shelter);

       Accommodation accommodation = new Accommodation(savedEvacuationRecord.getEvacuationId(), savedShelter.getShelterId(), "Accommodation for delete test");

       Accommodation savedAccommodation = accommodationService.registerAccommodation(accommodation);
       accommodationService.deleteAccommodation(savedAccommodation.getAccommodationId());

       try {
           accommodationService.requireAccommodationById(savedAccommodation.getAccommodationId());
           fail("Expected IllegalArgumentException for deleted accommodation");
       } catch (IllegalArgumentException e) {
           assertEquals("Accommodation with ID " + savedAccommodation.getAccommodationId() + " does not exist", e.getMessage());
       }
   }

   @Test
   void transferAccommodationShouldMoveToAnotherShelter() throws SQLException{
       Person person = new Person(
               "Alexandra",
               "Matei",
               LocalDate.of(1991, 5, 17),
               "0760606060",
               "alexandra.accommodation@test.com",
               "Romania",
               "Cluj",
               "Cluj-Napoca",
               "Strada Test 18",
               "Test person"
       );
       Person savedPerson = personService.registerPerson(person);

       EmergencyEvent emergencyEvent = new EmergencyEvent(
               "Test Transfer Event",
               EmergencyType.FLOOD,
               "Romania",
               "Cluj",
               "Cluj-Napoca",
               "Cluj area",
               LocalDateTime.now(),
               null,
               EmergencyStatus.ACTIVE,
               "Test emergency event",
               220
       );
       EmergencyEvent savedEmergencyEvent = emergencyEventService.registerEmergencyEvent(emergencyEvent);

       EvacuationRecord evacuationRecord = new EvacuationRecord(
               savedPerson.getPersonId(),
               savedEmergencyEvent.getEventId(),
               "Cluj-Napoca",
               false,
               null,
               "Test evacuation record"
       );
       EvacuationRecord savedEvacuationRecord = evacuationRecordService.registerEvacuationRecord(evacuationRecord);

       Shelter firstShelter = new Shelter(
               "First Transfer Shelter",
               "Romania",
               "Cluj",
               "Cluj-Napoca",
               "Strada Shelter 18",
               50,
               ShelterStatus.OPEN,
               "0770707070",
               "first-transfer@test.com",
               true,
               true,
               true,
               true,
               "Initial shelter for transfer test"
       );
       Shelter savedFirstShelter = shelterService.registerShelter(firstShelter);

       Shelter secondShelter = new Shelter(
               "Second Transfer Shelter",
               "Romania",
               "Cluj",
               "Cluj-Napoca",
               "Strada Shelter 19",
               50,
               ShelterStatus.OPEN,
               "0780808080",
               "second-transfer@test.com",
               true,
               true,
               true,
               true,
               "Destination shelter for transfer test"
       );
       Shelter savedSecondShelter = shelterService.registerShelter(secondShelter);

       Accommodation accommodation = new Accommodation(
               savedEvacuationRecord.getEvacuationId(),
               savedFirstShelter.getShelterId(),
               "Initial accommodation"
       );
       Accommodation savedAccommodation = accommodationService.registerAccommodation(accommodation);

       Accommodation transferredAccommodation = accommodationService.transferAccommodation(savedAccommodation.getAccommodationId(), savedSecondShelter.getShelterId());

       assertEquals(savedSecondShelter.getShelterId(), transferredAccommodation.getShelterId());
       assertNotEquals(savedAccommodation.getAccommodationId(), transferredAccommodation.getAccommodationId());

       Accommodation oldAccommodation = accommodationService.requireAccommodationById(savedAccommodation.getAccommodationId());
       assertNotNull(oldAccommodation.getCheckOutDatetime());
       assertNull(transferredAccommodation.getCheckOutDatetime());
   }
}

