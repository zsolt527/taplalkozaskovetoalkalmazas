package com.example.taplalkozaskovetoalkalmazas;

public class FoodItem {
    private String name;
    private String calory;
    private String userID;

    public FoodItem() {}

    public FoodItem(String name, String calory, String userID) {
        this.name = name;
        this.calory = calory;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public String getCalory() {
        return calory;
    }

    public String getUserID() {
        return userID;
    }
}
