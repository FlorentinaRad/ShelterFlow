package repository;

import model.Accommodation;
import model.EmergencyEvent;
import model.EvacuationRecord;
import model.Person;
import model.Shelter;
import model.enums.EmergencyStatus;
import model.enums.EmergencyType;
import model.enums.ShelterStatus;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AccommodationRepositoryTest {

    private final AccommodationRepository repository =
            new AccommodationRepository();

    private final PersonRepository personRepository =
            new PersonRepository();

    private final EmergencyEventRepository eventRepository =
            new EmergencyEventRepository();

    private final EvacuationRecordRepository evacuationRepository =
            new EvacuationRecordRepository();

    private final ShelterRepository shelterRepository =
            new ShelterRepository();


    @Test
    void saveShouldGenerateId() throws SQLException {
        Accommodation accommodation = createAccommodation();

        Accommodation savedAccommodation =
                repository.save(accommodation);

        assertNotNull(savedAccommodation.getAccommodationId());
    }


    @Test
    void findByIdShouldReturnAccommodationWhenExists() throws SQLException {
        Accommodation savedAccommodation =
                repository.save(createAccommodation());

        Optional<Accommodation> result =
                repository.findById(savedAccommodation.getAccommodationId());

        assertTrue(result.isPresent());
        assertEquals(
                savedAccommodation.getAccommodationId(),
                result.get().getAccommodationId()
        );

        // check_in_datetime este generat automat de SQL Server
        assertNotNull(result.get().getCheckInDatetime());

        // La o cazare noua check_out_datetime trebuie sa fie null
        assertNull(result.get().getCheckOutDatetime());
    }


    @Test
    void findByIdShouldReturnEmptyWhenAccommodationDoesNotExist()
            throws SQLException {

        Optional<Accommodation> result =
                repository.findById(99999999);

        assertTrue(result.isEmpty());
    }


    @Test
    void findAllShouldReturnAccommodations() throws SQLException {
        Accommodation savedAccommodation =
                repository.save(createAccommodation());

        List<Accommodation> accommodations =
                repository.findAll();

        assertFalse(accommodations.isEmpty());

        assertTrue(
                accommodations.stream().anyMatch(
                        accommodation ->
                                accommodation.getAccommodationId()
                                        .equals(savedAccommodation.getAccommodationId())
                )
        );
    }


    @Test
    void updateShouldModifyAccommodation() throws SQLException {
        Accommodation savedAccommodation =
                repository.save(createAccommodation());

        // Citim din baza pentru a obtine si check_in_datetime
        Accommodation accommodation =
                repository.findById(savedAccommodation.getAccommodationId())
                        .orElseThrow();

        accommodation.setCheckOutDatetime(
                accommodation.getCheckInDatetime().plusMinutes(10)
        );

        accommodation.setNotes("Updated accommodation");

        boolean updated =
                repository.update(accommodation);

        Optional<Accommodation> result =
                repository.findById(accommodation.getAccommodationId());

        assertTrue(updated);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getCheckOutDatetime());
        assertEquals(
                "Updated accommodation",
                result.get().getNotes()
        );
    }


    @Test
    void deleteShouldRemoveAccommodation() throws SQLException {
        Accommodation savedAccommodation =
                repository.save(createAccommodation());

        boolean deleted =
                repository.delete(savedAccommodation.getAccommodationId());

        Optional<Accommodation> result =
                repository.findById(savedAccommodation.getAccommodationId());

        assertTrue(deleted);
        assertTrue(result.isEmpty());
    }


    // Creeaza toate datele necesare pentru un Accommodation valid
    private Accommodation createAccommodation() throws SQLException {

        Person person = new Person(
                "Test",
                "Accommodation",
                LocalDate.of(1995, 5, 20),
                "0700000000",
                "accommodation.test@example.com",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                "Test person for accommodation"
        );

        Person savedPerson =
                personRepository.save(person);


        EmergencyEvent event = new EmergencyEvent(
                "Test Accommodation Event",
                EmergencyType.FLOOD,
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Cluj-Napoca metropolitan area",
                LocalDateTime.now(),
                null,
                EmergencyStatus.ACTIVE,
                "Test event for accommodation",
                1000
        );

        EmergencyEvent savedEvent =
                eventRepository.save(event);


        EvacuationRecord record = new EvacuationRecord(
                savedPerson.getPersonId(),
                savedEvent.getEventId(),
                "Cluj-Napoca",
                false,
                null,
                "Test evacuation for accommodation"
        );

        EvacuationRecord savedRecord =
                evacuationRepository.save(record);


        Shelter shelter = new Shelter(
                "Test Accommodation Shelter",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test shelter address",
                100,
                ShelterStatus.OPEN,
                "0700000000",
                "accommodation.shelter@example.com",
                true,
                true,
                true,
                true,
                "Test shelter for accommodation"
        );

        Shelter savedShelter =
                shelterRepository.save(shelter);


        return new Accommodation(
                savedRecord.getEvacuationId(),
                savedShelter.getShelterId(),
                "Test accommodation"
        );
    }
}