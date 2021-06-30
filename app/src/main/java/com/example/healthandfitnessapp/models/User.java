package com.example.healthandfitnessapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public Long km;
    public Long steps;
    public Long caloriesBurned;
    public Long drinkerWater;
    public Long completedExercises;
    public Long sleepTime;
    public Long date;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, Long km, Long steps, Long caloriesBurned, Long drinkerWater, Long completedExercises, Long sleepTime, Long date) {
        this.username = username;
        this.email = email;
        this.km = km;
        this.steps = steps;
        this.caloriesBurned = caloriesBurned;
        this.drinkerWater = drinkerWater;
        this.completedExercises = completedExercises;
        this.sleepTime = sleepTime;
        this.date = date;
    }
}