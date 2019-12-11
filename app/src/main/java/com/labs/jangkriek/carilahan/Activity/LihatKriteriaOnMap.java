package com.labs.jangkriek.carilahan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.labs.jangkriek.carilahan.R;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.BubbleLayout;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.VectorSource;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ANCHOR_BOTTOM;
import static com.mapbox.mapboxsdk.style.layers.Property.LINE_CAP_ROUND;
import static com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_BEVEL;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillAntialias;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class LihatKriteriaOnMap extends AppCompatActivity implements OnMapReadyCallback,
        MapboxMap.OnMapClickListener, MapboxMap.OnCameraIdleListener {

    private static final String GEOJSON_SOURCE_ID = "GEOJSON_SOURCE_ID";
    private static final String CALLOUT_IMAGE_ID = "CALLOUT_IMAGE_ID";
    private static final String CALLOUT_LAYER_ID = "CALLOUT_LAYER_ID";

    private static final String JENIS_TANAH = "jenis_tanah";
    private static final String URI_JENIS_TANAH = "mapbox://ridwanharts.7kulsbt0";
    private static final String SCL_JENIS_TANAH = "pgon_jenis_tanah-4t1g4e";

    private static final String KEMIRINGAN_LERENG = "kemiringan_lereng";
    private static final String URI_KEMIRINGAN_LERENG = "mapbox://ridwanharts.3b23f5j3";
    private static final String SCL_KEMIRINGAN_LERENG = "pgon_lereng-bnu7xi";

    private static final String KETERSEDIAAN_AIR = "ketersediaan_air";
    private static final String URI_KETERSEDIAAN_AIR = "mapbox://ridwanharts.9hge7fet";
    private static final String SCL_KETERSEDIAAN_AIR = "pgon_hidrologi-cfh8n7";

    private static final String KERAWANAN_BENCANA = "kerawanan_bencana";
    private static final String URI_KERAWANAN_BENCANA = "mapbox://ridwanharts.2if1cuio";
    private static final String SCL_KERAWANAN_BENCANA = "pgon_rawan_benc_banjir-52ry6c";

    private GeoJsonSource source;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private double latitude, longitude;
    private String tipeKriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_lihat_kriteria);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", latitude);
        longitude = intent.getDoubleExtra("longitude", longitude);
        tipeKriteria = intent.getStringExtra("tipe_kriteria");

    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        LihatKriteriaOnMap.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/ridwanharts/cjytgnl6o073w1cnv460nfx46"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                MarkerOptions options = new MarkerOptions();
                options.title("Temon");
                options.position(new LatLng(latitude, longitude));
                mapboxMap.addMarker(options);
                mapboxMap.addOnMapClickListener(LihatKriteriaOnMap.this);
                Toast.makeText(LihatKriteriaOnMap.this,
                        "Arahkan titik merah mendekati lokasi lahan anda", Toast.LENGTH_LONG).show();


                String idSource = "";
                String uri = "";
                String sourceLayer = "";
                if (tipeKriteria.equals(JENIS_TANAH)) {
                    idSource = JENIS_TANAH;
                    uri = URI_JENIS_TANAH;
                    sourceLayer = SCL_JENIS_TANAH;
                }
                if (tipeKriteria.equals(KEMIRINGAN_LERENG)) {
                    idSource = KEMIRINGAN_LERENG;
                    uri = URI_KEMIRINGAN_LERENG;
                    sourceLayer = SCL_KEMIRINGAN_LERENG;
                }
                if (tipeKriteria.equals(KETERSEDIAAN_AIR)) {
                    idSource = KETERSEDIAAN_AIR;
                    uri = URI_KETERSEDIAAN_AIR;
                    sourceLayer = SCL_KETERSEDIAAN_AIR;
                }
                if (tipeKriteria.equals(KERAWANAN_BENCANA)) {
                    idSource = KERAWANAN_BENCANA;
                    uri = URI_KERAWANAN_BENCANA;
                    sourceLayer = SCL_KERAWANAN_BENCANA;
                }

                style.addSource(new VectorSource(idSource, uri)
                );

                FillLayer terrainData = new FillLayer(idSource, idSource);
                terrainData.setSourceLayer(sourceLayer);
                terrainData.setProperties(
                        fillColor("#197901"),
                        fillOpacity(0.4f),
                        fillAntialias(true)
                );

                //style.addLayerBelow(terrainData, CALLOUT_LAYER_ID);
                style.addLayerAt(terrainData, 3);

                setUpLineLayer(style);
                mapboxMap.addOnCameraIdleListener(LihatKriteriaOnMap.this);
                showCrosshair();
                setupSource(style);
                setUpInfoWindowLayer(style);
                updateOutline(style);

            }
        });
    }

    private void setUpLineLayer(@NonNull Style loadedMapStyle) {
// Create a GeoJSONSource from an empty FeatureCollection
        loadedMapStyle.addSource(new GeoJsonSource("source",
                FeatureCollection.fromFeatures(new Feature[]{})));

// Use runtime styling to adjust the look of the building outline LineLayer
        loadedMapStyle.addLayerAt(new LineLayer("lineLayer", "source").withProperties(
                lineColor(Color.RED),
                lineWidth(4f),
                lineCap(LINE_CAP_ROUND),
                lineJoin(LINE_JOIN_BEVEL)
        ), 3);
    }

    private void showCrosshair() {
        View crosshair = new View(this);
        crosshair.setLayoutParams(new FrameLayout.LayoutParams(15, 15, Gravity.CENTER));
        crosshair.setBackgroundColor(Color.RED);
        mapView.addView(crosshair);
    }

    private LineString getBuildingFeatureOutline(@NonNull Style style) {
// Retrieve the middle of the map
        LatLng point = new LatLng(mapboxMap.getCameraPosition().target.getLatitude(),
                mapboxMap.getCameraPosition().target.getLongitude());
        final PointF pixel = mapboxMap.getProjection().toScreenLocation(point);

        handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));

        List<Point> pointList = new ArrayList<>();

