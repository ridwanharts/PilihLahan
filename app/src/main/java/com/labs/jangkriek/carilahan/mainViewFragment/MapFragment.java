package com.labs.jangkriek.carilahan.mainViewFragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.labs.jangkriek.carilahan.R;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;


public class MapFragment extends Fragment implements OnMapReadyCallback, MapboxMap.OnMapClickListener {


    private MapView mMapView;
    private MapboxMap mMapboxMap;
    private FloatingActionButton changeMapStyle, fabLayerKecamatan;

    private GeoJsonSource geoJsonSource;
    private LatLng currentPos = new LatLng(-7.905, 110.06);

    private String currentMap;

    private Context context;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Inflate the bubble_info for this fragment
        mMapView = view.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        changeMapStyle = view.findViewById(R.id.change_map_style);
        fabLayerKecamatan = view.findViewById(R.id.layer_kecamatan);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mMapboxMap = mapboxMap;
        geoJsonSource = new GeoJsonSource("source-main",
                Feature.fromGeometry(Point.fromLngLat(currentPos.getLongitude(),
                        currentPos.getLatitude())));
        mapboxMap.setStyle(Style.SATELLITE_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                //add static marker
                MarkerOptions options = new MarkerOptions();
                options.title("Temon");
                options.position(new LatLng(-7.905, 110.06));
                mMapboxMap.addMarker(options);

                currentMap = Style.SATELLITE_STREETS;
            }
        });

        //change map style
        changeMapStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentMap.equals(Style.SATELLITE_STREETS)){
                    mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {
                            // Custom map style has been loaded and map is now ready
                            currentMap = Style.MAPBOX_STREETS;
                        }
                    });
                }else{
                    mapboxMap.setStyle(Style.SATELLITE_STREETS, new Style.OnStyleLoaded() {
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
