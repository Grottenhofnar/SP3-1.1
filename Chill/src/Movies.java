import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import Utilities.TextUI;

public class Movies {
    static TextUI ui = new TextUI();
    static Scanner sc = new Scanner(System.in);
    private String name;
    private int releaseYear;
    private List<String> genres;
    private double rating;

    public Movies(String name, int releaseYear, String genreString, double rating) {
        this.name = name;
        this.releaseYear = releaseYear;
        this.genres = Arrays.asList(genreString.split(",\\s*"));
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public List<String> getGenres() {
        return genres;
    }

    public double getRating() {
        return rating;
    }

    public boolean hasGenres(String genre) {
        for (String g : genres) {
            if (g.equalsIgnoreCase(genre)) return true;
        }
        return false;
    }

    public static void searchMovies() {
        ArrayList<Movies> movies = loadMovies();

        try {
            ui.displayMsg("Du kan vælge at søge efter film med følgende muligheder: ");
            ui.displayMsg("1. Efter navn");
            ui.displayMsg("2. Efter kategorier");
            ui.displayMsg("3. Efter udgivelsesår");
            ui.displayMsg("4. Efter bedømmelse");
            ui.displayMsg("5. Print alle film ud");
            ui.displayMsg("6. Tilbage");
            System.out.print("Vælg: ");
            String choice = sc.nextLine();

            ArrayList<Movies> results = new ArrayList<>();

            switch (choice) {
                case "1":
                    ui.displayMsg("Indtast venligst filmens navn: ");
                    String inputName = sc.nextLine().toLowerCase();
                    for (Movies m : movies) {
                        if (m.getName().toLowerCase().contains(inputName)) results.add(m);
                    }
                    break;
                case "2":
                    ui.displayMsg("Indtast kategori: ");
                    String genres = sc.nextLine();
                    for (Movies m : movies) {
                        if (m.hasGenres(genres)) {
                            results.add(m);
                        }
                    }
                    break;
                case "3":
                    ui.displayMsg("Indtast udgivelsesår: ");
                    int year = Integer.parseInt(sc.nextLine());
                    for (Movies m : movies) {
                        if (m.getReleaseYear() == year) results.add(m);
                    }
                    break;
                case "4":
                    ui.displayMsg("Minimum bedømmelse: ");
                    double minRating = Double.parseDouble(sc.nextLine());
                    for (Movies m : movies) {
                        if (m.getRating() >= minRating) results.add(m);
                    }
                    break;
                case "5":
                    for (Movies m : movies) {
                        results.add(m);
                    }
                    break;
                case "6":
                    Menu.mainMenu();
                    break;
                default:
                    ui.displayMsg("Ugyldigt valg.");
                    searchMovies();
                    return;
            }


            if (results.isEmpty()) {
                ui.displayMsg("Ingen film fundet.");
                Menu.mainMenu();

            } else {
                ui.displayMsg("Følgende film blev fundet: ");
                for (int i = 0; i < results.size(); i++) {
                    ui.displayMsg((i + 1) + ". " + results.get(i));
                }
                ui.displayMsg("Vælg en film at se");
                int index = Integer.parseInt(sc.nextLine()) - 1;

                if (index >= 0 && index < results.size()) {
                    Menu.movieMenu(results.get(index));

                }
            }
        } catch (NumberFormatException e) {
            ui.displayMsg("Forkert input");
            searchMovies();
        }
    }

    public static ArrayList<Movies> loadMovies(){
        ArrayList<Movies> movies = new ArrayList<>();
        File file = new File("CSV/film.csv");

        try (Scanner reader = new Scanner(file)){
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;


                String[] parts = line.split(";");
                if (parts.length == 4) {
                    String name = parts[0].trim();
                    int year = Integer.parseInt(parts[1].trim());
                    String genreString = parts[2].trim();
                    String ratingStr = parts[3].trim().replace(",", ".");
                    double rating = Double.parseDouble(ratingStr);

                    movies.add(new Movies(name, year, genreString, rating));
                }
            }
        } catch (FileNotFoundException e){
            ui.displayMsg("Filmen blev ikke fundet");
        }
        return movies;
    }

    public static void saveMovie(Movies movies, String filename){
        try (FileWriter writer = new FileWriter(filename,true)){
            writer.write(movies.getName() + ";" + movies.getGenres() + ";" + movies.getReleaseYear() + movies.getRating());
        } catch (IOException e){
            System.out.println("Fejl under gemning af film.");
        }
    }

    public static void removeMovie(Movies movies, String filename){
        try {
            File inputFile = new File(filename);
            File tempFile = new File("CSV/temp.txt");

            try (Scanner reader = new Scanner(inputFile);
                 FileWriter writer = new FileWriter(tempFile)) {

                while (reader.hasNextLine()) {
                    String line = reader.nextLine();
                    if (!line.startsWith(movies.getName() + ";")) {
                        writer.write(line);
                    }
                }
            }

            inputFile.delete();
            tempFile.renameTo(inputFile);

        } catch (IOException e) {
            ui.displayMsg("Fejl under fjernelse af film");
        }
    }

    public static void viewList(String filename, String listName) {
        File file = new File(filename);
        if(!file.exists()) {
            ui.displayMsg("Ingen film i " + listName + ".");
            Menu.mainMenu();
            return;
        }

        ui.displayMsg(listName);
        try (Scanner reader = new Scanner(file)) {
            int i = 1;
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                ui.displayMsg(i++ + "." + line);
            }
        } catch (FileNotFoundException e) {
            ui.displayMsg("Kunne ikke finde filmen: " + filename);
        }

        ui.displayMsg("Tryk på en vilkårlig tast for at gå tilbage.");
        sc.nextLine();
        Menu.mainMenu();
    }

    public static void playingMenu(){
        ui.displayMsg("1. Sæt filmen på pause");
        ui.displayMsg("2. Vælg en anden film");
        ui.displayMsg("3. Tilbage");
        System.out.print("Vælg: ");
        String Choice =  sc.nextLine();

        switch (Choice) {
            case "1": ui.displayMsg("Filmen er nu sat på pause. Tryk på hvilken som helst knap for at starte igen");
                String startMovie = sc.nextLine();
                if (!startMovie.isEmpty() || startMovie.isEmpty()) {
                    ui.displayMsg("Filmen er begyndt igen");
                    playingMenu();
                }
                break;
            case "2":
                searchMovies();
                break;
            case "3":
                playingMenu();
                break;
            default:
                ui.displayMsg("Inputtet findes ikke. Prøv med 1 eller 2");
                playingMenu();

        }
    }
    @Override
    public String toString() {
        return "Movies{" +
                "name='" + name + '\'' +
                ", releaseYear=" + releaseYear +
                ", genres=" + genres +
                ", rating=" + rating +
                '}';
    }
}
