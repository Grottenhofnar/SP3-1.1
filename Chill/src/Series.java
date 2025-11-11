import Utilities.TextUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Series {

    private static TextUI ui = new TextUI();
    private static Scanner sc = new Scanner(System.in);
    private String name;
    private int runningYear;
    private List<String> genres;
    private double rating;
    private Map<Integer, Integer> seasonEpisodes;

    public Series(String name, int runningYear, String genreString, double rating, String seasonEpisodeString)
    {
        this.name = name;
        this.runningYear = runningYear;
        this.genres = Arrays.asList(genreString.split(",\\s*"));;
        this.rating = rating;
        this.seasonEpisodes = parseSeasonEpisodes(seasonEpisodeString);
    }

    private Map<Integer, Integer> parseSeasonEpisodes(String input) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        if (input == null || input.isEmpty()) return map;

        input = input.replaceAll("[;\\s]+$", "");

        String[] parts = input.split(",\\s*");
        for (String part : parts) {
            part = part.trim();
            if (part.isEmpty()) continue;


            String[] se = part.split("-");

            if (se.length != 2) {

                ui.displayMsg("Wrong season episode format" + part);
                continue;
            }
                try {
                    int season = Integer.parseInt(se[0].trim());
                    int episodes = Integer.parseInt(se[1].trim());
                    map.put(season, episodes);
                } catch (NumberFormatException e) {
                    ui.displayMsg("Could not parse season & episodes: " + part);
                }
            } return map;

    }

    public Map<Integer, Integer> getSeasonEpisodes() {
        return seasonEpisodes;
    }

    public int getRunningYear() {
        return runningYear;
    }

    public List<String> getGenres() {
        return genres;
    }

    public double getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public boolean hasGenres(String genre) {
        for (String g : genres) {
            if (g.equalsIgnoreCase(genre)) return true;
        }
        return false;
        }


    public static void searchSeries(){
        ArrayList<Series> series = loadSeries();

        ui.displayMsg("Du kan vælge at søge efter serier med følgende muligheder: ");
        ui.displayMsg("1. Efter navn");
        ui.displayMsg("2. Efter kategorier");
        ui.displayMsg("3. Efter Året det startede");
        ui.displayMsg("4. Efter bedømmelse");
        ui.displayMsg("5. Print alle serier ud");
        System.out.print("Vælg: ");
        String choice = sc.nextLine();

        ArrayList<Series> results = new ArrayList<>();

        switch (choice) {
            case "1":
                ui.displayMsg("Indtast venligst seriens navn: ");
                String inputName = sc.nextLine().toLowerCase();
                for (Series s : series) {
                    if (s.getName().toLowerCase().contains(inputName)) results.add(s);
                }
                break;

            case "2":
                ui.displayMsg("Indtast kategori: ");
                String genres = sc.nextLine();
                for (Series s : series) {
                    if (s.hasGenres(genres)) {
                        results.add(s);
                    }
                }
                break;

            case "3":
                ui.displayMsg("Indtast udgivelsesår: ");
                int year = Integer.parseInt(sc.nextLine());
                for (Series s : series) {
                    if (s.getRunningYear() == year) results.add(s);
                }
                break;
            case "4":
                ui.displayMsg("Minimum bedømmelse: ");
                double minRating = Double.parseDouble(sc.nextLine());
                for (Series s : series) {
                    if (s.getRating() >= minRating) results.add(s);
                }
                break;
            case "5":
                for (Series s : series) {
                    results.add(s);
                }
                break;
            default:
                ui.displayMsg("Ugyldigt valg.");
                searchSeries();
                return;
        }
        if (results.isEmpty()) {
            ui.displayMsg("Ingen serier fundet.");
            Menu.mainMenu();

        } else {
            ui.displayMsg("Følgende film blev fundet: ");
            for (int i = 0; i < results.size(); i++) {
                ui.displayMsg((i+1) + ". " + results.get(i));
            }
            ui.displayMsg("Vælg en film at se");
            int index = Integer.parseInt(sc.nextLine()) - 1;

            if (index >= 0 && index < results.size()) {
                Menu.seriesMenu(results.get(index));

            }
        }
    }

    public static ArrayList<Series> loadSeries() {
        ArrayList<Series> series = new ArrayList<>();
        File file = new File("CSV/serier.csv");

        try (Scanner reader = new Scanner(file, "Windows-1252")) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length == 5) {
                    String name = parts[0].trim();
                    int goingYear = Integer.parseInt(parts[1].trim());
                    String genreString = parts[2].trim();
                    String ratingStr = parts[3].trim().replace(",", ".");
                    double rating = Double.parseDouble(ratingStr);
                    String seasonEpisodeString = parts[4].trim();

                    series.add(new Series(name, goingYear, genreString, rating, seasonEpisodeString));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return series;
    }

    public static void saveSeries(Series series, String filename){
        try (FileWriter writer = new FileWriter(filename,true)){
            writer.write(series.getName() + ";" + series.getGenres() + ";" + series.getRunningYear() + series.getRating());
        } catch (IOException e){
            System.out.println("Fejl under gemning af film.");
        }
    }

    public static void removeSeries(Series series, String filename){
        try {
            File inputFile = new File(filename);
            File tempFile = new File("CSV/temp.txt");

            try (Scanner reader = new Scanner(inputFile);
                 FileWriter writer = new FileWriter(tempFile)) {

                while (reader.hasNextLine()) {
                    String line = reader.nextLine();
                    if (!line.startsWith(series.getName() + ";")) {
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
    public static void seriesPlayingMenu(){
        ui.displayMsg("1. Sæt serien på pause");
        ui.displayMsg("2. Vælg en anden serie");
        System.out.print("Vælg: ");
        String Choice =  sc.nextLine();

        switch (Choice) {
            case "1": ui.displayMsg("Serien er nu sat på pause. Tryk på hvilken som helst knap for at starte igen");
                String startSeries = sc.nextLine();
                if (!startSeries.isEmpty() || startSeries.isEmpty()) {
                    ui.displayMsg("Filmen er begyndt igen");
                    seriesPlayingMenu();
                }
                break;
            case "2": searchSeries();
                break;
            default:
                ui.displayMsg("Inputtet findes ikke. Prøv med 1 eller 2");
                seriesPlayingMenu();

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

    @Override
    public String toString() {
        return "Series{" +
                "name='" + name + '\'' +
                ", runningYear=" + runningYear +
                ", genres=" + genres +
                ", rating=" + rating +
                ", seasonAndEpisodes=" + seasonEpisodes +
                '}';
    }
}







