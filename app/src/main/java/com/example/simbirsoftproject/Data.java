package com.example.simbirsoftproject;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {

    String name;
    String description;
    String photoID;
    String startTime;
    String endTime;
    String year;
    String day;
    String month;

    public Data() {
    }

    public Data(String name, String description, String photoID) {
        this.name = name;
        this.description = description;
        this.photoID = photoID;
    }

    public Data(String name, String description, String photoID,
                String startTime, String endTime, String day, String month, String year) {
        this.name = name;
        this.description = description;
        this.photoID = photoID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    protected Data(Parcel in) {
        name = in.readString();
        description = in.readString();
        photoID = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        day = in.readString();
        month = in.readString();
        year = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhotoID() {
        return photoID;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(photoID);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(day);
        dest.writeString(month);
        dest.writeString(year);
    }
}
