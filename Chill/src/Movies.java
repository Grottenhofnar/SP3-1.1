
import java.util.Arrays;
import java.util.List;

public class Movies {

    private String name;
    private int releaseYear;
    private List<String> genres;
    private double rating;

    public Movies(String name, int releaseYear, String genresString, double rating) {
        this.name = name;
        this.releaseYear = releaseYear;
        this.genres = Arrays.asList(genresString.split(",\\s*"));
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

    public boolean hasGenre(String genre) {
        for (String s : genres) {
            if (s.equalsIgnoreCase(genre)) return true;
        }
        return false;
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
