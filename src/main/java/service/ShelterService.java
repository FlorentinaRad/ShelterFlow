package service;

import model.Shelter;
import repository.ShelterRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ShelterService {
    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    private void validateShelter(Shelter shelter) {
        if(shelter == null) {
            throw new IllegalArgumentException("Shelter cannot be null");
        }

        if(shelter.getName() == null || shelter.getName().isBlank()) {
            throw new IllegalArgumentException("Shelter name cannot be null or empty");
        }

        if(shelter.getCountry() == null || shelter.getCountry().isBlank()) {
            throw new IllegalArgumentException("Shelter country cannot be null or empty");
        }

        if(shelter.getCounty() == null || shelter.getCounty().isBlank()) {
            throw new IllegalArgumentException("Shelter county cannot be null or empty");
        }

        if(shelter.getLocality() == null || shelter.getLocality().isBlank()) {
            throw new IllegalArgumentException("Shelter locality cannot be null or empty");
        }

        if(shelter.getAddress() == null || shelter.getAddress().isBlank()) {
            throw new IllegalArgumentException("Shelter address cannot be null or empty");
        }

        if(shelter.getTotalCapacity() <= 0) {
            throw new IllegalArgumentException("Shelter total capacity must be greater than zero");
        }

        if(shelter.getStatus() == null) {
            throw new IllegalArgumentException("Shelter status cannot be null");
        }
    }

    public Shelter registerShelter(Shelter shelter) throws SQLException {
        validateShelter(shelter);
        return shelterRepository.save(shelter);
    }

    public Shelter requireShelterById(Integer shelterId) throws SQLException {
        if (shelterId == null || shelterId <= 0) {
            throw new IllegalArgumentException("Shelter ID must be a positive integer");
        }

        Optional<Shelter> shelter = shelterRepository.findById(shelterId);

        if (shelter.isEmpty()) {
            throw new IllegalArgumentException("Shelter with ID " + shelterId + " does not exist");
        }

        return shelter.get();
    }

    public List<Shelter> listAllShelters() throws SQLException {
        return shelterRepository.findAll();
    }

    public Shelter updateShelter(Shelter shelter) throws SQLException {
        validateShelter(shelter);

        if (shelter.getShelterId() == null || shelter.getShelterId() <= 0) {
            throw new IllegalArgumentException("Shelter ID must be a positive integer");
        }

        requireShelterById(shelter.getShelterId());

        boolean updated = shelterRepository.update(shelter);
        if (!updated) {
            throw new SQLException("Failed to update shelter with ID " + shelter.getShelterId());
        }
        return shelter;
    }

    public void deleteShelter(Integer shelterId) throws SQLException {
        requireShelterById(shelterId);

        boolean deleted = shelterRepository.delete(shelterId);
        if (!deleted) {
            throw new SQLException("Failed to delete shelter with ID " + shelterId);
        }
    }
}
