package com.example.simbirsoftproject;

public class Users {


    private String firstName;
    private String lastName;
    private String urlPhoto;

    public Users() {}

    public Users(String firstName, String lastName, String urlPhoto) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.urlPhoto = urlPhoto;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }
}
