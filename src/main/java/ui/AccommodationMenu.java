package ui;

import model.Accommodation;
import service.AccommodationService;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AccommodationMenu {
    private final Scanner scanner;
    private final AccommodationService accommodationService;

    public AccommodationMenu(Scanner scanner, AccommodationService accommodationService) {
        this.scanner = scanner;
        this.accommodationService = accommodationService;
    }

    public void show() {
        while (true) {
            System.out.println("Accommodations");
            System.out.println("1. Register accommodation");
            System.out.println("2. Find accommodation by ID");
            System.out.println("3. List all accommodations");
            System.out.println("4. Update accommodation");
            System.out.println("5. Delete accommodation");
            System.out.println("6. Transfer");
            System.out.println("0. Back");

            try {
                System.out.print("Choose an option: ");
                int option = scanner.nextInt();
                scanner.nextLine();

                if (option == 0) {
                    break;
                }

                switch (option) {
                    case 1:
                        registerAccommodation();
                        break;
                    case 2:
                        findAccommodationById();
                        break;
                    case 3:
                        listAllAccommodations();
                        break;
                    case 4:
                        updateAccommodation();
                        break;
                    case 5:
                        deleteAccommodation();
                        break;
                    case 6:
                        transferAccommodation();
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch(InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            } catch (DateTimeException e) {
                System.out.println("Invalid date or time.");
            }
        }
    }

    private void registerAccommodation() {
        System.out.println();
        System.out.println("Register accommodation");

        System.out.println("Evacuation ID: ");
        int evacuationId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Shelter ID: ");
        int shelterId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Notes (optional): ");
        String notes = scanner.nextLine().trim();
        if(notes.isEmpty()) {
            notes = null;
        }

        Accommodation accommodation = new Accommodation(
                evacuationId,
                shelterId,
                notes
        );

        try{
            accommodationService.registerAccommodation(accommodation);
            System.out.println("Accommodation registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findAccommodationById() {
        System.out.println("Accommodation ID: ");
        int accommodationId = scanner.nextInt();
        scanner.nextLine();

        try{
            Accommodation accommodation = accommodationService.requireAccommodationById(accommodationId);
            System.out.println(accommodation);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listAllAccommodations() {
        try{
            List<Accommodation> accommodations = accommodationService.listAllAccommodation();
            for (Accommodation accommodation : accommodations) {
                System.out.println(accommodation);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateAccommodation(){
        System.out.println("Accommodation ID: ");
        int accommodationId = scanner.nextInt();
        scanner.nextLine();

        try{
            Accommodation accommodation = accommodationService.requireAccommodationById(accommodationId);
            System.out.println("1. Check-out datetime");
            System.out.println("2. Notes");

            System.out.println("Choose field to update: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.println("New check-out datetime: ");
                    System.out.println("Year: ");
                    int year = scanner.nextInt();
                    System.out.println("Month: ");
                    int month = scanner.nextInt();
                    System.out.println("Day: ");
                    int day = scanner.nextInt();
                    System.out.println("Hour: ");
                    int hour = scanner.nextInt();
                    System.out.println("Minute: ");
                    int minute = scanner.nextInt();
                    scanner.nextLine();
                    LocalDateTime checkOut = LocalDateTime.of(year, month, day, hour, minute);
                    accommodation.setCheckOutDatetime(checkOut);
                    break;
                case 2:
                    System.out.println("New notes: ");
                    String notes = scanner.nextLine().trim();
                    if (notes.isEmpty()) {
                        notes = null;
                    }
                    accommodation.setNotes(notes);
                    break;
                default:
                    System.out.println("Invalid option.");
                    return;
            }
            accommodationService.updateAccommodation(accommodation);
            System.out.println("Accommodation updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteAccommodation() {
        System.out.println("Accommodation ID: ");
        int accommodationId = scanner.nextInt();
        scanner.nextLine();

        try{
            accommodationService.deleteAccommodation(accommodationId);
            System.out.println("Accommodation deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void transferAccommodation() {
        System.out.println("Accommodation ID: ");
        int accommodationId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("New shelter ID: ");
        int shelterId = scanner.nextInt();
        scanner.nextLine();

        try {
            accommodationService.transferAccommodation(accommodationId, shelterId);
            System.out.println("Accommodation transferred successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
