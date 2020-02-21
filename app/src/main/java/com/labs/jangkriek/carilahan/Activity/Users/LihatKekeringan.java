package com.labs.jangkriek.carilahan.Activity.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.VectorSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

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

public class LihatKekeringan extends AppCompatActivity implements OnMapReadyCallback,
        MapboxMap.OnMapClickListener, MapboxMap.OnCameraIdleListener {

    private static final String GEOJSON_SOURCE_ID = "GEOJSON_SOURCE_ID";
    private static final String CALLOUT_IMAGE_ID = "CALLOUT_IMAGE_ID";
    private static final String CALLOUT_LAYER_ID = "CALLOUT_LAYER_ID";

    private static final String KERAWANAN_BENCANA = "kerawanan_bencana";

    private static final String KERAWANAN_BENCANA_KEKERINGAN = "kerawanan_bencana_kekeringan";
    private static final String URI_KERAWANAN_BENCANA_KEKERINGAN = "mapbox://ridwanharts.de8cdzgv";
    private static final String SCL_KERAWANAN_BENCANA_KEKERINGAN = "pgon_rawan_benc_kekeringan-dx2q7k";

    String idSource = "";
    String uri = "";
    String sourceLayer = "";

    private GeoJsonSource source;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private double latitude, longitude;
    private static String tipeKriteria;
    private String lat;
    private String longi;
    private ValueAnimator valueAnimator;
    private GeoJsonSource geoJsonSource;
    private Style mStyle;
    private LatLng currentPos = new LatLng(-7.8855268466, 110.062440920);
    private TextView tvLatitudeLokasi,tvLongitudeLokasi;
    private RelativeLayout rlLatLong;
    private Button btnPilih;
    private ImageView ivFocus;
    private LinearLayout linearKekeringan, a,b,c,d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_lihat_kekeringan);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        ivFocus = findViewById(R.id.iv_focus);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", latitude);
        longitude = intent.getDoubleExtra("longitude", longitude);
        tipeKriteria = intent.getStringExtra("tipe_kriteria");

        if (getSupportActionBar() == null){
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            switch (tipeKriteria) {
                case "pilih_lokasi":
                    getSupportActionBar().setTitle("Pilih Lokasi Lahan");
                    break;
                case KERAWANAN_BENCANA:
                    getSupportActionBar().setTitle("Peta Rawan Kekeringan");
                    c = findViewById(R.id.kekeringan);
                    c.setVisibility(View.VISIBLE);
                    break;
            }

        }

    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        LihatKekeringan.this.mapboxMap = mapboxMap;
        geoJsonSource = new GeoJsonSource("source-red-marker",
                Feature.fromGeometry(Point.fromLngLat(currentPos.getLongitude(),
                        currentPos.getLatitude())));
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/ridwanharts/cjytgnl6o073w1cnv460nfx46"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                if (style.isFullyLoaded()) {

                    if (tipeKriteria.equals("pilih_lokasi")) {
                        tvLatitudeLokasi = findViewById(R.id.tv_latitude_lokasi);
                        tvLongitudeLokasi = findViewById(R.id.tv_longitude_lokasi);
                        tvLatitudeLokasi.setText("-7.8855268466");
                        tvLongitudeLokasi.setText("110.062440920");
                        btnPilih = findViewById(R.id.btn_red_marker_pilih);
                        btnPilih.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("latitude", lat);
                                intent.putExtra("longitude", longi);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });

                        showRedMarker(style);
                    } else {
                        rlLatLong = findViewById(R.id.linear_latlong);
                        rlLatLong.setVisibility(View.INVISIBLE);

                        LatLng selectedLocationLatLng = new LatLng(latitude, longitude);
                        CameraPosition newCameraPosition = new CameraPosition.Builder()
                                .target(selectedLocationLatLng)
                                .zoom(11)
                                .build();
                        mapboxMap.easeCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));
                        MarkerOptions options = new MarkerOptions();
                        options.title("Lahan Anda");
                        options.position(new LatLng(latitude, longitude));
                        mapboxMap.addMarker(options);
                        mapboxMap.addOnMapClickListener(LihatKekeringan.this);
                        Toast.makeText(LihatKekeringan.this,
                                "Arahkan titik merah mendekati lokasi lahan anda", Toast.LENGTH_LONG).show();


                        if (tipeKriteria.equals(KERAWANAN_BENCANA)){

                            idSource = KERAWANAN_BENCANA_KEKERINGAN;
                            uri = URI_KERAWANAN_BENCANA_KEKERINGAN;
                            sourceLayer = SCL_KERAWANAN_BENCANA_KEKERINGAN;
                            if (style.getSource(idSource) == null) {
                                style.addSource(new VectorSource(idSource, uri)
                                );
                            }

                            FillLayer terrainData = new FillLayer(sourceLayer, idSource);
                            terrainData.setSourceLayer(sourceLayer);
                            terrainData.setProperties(
                                    fillColor("#f74e4e"),
                                    fillOpacity(0.5f),
                                    fillAntialias(true)
                            );
                            style.addLayerAt(terrainData, 3);
                            /*

                            fabKekeringan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (style.isFullyLoaded()) {
                                        style.removeSource(sourceLayer);
                                        style.removeLayer(idSource);
                                        style.removeSource(idSource);
                                        style.removeLayerAt(3);
                                    }

                                    if (style.isFullyLoaded()) {
                                        idSource = KERAWANAN_BENCANA_KEKERINGAN;
                                        uri = URI_KERAWANAN_BENCANA_KEKERINGAN;
                                        sourceLayer = SCL_KERAWANAN_BENCANA_KEKERINGAN;
                                        if (style.getSource(idSource) == null) {
                                            style.addSource(new VectorSource(idSource, uri)
                                            );
                                        }

                                        FillLayer terrainData = new FillLayer(sourceLayer, idSource);
                                        terrainData.setSourceLayer(sourceLayer);
                                        terrainData.setProperties(
                                                fillColor("#f74e4e"),
                                                fillOpacity(0.5f),
                                                fillAntialias(true)
                                        );
                                        style.addLayerAt(terrainData, 3);


                                    }

                                }
                            });*/

                        }

                        //style.addLayerBelow(terrainData, CALLOUT_LAYER_ID);

                        setUpLineLayer(style);
                        mapboxMap.addOnCameraIdleListener(LihatKekeringan.this);
                        showCrosshair();
                        setupSource(style);
                        setUpInfoWindowLayer(style);
                        updateOutline(style);
                    }
                }
            }
        });

    }

    public void showRedMarker(Style styleRed){
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_place, null);
        Bitmap bitmap = BitmapUtils.getBitmapFromDrawable(drawable);

        styleRed.addImage(("marker_red"), bitmap);
        styleRed.addSource(geoJsonSource);
        styleRed.addLayer(new SymbolLayer("layer-red-marker", "source-red-marker")
                .withProperties(
                        iconImage("marker_red"),
                        PropertyFactory.iconIgnorePlacement(true),
                        iconAllowOverlap(true)
                ));

        mapboxMap.addOnMapClickListener(LihatKekeringan.this);
        LihatKekeringan.this.mStyle = styleRed;
    }

    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener =
            new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    LatLng animatedPosition = (LatLng) valueAnimator.getAnimatedValue();

                    geoJsonSource.setGeoJson(Point.fromLngLat(animatedPosition.getLongitude(), animatedPosition.getLatitude()));
                }
            };



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
/*        if (tipeKriteria.equals(KERAWANAN_BENCANA_BANJIR)){
            idLayer = KERAWANAN_BENCANA_BANJIR;
        }*/
        if (tipeKriteria.equals(KERAWANAN_BENCANA_KEKERINGAN)){
            idLayer = KERAWANAN_BENCANA_KEKERINGAN;
        }
