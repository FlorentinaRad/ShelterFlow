package repository;

import model.Shelter;
import model.enums.ShelterStatus;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ShelterRepositoryTest {
    private final ShelterRepository repository = new ShelterRepository();

    @Test
    void saveShouldGenerateId() throws SQLException {
        Shelter shelter = new Shelter(
                "Test Shelter",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                100,
                ShelterStatus.OPEN,
                "0700000000",
                "save.test@example.com",
                true,
                true,
                true,
                true,
                "Test for save()"
        );
        Shelter savedShelter = repository.save(shelter);
        assertNotNull(savedShelter.getShelterId());
    }

    @Test
    void findByIdShouldReturnShelter() throws SQLException {
        Shelter shelter = new Shelter(
                "Test Shelter",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                100,
                ShelterStatus.OPEN,
                "0700000000",
                "findById.test@example.com",
                true,
                true,
                true,
                true,
                "Test for findById()"
        );
        Shelter savedShelter = repository.save(shelter);
        Optional<Shelter> result = repository.findById(savedShelter.getShelterId());
        assertTrue(result.isPresent());
        assertEquals(savedShelter.getShelterId(), result.get().getShelterId());
    }

    @Test
    void findByIdShouldReturnEmptyWhenShelterDoesNotExist() throws SQLException {
        Optional<Shelter> result = repository.findById(99999999);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllShouldReturnShelters() throws SQLException{
        List<Shelter> shelters = repository.findAll();
        assertFalse(shelters.isEmpty());
    }

    @Test
    void updateShouldModifyShelter() throws SQLException {
        Shelter shelter = new Shelter(
                "Test Shelter",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                100,
                ShelterStatus.OPEN,
                "0700000000",
                "update.test@example.com",
                true,
                true,
                true,
                true,
                "Test for update()"
        );
        Shelter savedShelter = repository.save(shelter);
        savedShelter.setName("Updated Shelter");
        boolean updated = repository.update(savedShelter);

        Optional<Shelter> updatedShelter = repository.findById(savedShelter.getShelterId());
        assertTrue(updated);
        assertTrue(updatedShelter.isPresent());
        assertEquals("Updated Shelter", updatedShelter.get().getName());
    }

    @Test
    void deleteShouldRemoveShelter() throws SQLException {
        Shelter shelter = new Shelter(
                "Test Shelter",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Test address",
                100,
                ShelterStatus.OPEN,
                "0700000000",
                "delete.test@example.com",
                true,
                true,
                true,
                true,
                "Test for delete()"
        );
        Shelter savedShelter = repository.save(shelter);
        boolean deleted = repository.delete(savedShelter.getShelterId());

        Optional<Shelter> deletedShelter = repository.findById(savedShelter.getShelterId());
        assertTrue(deleted);
        assertTrue(deletedShelter.isEmpty());
    }

}
