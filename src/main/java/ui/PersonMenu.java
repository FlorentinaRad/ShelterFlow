package ui;

import model.Person;
import service.PersonService;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class PersonMenu {
    private final Scanner scanner;
    private final PersonService personService;

    public PersonMenu(Scanner scanner, PersonService personService) {
        this.scanner = scanner;
        this.personService = personService;
    }

    public void show() {
        while (true) {
            System.out.println("Persons");
            System.out.println("1. Register person");
            System.out.println("2. Find person by ID");
            System.out.println("3. List all persons");
            System.out.println("4. Update person");
            System.out.println("5. Delete person");
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
                        registerPerson();
                        break;
                    case 2:
                        findPersonById();
                        break;
                    case 3:
                        listAllPersons();
                        break;
                    case 4:
                        updatePerson();
                        break;
                    case 5:
                        deletePerson();
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch(InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            } catch (DateTimeException e) {
                System.out.println("Invalid date.");
            }
        }
    }

    private void registerPerson() {
        System.out.println();
        System.out.println("Register Person");

        System.out.println("First name: ");
        String firstName = scanner.nextLine().trim();

        System.out.println("Last name: ");
        String lastName = scanner.nextLine().trim();

        System.out.println("Birth year: ");
        int year = scanner.nextInt();
        System.out.println("Birth month: ");
        int month = scanner.nextInt();
        System.out.println("Birth day: ");
        int day = scanner.nextInt();
        scanner.nextLine();

        LocalDate birthDate = LocalDate.of(year, month, day);

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

        System.out.println("Home country (optional): ");
        String homeCountry = scanner.nextLine().trim();
        if (homeCountry.isEmpty()) {
            homeCountry = null;
        }

        System.out.println("Home county (optional): ");
        String homeCounty = scanner.nextLine().trim();
        if (homeCounty.isEmpty()) {
            homeCounty = null;
        }

        System.out.println("Home locality (optional): ");
        String homeLocality = scanner.nextLine().trim();
        if (homeLocality.isEmpty()) {
            homeLocality = null;
        }

        System.out.print("Home address (optional): ");
        String homeAddress = scanner.nextLine().trim();
        if (homeAddress.isEmpty()) {
            homeAddress = null;
        }

        System.out.print("Notes (optional): ");
        String notes = scanner.nextLine().trim();
        if (notes.isEmpty()) {
            notes = null;
        }

        Person person = new Person(
                firstName,
                lastName,
                birthDate,
                phoneNumber,
                email,
                homeCountry,
                homeCounty,
                homeLocality,
                homeAddress,
                notes
        );

        try {
            personService.registerPerson(person);
            System.out.println("Person registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findPersonById() {
        System.out.println("Person ID: ");
        int personId = scanner.nextInt();
        scanner.nextLine();

        try{
            Person person = personService.requirePersonById(personId);
            System.out.println(person);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listAllPersons() {
        try{
            List<Person> persons = personService.listAllPersons();
            for (Person person : persons) {
                System.out.println(person);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updatePerson() {
        System.out.println("Person ID: ");
        int personId = scanner.nextInt();
        scanner.nextLine();

        try {
            Person person = personService.requirePersonById(personId);

            System.out.println("1. First name");
            System.out.println("2. Last name");
            System.out.println("3. Birth date");
            System.out.println("4. Phone number");
            System.out.println("5. Email");
            System.out.println("6. Home country");
            System.out.println("7. Home county");
            System.out.println("8. Home locality");
            System.out.println("9. Home address");
            System.out.println("10. Notes");

            System.out.println("Choose field to update: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.print("New first name: ");
                    person.setFirstName(scanner.nextLine().trim());
                    break;
                case 2:
                    System.out.print("New last name: ");
                    person.setLastName(scanner.nextLine().trim());
                    break;
                case 3:
                    System.out.print("New birth year: ");
                    int year = scanner.nextInt();
                    System.out.print("New birth month: ");
                    int month = scanner.nextInt();
                    System.out.print("New birth day: ");
                    int day = scanner.nextInt();
                    scanner.nextLine();
                    LocalDate birthDate = LocalDate.of(year, month, day);
                    person.setBirthDate(birthDate);
                    break;
                case 4:
                    System.out.print("New phone number: ");
                    String phoneNumber = scanner.nextLine().trim();
                    if (phoneNumber.isEmpty()) {
                        phoneNumber = null;
                    }
                    person.setPhoneNumber(phoneNumber);
                    break;
                case 5:
                    System.out.print("New email: ");
                    String email = scanner.nextLine().trim();
                    if (email.isEmpty()) {
                        email = null;
                    }
                    person.setEmail(email);
                    break;
                case 6:
                    System.out.print("New home country: ");
                    String homeCountry = scanner.nextLine().trim();
                    if (homeCountry.isEmpty()) {
                        homeCountry = null;
                    }
                    person.setHomeCountry(homeCountry);
                    break;
                case 7:
                    System.out.print("New home county: ");
                    String homeCounty = scanner.nextLine().trim();
                    if (homeCounty.isEmpty()) {
                        homeCounty = null;
                    }
                    person.setHomeCounty(homeCounty);
                    break;
                case 8:
                    System.out.print("New home locality: ");
                    String locality = scanner.nextLine().trim();
                    if (locality.isEmpty()) {
                        locality = null;
                    }
                    person.setHomeLocality(locality);
                    break;
                case 9:
                    System.out.print("New home address: ");
                    String homeAddress = scanner.nextLine().trim();
                    if (homeAddress.isEmpty()) {
                        homeAddress = null;
                    }
                    person.setHomeAddress(homeAddress);
                    break;
                case 10:
                    System.out.print("New notes: ");
                    String notes = scanner.nextLine().trim();
                    if (notes.isEmpty()) {
                        notes = null;
                    }
                    person.setNotes(notes);
                    break;
                default:
                    System.out.println("Invalid option.");
                    return;
            }

            personService.updatePerson(person);
            System.out.println("Person updated successfully.");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deletePerson(){
        System.out.println("Person ID: ");
        int personId = scanner.nextInt();
        scanner.nextLine();

        try{
            personService.deletePerson(personId);
            System.out.println("Person deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
