package ui;

import model.MissingPersonReport;
import model.enums.MissingPersonStatus;
import service.MissingPersonReportService;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MissingPersonReportMenu {
    private final Scanner scanner;
    private final MissingPersonReportService missingPersonReportService;

    public MissingPersonReportMenu(Scanner scanner, MissingPersonReportService missingPersonReportService) {
        this.scanner = scanner;
        this.missingPersonReportService = missingPersonReportService;
    }

    public void show() {
        while (true) {
            System.out.println("Missing person reports");
            System.out.println("1. Register missing person report");
            System.out.println("2. Find missing person report by ID");
            System.out.println("3. List all missing person reports");
            System.out.println("4. Update missing person report");
            System.out.println("5. Delete missing person report");
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
                        registerMissingPersonReport();
                        break;
                    case 2:
                        findMissingPersonReportById();
                        break;
                    case 3:
                        listAllMissingPersonReports();
                        break;
                    case 4:
                        updateMissingPersonReport();
                        break;
                    case 5:
                        deleteMissingPersonReport();
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

    private void registerMissingPersonReport() {
        System.out.println();
        System.out.println("Register missing person");

        System.out.println("Person ID: ");
        int personId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Emergency event ID: ");
        int eventId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Last seen datetime known? ");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int option = scanner.nextInt();
        scanner.nextLine();

        LocalDateTime lastSeenDateTime;
        if (option == 1) {
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

            lastSeenDateTime = LocalDateTime.of(year, month, day, hour, minute);
        } else if (option == 2) {
            lastSeenDateTime = null;
        } else {
            System.out.println("Invalid option.");
            return;
        }

        System.out.println("Last known location (optional): ");
        String lastKnownLocation = scanner.nextLine().trim();
        if (lastKnownLocation.isEmpty()) {
            lastKnownLocation = null;
        }

        System.out.println("Reported by: ");
        String reportedByName = scanner.nextLine().trim();

        System.out.println("Reported by phone (optional): ");
        String reportedByPhone = scanner.nextLine().trim();
        if (reportedByPhone.isEmpty()) {
            reportedByPhone= null;
        }

        System.out.println("Notes (optional): ");
        String notes = scanner.nextLine().trim();
        if (notes.isEmpty()) {
            notes= null;
        }

        MissingPersonReport missingPersonReport = new MissingPersonReport(
                personId,
                eventId,
                lastSeenDateTime,
                lastKnownLocation,
                reportedByName,
                reportedByPhone,
                notes
        );

        try{
            missingPersonReportService.registerMissingPersonReport(missingPersonReport);
            System.out.println("Missing person report registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findMissingPersonReportById() {
        System.out.println("Missing person record ID: ");
        int missingPersonId = scanner.nextInt();
        scanner.nextLine();

        try{
            MissingPersonReport missingPersonReport = missingPersonReportService.requireMissingPersonReportById(missingPersonId);
            System.out.println(missingPersonReport);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listAllMissingPersonReports() {
        try{
            List<MissingPersonReport> missingPersonReports = missingPersonReportService.listAllMissingPersonReports();
            for (MissingPersonReport missingPersonReport : missingPersonReports) {
                System.out.println(missingPersonReport);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateMissingPersonReport() {
        System.out.println("Missing Person Report ID: ");
        int missingPersonReportId = scanner.nextInt();
        scanner.nextLine();

        try{
            MissingPersonReport missingPersonReport = missingPersonReportService.requireMissingPersonReportById(missingPersonReportId);
            System.out.println("1. Last seen datetime");
            System.out.println("2. Last known location");
            System.out.println("3. Reported by name");
            System.out.println("4. Reported by phone");
            System.out.println("5. Status");
            System.out.println("6. Notes");

            System.out.println("Choose field to update: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.println("New last seen datetime: ");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    int lastSeenDateTime = scanner.nextInt();
                    scanner.nextLine();

                    if(lastSeenDateTime == 1) {
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

                        LocalDateTime lastSeenDatetime = LocalDateTime.of(year, month, day, hour, minute);
                        missingPersonReport.setLastSeenDatetime(lastSeenDatetime);

                    } else if (lastSeenDateTime == 2) {
                        missingPersonReport.setLastSeenDatetime(null);
                    } else {
                        System.out.println("Invalid option.");
                        return;
                    }
                    break;
                case 2:
                    System.out.println("New last known location (optional): ");
                    String lastKnownLocation = scanner.nextLine().trim();
                    if (lastKnownLocation.isEmpty()) {
                        lastKnownLocation = null;
                    }
                    missingPersonReport.setLastKnownLocation(lastKnownLocation);
                    break;
                case 3:
                    System.out.println("New reported by name: ");
                    missingPersonReport.setReportedByName(scanner.nextLine().trim());
                    break;
                case 4:
                    System.out.println("New reported by phone (optional): ");
                    String reportedByPhone = scanner.nextLine().trim();
                    if (reportedByPhone.isEmpty()) {
                        reportedByPhone = null;
                    }
                    missingPersonReport.setReportedByPhone(reportedByPhone);
                    break;
                case 5:
                    System.out.println("New status: ");
                    System.out.println("1. MISSING");
                    System.out.println("2. LOCATED_SAFE");
                    System.out.println("3. HOSPITALIZED");
                    System.out.println("4. DECEASED");
                    System.out.println("5. CANCELLED");

                    System.out.println("Choose status: ");
                    int statusOption = scanner.nextInt();
                    scanner.nextLine();

                    if(statusOption == 1) {
                        missingPersonReport.setStatus(MissingPersonStatus.MISSING);
                        missingPersonReport.setResolvedDatetime(null);

                    } else {
                        switch (statusOption) {
                            case 2:
                                missingPersonReport.setStatus(MissingPersonStatus.LOCATED_SAFE);
                                break;
                            case 3:
                                missingPersonReport.setStatus(MissingPersonStatus.HOSPITALIZED);
                                break;
                            case 4:
                                missingPersonReport.setStatus(MissingPersonStatus.DECEASED);
                                break;
                            case 5:
                                missingPersonReport.setStatus(MissingPersonStatus.CANCELLED);
                                break;
                            default:
                                System.out.println("Invalid status.");
                                return;
                        }
                        System.out.println("Resolved datetime: ");
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
                        LocalDateTime resolvedDatetime = LocalDateTime.of(year, month, day, hour, minute);
                        missingPersonReport.setResolvedDatetime(resolvedDatetime);
                    }
                    break;
                case 6:
                    System.out.println("New notes (optional): ");
                    String notes = scanner.nextLine().trim();
                    if(notes.isEmpty()) {
                        notes = null;
                    }
                    missingPersonReport.setNotes(notes);
                    break;
                default:
                    System.out.println("Invalid option.");
                    return;
            }
            missingPersonReportService.updateMissingPersonReport(missingPersonReport);
            System.out.println("Missing person report updated successfully.");
        }  catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteMissingPersonReport() {
        System.out.println("Missing person report ID: ");
        int missingPersonReportId = scanner.nextInt();
        scanner.nextLine();

        try{
            missingPersonReportService.deleteMissingPersonReport(missingPersonReportId);
            System.out.println("Missing person report deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
