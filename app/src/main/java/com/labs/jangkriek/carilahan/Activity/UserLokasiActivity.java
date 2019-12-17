package com.labs.jangkriek.carilahan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.labs.jangkriek.carilahan.Adapter.LokasiUserAdapter;
import com.labs.jangkriek.carilahan.Database.DbUserLokasi;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.POJO.Respon;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Utils.RegisterApi;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.labs.jangkriek.carilahan.Activity.MainActivity.getUsername;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class UserLokasiActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {

    public static final String URL = "https://ridwanharts.000webhostapp.com/";
    private List<Lokasi> lokasiList = new ArrayList<>();
    private List<Lokasi> lokasiListFromServer = new ArrayList<>();
    private DbUserLokasi dbUserLokasi;
    private LokasiUserAdapter lokasiUserAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private GeoJsonSource geoJsonSource;
    private LatLng currentPos = new LatLng(-7.8855268466, 110.062440920);
    private FeatureCollection lokasiCollection;
    private static final String ICON_MARKER = "icon_marker";
    private static final String MARKER = "marker";
    private static final String LAYER_MARKER = "layer_marker";

    private ImageButton ibDelete, ibDownload;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_user_lokasi);

        mapView = findViewById(R.id.mapview_user_location);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        dbUserLokasi = new DbUserLokasi(this);
        progressBar = findViewById(R.id.progress_bar);
        ibDelete = findViewById(R.id.ib_refresh);
        ibDownload = findViewById(R.id.ib_download);
        ibDownload.setVisibility(View.INVISIBLE);

        loadDataLokasiFromServer();
        /*if (lokasiList.addAll(dbUserLokasi.getAllLokasi())){
            if (lokasiList.size()==0){
                Toast.makeText(getApplicationContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), lokasiList.size()+" Data Termuat", Toast.LENGTH_SHORT).show();
            }
        }*/

        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbUserLokasi.deleteLokasi();
                lokasiList.clear();
                lokasiUserAdapter.notifyDataSetChanged();
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull MapboxMap mapboxMap) {

                    }
                });
                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, "Database Deleted", Snackbar.LENGTH_LONG).setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark)).show();
            }
        });

        ibDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dbUserLokasi.deleteLokasi();
                    for (int i=0 ; i<lokasiListFromServer.size() ; i++){

                        //convert kembali string base64 jadi bitmap
                        byte[] decodedString = Base64.decode(lokasiListFromServer.get(i).getGambar(), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        String nama = lokasiListFromServer.get(i).getNama();
                        double lat = lokasiListFromServer.get(i).getLatitude();
                        double longi = lokasiListFromServer.get(i).getLongitude();
                        //double dayaDukungTanah = lokasiListFromServer.get(i).getDayaDukungTanah();
                        //double ketersediaanAir = lokasiListFromServer.get(i).getKetersediaanAir();
                        //double kemiringanLereng = lokasiListFromServer.get(i).getKemiringanLereng();
                        double aksebilitas = lokasiListFromServer.get(i).getAksebilitas();
                        //double hargaLahan = lokasiListFromServer.get(i).getPerubahanLahan();
                        //double kerawananBencana = lokasiListFromServer.get(i).getKerawananBencana();
                        double jarakKeBandara = lokasiListFromServer.get(i).getJarakKeBandara();
                        int status = lokasiListFromServer.get(i).getStatus();

                        /*dbUserLokasi.insertLokasi(
                                nama, lat, longi,
                                dayaDukungTanah,ketersediaanAir,kemiringanLereng,aksebilitas,hargaLahan,kerawananBencana,jarakKeBandara,
                                status,decodedByte
                        );*/

                    }
                    Toast.makeText(getApplicationContext(),"tidak sama "+lokasiList.size(), Toast.LENGTH_SHORT).show();
                    //lokasiList.addAll(dbUserLokasi.getAllLokasi());

                    lokasiUserAdapter.notifyDataSetChanged();
                    mapView.invalidate();
                    mapboxMap.getStyle(new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {

                        }
                    });

                    finish();
                    startActivity(getIntent());

            }
        });

    }

    private void initRecyclerView (){
        lokasiUserAdapter = new LokasiUserAdapter(this, lokasiList, mapboxMap);
        recyclerView = findViewById(R.id.rv_list_user_lokasi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(lokasiUserAdapter);
        progressBar.setVisibility(View.GONE);
    }


    private void loadDataLokasiFromServer(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("", "message : "+message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RegisterApi api = retrofit.create(RegisterApi.class);
        Call<Respon> call = api.view();
        call.enqueue(new Callback<Respon>() {
            @Override
            public void onResponse(Call<Respon> call, Response<Respon> response) {
                String value = response.body().getValue();
                if (value.equals("1")){
                    lokasiListFromServer = response.body().getLokasiList();
                    Toast.makeText(getApplicationContext(),lokasiListFromServer.size()+" : "+lokasiList.size(), Toast.LENGTH_SHORT).show();
                    if (lokasiListFromServer.size()!=lokasiList.size()){

                        ibDownload.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"Data baru terdeteksi ", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<Respon> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return false;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {

        UserLokasiActivity.this.mapboxMap = mapboxMap;
        geoJsonSource = new GeoJsonSource("source_id",
                Feature.fromGeometry(Point.fromLngLat(currentPos.getLongitude(), currentPos.getLatitude())));
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/ridwanharts/cjytgnl6o073w1cnv460nfx46"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                initDataLatLong();
                initMarkerIcons(style);
                initRecyclerView();
            }
        });

    }

    private void initDataLatLong(){

        List<Lokasi> l = dbUserLokasi.getLatLongLokasi();
        LatLng[] lokasi = new LatLng[l.size()];

        for (int i=0;i<lokasiList.size();i++){
            lokasi[i] = new LatLng(l.get(i).getLatitude(), l.get(i).getLongitude());
        }

        lokasiCollection = FeatureCollection.fromFeatures(new Feature[] {});
        List<Feature> featureList = new ArrayList<>();
        if (lokasiCollection != null){
            for (LatLng latLng : lokasi){
                featureList.add(Feature.fromGeometry(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
            }
            lokasiCollection = FeatureCollection.fromFeatures(featureList);
        }
    }

    private void initMarkerIcons(@NotNull Style loadStyle){
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_loc_blue, null);
        Bitmap bitmap = BitmapUtils.getBitmapFromDrawable(drawable);

        loadStyle.addImage(ICON_MARKER, bitmap);
        loadStyle.addSource(new GeoJsonSource(MARKER, lokasiCollection));
        loadStyle.addLayer(new SymbolLayer(LAYER_MARKER, MARKER).withProperties(
                iconImage(ICON_MARKER),
                iconAllowOverlap(true),
                iconOffset(new Float[] {0f, -4f})
        ));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(this, MainActivity.class);
        a.putExtra("LOGIN", getUsername());
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        finish();
    }
}
