package service;

import model.Accommodation;
import model.Shelter;
import model.enums.ShelterStatus;
import repository.AccommodationRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final EvacuationRecordService evacuationRecordService;
    private final ShelterService shelterService;

    public AccommodationService(AccommodationRepository accommodationRepository, EvacuationRecordService evacuationRecordService, ShelterService shelterService) {
        this.accommodationRepository = accommodationRepository;
        this.evacuationRecordService = evacuationRecordService;
        this.shelterService = shelterService;
    }

    private void validateAccommodation(Accommodation accommodation) throws SQLException {
        if (accommodation == null) {
            throw new IllegalArgumentException("Accommodation cannot be null");
        }
        if (accommodation.getEvacuationId() == null || accommodation.getEvacuationId() <= 0) {
            throw new IllegalArgumentException("Evacuation record ID must be a positive integer");
        }
        evacuationRecordService.requireEvacuationRecordById(accommodation.getEvacuationId());

        if (accommodation.getShelterId() == null || accommodation.getShelterId() <= 0) {
            throw new IllegalArgumentException("Shelter ID must be a positive integer");
        }
        shelterService.requireShelterById(accommodation.getShelterId());
    }

    public Accommodation registerAccommodation(Accommodation accommodation) throws SQLException {
        validateAccommodation(accommodation);

        Shelter shelter = shelterService.requireShelterById(accommodation.getShelterId());

        if (shelter.getStatus() != ShelterStatus.OPEN) {
            throw new IllegalStateException("Cannot accommodate a person in a closed shelter");
        }

        Optional<Accommodation> activeAccommodation = accommodationRepository.findActiveByEvacuationId(accommodation.getEvacuationId());
        if (activeAccommodation.isPresent()) {
            throw new IllegalStateException("Evacuation record already has an active accommodation");
        }

        int activeAccommodationCount = accommodationRepository.countActiveByShelterId(accommodation.getShelterId());
        if (activeAccommodationCount >= shelter.getTotalCapacity()) {
            throw new IllegalStateException("Shelter has reached its maximum capacity");
        }

        return accommodationRepository.save(accommodation);
    }

    public Accommodation requireAccommodationById(Integer accommodationId) throws SQLException {
        if (accommodationId == null || accommodationId <= 0) {
            throw new IllegalArgumentException("Accommodation ID must be a positive integer");
        }

        Optional<Accommodation> accommodation = accommodationRepository.findById(accommodationId);
        if (accommodation.isEmpty()) {
            throw new IllegalArgumentException("Accommodation with ID " + accommodationId + " does not exist");
        }

        return accommodation.get();
    }

    public List<Accommodation> listAllAccommodations() throws SQLException {
        return accommodationRepository.findAll();
    }

    public Accommodation updateAccommodation(Accommodation accommodation) throws SQLException {
        validateAccommodation(accommodation);
        if (accommodation.getAccommodationId() == null || accommodation.getAccommodationId() <= 0) {
            throw new IllegalArgumentException("Accommodation ID must be a positive integer");
        }

        Accommodation existingAccommodation = requireAccommodationById(accommodation.getAccommodationId());

        if (!existingAccommodation.getEvacuationId().equals(accommodation.getEvacuationId())) {
            throw new IllegalArgumentException("Evacuation record cannot be changed for an existing accommodation");
        }

        if (!existingAccommodation.getShelterId().equals(accommodation.getShelterId())) {
            throw new IllegalArgumentException("Shelter cannot be changed for an existing accommodation");
        }

        if (accommodation.getCheckOutDatetime() != null && accommodation.getCheckOutDatetime().isBefore(existingAccommodation.getCheckInDatetime())) {
            throw new IllegalArgumentException("Check-out datetime cannot be before check-in datetime");
        }

        if(existingAccommodation.getCheckOutDatetime() != null && accommodation.getCheckOutDatetime() == null) {
            throw new IllegalStateException("A completed accommodation cannot be reactivated");
        }

        boolean updated = accommodationRepository.update(accommodation);
        if (!updated) {
            throw new SQLException("Failed to update accommodation with ID " + accommodation.getAccommodationId());
        }

        return accommodation;
    }

    public void deleteAccommodation(Integer accommodationId) throws SQLException {
        requireAccommodationById(accommodationId);

        boolean deleted = accommodationRepository.delete(accommodationId);
        if (!deleted) {
            throw new SQLException("Failed to delete accommodation with ID " + accommodationId);
        }
    }

    public Accommodation transferAccommodation(Integer accommodationId, Integer newShelterId) throws SQLException {
        Accommodation currentAccommodation = requireAccommodationById(accommodationId);

        if (currentAccommodation.getCheckOutDatetime() != null) {
            throw new IllegalStateException("Only an active accommodation can be transferred");
        }

        if (newShelterId == null || newShelterId <= 0) {
            throw new IllegalArgumentException("New shelter ID must be a positive integer");
        }

        if (currentAccommodation.getShelterId().equals(newShelterId)) {
            throw new IllegalArgumentException("New shelter must be different from the current shelter");
        }

        Shelter newShelter = shelterService.requireShelterById(newShelterId);

        if (newShelter.getStatus() != ShelterStatus.OPEN) {
            throw new IllegalStateException("Cannot transfer a person to a closed shelter");
        }

        int activeAccommodationCount = accommodationRepository.countActiveByShelterId(newShelterId);
        if (activeAccommodationCount >= newShelter.getTotalCapacity()) {
            throw new IllegalStateException("Shelter has reached its maximum capacity");
        }

        LocalDateTime transferDatetime = LocalDateTime.now();

        if (transferDatetime.isBefore(currentAccommodation.getCheckInDatetime())) {
            transferDatetime = currentAccommodation.getCheckInDatetime();
        }
        currentAccommodation.setCheckOutDatetime(transferDatetime);
        updateAccommodation(currentAccommodation);

        Accommodation newAccommodation = new Accommodation(currentAccommodation.getEvacuationId(), newShelterId, "Transferred from shelter ID " + currentAccommodation.getShelterId());

        return registerAccommodation(newAccommodation);
    }
}
