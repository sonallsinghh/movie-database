package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Director {
    private static final String COMMA_DELIMITER = ",";
    private String id;
    private String name;
    private String dateOfBirth;
    private String nationality;

    public Director(String id, String name, String dateOfBirth, String nationality) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    @Override
    public String toString() {
        return String.format("Director: %s (DOB: %s, Nationality: %s)", name, dateOfBirth, nationality);
    }

    public void loadDirectors(String filepath) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filepath))) {
            List<List<String>> records = reader.lines()
                    .map(line -> Arrays.asList(line.split(COMMA_DELIMITER)))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