/*        if (tipeKriteria.equals(KERAWANAN_BENCANA_LONGSOR)){
            idLayer = KERAWANAN_BENCANA_LONGSOR;
        }
        if (tipeKriteria.equals(KERAWANAN_BENCANA_TSUNAMI)){
            idLayer = KERAWANAN_BENCANA_TSUNAMI;
        }*/
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
            //Toast.makeText(this, "Tidak Ada", Toast.LENGTH_SHORT).show();
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
                new GenerateViewIconTask(LihatKekeringan.this).execute(FeatureCollection.fromFeature(feature));
            }
        } else {
            //Toast.makeText(this, "Tidak ditemukan data", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        if (tipeKriteria.equals("pilih_lokasi")){
            if (!mStyle.isFullyLoaded()) {
                return false;
            }
            if (valueAnimator != null && valueAnimator.isStarted()) {
                currentPos = (LatLng) valueAnimator.getAnimatedValue();
                valueAnimator.cancel();
            }

            valueAnimator = ObjectAnimator
                    .ofObject(latLngEvaluator, currentPos, point)
                    .setDuration(1000);
            valueAnimator.addUpdateListener(animatorUpdateListener);
            valueAnimator.start();

            currentPos = point;

            tvLatitudeLokasi.setText(String.valueOf(currentPos.getLatitude()));
            tvLongitudeLokasi.setText(String.valueOf(currentPos.getLongitude()));
            lat = tvLatitudeLokasi.getText().toString();
            longi = tvLongitudeLokasi.getText().toString();

            return true;
        }else {

            return handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));
        }



    }

    private static final TypeEvaluator<LatLng> latLngEvaluator = new TypeEvaluator<LatLng>() {

        private final LatLng latLng = new LatLng();

        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    };

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

        private final WeakReference<LihatKekeringan> activityRef;
        private Feature featureAtMapClickPoint;

        GenerateViewIconTask(LihatKekeringan activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected HashMap<String, Bitmap> doInBackground(FeatureCollection... params) {
            LihatKekeringan activity = activityRef.get();
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
                            if (entry.getKey().equals("TA_1") ||
                                    entry.getKey().equals("oneway") ||
                                    entry.getKey().equals("TK_BAHAYA")){
                                //
                                if (entry.getKey().equals("TK_BAHAYA")){
                                    titleTextView.setText("RAWAN LONGSOR");
                                    break;
                                }

                            }else {
                                if (entry.getKey().equals("LAYER")){
                                    titleTextView.setText("Rawan Longsor");
                                }
                                stringBuilder.append(String.format("%s",  entry.getValue()));
                                stringBuilder.append(System.getProperty("line.separator"));
                            }

                        }

                        TextView propertiesListTextView = bubbleLayout.findViewById(R.id.info_window_feature_properties_list);
                        propertiesListTextView.setText(stringBuilder.toString());

                        int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        bubbleLayout.measure(measureSpec, measureSpec);

                        float measuredWidth = bubbleLayout.getMeasuredWidth();

                        bubbleLayout.setArrowPosition(measuredWidth / 2 - 5);

                        Bitmap bitmap = LihatKekeringan.SymbolGenerator.generate(bubbleLayout);
                        imagesMap.put(CALLOUT_IMAGE_ID, bitmap);
                    }
                }
            }

            return imagesMap;
        }

        @Override
        protected void onPostExecute(HashMap<String, Bitmap> bitmapHashMap) {
            super.onPostExecute(bitmapHashMap);
            LihatKekeringan activity = activityRef.get();
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

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
        finish();
    }
}
