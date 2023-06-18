package com.example.gps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class MinhaLocalizacaoListener implements LocationListener {
    public static double latitude;
    public static double longitude;

    @Override
    public void onLocationChanged(@NonNull Location location) {
        location.getLatitude();
        location.getLongitude();
        this.latitude  = location.getLatitude();
        this.longitude = location.getLongitude();
    }

}
