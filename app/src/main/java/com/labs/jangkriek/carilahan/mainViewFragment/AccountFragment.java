package com.labs.jangkriek.carilahan.mainViewFragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.labs.jangkriek.carilahan.Activity.LogInSignUp.LoginActivity;
import com.labs.jangkriek.carilahan.R;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import static com.labs.jangkriek.carilahan.Activity.MainActivity.getLoginType;
import static com.mapbox.mapboxsdk.style.layers.Property.LINE_CAP_ROUND;
import static com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_BEVEL;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements OnMapReadyCallback, MapboxMap.OnMapClickListener, MapboxMap.OnCameraIdleListener {


    private MapView mMapView;
    private MapboxMap mMapboxMap;
    private FeatureCollection featureCollection;

    private FloatingActionButton setStyle, showArea, fabLogOut;

    private GeoJsonSource geoJsonSource;
    private LatLng currentPos = new LatLng(-7.905, 110.06);

    private String currentMap;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Inflate the layout for this fragment
        mMapView = view.findViewById(R.id.map_view3);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        setStyle = view.findViewById(R.id.set_style);
        showArea = view.findViewById(R.id.show_area);
        fabLogOut = view.findViewById(R.id.logout);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        fabLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.putExtra("LOGIN", getLoginType());
                startActivity(i);
                getActivity().finish();
            }
        });

    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mMapboxMap = mapboxMap;
        geoJsonSource = new GeoJsonSource("source-main",
                Feature.fromGeometry(Point.fromLngLat(currentPos.getLongitude(),
                        currentPos.getLatitude())));
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/ridwanharts/cjytgnl6o073w1cnv460nfx46"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                setUpLineLayer(style);
                mMapboxMap.addOnCameraIdleListener(AccountFragment.this);
                showCrosshair();
                Toast.makeText(getActivity(), R.string.move_map_around_building_out_instruction,
                        Toast.LENGTH_SHORT).show();
                updateOutline(style);

                currentMap = Style.SATELLITE_STREETS;
            }
        });

        //change map style

        setStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentMap.equals(Style.SATELLITE_STREETS)){
                    mMapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {
                            // Custom map style has been loaded and map is now ready
                            currentMap = Style.MAPBOX_STREETS;
                        }
                    });
                }else{
                    mMapboxMap.setStyle(Style.SATELLITE_STREETS, new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {
                            // Custom map style has been loaded and map is now ready
                            currentMap = Style.SATELLITE_STREETS;
                        }
                    });
                }

            }
        });


    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return false;
    }


    /**
     * Sets up the source and layer for drawing the building outline
     */
    private void setUpLineLayer(@NonNull Style loadedMapStyle) {
        // Create a GeoJSONSource from an empty FeatureCollection
        loadedMapStyle.addSource(new GeoJsonSource("source",
                FeatureCollection.fromFeatures(new Feature[]{})));

        // Use runtime styling to adjust the look of the building outline LineLayer
        loadedMapStyle.addLayer(new LineLayer("lineLayer", "source").withProperties(
                lineColor(Color.RED),
                lineWidth(6f),
                lineCap(LINE_CAP_ROUND),
                lineJoin(LINE_JOIN_BEVEL)
        ));
    }

    @Override
    public void onCameraIdle() {
        mMapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                updateOutline(style);
            }
        });
    }

    /**
     * Query the map for a building Feature in the map's building layer. The query happens in the middle of the
     * map ("the target"). If there's a building Feature in the middle of the map, its coordinates are turned
     * into a list of Point objects so that a LineString can be created.
     *
     * @return the LineString built via the building's coordinates
     */
    private LineString getBuildingFeatureOutline(@NonNull Style style) {
        // Retrieve the middle of the map
        final PointF pixel = mMapboxMap.getProjection().toScreenLocation(new LatLng(
                mMapboxMap.getCameraPosition().target.getLatitude(),
                mMapboxMap.getCameraPosition().target.getLongitude()
        ));

        List<Point> pointList = new ArrayList<>();

        // Check whether the map style has a building layer
        if (style.getLayer("batas-kec-temon-line") != null) {

            // Retrieve the building Feature that is displayed in the middle of the map
            List<Feature> features = mMapboxMap.queryRenderedFeatures(pixel, "batas-kec-temon-line");
            if (features.size() > 0) {
                if (features.get(0).geometry() instanceof Polygon) {
                    Polygon buildingFeature = (Polygon) features.get(0).geometry();
                    // Build a list of Point objects from the building Feature's coordinates
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
                // Create a LineString from the list of Point objects
            }
        } else {
            Toast.makeText(getActivity(), R.string.building_layer_not_present, Toast.LENGTH_SHORT).show();
        }
        return LineString.fromLngLats(pointList);
    }

    /**
     * Update the FeatureCollection used by the building outline LineLayer. Then refresh the map.
     */
    private void updateOutline(@NonNull Style style) {
        // Update the data source used by the building outline LineLayer and refresh the map
        featureCollection = FeatureCollection.fromFeatures(new Feature[]
                {Feature.fromGeometry(getBuildingFeatureOutline(style))});
        GeoJsonSource source = style.getSourceAs("source");
        if (source != null) {
            source.setGeoJson(featureCollection);
        }
    }

    private void showCrosshair() {
        View crosshair = new View(getContext());
        crosshair.setLayoutParams(new FrameLayout.LayoutParams(20, 20, Gravity.CENTER));
        crosshair.setBackgroundColor(Color.RED);
        mMapView.addView(crosshair);
    }




    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

}
