import Utilities.TextUI;
import java.util.Scanner;

public class Menu {

    static TextUI ui = new TextUI();
    static Scanner sc = new Scanner(System.in);

    public static void startMenu(){
        ui.displayMsg("Velkommen til Chill! Du er følgende valgmuligheder: ");
        ui.displayMsg("1. Opret dig som bruger");
        ui.displayMsg("2. Log ind");
        ui.displayMsg("3. Luk programmet");
        System.out.print("Vælg: ");
        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                User.createUser();
                break;
            case "2":
                User.logInUser();
                break;
            case "3":
                break;
            default:
                ui.displayMsg("Ugyldigt valg. Prøv venligst igen.");
                startMenu();
        }

    }
    public static void mainMenu(){
        ui.displayMsg("********** MENU **********");
        ui.displayMsg("1. Søg efter film");
        ui.displayMsg("2. Se din liste over sete film");
        ui.displayMsg("3. Se din liste over gemte film");
        ui.displayMsg("4. Log ud");
        System.out.print("Vælg: ");
        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                Movies.searchMovies();
                break;
            case "2":
                Movies.viewList("CSV/FilmSet.txt", "Sete film: ");
                break;
            case "3":
                Movies.viewList("CSV/SeSenere.txt", "Se senere: ");
                break;
            case "4":
                ui.displayMsg("Du er nu logget ud.");
                startMenu();
                break;
            default:
                ui.displayMsg("Ugyldigt valg. Prøv venligst igen.");
                mainMenu();
        }
    }
}


