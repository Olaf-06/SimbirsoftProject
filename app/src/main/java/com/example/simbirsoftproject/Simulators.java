package com.example.simbirsoftproject;

public class Simulators {
    String name;
    String description;
    String photoID;

    public Simulators() {}

    public Simulators(String name, String description, String photoID){
        this.name = name;
        this.description = description;
        this.photoID = photoID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhotoID() {
        return photoID;
    }
}
