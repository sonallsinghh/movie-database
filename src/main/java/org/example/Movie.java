package org.example;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class Movie {

    private static final String COMMA_DELIMITER = "," ;
    String id;
    String title;
    int releaseYear;
    String genre;
    double rating;
    int duration;
    String directorId;
    List<String> actorIds;

    public Movie(String id, String title, int releaseYear, String genre, double rating, int duration, String directorId, List<String> actorIds) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.rating = rating;
        this.duration = duration;
        this.directorId = directorId;
        this.actorIds = actorIds;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Title: %s | Year: %d | Genre: %s | Rating: %.1f | actorIds|  Duration: %d mins",
                id, title, releaseYear, genre, rating, actorIds,duration);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public double getRating() {
        return rating;
    }

    public int getDuration() {
        return duration;
    }

    public String getDirectorId() {
        return directorId;
    }

    public List<String> getActorIds() {
        return actorIds;
    }

    public void setRating(double newRating) {
        if (newRating >= 0 && newRating <= 10) {
            this.rating = newRating;
            System.out.printf("The rating of '%s' has been updated to %.1f\n", title, newRating);
        } else {
            System.out.println("Invalid rating. Please enter a value between 0 and 10.");
        }
    }

}
