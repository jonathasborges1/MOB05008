package com.example.apprateionavegacao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;

public class MainActivity extends AppCompatActivity {
    private MapView mMapView;
    private LocationDisplay mLocationDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = findViewById(R.id.mapView);
    }

    private void setupLocationDisplay() {
        mLocationDisplay = mMapView.getLocationDisplay();
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
        mLocationDisplay.startAsync();
    }

    private void setupGPS() {
        mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {
            if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                return;
            }

            int requestPermissionsCode = 2;

            String[] requestPermissions = new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION };

            if (!(ContextCompat.checkSelfPermission(MainActivity.this, requestPermissions[0])
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    MainActivity.this, requestPermissions[1]) == PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(MainActivity.this, requestPermissions,
                        requestPermissionsCode);
            } else {
                Toast.makeText(MainActivity.this, "Erro.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationDisplay.startAsync();
        } else {
            Toast.makeText(MainActivity.this, "Permissão Recusada", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        if (mMapView != null) mMapView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView != null) mMapView.resume();
    }

    @Override
    protected void onDestroy() {
        if (mMapView != null) mMapView.dispose();
        super.onDestroy();
    }
    public void buscarInformacoesGPS(View view) {
        LocationManager mLocManager = null;
        LocationListener mLocListener;
        mLocManager = (LocationManager) getSystemService(MainActivity.this.LOCATION_SERVICE);
        mLocListener = new MinhaLocalizacaoListener();

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 1);

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {
                            Manifest.permission.ACCESS_NETWORK_STATE
                    }, 1);

            return;
        }

        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);
        if (mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            String texto =
                    "Latitude.: " + MinhaLocalizacaoListener.latitude + "\n" +
                            "Longitude: " + MinhaLocalizacaoListener.longitude + "\n";

            Toast.makeText(this, texto, Toast.LENGTH_LONG).show();

            if (mMapView != null) {
                Basemap.Type basemapType = Basemap.Type.IMAGERY;
                double latitude   = MinhaLocalizacaoListener.latitude;
                double longitude  = MinhaLocalizacaoListener.longitude;
                int levelOfDetail = 20;
                ArcGISMap map = new ArcGISMap(basemapType, latitude, longitude, levelOfDetail);
                mMapView.setMap(map);

                setupLocationDisplay();
                setupGPS();
            }
        } else {
            Toast.makeText(this, "GPS desabilitado.", Toast.LENGTH_LONG).show();
        }

    }
}