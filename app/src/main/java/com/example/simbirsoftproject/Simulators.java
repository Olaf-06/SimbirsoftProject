package com.example.simbirsoftproject;

import android.graphics.Bitmap;

public class Simulators {
    String name;
    String description;
    Bitmap photoID;

    Simulators(String name, String description, Bitmap photoID){
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

    public Bitmap getPhotoID() {
        return photoID;
    }
}
