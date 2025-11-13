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
    private String genreString;
    private double rating;
    private Map<Integer, Integer> seasonEpisodes;
    private String seasonEpisodeString;

    public Series(String name, int runningYear, String genreString, double rating, String seasonEpisodeString) {
        this.name = name;
        this.runningYear = runningYear;
        this.genres = Arrays.asList(genreString.split(",\\s*"));
        ;
        this.genreString = genreString.trim();
        this.seasonEpisodeString = seasonEpisodeString.trim();
        this.rating = rating;
        this.seasonEpisodes = parseSeasonEpisodes(this.seasonEpisodeString);
    }

    private Map<Integer, Integer> parseSeasonEpisodes(String input) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        if (input == null || input.isEmpty()) return map;

        input = input.replaceAll("\\s*;\\s*", ", ").trim();
        input = input.replaceAll("[,\\s]+$", "");

        String[] parts = input.split("[,]\\s*");

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
        }
        return map;
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

    public String getSeasonEpisodeString() {
        return seasonEpisodeString;
    }

    public String getGenreString() {
        return genreString;
    }

    public boolean hasGenres(String genre) {
        for (String g : genres) {
            if (g.equalsIgnoreCase(genre)) return true;
        }
        return false;
    }

    public static void searchSeries() {
        ArrayList<Series> series = loadSeries();

        ui.displayMsg("Du kan vælge at søge efter serier med følgende muligheder: ");
        ui.displayMsg("1. Efter navn");
        ui.displayMsg("2. Efter kategorier");
        ui.displayMsg("3. Efter Året det startede");
        ui.displayMsg("4. Efter bedømmelse");
        ui.displayMsg("5. Print alle serier ud");
        ui.displayMsg("6. Tilbage");
        System.out.print("Vælg: ");
        String choice = sc.nextLine();

        ArrayList<Series> results = new ArrayList<>();
        try {
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
                case "6":
                    Menu.mainMenu();
                    break;
                default:
                    ui.displayMsg("Ugyldigt valg.");
                    searchSeries();
                    return;
            }

            if (results.isEmpty()) {
                ui.displayMsg("Ingen serier fundet.");
                searchSeries();

            } else {
                ui.displayMsg("Følgende serier blev fundet: ");
                for (int i = 0; i < results.size(); i++) {
                    ui.displayMsg((i + 1) + ". " + results.get(i));
                }
                ui.displayMsg("Vælg en serie at se");
                int index = Integer.parseInt(sc.nextLine()) - 1;

                if (index >= 0 && index < results.size()) {
                    Menu.seriesMenu(results.get(index));

                }
            }
        } catch (NumberFormatException e) {
            ui.displayMsg("Forkert input.");
            searchSeries();
        }
    }
    public static ArrayList<Series> loadSeries() {
        ArrayList<Series> series = new ArrayList<>();
        File file = new File("CSV/serier.csv");

        try (Scanner reader = new Scanner(file, "Windows-1252")) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";", 5);
                if (parts.length < 5) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }
                String name = parts[0].trim();

                String yearField = parts[1].trim();
                String startYearStr = yearField.split("-")[0].trim();
                int goingYear = Integer.parseInt(startYearStr);

                String genreString = parts[2].trim();

                String ratingStr = parts[3].trim().replace(",", ".");
                double rating = Double.parseDouble(ratingStr);

                String seasonEpisodeString = parts[4].trim().replaceAll("\\s+", " ");

                series.add(new Series(name, goingYear, genreString, rating, seasonEpisodeString));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (NumberFormatException e) {
            System.out.println("Error parsing numeric value: " + e.getMessage());
        }

        return series;
    }
    public static void saveSeries(Series series, String filename) {
        File file = new File(filename);

        boolean duplicateFound = false;

        if (file.exists()) {
            try (Scanner reader = new Scanner(file)) {
                while (reader.hasNextLine()) {
                    String line = reader.nextLine().trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split(";", 5);
                    if (parts.length < 5) continue;

                    String existingName = parts[0].trim();
                    String existingYear = parts[1].trim();

                    if (existingName.equalsIgnoreCase(series.getName())
                            && existingYear.equals(String.valueOf(series.getRunningYear()))) {
                        duplicateFound = true;
                        break;
                    }
                }
            } catch (FileNotFoundException e) {
                ui.displayMsg("Kunne ikke finde filen: " + filename);
            }
        }

        if (!duplicateFound) {
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(series.getName() + ";"
                        + series.getRunningYear() + ";"
                        + series.getGenreString() + ";"
                        + series.getRating() + ";"
                        + series.getSeasonEpisodeString()
                        + System.lineSeparator());
                ui.displayMsg("Serien '" + series.getName() + "' blev gemt.");
            } catch (IOException e) {
                ui.displayMsg("Fejl under gemning af serien.");
            }
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
                        writer.write(line + System.lineSeparator());
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
        ui.displayMsg("3. Tilbage til hovedmenuen");
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
            case "3":
                Menu.mainMenu();
                break;
            default:
                ui.displayMsg("Inputtet findes ikke.");
                seriesPlayingMenu();
        }
    }

    public static void viewList(String filename, String listName) {
        File file = new File(filename);
        if(!file.exists()) {
            ui.displayMsg("Ingen serier i " + listName + ".");
            Menu.mainMenu();
            return;
        }

        ui.displayMsg(listName + "\n");

        try (Scanner reader = new Scanner(file)) {
            int i = 1;
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";", 5);
                if (parts.length < 5) {
                    ui.displayMsg(i++ + ". [Ugyldig linje] " + line);
                    continue;
                }

                String name = parts[0].trim();
                String year = parts[1].trim();
                String genres = parts[2].trim();
                String rating = parts[3].trim();
                String seasonEp = parts[4].trim();

                ui.displayMsg(i++ + ". " + name + " (" + year + ")\n"
                        + "   Genre(s): " + genres + "\n"
                        + "   Rating: " + rating + "\n"
                        + "   Sæsoner & episoder: " + seasonEp + "\n");
            }
        } catch (FileNotFoundException e) {
            ui.displayMsg("Kunne ikke finde serien: " + filename);
        }

        ui.displayMsg("Tryk på en vilkårlig tast for at gå tilbage.");
        sc.nextLine();
        Menu.mainMenu();
    }
    @Override
    public String toString() {
        return "Serie: " + name + "(" + rating + ")" + " - " + runningYear + " - " + genres;
    }
}