// Check whether the map style has a building layer
        String idLayer = "";
        if (tipeKriteria.equals(JENIS_TANAH)){
            idLayer = JENIS_TANAH;
        }
        if (tipeKriteria.equals(KEMIRINGAN_LERENG)){
            idLayer = KEMIRINGAN_LERENG;
        }
        if (tipeKriteria.equals(KETERSEDIAAN_AIR)){
            idLayer = KETERSEDIAAN_AIR;
        }
        if (tipeKriteria.equals(KERAWANAN_BENCANA)){
            idLayer = KERAWANAN_BENCANA;
        }
        if (style.getLayer(idLayer) != null) {

// Retrieve the building Feature that is displayed in the middle of the map
            List<Feature> features = mapboxMap.queryRenderedFeatures(pixel, idLayer);
            if (features.size() > 0) {
                if (features.get(0).geometry() instanceof Polygon) {
                    Polygon buildingFeature = (Polygon) features.get(0).geometry();
// Build a list of Point objects from the building Feature's coordinates
                    if (buildingFeature != null) {
                        for (int i = 0; i < buildingFeature.coordinates().size(); i++) {
                            for (int j = 0;
                                 j < buildingFeature.coordinates().get(i).size(); j++) {
                                pointList.add(Point.fromLngLat(
                                        buildingFeature.coordinates().get(i).get(j).longitude(),
                                        buildingFeature.coordinates().get(i).get(j).latitude()
                                ));
                            }
                        }
                    }
                }
// Create a LineString from the list of Point objects
            }
        } else {
            Toast.makeText(this, "Tidak Ada", Toast.LENGTH_SHORT).show();
        }
        return LineString.fromLngLats(pointList);
    }

    private void updateOutline(@NonNull Style style) {
// Update the data source used by the building outline LineLayer and refresh the map
        FeatureCollection featureCollection = FeatureCollection.fromFeatures(new Feature[]
                {Feature.fromGeometry(getBuildingFeatureOutline(style))});
        GeoJsonSource source = style.getSourceAs("source");
        if (source != null) {
            source.setGeoJson(featureCollection);
        }
    }

    /**
     * Adds the GeoJSON source to the map
     */
    private void setupSource(@NonNull Style loadedStyle) {
        source = new GeoJsonSource(GEOJSON_SOURCE_ID);
        loadedStyle.addSource(source);
    }

    /**
     * Needed to show the Feature properties info window.
     */
    private void refreshSource(Feature featureAtClickPoint) {
        if (source != null) {
            source.setGeoJson(featureAtClickPoint);
        }
    }

    /**
     * Adds a SymbolLayer to the map to show the Feature properties info window.
     */
    private void setUpInfoWindowLayer(@NonNull Style loadedStyle) {
        loadedStyle.addLayer(new SymbolLayer(CALLOUT_LAYER_ID, GEOJSON_SOURCE_ID)
                .withProperties(
// show image with id title based on the value of the name feature property
                        iconImage(CALLOUT_IMAGE_ID),

// set anchor of icon to bottom-left
                        iconAnchor(ICON_ANCHOR_BOTTOM),

// prevent the feature property window icon from being visible even
// if it collides with other previously drawn symbols
                        iconAllowOverlap(false),

// prevent other symbols from being visible even if they collide with the feature property window icon
                        iconIgnorePlacement(false),

// offset the info window to be above the marker
                        iconOffset(new Float[] {-2f, -28f})
                ));
    }

    private boolean handleClickIcon(PointF screenPoint) {
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint);
        if (!features.isEmpty()) {
            Feature feature = features.get(0);

            StringBuilder stringBuilder = new StringBuilder();

            if (feature.properties() != null) {
                for (Map.Entry<String, JsonElement> entry : feature.properties().entrySet()) {
                    stringBuilder.append(String.format("%s - %s", entry.getKey(), entry.getValue()));
                    stringBuilder.append(System.getProperty("line.separator"));
                }
                new GenerateViewIconTask(LihatKriteriaOnMap.this).execute(FeatureCollection.fromFeature(feature));
            }
        } else {
            //Toast.makeText(this, "Tidak ditemukan data", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));
    }

    /**
     * Invoked when the bitmap has been generated from a view.
     */
    public void setImageGenResults(HashMap<String, Bitmap> imageMap) {
        if (mapboxMap != null) {
            mapboxMap.getStyle(style -> {
                style.addImages(imageMap);
            });
        }
    }

    @Override
    public void onCameraIdle() {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                updateOutline(style);
            }
        });
    }

    private static class GenerateViewIconTask extends AsyncTask<FeatureCollection, Void, HashMap<String, Bitmap>> {

        private final WeakReference<LihatKriteriaOnMap> activityRef;
        private Feature featureAtMapClickPoint;

        GenerateViewIconTask(LihatKriteriaOnMap activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected HashMap<String, Bitmap> doInBackground(FeatureCollection... params) {
            LihatKriteriaOnMap activity = activityRef.get();
            HashMap<String, Bitmap> imagesMap = new HashMap<>();
            if (activity != null) {
                LayoutInflater inflater = LayoutInflater.from(activity);

                if (params[0].features() != null) {
                    featureAtMapClickPoint = params[0].features().get(0);

                    StringBuilder stringBuilder = new StringBuilder();

                    BubbleLayout bubbleLayout = (BubbleLayout) inflater.inflate(
                            R.layout.activity_query_feature_window_symbol_layer, null);

                    TextView titleTextView = bubbleLayout.findViewById(R.id.info_window_title);
                    titleTextView.setText(activity.getString(R.string.query_feature_marker_title));

                    if (featureAtMapClickPoint.properties() != null) {
                        for (Map.Entry<String, JsonElement> entry : featureAtMapClickPoint.properties().entrySet()) {
                            stringBuilder.append(String.format("%s : %s", entry.getKey(), entry.getValue()));
                            stringBuilder.append(System.getProperty("line.separator"));
                            if (entry.getKey().equals("JNS_TANAH")){
                                titleTextView.setText("Jenis Tanah = "+entry.getValue()+"");
                            }
                        }

                        TextView propertiesListTextView = bubbleLayout.findViewById(R.id.info_window_feature_properties_list);
                        propertiesListTextView.setText(stringBuilder.toString());

                        int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        bubbleLayout.measure(measureSpec, measureSpec);

                        float measuredWidth = bubbleLayout.getMeasuredWidth();

                        bubbleLayout.setArrowPosition(measuredWidth / 2 - 5);

                        Bitmap bitmap = LihatKriteriaOnMap.SymbolGenerator.generate(bubbleLayout);
                        imagesMap.put(CALLOUT_IMAGE_ID, bitmap);
                    }
                }
            }

            return imagesMap;
        }

        @Override
        protected void onPostExecute(HashMap<String, Bitmap> bitmapHashMap) {
            super.onPostExecute(bitmapHashMap);
            LihatKriteriaOnMap activity = activityRef.get();
            if (activity != null && bitmapHashMap != null) {
                activity.setImageGenResults(bitmapHashMap);
                activity.refreshSource(featureAtMapClickPoint);
            }
        }

    }

    /**
     * Utility class to generate Bitmaps for Symbol.
     */
    private static class SymbolGenerator {

        /**
         * Generate a Bitmap from an Android SDK View.
         *
         * @param view the View to be drawn to a Bitmap
         * @return the generated bitmap
         */
        static Bitmap generate(@NonNull View view) {
            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(measureSpec, measureSpec);

            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();

            view.layout(0, 0, measuredWidth, measuredHeight);
            Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return bitmap;
        }
    }

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
        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this);
        }
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}