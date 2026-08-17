package ui;

import repository.PersonRepository;
import service.PersonService;

import java.util.Scanner;

public class ConsoleApplication {
   private final Scanner scanner = new Scanner(System.in);

   private final PersonRepository personRepository = new PersonRepository();
   private final PersonService personService = new PersonService(personRepository);
   private final PersonMenu personMenu = new PersonMenu(scanner, personService);

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

           System.out.println("Choose an option: ");
           int option = scanner.nextInt();

           if (option == 0) {
               System.out.println("Exiting ShelterFlow...");
               break;
           }

           switch (option) {
               case 1:
                   personMenu.show();
                   break;
               case 2:
                   System.out.println("Emergency events selected");
                   break;
               case 3:
                   System.out.println("Shelters selected");
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
       }
   }
}
