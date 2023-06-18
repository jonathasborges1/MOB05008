package com.example.gps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Ponto p1;
    Ponto p2;
    String PROVIDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PROVIDER = LocationManager.GPS_PROVIDER;
    }

    public void reset(View view) {
        p1 = new Ponto();
        p2 = new Ponto();

        ((EditText) findViewById(R.id.edtPontoA)).setText("");
        ((EditText) findViewById(R.id.edtPontoB)).setText("");
    }

    public void lerPontoA(View view) {
        p1 = this.getPonto();
        EditText edtPonto = findViewById(R.id.edtPontoA);

        if (p1 != null) {
            edtPonto.setText(p1.imprimir2());
        }
    }

    public void lerPontoB(View view) {
        p2 = this.getPonto();
        EditText edtPonto = findViewById(R.id.edtPontoB);

        if (p2 != null) {
            edtPonto.setText(p2.imprimir2());
        }
    }

    public Ponto getPonto() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 1);

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { Manifest.permission.ACCESS_NETWORK_STATE }, 1);

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, 1);

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { Manifest.permission.ACCESS_BACKGROUND_LOCATION }, 1);

            return null;
        }

        LocationManager mLocManager   = (LocationManager) getSystemService(MainActivity.this.LOCATION_SERVICE);
        LocationListener mLocListener = new MinhaLocalizacaoListener();

        mLocManager.requestLocationUpdates(PROVIDER, 0, 0, mLocListener);
        Location localAtual = mLocManager.getLastKnownLocation(PROVIDER);

        if (!mLocManager.isProviderEnabled(PROVIDER)) {
            Toast.makeText(this, "GPS desabilitado.", Toast.LENGTH_LONG).show();
        }

        return new Ponto(localAtual.getLatitude(), localAtual.getLongitude(), localAtual.getAltitude());
    }

    private void mostrarGoogleMaps(double latitude, double longitude) {
        WebView wv = findViewById(R.id.webv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude);
    }

    public void verPontoA(View view) {
        this.mostrarGoogleMaps(p1.getLatitude(), p1.getLongitude());
    }

    public void verPontoB(View view) {
        this.mostrarGoogleMaps(p2.getLatitude(), p2.getLongitude());
    }

    public void calcularDistancia(View view) {
        LocationManager mLocManager   = (LocationManager) getSystemService(MainActivity.this.LOCATION_SERVICE);

        float[] resultado = new float[1];
        Location.distanceBetween(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude(), resultado);

        if (mLocManager.isProviderEnabled(PROVIDER)) {
            String texto = "*** Distância ***: " + resultado[0] + "\n";
            Toast.makeText(this, texto, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "GPS desabilitado.", Toast.LENGTH_LONG).show();
        }
    }
}