package com.labs.jangkriek.carilahan.Activity.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.labs.jangkriek.carilahan.R;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class PlottingAreaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String CIRCLE_SOURCE_ID = "circle-source-id";
    private static final String FILL_SOURCE_ID = "fill-source-id";
    private static final String LINE_SOURCE_ID = "line-source-id";
    private static final String CIRCLE_LAYER_ID = "circle-layer-id";
    private static final String FILL_LAYER_ID = "fill-layer-polygon-id";
    private static final String LINE_LAYER_ID = "line-layer-id";
    private static final String SYMBOL_ICON_ID = "SYMBOL_ICON_ID";
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private List<Point> fillLayerPointList = new ArrayList<>();
    private List<Point> lineLayerPointList = new ArrayList<>();
    private List<Feature> circleLayerFeatureList = new ArrayList<>();
    private List<List<Point>> listOfList;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private GeoJsonSource circleSource;
    private GeoJsonSource fillSource;
    private GeoJsonSource lineSource;
    private Point firstPointOfPolygon;
    private double latitude, longitude;
    private Button btnSavePlot;
    private FeatureCollection lokasiCollection;
    private Bitmap imageBitmap;
    private ImageView ivUploadGambar;

    //private TextView tvPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Mapbox access token is configured here. This needs to be called either in your application
// object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token));

// This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_plotting_area);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", latitude);
        longitude = intent.getDoubleExtra("longitude", longitude);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        buttonAction();
    }

    private void buttonAction() {

        btnSavePlot = findViewById(R.id.simpan_plot_area);
        btnSavePlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("latlong", (Serializable) fillLayerPointList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/ridwanharts/cjytgnl6o073w1cnv460nfx46"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                LatLng selectedLocationLatLng = new LatLng(latitude, longitude);

                CameraPosition newCameraPosition = new CameraPosition.Builder()
                        .zoom(17)
                        .target(selectedLocationLatLng)
                        .build();
                mapboxMap.easeCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition), 4000);


// Add sources to the map
                circleSource = initCircleSource(style);
                fillSource = initFillSource(style);
                lineSource = initLineSource(style);

// Add layers to the map
                initCircleLayer(style);
                initLineLayer(style);
                initFillLayer(style);

                initFloatingActionButtonClickListeners();
                //Toast.makeText(PlottingAreaActivity.this, R.string.trace_instruction, Toast.LENGTH_LONG).show();

                initMarkerIcons(style);


            }
        });
    }

    /**
     * Set the button click listeners
     */
    private void initFloatingActionButtonClickListeners() {
        Button clearBoundariesFab = findViewById(R.id.clear_button);
        clearBoundariesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearEntireMap();
            }
        });

        FloatingActionButton dropPinFab = findViewById(R.id.drop_pin_button);
        dropPinFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

// Use the map click location to create a Point object
                Point mapTargetPoint = Point.fromLngLat(mapboxMap.getCameraPosition().target.getLongitude(),
                        mapboxMap.getCameraPosition().target.getLatitude());

// Make note of the first map click location so that it can be used to create a closed polygon later on
                if (circleLayerFeatureList.size() == 0) {
                    firstPointOfPolygon = mapTargetPoint;
                }

// Add the click point to the circle layer and update the display of the circle layer data
                circleLayerFeatureList.add(Feature.fromGeometry(mapTargetPoint));
                if (circleSource != null) {
                    circleSource.setGeoJson(FeatureCollection.fromFeatures(circleLayerFeatureList));
                }

// Add the click point to the line layer and update the display of the line layer data
                if (circleLayerFeatureList.size() < 3) {
                    lineLayerPointList.add(mapTargetPoint);
                } else if (circleLayerFeatureList.size() == 3) {
                    lineLayerPointList.add(mapTargetPoint);
                    lineLayerPointList.add(firstPointOfPolygon);
                } else {
                    lineLayerPointList.remove(circleLayerFeatureList.size() - 1);
                    lineLayerPointList.add(mapTargetPoint);
                    lineLayerPointList.add(firstPointOfPolygon);
                }
                if (lineSource != null) {
                    lineSource.setGeoJson(FeatureCollection.fromFeatures(new Feature[]
                            {Feature.fromGeometry(LineString.fromLngLats(lineLayerPointList))}));
                }

