package com.example.healthandfitnessapp.services;


import android.location.Location;

public class LocationService extends Location {
    private boolean useMetricUnits = false;

    public LocationService(Location location) {
        this(location, true);
    }

    public LocationService(Location location, boolean useMetricUnits) {
        super(location);
        this.useMetricUnits = useMetricUnits;
    }

    public boolean getUserMetricUnits() {
        return this.useMetricUnits;
    }

    public void setUserMetricUnits(boolean useMetricUnits) {
        this.useMetricUnits = useMetricUnits;
    }

    @Override
    public float distanceTo(Location dest) {
        float distance = super.distanceTo(dest);
        if (!this.getUserMetricUnits()) {
            //convert meters to feet
            distance *= 3.28083989501312f;
        }
        return distance;
    }

    @Override
    public double getAltitude() {
        double altitude = super.getAltitude();
        if (!this.getUserMetricUnits()) {
            //convert meters to feet
            altitude *= 3.28083989501312d;
        }
        return altitude;
    }

    @Override
    public float getSpeed() {
        float speed = super.getSpeed() * 3.6f;
        if (!this.getUserMetricUnits()) {
            //convert meters/second to miles/hour
            speed *= 2.23693629f;
        }
        return speed;
    }

    @Override
    public float getAccuracy() {
        float accuracy = super.getAccuracy();
        if (!this.getUserMetricUnits()) {
            //convert meters to feet
            accuracy *= 3.28083989501312f;
        }
        return accuracy;
    }
}