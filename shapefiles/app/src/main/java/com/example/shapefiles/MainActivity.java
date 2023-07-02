package com.example.shapefiles;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private MapView mMapView;
    private FeatureLayer shapefileFeatureLayer;
    private Callout mCallout;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = findViewById(R.id.mapView);
        setupMapHidrografia();
        setupMap_callout();
    }


    private void setupMapHidrografia() {
        ArcGISRuntimeEnvironment.setApiKey("AAPKf77195a103a94faea21e2af848f82937waTM0UAWswfNAGFPkCQN5hrhXhxnPBuW40UTZRuLqlheM9jil6KjK4cNPs6nfIRe");
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_TOPOGRAPHIC);
        this.mMapView.setMap(map);

        ServiceFeatureTable shapefileFeatureTable = new ServiceFeatureTable("https://services2.arcgis.com/EnfUnovYrafK3K5h/arcgis/rest/services/doce/FeatureServer/0");
        this.shapefileFeatureLayer = new FeatureLayer(shapefileFeatureTable);
        this.mMapView.getMap().getOperationalLayers().add(shapefileFeatureLayer);
        this.mMapView.setViewpoint(new Viewpoint(-20.0, -41.0, 2500000.0));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupMap_callout(){
        this.setupMapHidrografia();
        mCallout = mMapView.getCallout();

        mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mMapView) {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                if (mCallout.isShowing()) {
                    mCallout.dismiss();
                }

                final Point screenPoint = new Point(Math.round(e.getX()), Math.round(e.getY()));
                int tolerance = 10;
                final ListenableFuture<IdentifyLayerResult> identifyLayerResultListenableFuture;
                identifyLayerResultListenableFuture = mMapView.identifyLayerAsync(shapefileFeatureLayer, screenPoint,
                        tolerance, false, 1);
                identifyLayerResultListenableFuture.addDoneListener(() -> {
                    try {
                        IdentifyLayerResult identifyLayerResult = identifyLayerResultListenableFuture.get();

                        TextView calloutContent = new TextView(getApplicationContext());
                        calloutContent.setTextColor(Color.BLACK);
                        calloutContent.setSingleLine(false);
                        calloutContent.setVerticalScrollBarEnabled(true);
                        calloutContent.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
                        calloutContent.setMovementMethod(new ScrollingMovementMethod());

                        for (GeoElement element : identifyLayerResult.getElements()) {
                            Feature feature = (Feature) element;
                            Map<String, Object> attr = feature.getAttributes();
                            Set<String> keys = attr.keySet();
                            for (String key : keys) {
                                Object value = attr.get(key);
                                calloutContent.append(key + " | " + value + "\n");
                            }

                            Envelope envelope = feature.getGeometry().getExtent();
                            mMapView.setViewpointGeometryAsync(envelope, 200);
                            mCallout.setLocation(envelope.getCenter());
                            mCallout.setContent(calloutContent);
                            mCallout.show();
                        }
                    } catch (Exception e1) {
                        Log.e(getResources().getString(R.string.app_name), "Selecão falhou: " + e1.getMessage());
                    }
                });
                return super.onSingleTapConfirmed(e);
            }
        });
    }
}