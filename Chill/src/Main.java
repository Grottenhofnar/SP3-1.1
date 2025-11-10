import Utilities.TextUI;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

TextUI ui = new TextUI();
Scanner sc = new Scanner(System.in);

public void main(String[] args) {

    startMenu();
}

public void startMenu(){
    ui.displayMsg("Velkommen til Chill! Du er følgende valgmuligheder: ");
    ui.displayMsg("1. Opret dig som bruger");
    ui.displayMsg("2. Log ind");
    System.out.print("Vælg: ");
    String choice = sc.nextLine();

    switch (choice) {
        case "1":
            createUser();
            break;
        case "2":
            logInUser();
            break;
        default:
            ui.displayMsg("Ugyldigt valg. Prøv venligst igen.");
            startMenu();
    }

}

private void createUser(){
    try{
        File dir = new File("CSV");

        FileWriter writer = new FileWriter("CSV/Brugere.txt", true);

        ui.displayMsg("Indtast dit brugernavn: ");
        String accountName = sc.nextLine();

        ui.displayMsg("Indtast din adgangskode: ");
        String password = sc.nextLine();

        writer.write(accountName + ";" + password + "\n");
        writer.close();

        ui.displayMsg("Kontoen er nu oprettet. Velkommen til Chill!");
        startMenu();
    } catch (IOException e) {
        ui.displayMsg("Der er desværre sket en fejl. Prøv venligst igen.");
        e.printStackTrace();
    }
}


private void logInUser(){
    File accounts = new File("CSV/Brugere.txt");
    if (!accounts.exists()) {
        ui.displayMsg("Kontoen blev ikke fundet. Prøv igen, eller opret dig");
        startMenu();
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
        mainMenu();
    } else {
        ui.displayMsg("Forkert brugernavn, eller adgangskode");
        startMenu();
    }
}

public void mainMenu(){
    ui.displayMsg("Velkommen til Chill!");

    ui.displayMsg("1. Søg efter film");
    ui.displayMsg("2. Se din liste over sete film");
    ui.displayMsg("3. Se din liste over gemte film");
    ui.displayMsg("4. Log ud");
    System.out.print("Vælg: ");
    String choice = sc.nextLine();

    switch (choice) {
        case "1":
            searchMovies();
            break;
        case "2":
            viewList("CSV/FilmSet.txt", "Sete film");
            break;
        case "3":
            viewList("CSV/SeSenere.txt", "Se senere liste");
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


public void searchMovies(){
ArrayList<Movies> movies = loadMovies();

ui.displayMsg("Du kan vælge at søge efter film med følgende muligheder: ");
ui.displayMsg("1. Efter navn");
ui.displayMsg("2. Efter kategorier");
ui.displayMsg("3. Efter udgivelsesår");
ui.displayMsg("4. Efter bedømmelse");
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
        ui.displayMsg("Indtast kategori:");
        String genres = sc.nextLine().toLowerCase();
        for (Movies m : movies) {
            if (m.getGenres().contains(genres)) results.add(m);
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

    default:
        ui.displayMsg("Ugyldigt valg.");
        searchMovies();
        return;
}
    if (results.isEmpty()) {
        ui.displayMsg("Ingen film fundet.");
        mainMenu();

    } else {
        ui.displayMsg("Følgende film blev fundet: ");
        for (int i = 0; i < results.size(); i++) {
            ui.displayMsg((i+1) + ". " + results.get(i));
        }

        ui.displayMsg("Vælge en film at se");
        int index = Integer.parseInt(sc.nextLine()) - 1;

        if (index >= 0 && index < results.size()) {
            movieMenu(results.get(index));
        }

    }

}

public void movieMenu(Movies movies){
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
            mainMenu();
            break;
        case "2":
            saveMovie(movies, "CSV/SeeLater.txt");
            ui.displayMsg(movies.getName() + " er gemt til din se senere liste");
            mainMenu();
            break;
        case "3":
             removeMovie(movies, "CSV/SeeLater.txt");
             ui.displayMsg(movies.getName() + " er fjernet fra din se senere liste");
             mainMenu();
             break;
        case "4":
            mainMenu();
            break;
        default:
            movieMenu(movies);
    }
}

public ArrayList<Movies> loadMovies(){
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

public void saveMovie(Movies movies, String filename){
    try (FileWriter writer = new FileWriter(filename,true)){
        writer.write(movies.getName() + ";" + movies.getGenres() + ";" + movies.getReleaseYear() + movies.getRating());
    } catch (IOException e){
        System.out.println("Fejl under gemning af film.");
    }
}

public void removeMovie(Movies movies, String filename){
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

public void viewList(String filename, String listName) {
    File file = new File(filename);
    if(!file.exists()) {
        ui.displayMsg("Ingen film i " + listName + ".");
        mainMenu();
        return;
    }

    ui.displayMsg("Liste " + listName);
    try (Scanner reader = new Scanner(file)) {
        int i = 1;
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            ui.displayMsg(i++ + "." + line);
        }
    } catch (FileNotFoundException e) {
        ui.displayMsg("Kunne ikke finde filmen: " + filename);
    }

    ui.displayMsg("Tryk enter for at gå tilbage.");
    sc.nextLine();
    mainMenu();
}



