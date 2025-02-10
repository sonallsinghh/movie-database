package org.example;

public class Actor {


    String id;
    String name;
    String dateOfBirth;
    String nationality;

    public Actor(String id, String name, String dateOfBirth, String nationality) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        return String.format("Actor: %s (DOB: %s, Nationality: %s)", name, dateOfBirth, nationality);
    }

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
}
