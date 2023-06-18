package com.example.gps;

public class Ponto {
    private double latitude;
    private double longitude;
    private double altitude;

    public Ponto() {
        this.latitude  = 0;
        this.longitude = 0;
        this.altitude  = 0;
    }

    public Ponto(double latitude, double longitude) {
        this();
        this.latitude  = latitude;
        this.longitude = longitude;
    }

    public Ponto(double latitude, double longitude, double altitude) {
        this();
        this.latitude  = latitude;
        this.longitude = longitude;
        this.altitude  = altitude;
    }

    public String imprimir2() {
        String aux =
                "****************************\n" +
                        "Latitude.: " + this.latitude  + "\n" +
                        "Longitude: " + this.longitude + "\n" +
                        "Altitude.: " + this.altitude  + "\n" +
                        "****************************\n";
        return aux;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }
}

