package org.example;

public class Director {
    private String id;
    private final String name;
    private final String dateOfBirth;
    private final String nationality;

    public Director(String id, String name, String dateOfBirth, String nationality) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
    }

    // Getter methods

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("Director: %s (DOB: %s, Nationality: %s)", name, dateOfBirth, nationality);
    }

}
