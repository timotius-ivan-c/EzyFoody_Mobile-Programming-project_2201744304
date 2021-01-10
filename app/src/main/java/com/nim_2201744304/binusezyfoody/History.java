package com.nim_2201744304.binusezyfoody;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class History implements Serializable {
    private  Restaurant restaurant;
    private ArrayList<Item> items;
    private Date date;
    private ArrayList<Integer> itemCounts = new ArrayList<>();
    private int itemCount = 0;
    private LatLng coordinate = null;
    private String address;
    private  Double distance = 0.0;
    private Integer total = 0;

    public History() {
        items = new ArrayList<>();
    }

    public History(Restaurant restaurant, ArrayList<Item> items, Date date, ArrayList<Integer> itemCounts, int itemCount, LatLng coordinate, String address, Double distance) {
        this.restaurant = restaurant;
        this.items = items;
        this.date = date;
        this.itemCounts = itemCounts;
        this.itemCount = itemCount;
        this.coordinate = coordinate;
        this.address = address;
        this.distance = distance;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public LatLng getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(LatLng coordinate) {
        this.coordinate = coordinate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public ArrayList<Integer> getItemCounts() {
        return itemCounts;
    }

    public void setItemCounts(ArrayList<Integer> itemCounts) {
        this.itemCounts = itemCounts;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
