package ui;


import model.EvacuationRecord;
import service.EvacuationRecordService;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class EvacuationRecordMenu {
    private final Scanner scanner;
    private final EvacuationRecordService evacuationRecordService;

    public EvacuationRecordMenu(Scanner scanner, EvacuationRecordService evacuationRecordService) {
        this.scanner = scanner;
        this.evacuationRecordService = evacuationRecordService;
    }

    public void show() {
        while (true) {
            System.out.println("Evacuation record");
            System.out.println("1. Register evacuation record");
            System.out.println("2. Find evacuation record by ID");
            System.out.println("3. List all evacuation records");
            System.out.println("4. Update evacuation record");
            System.out.println("5. Delete evacuation record");
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
                        registerEvacuationRecord();
                        break;
                    case 2:
                        findEvacuationRecordById();
                        break;
                    case 3:
                        listAllEvacuationRecords();
                        break;
                    case 4:
                        updateEvacuationRecord();
                        break;
                    case 5:
                        deleteEvacuationRecord();
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

    private void registerEvacuationRecord() {
        System.out.println();
        System.out.println("Register Evacuation Record");

        System.out.println("Person ID: ");
        int personId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Emergency event ID: ");
        int eventId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Evacuation location (optional): ");
        String evacuationLocation = scanner.nextLine().trim();
        if (evacuationLocation.isEmpty()) {
            evacuationLocation = null;
        }

        System.out.println("Needs assistance: ");
        System.out.println("1. Yes");
        System.out.println("2. No");
        boolean needsAssistance;
        int option = scanner.nextInt();
        scanner.nextLine();
        if (option == 1) {
            needsAssistance = true;
        } else if (option == 2) {
            needsAssistance = false;
        } else {
            System.out.println("Invalid option.");
            return;
        }

        System.out.println("Assistance details (optional): ");
        String assistanceDetails = scanner.nextLine().trim();
        if (assistanceDetails.isEmpty()) {
            assistanceDetails = null;
        }

        System.out.println("Notes (optional): ");
        String notes= scanner.nextLine().trim();
        if (notes.isEmpty()) {
            notes = null;
        }

        EvacuationRecord evacuationRecord = new EvacuationRecord(
                        personId,
                        eventId,
                        evacuationLocation,
                        needsAssistance,
                        assistanceDetails,
                        notes
        );

        try {
            evacuationRecordService.registerEvacuationRecord(evacuationRecord);
            System.out.println("Evacuation record registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findEvacuationRecordById() {
        System.out.println("Evacuation record ID: ");
        int evacuationRecordId = scanner.nextInt();
        scanner.nextLine();

        try{
            EvacuationRecord evacuationRecord = evacuationRecordService.requireEvacuationRecordById(evacuationRecordId);
            System.out.println(evacuationRecord);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listAllEvacuationRecords() {
        try{
            List<EvacuationRecord> evacuationRecords = evacuationRecordService.listAllEvacuationRecords();
            for (EvacuationRecord evacuationRecord : evacuationRecords) {
                System.out.println(evacuationRecord);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateEvacuationRecord() {
        System.out.println("Evacuation Record ID: ");
        int evacuationRecordId = scanner.nextInt();
        scanner.nextLine();

        try {
            EvacuationRecord evacuationRecord = evacuationRecordService.requireEvacuationRecordById(evacuationRecordId);
            System.out.println("1. Person ID");
            System.out.println("2. Emergency event ID");
            System.out.println("3. Evacuation location");
            System.out.println("4. Needs assistance");
            System.out.println("5. Assistance details");
            System.out.println("6. Notes");

            System.out.println("Choose field to update: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.println("New person ID: ");
                    evacuationRecord.setPersonId(scanner.nextInt());
                    scanner.nextLine();
                    break;
                case 2:
                    System.out.println("New emergency event ID: ");
                    evacuationRecord.setEventId(scanner.nextInt());
                    scanner.nextLine();
                    break;
                case 3:
                    System.out.println("New evacuation location: ");
                    String evacuationLocation = scanner.nextLine().trim();
                    if (evacuationLocation.isEmpty()) {
                        evacuationLocation = null;
                    }
                    evacuationRecord.setEvacuationLocation(evacuationLocation);
                    break;
                case 4:
                    System.out.println("Needs assistance: ");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    System.out.print("Choose an option: ");
                    int needsAssistance = scanner.nextInt();
                    scanner.nextLine();
                    if (needsAssistance == 1) {
                        evacuationRecord.setNeedsAssistance(true);
                    } else if (needsAssistance == 2) {
                        evacuationRecord.setNeedsAssistance(false);
                    } else {
                        System.out.println("Invalid option.");
                        return;
                    }
                    break;
                case 5:
                    System.out.println("New assistance details: ");
                    String assistanceDetails = scanner.nextLine().trim();
                    if (assistanceDetails.isEmpty()) {
                        assistanceDetails = null;
                    }
                    evacuationRecord.setAssistanceDetails(assistanceDetails);
                    break;
                case 6:
                    System.out.println("New notes: ");
                    String notes = scanner.nextLine().trim();
                    if (notes.isEmpty()) {
                        notes = null;
                    }
                    evacuationRecord.setNotes(notes);
                    break;
                default:
                    System.out.println("Invalid option.");
                    return;
            }
            evacuationRecordService.updateEvacuationRecord(evacuationRecord);
            System.out.println("Evacuation record updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteEvacuationRecord() {
        System.out.println("Evacuation record ID: ");
        int evacuationRecordId = scanner.nextInt();
        scanner.nextLine();

        try{
            evacuationRecordService.deleteEvacuationRecord(evacuationRecordId);
            System.out.println("Evacuation record deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
