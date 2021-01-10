package com.nim_2201744304.binusezyfoody;

import java.io.Serializable;

import java.util.ArrayList;

public class Restaurant implements Serializable {
    private float latitude;
    private float longitude;
    private String name;
    private ArrayList<Item> menus;

    public Restaurant() {
    }

    public Restaurant(float latitude, float longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public Restaurant(float latitude, float longitude, String name, ArrayList<Item> menus) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.menus = menus;
    }

    //    protected Restaurant(Parcel in) {
//        latitude = in.readFloat();
//        longitude = in.readFloat();
//        name = in.readString();
//    }

//    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
//        @Override
//        public Restaurant createFromParcel(Parcel in) {
//            return new Restaurant(in);
//        }
//
//        @Override
//        public Restaurant[] newArray(int size) {
//            return new Restaurant[size];
//        }
//    };

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Item> getMenus() {
        return menus;
    }

    public void setMenus(ArrayList<Item> menus) {
        this.menus = menus;
    }

    //    @Override
//    public int describeContents() {
//        return 0;
//    }

//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeFloat(latitude);
//        dest.writeFloat(longitude);
//        dest.writeString(name);
//    }

    //    public HashMap<String, String> getGrades() {
//        return Grades;
//    }
}
