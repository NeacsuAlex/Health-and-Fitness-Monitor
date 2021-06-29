package com.example.healthandfitnessapp.models;

import java.util.Date;

public class Notification extends Element {
    public String title;
    public String message;
    public Date date;

    public Notification() {
        // Default constructor required for calls to DataSnapshot.getValue(Review.class)
    }

    public Notification(String title,String message, Date date){
        this.title=title;
        this.message=message;
        this.date=date;
    }
}