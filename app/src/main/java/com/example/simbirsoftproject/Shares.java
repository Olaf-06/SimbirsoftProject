package com.example.simbirsoftproject;

public class Shares {
    String name;
    String description;
    String photoID;

    public Shares() {}

    public Shares(String name, String description, String photoID){
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

