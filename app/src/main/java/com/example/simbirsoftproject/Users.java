package com.example.simbirsoftproject;

public class Users {

    String adminRights;
    String firstName;
    String lastName;
    String urlPhoto;

    public Users() {}

    public Users(String adminRights, String firstName, String lastName, String urlPhoto) {
        this.adminRights = adminRights;
        this.firstName = firstName;
        this.lastName = lastName;
        this.urlPhoto = urlPhoto;
    }

    public String getAdminRights() { return adminRights; }

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
