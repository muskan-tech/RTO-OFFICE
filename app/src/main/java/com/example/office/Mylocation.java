package com.example.office;

public class Mylocation {
    private double latitude;
    private double longitude;
     public Mylocation(double lat,double longitude){
         this.latitude=lat;
         this.longitude=longitude;
     }
     public Mylocation(){}

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
