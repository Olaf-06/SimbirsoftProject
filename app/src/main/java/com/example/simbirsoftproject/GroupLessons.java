package com.example.simbirsoftproject;

public class GroupLessons {
    String name;
    String description;
    String photoID;

    public GroupLessons() {}

    public GroupLessons(String name, String description, String photoID){
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
