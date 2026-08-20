package ui;

import repository.EmergencyEventRepository;
import repository.PersonRepository;
import repository.ShelterRepository;
import service.EmergencyEventService;
import service.PersonService;
import service.ShelterService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleApplication {
   private final Scanner scanner = new Scanner(System.in);

   private final PersonRepository personRepository = new PersonRepository();
   private final PersonService personService = new PersonService(personRepository);
   private final PersonMenu personMenu = new PersonMenu(scanner, personService);

   private final EmergencyEventRepository emergencyEventRepository = new EmergencyEventRepository();
   private final EmergencyEventService emergencyEventService = new EmergencyEventService(emergencyEventRepository);
   private final EmergencyEventMenu emergencyEventMenu = new EmergencyEventMenu(scanner, emergencyEventService);

   private final ShelterRepository shelterRepository = new ShelterRepository();
   private final ShelterService shelterService = new ShelterService(shelterRepository);
   private final ShelterMenu shelterMenu = new ShelterMenu(scanner, shelterService);

   public void run() {
       while (true) {
           System.out.println("ShelterFlow");
           System.out.println("1. Persons");
           System.out.println("2. Emergency events");
           System.out.println("3. Shelters");
           System.out.println("4. Evacuations");
           System.out.println("5. Accommodations");
           System.out.println("6. Missing person reports");
           System.out.println("0. Exit");

           try {
               System.out.println("Choose an option: ");
               int option = scanner.nextInt();
               scanner.nextLine();

               if (option == 0) {
                   System.out.println("Exiting ShelterFlow...");
                   break;
               }

               switch (option) {
                   case 1:
                       personMenu.show();
                       break;
                   case 2:
                       emergencyEventMenu.show();
                       break;
                   case 3:
                       shelterMenu.show();
                       break;
                   case 4:
                       System.out.println("Evacuations selected");
                       break;
                   case 5:
                       System.out.println("Accommodations selected");
                       break;
                   case 6:
                       System.out.println("Missing person reports selected");
                       break;
                   default:
                       System.out.println("Invalid option");
               }
           } catch(InputMismatchException e) {
               System.out.println("Invalid input. Please enter a number.");
               scanner.nextLine();
           }
       }
   }
}
