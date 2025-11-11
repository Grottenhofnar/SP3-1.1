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
        ui.displayMsg("2. Søg efter Serier");
        ui.displayMsg("3. Se din liste over sete film");
        ui.displayMsg("4. Se din liste over gemte film");
        ui.displayMsg("5. Log ud");
        System.out.print("Vælg: ");
        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                Movies.searchMovies();
                break;
            case "2":
                Series.searchSeries();
                break;
            case "3":
                Movies.viewList("CSV/FilmSet.txt", "Sete film: ");
                break;
            case "4":
                Movies.viewList("CSV/SeSenere.txt", "Se senere: ");
                break;
            case "5":
                ui.displayMsg("Du er nu logget ud.");
                startMenu();
                break;
            default:
                ui.displayMsg("Ugyldigt valg. Prøv venligst igen.");
                mainMenu();
        }
    }

    public static void movieMenu(Movies movies){
        ui.displayMsg(" Du har valgt: " + movies.getName());
        ui.displayMsg("1. Afspil filmen");
        ui.displayMsg("2. Gem til 'Se senere'");
        ui.displayMsg("3. Fjern fra 'Se senere'");
        ui.displayMsg("4. Tilbage");
        System.out.print("Vælg: ");
        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                ui.displayMsg("Afspiller nu: " + movies.getName());
                Movies.saveMovie(movies,"CSV/FilmSet.txt");
                Movies.playingMenu();
                break;
            case "2":
                Movies.saveMovie(movies, "CSV/SeSenere.txt");
                ui.displayMsg(movies.getName() + " er gemt til din se senere liste");
                Menu.mainMenu();
                break;
            case "3":
                Movies.removeMovie(movies, "CSV/SeSenere.txt");
                ui.displayMsg(movies.getName() + " er fjernet fra din se senere liste");
                Menu.mainMenu();
                break;
            case "4":
                Menu.mainMenu();
                break;
            default:
                movieMenu(movies);
        }
    }

    public static void seriesMenu(Series series){
        ui.displayMsg(" Du har valgt: " + series.getName());
        ui.displayMsg("1. Afspil Serien");
        ui.displayMsg("2. Gem til 'Se senere'");
        ui.displayMsg("3. Fjern fra 'Se senere'");
        ui.displayMsg("4. Se hvor mange sæsoner, og episoder den har");
        ui.displayMsg("5. Tilbage");
        System.out.print("Vælg: ");
        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                ui.displayMsg("Afspiller nu: " + series.getName());
                Series.saveSeries(series,"CSV/FilmSet.txt");
                Series.seriesPlayingMenu();
                break;
            case "2":
                Series.saveSeries(series, "CSV/SeSenere.txt");
                ui.displayMsg(series.getName() + " er gemt til din se senere liste");
                Menu.mainMenu();
                break;
            case "3":
                Series.removeSeries(series, "CSV/SeSenere.txt");
                ui.displayMsg(series.getName() + " er fjernet fra din se senere liste");
                Menu.mainMenu();
                break;
            case "4":
                series.getSeasonEpisodes();
                ui.displayMsg(series.getName()+" har: "+ series.getSeasonEpisodes() + "Sæsoner og episoder");
                break;
            case "5":
                Menu.mainMenu();
                break;
            default:
                seriesMenu(series);
        }
    }

}




