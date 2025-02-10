package org.example;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Period;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImportCSV {

    public List<Movie> movies = new ArrayList<>();
    public Map<String, Actor> actors = new HashMap<>();
    public Map<String, Director> directors = new HashMap<>();


    public void loadData(String fileName, Consumer<String[]> processor) {
        long startTime = System.nanoTime();
        try (Stream<String> lines = Files.lines(Paths.get(getClass().getClassLoader().getResource(fileName).toURI()))) {
            lines.skip(1).map(line -> line.split(","))
                    .filter(parts -> parts.length > 0)
                    .forEach(processor);
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error reading file: " + fileName + " - " + e.getMessage());
        }
        long endTime = System.nanoTime();
        System.out.printf("Loaded data from %s in %.2f ms\n", fileName, (endTime - startTime) / 1_000_000.0);
    }

    public void loadMovies() {
        loadData("movies_large.csv", parts -> {
            if (parts.length >= 8) {
                String id = parts[0];
                String title = parts[1];
                int year = Integer.parseInt(parts[2].trim());
                String genre = parts[3];
                double rating = Double.parseDouble(parts[4].trim());
                int duration = Integer.parseInt(parts[5].trim());
                String directorId = parts[6];
                String actorIdsString = parts[7].replace("\"", "").trim();
                List<String> actorIds = actorIdsString.isEmpty() ? new ArrayList<>() : Arrays.asList(actorIdsString.split(","));
                movies.add(new Movie(id, title, year, genre, rating, duration, directorId, actorIds));
            }
        });
    }

    public void loadActors() {
        loadData("actors_large.csv", parts -> {
            if (parts.length >= 4) {
                String id = parts[0];
                String name = parts[1];
                String dob = parts[2];
                String nationality = parts[3];
                actors.put(id, new Actor(id, name, dob, nationality));
            }
        });
    }

    public void loadDirectors() {
        loadData("directors_large.csv", parts -> {
            if (parts.length >= 4) {
                String id = parts[0];
                String name = parts[1];
                String dob = parts[2];
                String nationality = parts[3];
                directors.put(id, new Director(id, name, dob, nationality));
            }
        });
    }

    // Get Movie Information by ID or Title
    public void getMovieInformation(String input) {
        Optional<Movie> movieOpt = movies.stream()
                .filter(m -> m.getId().equals(input) || m.getTitle().equalsIgnoreCase(input))
                .findFirst();

        if (movieOpt.isPresent()) {
            Movie movie = movieOpt.get();
            String directorName = directors.getOrDefault(movie.getDirectorId(), new Director("", "Unknown", "", "")).getName();
            List<String> actorNames = movie.getActorIds().stream()
                    .map(id -> actors.getOrDefault(id, new Actor(id, "Unknown", "", "")).getName())
                    .toList();

            System.out.println("\nMovie Details:");
            System.out.println("Title: " + movie.getTitle());
            System.out.println("Release Year: " + movie.getYear());
            System.out.println("Genre: " + movie.getGenre());
            System.out.println("Rating: " + movie.getRating());
            System.out.println("Duration: " + movie.getDuration() + " mins");
            System.out.println("Director: " + directorName);
            System.out.println("Actors: " + String.join(", ", actorNames));
        } else {
            System.out.println("Movie not found.");
        }
    }

    public void getTop10RatedMovies() {
        System.out.println("\nTop 10 Rated Movies:");
        movies.stream()
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed())
                .limit(10)
                .forEach(m -> System.out.printf("%s (%.1f)\n", m.getTitle(), m.getRating()));
    }

    public void getMoviesByGenre(String genre) {
        List<Movie> filteredMovies = movies.stream()
                .filter(m -> m.getGenre().equalsIgnoreCase(genre))
                .toList();

        if (filteredMovies.isEmpty()) {
            System.out.println("No movies found in this genre.");
        } else {
            System.out.println("\nMovies in " + genre + " Genre:");
            filteredMovies.forEach(m -> System.out.println(m.getTitle() + " (" + m.getYear() + ")"));
        }
    }

    public void getMoviesByDirector(String directorName) {
        List<Movie> filteredMovies = movies.stream()
                .filter(m -> directors.getOrDefault(m.getDirectorId(), new Director("", "Unknown", "", "")).getName().equalsIgnoreCase(directorName))
                .toList();

        if (filteredMovies.isEmpty()) {
            System.out.println("No movies found for this director.");
        } else {
            System.out.println("\nMovies by Director " + directorName + ":");
            filteredMovies.forEach(m -> System.out.println(m.getTitle() + " (" + m.getYear() + ")"));
        }
    }

    public void getMoviesByYear(int releaseYear) {
        List<Movie> filteredMovies = movies.stream()
                .filter(m -> m.getYear() == releaseYear)
                .collect(Collectors.toList());

        if (filteredMovies.isEmpty()) {
            System.out.println("No movies found for this year.");
        } else {
            System.out.println("\nMovies released in " + releaseYear + ":");
            filteredMovies.forEach(m -> System.out.println(m.getTitle()));
        }
    }

    public void getMoviesByYearRange(int startYear, int endYear) {
        List<Movie> filteredMovies = movies.stream()
                .filter(m -> m.getYear() >= startYear && m.getYear() <= endYear)
                .toList();

        if (filteredMovies.isEmpty()) {
            System.out.println("No movies found in this year range.");
        } else {
            System.out.println("\nMovies released between " + startYear + " and " + endYear + ":");
            filteredMovies.forEach(m -> System.out.println(m.getTitle() + " (" + m.getYear() + ")"));
        }
    }

    public void addNewMovie(Movie movie) {
        movies.add(movie);
        System.out.println("Movie added successfully!");
    }

    public void updateMovieRating(String movieId, double newRating) {
        movies.stream()
                .filter(m -> m.getId().equals(movieId))
                .findFirst()
                .ifPresentOrElse(
                        movie -> {
                            movie.setRating(newRating);
                            System.out.println("Rating updated successfully!");
                        },
                        () -> System.out.println("Movie not found."));
    }

    public void deleteMovie(String movieId) {
        if (movies.removeIf(m -> m.getId().equals(movieId))) {
            System.out.println("Movie deleted successfully!");
        } else {
            System.out.println("Movie not found.");
        }
    }

    public void sortAndDisplayMoviesByYear() {
        System.out.println("\nMovies sorted by Release Year:");
        movies.stream()
                .sorted(Comparator.comparingInt(Movie::getYear))
                .limit(15)
                .forEach(m -> System.out.println(m.getTitle() + " (" + m.getYear() + ")"));
    }

    public void getTop5DirectorsByMovies() {
        System.out.println("\nTop 5 Directors with Most Movies:");
        movies.stream()
                .collect(Collectors.groupingBy(Movie::getDirectorId, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> {
                    String directorName = directors.getOrDefault(entry.getKey(), new Director("", "Unknown", "", "")).getName();
                    System.out.printf("%s (%d movies)\n", directorName, entry.getValue());
                });
    }


    public void getActorsWithMultipleMovies() {
        System.out.println("\nActors with Multiple Movies:");

        // Count occurrences of each actor in the movies
        Map<String, Long> actorCounts = movies.stream()
                .flatMap(movie -> movie.getActorIds().stream())
                .collect(Collectors.groupingBy(actorId -> actorId, Collectors.counting()));

        // Filter and display actors who acted in more than one movie
        actorCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .forEach(entry -> {
                    String actorId = entry.getKey();
                    Actor actor = actors.getOrDefault(actorId, new Actor(actorId, "Unknown", "", ""));
                    System.out.printf("%s (%s) - Movies: %d\n", actor.getName(), actor.getNationality(), entry.getValue());
                });
    }

    public void getMoviesOfYoungestActor() {
        System.out.println("\nMovies of the Youngest Actor as of 10-02-2025:");

        // Define the target date
        LocalDate referenceDate = LocalDate.of(2025, 2, 10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Find the youngest actor
        Optional<Actor> youngestActorOpt = actors.values().stream()
                .filter(actor -> !actor.getDateOfBirth().isEmpty())
                .min(Comparator.comparing(actor -> {
                    LocalDate dob = LocalDate.parse(actor.getDateOfBirth(), formatter);
                    return Period.between(dob, referenceDate).getYears();
                }));

        if (youngestActorOpt.isPresent()) {
            Actor youngestActor = youngestActorOpt.get();
            LocalDate dob = LocalDate.parse(youngestActor.getDateOfBirth(), formatter);
            int age = Period.between(dob, referenceDate).getYears();

            System.out.printf("Youngest Actor: %s (Age: %d)\n", youngestActor.getName(), age);

            // Get movies featuring the youngest actor
            List<Movie> actorMovies = movies.stream()
                    .filter(movie -> movie.getActorIds().contains(youngestActor.getId()))
                    .toList();

            if (!actorMovies.isEmpty()) {
                System.out.println("\nMovies:");
                actorMovies.forEach(movie ->
                        System.out.printf("Title: %s, Release Year: %d, Genre: %s, Rating: %.1f\n",
                                movie.getTitle(), movie.getYear(), movie.getGenre(), movie.getRating()));
            } else {
                System.out.println("No movies found for this actor.");
            }
        } else {
            System.out.println("No valid actor data found.");
        }
    }


}