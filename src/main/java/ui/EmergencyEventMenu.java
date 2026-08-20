package ui;

import model.EmergencyEvent;
import model.enums.EmergencyStatus;
import model.enums.EmergencyType;
import service.EmergencyEventService;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class EmergencyEventMenu {
    private final Scanner scanner;
    private final EmergencyEventService emergencyEventService;

    public EmergencyEventMenu(Scanner scanner, EmergencyEventService emergencyEventService) {
        this.scanner = scanner;
        this.emergencyEventService = emergencyEventService;
    }

    public void show() {
        while (true) {
            System.out.println("Emergency Event");
            System.out.println("1. Register emergency event");
            System.out.println("2. Find emergency event by ID");
            System.out.println("3. List all emergency events");
            System.out.println("4. Update emergency event");
            System.out.println("5. Delete emergency event");
            System.out.println("0. Back");

            try {
                System.out.println("Choose an option: ");
                int option = scanner.nextInt();
                scanner.nextLine();

                if (option == 0) {
                    break;
                }

                switch (option) {
                    case 1:
                        registerEmergencyEvent();
                        break;
                    case 2:
                        findEmergencyEventById();
                        break;
                    case 3:
                        listAllEmergencyEvents();
                        break;
                    case 4:
                        updateEmergencyEvent();
                        break;
                    case 5:
                        deleteEmergencyEvent();
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

    private void registerEmergencyEvent() {
        System.out.println();
        System.out.println("Register Emergency Event");

        System.out.println("Name: ");
        String name = scanner.nextLine().trim();

        System.out.println("Type:");
        System.out.println("1. Flood");
        System.out.println("2. Fire");
        System.out.println("3. Earthquake");
        System.out.println("4. Landslide");
        System.out.println("5. Extreme weather");
        System.out.println("6. Air raid");
        System.out.println("7. Other");

        System.out.print("Choose type: ");
        int typeOption = scanner.nextInt();
        scanner.nextLine();

        EmergencyType type;

        switch (typeOption) {
            case 1:
                type = EmergencyType.FLOOD;
                break;
            case 2:
                type = EmergencyType.FIRE;
                break;
            case 3:
                type = EmergencyType.EARTHQUAKE;
                break;
            case 4:
                type = EmergencyType.LANDSLIDE;
                break;
            case 5:
                type = EmergencyType.EXTREME_WEATHER;
                break;
            case 6:
                type = EmergencyType.AIR_RAID;
                break;
            case 7:
                type = EmergencyType.OTHER;
                break;
            default:
                System.out.println("Invalid emergency type.");
                return;
        }

        System.out.println("Country: ");
        String country = scanner.nextLine().trim();

        System.out.println("County: ");
        String county = scanner.nextLine().trim();

        System.out.println("Locality (optional): ");
        String locality = scanner.nextLine().trim();
        if (locality.isEmpty()) {
            locality = null;
        }

        System.out.print("Affected area (optional): ");
        String affectedArea = scanner.nextLine().trim();
        if (affectedArea.isEmpty()) {
            affectedArea = null;
        }

        System.out.println("Start date time: ");
        System.out.println("Year: ");
        int startYear = scanner.nextInt();
        System.out.println("Month: ");
        int startMonth = scanner.nextInt();
        System.out.println("Day: ");
        int startDay = scanner.nextInt();
        System.out.println("Hour: ");
        int startHour = scanner.nextInt();
        System.out.println("Minute: ");
        int startMinute = scanner.nextInt();
        scanner.nextLine();
        LocalDateTime startDateTime = LocalDateTime.of(startYear, startMonth, startDay, startHour, startMinute);

        System.out.println("Status: ");
        System.out.println("1. ACTIVE");
        System.out.println("2. CLOSED");
        System.out.println("Choose status: ");
        int statusOption = scanner.nextInt();
        scanner.nextLine();

        EmergencyStatus status;

        switch (statusOption) {
            case 1:
                status = EmergencyStatus.ACTIVE;
                break;
            case 2:
                status = EmergencyStatus.CLOSED;
                break;
            default:
                System.out.println("Invalid status.");
                return;
        }

        LocalDateTime endDateTime = null;
        if (status == EmergencyStatus.CLOSED) {
            System.out.println("End date time: ");
            System.out.println("Year: ");
            int endYear = scanner.nextInt();
            System.out.println("Month: ");
            int endMonth = scanner.nextInt();
            System.out.println("Day: ");
            int endDay = scanner.nextInt();
            System.out.println("Hour: ");
            int endHour = scanner.nextInt();
            System.out.println("Minute: ");
            int endMinute = scanner.nextInt();
            scanner.nextLine();
            endDateTime = LocalDateTime.of(endYear, endMonth, endDay, endHour, endMinute);
        }

        System.out.print("Description (optional): ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            description = null;
        }

        System.out.println("Estimated affected people (-1 if unknown): ");
        Integer estimatedAffectedPeople = scanner.nextInt();
        scanner.nextLine();

        if (estimatedAffectedPeople == -1) {
            estimatedAffectedPeople = null;
        }

        EmergencyEvent emergencyEvent = new EmergencyEvent(
                name,
                type,
                country,
                county,
                locality,
                affectedArea,
                startDateTime,
                endDateTime,
                status,
                description,
                estimatedAffectedPeople
        );

        try{
            emergencyEventService.registerEmergencyEvent(emergencyEvent);
            System.out.println("Emergency event registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findEmergencyEventById() {
        System.out.println("Emergency event ID: ");
        int eventId = scanner.nextInt();
        scanner.nextLine();

        try{
            EmergencyEvent event = emergencyEventService.requireEmergencyEventById(eventId);
            System.out.println(event);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listAllEmergencyEvents() {
        try{
            List<EmergencyEvent> events = emergencyEventService.listAllEmergencyEvents();
            for(EmergencyEvent event : events){
                System.out.println(event);
            }
        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateEmergencyEvent() {
        System.out.println("Emergency event ID: ");
        int eventId = scanner.nextInt();
        scanner.nextLine();

        try{
            EmergencyEvent event = emergencyEventService.requireEmergencyEventById(eventId);

            System.out.println("1. Name");
            System.out.println("2. Type");
            System.out.println("3. Country");
            System.out.println("4. County");
            System.out.println("5. Locality");
            System.out.println("6. Affected area");
            System.out.println("7. Start date time");
            System.out.println("8. End date time");
            System.out.println("9. Status");
            System.out.println("10. Description");
            System.out.println("11. Estimated affected people");

            System.out.println("Choose field to update: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.println("New name: ");
                    event.setName(scanner.nextLine().trim());
                    break;
                case 2:
                    System.out.println("New type:");
                    System.out.println("1. Flood");
                    System.out.println("2. Fire");
                    System.out.println("3. Earthquake");
                    System.out.println("4. Landslide");
                    System.out.println("5. Extreme weather");
                    System.out.println("6. Air raid");
                    System.out.println("7. Other");

                    System.out.print("Choose type: ");
                    int typeOption = scanner.nextInt();
                    scanner.nextLine();

                    switch (typeOption) {
                        case 1:
                            event.setType(EmergencyType.FLOOD);
                            break;
                        case 2:
                            event.setType(EmergencyType.FIRE);
                            break;
                        case 3:
                            event.setType(EmergencyType.EARTHQUAKE);
                            break;
                        case 4:
                            event.setType(EmergencyType.LANDSLIDE);
                            break;
                        case 5:
                            event.setType(EmergencyType.EXTREME_WEATHER);
                            break;
                        case 6:
                            event.setType(EmergencyType.AIR_RAID);
                            break;
                        case 7:
                            event.setType(EmergencyType.OTHER);
                            break;
                        default:
                            System.out.println("Invalid emergency type.");
                            return;
                    }
                    break;
                case 3:
                    System.out.println("New country: ");
                    event.setCountry(scanner.nextLine().trim());
                    break;
                case 4:
                    System.out.println("New county: ");
                    event.setCounty(scanner.nextLine().trim());
                    break;
                case 5:
                    System.out.println("New locality: ");
                    String locality = scanner.nextLine().trim();
                    if (locality.isEmpty()) {
                        locality = null;
                    }
                    event.setLocality(locality);
                    break;
                case 6:
                    System.out.println("New affected area: ");
                    String affectedArea = scanner.nextLine().trim();
                    if (affectedArea.isEmpty()) {
                        affectedArea = null;
                    }
                    event.setAffectedArea(affectedArea);
                    break;
                case 7 :
                    System.out.println("New start date time: ");
                    System.out.println("Year: ");
                    int startYear = scanner.nextInt();
                    System.out.print("Month: ");
                    int startMonth = scanner.nextInt();
                    System.out.print("Day: ");
                    int startDay = scanner.nextInt();
                    System.out.print("Hour: ");
                    int startHour = scanner.nextInt();
                    System.out.print("Minute: ");
                    int startMinute = scanner.nextInt();
                    scanner.nextLine();
                    LocalDateTime startDateTime = LocalDateTime.of(startYear, startMonth, startDay, startHour, startMinute);
                    event.setStartDatetime(startDateTime);
                    break;
                case 8:
                    System.out.println("New end date time: ");
                    System.out.print("Year: ");
                    int endYear = scanner.nextInt();
                    System.out.print("Month: ");
                    int endMonth = scanner.nextInt();
                    System.out.print("Day: ");
                    int endDay = scanner.nextInt();
                    System.out.print("Hour: ");
                    int endHour = scanner.nextInt();
                    System.out.print("Minute: ");
                    int endMinute = scanner.nextInt();
                    scanner.nextLine();
                    LocalDateTime endDateTime = LocalDateTime.of(endYear, endMonth, endDay, endHour, endMinute);
                    event.setEndDatetime(endDateTime);
                    break;
                case 9:
                    System.out.println("New status: ");
                    System.out.println("1. ACTIVE");
                    System.out.println("2. CLOSED");
                    System.out.println("Choose status: ");
                    int statusOption = scanner.nextInt();
                    scanner.nextLine();

                    switch(statusOption) {
                        case 1:
                            event.setStatus(EmergencyStatus.ACTIVE);
                            event.setEndDatetime(null);
                            break;
                        case 2:
                            event.setStatus(EmergencyStatus.CLOSED);
                            System.out.println("End date time: ");
                            System.out.print("Year: ");
                            int statusEndYear = scanner.nextInt();
                            System.out.print("Month: ");
                            int statusEndMonth = scanner.nextInt();
                            System.out.print("Day: ");
                            int statusEndDay = scanner.nextInt();
                            System.out.print("Hour: ");
                            int statusEndHour = scanner.nextInt();
                            System.out.print("Minute: ");
                            int statusEndMinute = scanner.nextInt();
                            scanner.nextLine();
                            LocalDateTime statusEndDateTime = LocalDateTime.of(statusEndYear, statusEndMonth, statusEndDay, statusEndHour, statusEndMinute);
                            event.setEndDatetime(statusEndDateTime);
                            break;
                        default:
                            System.out.println("Invalid status.");
                            return;
                    }
                    break;
                case 10:
                    System.out.println("New description: ");
                    String description = scanner.nextLine().trim();
                    if (description.isEmpty()) {
                        description = null;
                    }
                    event.setDescription(description);
                    break;
                case 11:
                    System.out.println("New estimated affected people (-1 if unknown): ");
                    Integer estimatedAffectedPeople = scanner.nextInt();
                    scanner.nextLine();
                    if(estimatedAffectedPeople == -1) {
                        estimatedAffectedPeople = null;
                    }
                    event.setEstimatedAffectedPeople(estimatedAffectedPeople);
                    break;
                default:
                    System.out.println("Invalid option.");
                    return;
            }
            emergencyEventService.updateEmergencyEvent(event);
            System.out.println("Emergency event updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteEmergencyEvent() {
        System.out.println("Event ID: ");
        int eventId = scanner.nextInt();
        scanner.nextLine();

        try{
            emergencyEventService.deleteEmergencyEvent(eventId);
            System.out.println("Emergency event deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