// Add the click point to the fill layer and update the display of the fill layer data
                if (circleLayerFeatureList.size() < 3) {
                    fillLayerPointList.add(mapTargetPoint);
                } else if (circleLayerFeatureList.size() == 3) {
                    fillLayerPointList.add(mapTargetPoint);
                    fillLayerPointList.add(firstPointOfPolygon);
                } else {
                    fillLayerPointList.remove(fillLayerPointList.size() - 1);
                    fillLayerPointList.add(mapTargetPoint);
                    fillLayerPointList.add(firstPointOfPolygon);
                }
                listOfList = new ArrayList<>();
                listOfList.add(fillLayerPointList);
                List<Feature> finalFeatureList = new ArrayList<>();
                finalFeatureList.add(Feature.fromGeometry(Polygon.fromLngLats(listOfList)));
                FeatureCollection newFeatureCollection = FeatureCollection.fromFeatures(finalFeatureList);

                //tvPrint = findViewById(R.id.listFill);
                if (fillSource != null) {
                    fillSource.setGeoJson(newFeatureCollection);
                    for (int i=0;i<fillLayerPointList.size();i++){
                        //tvPrint.append(fillLayerPointList.get(i).toString()+"\n");
                    }
                }


            }
        });
    }

    /**
     * Remove the drawn area from the map by resetting the FeatureCollections used by the layers' sources
     */
    private void clearEntireMap() {
        fillLayerPointList = new ArrayList<>();
        circleLayerFeatureList = new ArrayList<>();
        lineLayerPointList = new ArrayList<>();
        if (circleSource != null) {
            circleSource.setGeoJson(FeatureCollection.fromFeatures(new Feature[] {}));
        }
        if (lineSource != null) {
            lineSource.setGeoJson(FeatureCollection.fromFeatures(new Feature[] {}));
        }
        if (fillSource != null) {
            fillSource.setGeoJson(FeatureCollection.fromFeatures(new Feature[] {}));
        }
    }

    /**
     * Set up the CircleLayer source for showing map click points
     */
    private GeoJsonSource initCircleSource(@NonNull Style loadedMapStyle) {
        FeatureCollection circleFeatureCollection = FeatureCollection.fromFeatures(new Feature[] {});
        GeoJsonSource circleGeoJsonSource = new GeoJsonSource(CIRCLE_SOURCE_ID, circleFeatureCollection);
        loadedMapStyle.addSource(circleGeoJsonSource);
        return circleGeoJsonSource;
    }

    /**
     * Set up the CircleLayer for showing polygon click points
     */
    private void initCircleLayer(@NonNull Style loadedMapStyle) {
        CircleLayer circleLayer = new CircleLayer(CIRCLE_LAYER_ID,
                CIRCLE_SOURCE_ID);
        circleLayer.setProperties(
                circleRadius(4f),
                circleColor(Color.parseColor("#d004d3"))
        );
        loadedMapStyle.addLayer(circleLayer);
    }

    /**
     * Set up the FillLayer source for showing map click points
     */
    private GeoJsonSource initFillSource(@NonNull Style loadedMapStyle) {
        FeatureCollection fillFeatureCollection = FeatureCollection.fromFeatures(new Feature[] {});
        GeoJsonSource fillGeoJsonSource = new GeoJsonSource(FILL_SOURCE_ID, fillFeatureCollection);
        loadedMapStyle.addSource(fillGeoJsonSource);
        return fillGeoJsonSource;
    }

    /**
     * Set up the FillLayer for showing the set boundaries' polygons
     */
    private void initFillLayer(@NonNull Style loadedMapStyle) {
        FillLayer fillLayer = new FillLayer(FILL_LAYER_ID,
                FILL_SOURCE_ID);
        fillLayer.setProperties(
                fillOpacity(.4f),
                fillColor(Color.parseColor("#B0F74E4E"))
        );
        loadedMapStyle.addLayerBelow(fillLayer, LINE_LAYER_ID);
    }

    /**
     * Set up the LineLayer source for showing map click points
     */
    private GeoJsonSource initLineSource(@NonNull Style loadedMapStyle) {
        FeatureCollection lineFeatureCollection = FeatureCollection.fromFeatures(new Feature[] {});
        GeoJsonSource lineGeoJsonSource = new GeoJsonSource(LINE_SOURCE_ID, lineFeatureCollection);
        loadedMapStyle.addSource(lineGeoJsonSource);
        return lineGeoJsonSource;
    }

    /**
     * Set up the LineLayer for showing the set boundaries' polygons
     */
    private void initLineLayer(@NonNull Style loadedMapStyle) {
        LineLayer lineLayer = new LineLayer(LINE_LAYER_ID,
                LINE_SOURCE_ID);
        lineLayer.setProperties(
                lineColor(Color.WHITE),
                lineWidth(3f)
        );
        loadedMapStyle.addLayerBelow(lineLayer, CIRCLE_LAYER_ID);
    }


    private void initMarkerIcons(@NonNull Style loadedMapStyle) {

        lokasiCollection = FeatureCollection.fromFeatures(new Feature[] {});
        List<Feature> lokasiList = new ArrayList<>();
        if (lokasiCollection != null) {
            lokasiList.add(Feature.fromGeometry(Point.fromLngLat(longitude, latitude)));
            lokasiCollection = FeatureCollection.fromFeatures(lokasiList);
        }

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_loc_blue, null);
        Bitmap bitmap = BitmapUtils.getBitmapFromDrawable(drawable);

        loadedMapStyle.addImage(SYMBOL_ICON_ID, bitmap);
        loadedMapStyle.addSource(new GeoJsonSource(SOURCE_ID, lokasiCollection));
        loadedMapStyle.addLayer(new SymbolLayer(LAYER_ID, SOURCE_ID).withProperties(
                iconImage(SYMBOL_ICON_ID),
                iconAllowOverlap(true),
                iconOffset(new Float[] {0f, -4f})
        ));
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
