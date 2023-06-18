package com.example.gps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MinhaLocalizacaoListener implements LocationListener {
    public static double latitude;
    public static double longitude;

    @Override
    public void onLocationChanged(Location location) {
        this.latitude  = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // não precisa implementar
    }

    @Override
    public void onProviderEnabled(String provider) {
        // não precisa implementar
    }

    @Override
    public void onProviderDisabled(String provider) {
        // não precisa implementar
    }
}
