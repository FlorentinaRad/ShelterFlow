package ui;

import model.Shelter;
import model.enums.ShelterStatus;
import service.ShelterService;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class ShelterMenu {
    private final Scanner scanner;
    private final ShelterService shelterService;

    public ShelterMenu(Scanner scanner, ShelterService shelterService) {
        this.scanner = scanner;
        this.shelterService = shelterService;
    }

    public void show() {
        while (true) {
            System.out.println("Shelters");
            System.out.println("1. Register shelter");
            System.out.println("2. Find shelter by ID");
            System.out.println("3. List all shelters");
            System.out.println("4. Update shelter");
            System.out.println("5. Delete shelter");
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
                        registerShelter();
                        break;
                    case 2:
                        findShelterById();
                        break;
                    case 3:
                        listAllShelters();
                        break;
                    case 4:
                        updateShelter();
                        break;
                    case 5:
                        deleteShelter();
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch(InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    private void registerShelter() {
        System.out.println();
        System.out.println("Register Shelter");

        System.out.println("Name: ");
        String name = scanner.nextLine().trim();

        System.out.println("Country: ");
        String country = scanner.nextLine().trim();

        System.out.println("County: ");
        String county = scanner.nextLine().trim();

        System.out.println("Locality: ");
        String locality = scanner.nextLine().trim();

        System.out.print("Address: ");
        String address = scanner.nextLine().trim();

        System.out.println("Total capacity: ");
        int totalCapacity = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Status: ");
        System.out.println("1. OPEN");
        System.out.println("2. CLOSED");
        System.out.println("Choose status: ");
        int statusOption = scanner.nextInt();
        scanner.nextLine();

        ShelterStatus status;

        switch (statusOption) {
            case 1:
                status = ShelterStatus.OPEN;
                break;
            case 2:
                status = ShelterStatus.CLOSED;
                break;
            default:
                System.out.println("Invalid status.");
                return;
        }

        System.out.println("Phone number (optional): ");
        String phoneNumber = scanner.nextLine().trim();
        if (phoneNumber.isEmpty()) {
            phoneNumber = null;
        }

        System.out.println("Email (optional): ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            email = null;
        }

        System.out.println("Access ramp: ");
        System.out.println("1. Yes");
        System.out.println("2. No");
        boolean accessRamp;
        int optionAccessRamp = scanner.nextInt();
        scanner.nextLine();

        if (optionAccessRamp == 1) {
            accessRamp = true;
        } else if (optionAccessRamp == 2) {
            accessRamp = false;
        } else {
            System.out.println("Invalid option.");
            return;
        }

        System.out.println("Children area: ");
        System.out.println("1. Yes");
        System.out.println("2. No");
        boolean childrenArea;
        int optionChildrenArea = scanner.nextInt();
        scanner.nextLine();

        if (optionChildrenArea == 1) {
            childrenArea = true;
        } else if (optionChildrenArea == 2) {
            childrenArea = false;
        } else {
            System.out.println("Invalid option.");
            return;
        }

        System.out.println("Medical room: ");
        System.out.println("1. Yes");
        System.out.println("2. No");
        boolean medicalRoom;
        int optionMedicalRoom = scanner.nextInt();
        scanner.nextLine();

        if (optionMedicalRoom == 1) {
            medicalRoom = true;
        } else if (optionMedicalRoom == 2) {
            medicalRoom = false;
        } else {
            System.out.println("Invalid option.");
            return;
        }

        System.out.println("Accepts pets: ");
        System.out.println("1. Yes");
        System.out.println("2. No");
        boolean acceptsPets;
        int optionAcceptsPets = scanner.nextInt();
        scanner.nextLine();

        if (optionAcceptsPets == 1) {
            acceptsPets = true;
        } else if (optionAcceptsPets == 2) {
            acceptsPets = false;
        } else {
            System.out.println("Invalid option.");
            return;
        }

        System.out.println("Public information (optional): ");
        String publicInformation = scanner.nextLine().trim();
        if (publicInformation.isEmpty()) {
            publicInformation = null;
        }

        Shelter shelter = new Shelter(
                name,
                country,
                county,
                locality,
                address,
                totalCapacity,
                status,
                phoneNumber,
                email,
                accessRamp,
                childrenArea,
                medicalRoom,
                acceptsPets,
                publicInformation
        );

        try{
            shelterService.registerShelter(shelter);
            System.out.println("Shelter registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findShelterById() {
        System.out.println("Shelter ID: ");
        int shelterId = scanner.nextInt();
        scanner.nextLine();

        try {
            Shelter shelter = shelterService.requireShelterById(shelterId);
            System.out.println(shelter);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listAllShelters() {
        try{
            List<Shelter> shelters = shelterService.listAllShelters();
            for(Shelter shelter : shelters) {
                System.out.println(shelter);
            }
        } catch(SQLException e) {
                System.out.println(e.getMessage());
        }
    }

    private void updateShelter() {
        System.out.println("Shelter ID: ");
        int shelterId = scanner.nextInt();
        scanner.nextLine();

        try {
            Shelter shelter = shelterService.requireShelterById(shelterId);

            System.out.println("1. Name");
            System.out.println("2. Country");
            System.out.println("3. County");
            System.out.println("4. Locality");
            System.out.println("5. Address");
            System.out.println("6. Total capacity");
            System.out.println("7. Status");
            System.out.println("8. Phone number");
            System.out.println("9. Email");
            System.out.println("10. Access ramp");
            System.out.println("11. Children area");
            System.out.println("12. Medical room");
            System.out.println("13. Accepts pets");
            System.out.println("14. Public information");

            System.out.println("Choose field to update: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option){
                case 1:
                    System.out.println("New name: ");
                    shelter.setName(scanner.nextLine().trim());
                    break;
                case 2:
                    System.out.println("New country: ");
                    shelter.setCountry(scanner.nextLine().trim());
                    break;
                case 3:
                    System.out.println("New county: ");
                    shelter.setCounty(scanner.nextLine().trim());
                    break;
                case 4:
                    System.out.println("New locality: ");
                    shelter.setLocality(scanner.nextLine().trim());
                    break;
                case 5:
                    System.out.println("New address: ");
                    shelter.setAddress(scanner.nextLine().trim());
                    break;
                case 6:
                    System.out.println("New total capacity: ");
                    shelter.setTotalCapacity(scanner.nextInt());
                    scanner.nextLine();
                    break;
                case 7:
                    System.out.println("New status: ");
                    System.out.println("1. OPEN");
                    System.out.println("2. CLOSED");
                    System.out.println("Choose status: ");
                    int statusOption = scanner.nextInt();
                    scanner.nextLine();

                    switch(statusOption) {
                        case 1:
                            shelter.setStatus(ShelterStatus.OPEN);
                            break;
                        case 2:
                            shelter.setStatus(ShelterStatus.CLOSED);
                            break;
                        default:
                            System.out.println("Invalid option.");
                            return;
                    }
                    break;
                case 8:
                    System.out.println("New phone number: ");
                    String phoneNumber = scanner.nextLine().trim();
                    if (phoneNumber.isEmpty()) {
                        phoneNumber = null;
                    }
                    shelter.setPhoneNumber(phoneNumber);
                    break;
                case 9:
                    System.out.println("New email: ");
                    String email = scanner.nextLine().trim();
                    if (email.isEmpty()) {
                        email = null;
                    }
                    shelter.setEmail(email);
                    break;
                case 10:
                    System.out.println("Access ramp:");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    System.out.print("Choose an option: ");

                    int accessRampOption = scanner.nextInt();
                    scanner.nextLine();

                    if (accessRampOption == 1) {
                        shelter.setAccessRamp(true);
                    } else if (accessRampOption == 2) {
                        shelter.setAccessRamp(false);
                    } else {
                        System.out.println("Invalid option.");
                        return;
                    }
                    break;

                case 11:
                    System.out.println("Children area:");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    System.out.print("Choose an option: ");

                    int childrenAreaOption = scanner.nextInt();
                    scanner.nextLine();

                    if (childrenAreaOption == 1) {
                        shelter.setChildrenArea(true);
                    } else if (childrenAreaOption == 2) {
                        shelter.setChildrenArea(false);
                    } else {
                        System.out.println("Invalid option.");
                        return;
                    }
                    break;

                case 12:
                    System.out.println("Medical room:");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    System.out.print("Choose an option: ");

                    int medicalRoomOption = scanner.nextInt();
                    scanner.nextLine();

                    if (medicalRoomOption == 1) {
                        shelter.setMedicalRoom(true);
                    } else if (medicalRoomOption == 2) {
                        shelter.setMedicalRoom(false);
                    } else {
                        System.out.println("Invalid option.");
                        return;
                    }
                    break;

                case 13:
                    System.out.println("Accepts pets:");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    System.out.print("Choose an option: ");

                    int acceptsPetsOption = scanner.nextInt();
                    scanner.nextLine();

                    if (acceptsPetsOption == 1) {
                        shelter.setAcceptsPets(true);
                    } else if (acceptsPetsOption == 2) {
                        shelter.setAcceptsPets(false);
                    } else {
                        System.out.println("Invalid option.");
                        return;
                    }
                    break;
                case 14:
                    System.out.println("New public information");
                    String publicInformation = scanner.nextLine().trim();
                    if (publicInformation.isEmpty()) {
                        publicInformation = null;
                    }
                    shelter.setPublicInformation(publicInformation);
                    break;
                default:
                    System.out.println("Invalid option.");
                    return;
            }
            shelterService.updateShelter(shelter);
            System.out.println("Shelter updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteShelter() {
        System.out.println("Shelter ID: ");
        int shelterId = scanner.nextInt();
        scanner.nextLine();

        try {
            shelterService.deleteShelter(shelterId);
            System.out.println("Shelter deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
