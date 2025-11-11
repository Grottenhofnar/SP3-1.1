import Utilities.TextUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class User {

    static TextUI ui = new TextUI();
    static Scanner sc = new Scanner(System.in);

    public static void createUser(){
        try{
            File dir = new File("CSV");

            FileWriter writer = new FileWriter("CSV/Brugere.txt", true);

            ui.displayMsg("Indtast dit brugernavn: ");
            String accountName = sc.nextLine();

            ui.displayMsg("Indtast din adgangskode: ");
            String password = sc.nextLine();

            writer.write(accountName + ";" + password + "\n");
            writer.close();

            ui.displayMsg("Kontoen er nu oprettet.");
            Menu.startMenu();
        } catch (IOException e) {
            ui.displayMsg("Der er desværre sket en fejl. Prøv venligst igen.");
            e.printStackTrace();
        }
    }

    public static void logInUser(){
        File accounts = new File("CSV/Brugere.txt");
        if (!accounts.exists()) {
            ui.displayMsg("Kontoen blev ikke fundet. Prøv igen, eller opret dig");
            Menu.startMenu();
            return;
        }

        ui.displayMsg("Indtast dit brugernavn: ");
        String inputName = sc.nextLine();

        ui.displayMsg("Indtast din adgangskode: ");
        String inputPassword = sc.nextLine();

        boolean loginSuccess = false;

        try (Scanner reader = new Scanner(accounts)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    String name = parts[0];
                    String password = parts[1];
                    if (name.equals(inputName) && password.equals(inputPassword)) {
                        loginSuccess = true;
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            ui.displayMsg("Der opstod en fejl, prøv venligst igen");
            e.printStackTrace();
        }

        if (loginSuccess){
            Menu.mainMenu();
        } else {
            ui.displayMsg("Forkert brugernavn, eller adgangskode");
            Menu.startMenu();
        }
    }
}





