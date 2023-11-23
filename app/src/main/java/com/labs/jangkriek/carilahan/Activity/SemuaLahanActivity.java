package com.labs.jangkriek.carilahan.Activity;


import android.animation.TypeEvaluator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.labs.jangkriek.carilahan.Adapter.KelolaLahankuAdapter;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.POJO.PointLatLong;
import com.labs.jangkriek.carilahan.POJO.Respon;
import com.labs.jangkriek.carilahan.POJO.Users;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Utils.RegisterApi;
import com.labs.jangkriek.carilahan.mainViewFragment.AccountFragment;
import com.labs.jangkriek.carilahan.mainViewFragment.GuestHomeFragment;
import com.labs.jangkriek.carilahan.mainViewFragment.MapFragment;
import com.labs.jangkriek.carilahan.mainViewFragment.UsersHomeFragment;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.labs.jangkriek.carilahan.Utils.Constant.URL;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;


public class SemuaLahanActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private static final String SYMBOL_ICON_ID = "SYMBOL_ICON_ID";
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private static final int GALLERY_REQUEST_CODE = 7;
    public MapboxMap mapboxMap;
    private MapView mapView;
    private FeatureCollection lokasiCollection;
    private Boolean reset;
    private static Boolean isSemuaLahan;
    private static List<Users> userList = new ArrayList<Users>();

    private KelolaLahankuAdapter mAdapter;

    //private List<Lokasi> lokasiList = new ArrayList<>();
    private List<Lokasi> lokasiListFromServer = new ArrayList<>();
    private List<PointLatLong> userLatLongListFromServer = new ArrayList<>();
    private static HashMap<String, List<Point>> dataPointHash = new HashMap<String, List<Point>>();
    ArrayList<String> listCreatedAt = new ArrayList<String>();
    List<List<Point>> POINTS = new ArrayList<>();

    private Style mStyle;
    private RelativeLayout rvLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_semua_lahan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getSupportActionBar() == null){
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Daftar Seluruh Lahan");
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        reset = false;
        rvLoading = findViewById(R.id.rv_loading);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh_menu, menu);

        return true;
    }

    public static Boolean getIsSemuaLahan(){
        return isSemuaLahan;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        SemuaLahanActivity.this.mapboxMap = mapboxMap;

        //new Style.Builder().fromUri("mapbox://styles/ridwanharts/cjytgnl6o073w1cnv460nfx46"
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/ridwanharts/cjytgnl6o073w1cnv460nfx46"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                isSemuaLahan = true;
                mStyle = style;
                rvLoading.setVisibility(View.VISIBLE);
                loadDataLokasiFromServer();
            }
        });
    }

    private void loadDataLokasiFromServer(){
        getUser();
        reset = true;
        rvLoading.setVisibility(View.VISIBLE);
        lokasiListFromServer.clear();
        userLatLongListFromServer.clear();

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
                    //Toast.makeText(getApplicationContext(),lokasiListFromServer.size()+" : "+lokasiList.size(), Toast.LENGTH_SHORT).show();
                    if (lokasiListFromServer.size() != 0){
                        //ibDownload.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"Data lahan berhasil didownload : "+lokasiListFromServer.size(), Toast.LENGTH_SHORT).show();
                        Call<Respon> getUserLatLong = api.get_user_latlong();
                        getUserLatLong.enqueue(new Callback<Respon>() {
                            @Override
                            public void onResponse(Call<Respon> call, Response<Respon> response) {
                                String value = response.body().getValue();

                                if (value.equals("1")) {
                                    userLatLongListFromServer = response.body().getPoint();
                                }
                                Toast.makeText(getApplicationContext(),"Data plot berhasil didownload : "+userLatLongListFromServer.size(), Toast.LENGTH_SHORT).show();
                                rvLoading.setVisibility(View.INVISIBLE);

                                initDataLokasiCollection();
                                initMarkerIcons(mStyle);
                                initRecyclerviewLatlong();
                                initFillLayer(mStyle);


                            }

                            @Override
                            public void onFailure(Call<Respon> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),"Internet Problem", Toast.LENGTH_SHORT).show();
                                rvLoading.setVisibility(View.INVISIBLE);
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(),"Anda belum menambahkan data lahan", Toast.LENGTH_SHORT).show();
                        rvLoading.setVisibility(View.INVISIBLE);
                    }

                }
            }

            @Override
            public void onFailure(Call<Respon> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
                rvLoading.setVisibility(View.INVISIBLE);

            }
        });
    }

    private void getUser(){

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
        Call<Respon> call = api.view_user();
        call.enqueue(new Callback<Respon>() {
            @Override
            public void onResponse(Call<Respon> call, Response<Respon> response) {
                String value = response.body().getValue();
                if (value.equals("1")){
                    userList = response.body().getUsersList();
                    Log.e("status", "berhasil"+userList.size());
                }
            }

            @Override
            public void onFailure(Call<Respon> call, Throwable t) {
                Log.e("error", "gagal");
            }
        });
    }

    public static List<Users> userList(){
        return userList;
    }

    private void initDataLokasiCollection() {

        LatLng[] lokasi = new LatLng[lokasiListFromServer.size()];

        for (int i=0;i<lokasiListFromServer.size();i++){

            //if data lahan sama dgn id user
            //if (lokasiListFromServer.get(i).getId_user() == getIdUser()){
                //get data created at disimpan ke list
                //lokasiListUser.add(lokasiListFromServer.get(i));
                listCreatedAt.add(lokasiListFromServer.get(i).getCreated_at());
            //}
        }

        for (int i=0;i<listCreatedAt.size();i++){

            List<Point> points = new ArrayList<>();

            Collections.sort(userLatLongListFromServer, new Comparator<PointLatLong>() {
                @Override
                public int compare(PointLatLong o1, PointLatLong o2) {
                    return o1.getNo_point() - o2.getNo_point();
                }
            });
            for (int j=0;j<userLatLongListFromServer.size();j++){
                String createdAt = userLatLongListFromServer.get(j).getCreated_at();
                Double lat = userLatLongListFromServer.get(j).getLatitude();
                Double longi = userLatLongListFromServer.get(j).getLongitude();

                if (listCreatedAt.get(i).equals(createdAt)){
                    points.add(Point.fromLngLat(longi, lat));
                    dataPointHash.put(createdAt, points);
                }

            }
        }

/*        Log.e("size L", listCreatedAt.size()+"");
        Log.e("L0", listCreatedAt.get(0)+"");
        Log.e("data hash", dataPointHash.size()+"");
        Log.e("data hash", dataPointHash.get(listCreatedAt.get(0))+"");*/


        for(int i=0;i<lokasiListFromServer.size();i++) {
            //if (lokasiListFromServer.get(i).getId_user() == getIdUser()) {
                lokasi[i] = new LatLng(lokasiListFromServer.get(i).getLatitude(), lokasiListFromServer.get(i).getLongitude());
            //}
        }

        lokasiCollection = FeatureCollection.fromFeatures(new Feature[] {});
        List<Feature> lokasiList = new ArrayList<>();
        if (lokasi.length != 0) {
            for (LatLng latLngLoc : lokasi) {
                lokasiList.add(Feature.fromGeometry(Point.fromLngLat(latLngLoc.getLongitude(), latLngLoc.getLatitude())));
            }
            lokasiCollection = FeatureCollection.fromFeatures(lokasiList);
        }
    }

    private void initFillLayer(@NonNull Style loadedMapStyle) {

        if (reset){
            if (loadedMapStyle.isFullyLoaded()){
                loadedMapStyle.removeLayer("layer-id11");
                loadedMapStyle.removeSource("source-id11");
            }

        }

        for (int i=0;i<listCreatedAt.size();i++){
            POINTS.add(dataPointHash.get(listCreatedAt.get(i)));
        }

        if (loadedMapStyle.isFullyLoaded()) {
            loadedMapStyle.addSource(new GeoJsonSource("source-id11", Polygon.fromLngLats(POINTS)));
            loadedMapStyle.addLayerBelow(new FillLayer("layer-id11", "source-id11").withProperties(
                    fillOpacity(0.4f),
                    fillColor(Color.parseColor("#f74e4e"))
                    ), LAYER_ID
            );
        }

    }

    private void initMarkerIcons(@NonNull Style loadedMapStyle) {

        if (reset){
            if (loadedMapStyle.isFullyLoaded()){
                loadedMapStyle.removeLayer(LAYER_ID);
                loadedMapStyle.removeSource(SOURCE_ID);
            }

        }

        if (loadedMapStyle.isFullyLoaded()) {

            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_loc_blue, null);
            Bitmap bitmap = BitmapUtils.getBitmapFromDrawable(drawable);

            loadedMapStyle.addImage(SYMBOL_ICON_ID, bitmap);
            loadedMapStyle.addSource(new GeoJsonSource(SOURCE_ID, lokasiCollection));
            loadedMapStyle.addLayer(new SymbolLayer(LAYER_ID, SOURCE_ID).withProperties(
                    iconImage(SYMBOL_ICON_ID),
                    iconAllowOverlap(true),
                    iconOffset(new Float[]{0f, -4f})
            ));
        }
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return false;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
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

    public void initRecyclerviewLatlong(){
        RecyclerView recyclerView = findViewById(R.id.rv_list_lokasi);
        mAdapter = new KelolaLahankuAdapter(this, lokasiListFromServer, mapboxMap);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                
            } else {
                Toast.makeText(SemuaLahanActivity.this, "Please give your permission.", Toast.LENGTH_LONG).show();
            }
        }
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

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
        isSemuaLahan = false;
        Intent a = new Intent(this, MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            isSemuaLahan = false;
            finish(); // close this activity and return to preview activity (if there is any)
        }
        if (item.getItemId()==R.id.refresh_menu){
            rvLoading.setVisibility(View.VISIBLE);
            loadDataLokasiFromServer();
        }
        return super.onOptionsItemSelected(item);
    }

}