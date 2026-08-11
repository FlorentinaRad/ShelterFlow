package service;

import model.Shelter;
import model.enums.ShelterStatus;
import org.junit.jupiter.api.Test;
import repository.ShelterRepository;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShelterServiceTest {
    private final ShelterRepository shelterRepository = new ShelterRepository();
    private final ShelterService shelterService = new ShelterService(shelterRepository);

    @Test
    void registerShelterShouldSaveValidShelter() throws SQLException{
        Shelter shelter = new Shelter(
                "Test Shelter",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Memorandumului 10",
                100,
                ShelterStatus.OPEN,
                "0712345678",
                "shelter@test.com",
                true,
                true,
                true,
                true,
                "Test shelter for integration tests"
        );
        Shelter savedShelter = shelterService.registerShelter(shelter);
        assertNotNull(savedShelter.getShelterId());
    }

    @Test
    void registerShelterShouldRejectNullShelter() throws SQLException {
        try {
            shelterService.registerShelter(null);
            fail("Expected IllegalArgumentException for null shelter");
        } catch (IllegalArgumentException e) {
            assertEquals("Shelter cannot be null", e.getMessage());
        }
    }

    @Test
    void registerShelterShouldRejectInvalidCapacity() throws SQLException {
        Shelter shelter = new Shelter(
                "Test Shelter",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Memorandumului 10",
                0,
                ShelterStatus.OPEN,
                "0712345678",
                "shelter@test.com",
                true,
                true,
                true,
                true,
                "Shelter with invalid capacity"
        );
        try {
            shelterService.registerShelter(shelter);
            fail("Expected IllegalArgumentException for invalid capacity");
        } catch (IllegalArgumentException e) {
            assertEquals("Shelter total capacity must be greater than zero", e.getMessage());
        }
    }

    @Test
    void requireShelterByIdShouldReturnShelterWhenExists() throws SQLException{
        Shelter shelter = new Shelter(
                "Test Shelter",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Memorandumului 10",
                100,
                ShelterStatus.OPEN,
                "0712345678",
                "shelter@test.com",
                true,
                true,
                true,
                true,
                "Test shelter for integration tests"
        );
        Shelter savedShelter = shelterService.registerShelter(shelter);
        Shelter retrievedShelter = shelterService.requireShelterById(savedShelter.getShelterId());
        assertEquals(savedShelter.getShelterId(), retrievedShelter.getShelterId());
    }

    @Test
    void listAllSheltersShouldReturnShelters() throws SQLException {
        Shelter shelter = new Shelter(
                "Test Shelter",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Memorandumului 10",
                100,
                ShelterStatus.OPEN,
                "0712345678",
                "shelter@test.com",
                true,
                true,
                true,
                true,
                "Test shelter for integration tests"
        );
        Shelter savedShelter = shelterService.registerShelter(shelter);
        List<Shelter> shelters = shelterService.listAllShelters();

        boolean found = false;
        for (Shelter currentShelter : shelters) {
            if (currentShelter.getShelterId().equals(savedShelter.getShelterId())) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void updateShelterShouldModifyShelter() throws SQLException{
        Shelter shelter = new Shelter(
                "Test Shelter",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Memorandumului 10",
                100,
                ShelterStatus.OPEN,
                "0712345678",
                "shelter@test.com",
                true,
                true,
                true,
                true,
                "Test shelter for integration tests"
        );
        Shelter savedShelter = shelterService.registerShelter(shelter);
        savedShelter.setTotalCapacity(150);
        shelterService.updateShelter(savedShelter);
        Shelter result = shelterService.requireShelterById(savedShelter.getShelterId());
        assertEquals(150, result.getTotalCapacity());
    }

    @Test
    void deleteShelterShouldRemoveShelter() throws SQLException{
        Shelter shelter = new Shelter(
                "Test Shelter",
                "Romania",
                "Cluj",
                "Cluj-Napoca",
                "Strada Memorandumului 10",
                100,
                ShelterStatus.OPEN,
                "0712345678",
                "shelter@test.com",
                true,
                true,
                true,
                true,
                "Test shelter for integration tests"
        );
        Shelter savedShelter = shelterService.registerShelter(shelter);
        shelterService.deleteShelter(savedShelter.getShelterId());

        try {
            shelterService.requireShelterById(savedShelter.getShelterId());
            fail("Expected IllegalArgumentException for deleted shelter");
        } catch (IllegalArgumentException e) {
            assertEquals("Shelter with ID " + savedShelter.getShelterId() + " does not exist", e.getMessage());
        }
    }

}
