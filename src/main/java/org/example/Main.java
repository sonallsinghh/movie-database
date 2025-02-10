package org.example;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Movie Database System!!!");

        ImportCSV movieService = new ImportCSV();

        // Load CSV files from the resource directory
        movieService.loadMovies();
        movieService.loadActors();
        movieService.loadDirectors();

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nSelect an option:");
            System.out.println("1 : Get Movie information");
            System.out.println("2 : Get Top 10 rated movies");
            System.out.println("3 : Get Movies by Genre");
            System.out.println("4 : Get Movies by Director");
            System.out.println("5 : Get Movies by Release Year");
            System.out.println("6 : Get Movies by release year range");
            System.out.println("7 : Add a new movie");
            System.out.println("8 : Update Movie Rating");
            System.out.println("9 : Delete a movie");
            System.out.println("10 : Sort and return 15 movies by the release year");
            System.out.println("11 : Get Directors with the Most Movies");
            System.out.println("12 : Get Actors Who Have Worked in Multiple Movies");
            System.out.println("13 : Get the movies of the actor who is the youngest as of 10-02-2025.");
            System.out.println("14 : Exit");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Movie ID or Title: ");
                    String input = scanner.nextLine();
                    movieService.getMovieInformation(input);
                }
                case 2 -> movieService.getTop10RatedMovies();
                case 3 -> {
                    System.out.print("Enter Genre: ");
                    String genre = scanner.nextLine();
                    movieService.getMoviesByGenre(genre);
                }

                case 4 -> {
                    System.out.print("Enter Director: ");
                    String director = scanner.nextLine();
                    movieService.getMoviesByDirector(director);
                }


                case 5 -> {
                    System.out.print("Enter Release Year: ");
                    int year = scanner.nextInt();
                    movieService.getMoviesByYear(year);
                }
                case 6 -> {
                    System.out.print("Enter Year Range (start-end): ");
                    String range = scanner.nextLine();
                    String[] years = range.split("-");
                    int startYear = Integer.parseInt(years[0].trim());
                    int endYear = Integer.parseInt(years[1].trim());
                    movieService.getMoviesByYearRange(startYear, endYear);
                }
                case 7 -> {
                    System.out.print("Enter Movie Details (ID,Title,Year,Genre,Rating,Duration,DirectorID,ActorIDs): ");
                    String details = scanner.nextLine();
                    String[] parts = details.split(",");
                    if (parts.length >= 8) {
                        Movie newMovie = new Movie(parts[0], parts[1], Integer.parseInt(parts[2].trim()), parts[3],
                                Double.parseDouble(parts[4].trim()), Integer.parseInt(parts[5].trim()), parts[6],
                                List.of(parts[7].replace("\"", "").trim().split(" ")));
                        movieService.addNewMovie(newMovie);
                    } else {
                        System.out.println("Invalid input format.");
                    }
                }
                case 8 -> {
                    System.out.print("Enter Movie ID: ");
                    String movieId = scanner.nextLine();
                    System.out.print("Enter New Rating: ");
                    double newRating = scanner.nextDouble();
                    movieService.updateMovieRating(movieId, newRating);
                }
                case 9 -> {
                    System.out.print("Enter Movie ID to Delete: ");
                    String movieId = scanner.nextLine();
                    movieService.deleteMovie(movieId);
                }
                case 10 -> movieService.sortAndDisplayMoviesByYear();
                case 11 -> movieService.getTop5DirectorsByMovies();
                case 12 -> movieService.getActorsWithMultipleMovies();
                case 13 -> movieService.getMoviesOfYoungestActor();
                case 14 -> {
                    System.out.println("Exiting the Movie Database System. Goodbye!");
                    exit = true;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}
