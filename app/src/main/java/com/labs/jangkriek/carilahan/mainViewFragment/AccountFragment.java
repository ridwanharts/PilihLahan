package com.labs.jangkriek.carilahan.mainViewFragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.labs.jangkriek.carilahan.Activity.LogInSignUp.LoginActivity;
import com.labs.jangkriek.carilahan.PrefConfig;
import com.labs.jangkriek.carilahan.R;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements OnMapReadyCallback, MapboxMap.OnMapClickListener {


    /*private MapView mMapView;
    private MapboxMap mMapboxMap;
    private FeatureCollection featureCollection;
    private FloatingActionButton setStyle, showArea, fabLogOut;
    private GeoJsonSource geoJsonSource;
    private LatLng currentPos = new LatLng(-7.905, 110.06);
    private String currentMap;*/
    private Button btnLogout;
    private TextView tvUser;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Inflate the bubble_info for this fragment
/*        mMapView = view.findViewById(R.id.map_view3);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        setStyle = view.findViewById(R.id.set_style);
        showArea = view.findViewById(R.id.show_area);
        fabLogOut = view.findViewById(R.id.logout);*/
        btnLogout = view.findViewById(R.id.logout);
        tvUser = view.findViewById(R.id.username_logout);

        tvUser.setText(PrefConfig.getUsernameLogin(getActivity()));
        if (PrefConfig.getTypeLogin(getContext()).equals("GUEST")){
            tvUser.setText("GUEST");
            btnLogout.setText("Login");
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefConfig.clearLoggedInUser(getActivity());
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        /*this.mMapboxMap = mapboxMap;
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

*/
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return false;
    }


    /**
     * Sets up the source and layer for drawing the building outline
     */
    /*private void setUpLineLayer(@NonNull Style loadedMapStyle) {
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
    }*/


    @Override
    public void onStart() {
        super.onStart();
       // mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        //mMapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //mMapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //mMapView.onSaveInstanceState(outState);
    }

}

