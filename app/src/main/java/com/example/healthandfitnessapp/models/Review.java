package com.example.healthandfitnessapp.models;

public class Review extends Element {
    public String username;
    public String review;
    public Long nrStars;

    public Review() {
        // Default constructor required for calls to DataSnapshot.getValue(Review.class)
    }

    public Review(String username, String review, Long nrStars) {
        this.nrStars = nrStars;
        this.review = review;
        this.username = username;
    }
}
