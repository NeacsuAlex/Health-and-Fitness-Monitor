package com.example.healthandfitnessapp.models;

public class FitnessProgramme {

    public String title;
    public String description;
    public String urlVideo;
    public String urlThumbnail;
    public String duration;
    public String difficulty;

    public FitnessProgramme() {
        // Default constructor required for calls to DataSnapshot.getValue(FitnessProgramme.class)
    }

    public FitnessProgramme(String title,String description,String urlVideo,String urlThumbnail,String duration,String difficulty) {
        this.title=title;
        this.description=description;
        this.urlVideo=urlVideo;
        this.urlThumbnail=urlThumbnail;
        this.duration=duration;
        this.difficulty=difficulty;
    }
}
