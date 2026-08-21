package ui;

import repository.*;
import service.*;

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

   private final EvacuationRecordRepository evacuationRecordRepository = new EvacuationRecordRepository();
   private final EvacuationRecordService evacuationRecordService = new EvacuationRecordService(evacuationRecordRepository, personService, emergencyEventService);
   private final EvacuationRecordMenu evacuationRecordMenu = new EvacuationRecordMenu(scanner, evacuationRecordService);

   private final AccommodationRepository accommodationRepository = new AccommodationRepository();
   private final AccommodationService accommodationService = new AccommodationService(accommodationRepository, evacuationRecordService, shelterService);
   private final AccommodationMenu accommodationMenu = new AccommodationMenu(scanner, accommodationService);


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
                       evacuationRecordMenu.show();
                       break;
                   case 5:
                       accommodationMenu.show();
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
