package com.labs.jangkriek.carilahan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.labs.jangkriek.carilahan.R;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class DetailLokasiActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private String nama, username, no_hp;
    private double lat, longi, harga, k4, k6, luas;
    CarouselView carouselView;
    private List<String> mImages = new ArrayList<>();
    Locale localeID = new Locale("in", "ID");
    public MapboxMap mapboxMap;
    private static final String SYMBOL_ICON_ID = "SYMBOL_ICON_ID";
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String LAYER_ID = "LAYER_ID";
    NumberFormat kurensiID = NumberFormat.getCurrencyInstance(localeID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_detail_lokasi);

        Toolbar toolbar = findViewById(R.id.toolbar);

        if (getSupportActionBar() == null){
            setSupportActionBar(toolbar);
        }

        carouselView = findViewById(R.id.carouselView);
        TextView tvNama = findViewById(R.id.tv_nama_lokasi_detail);
        TextView tvHarga = findViewById(R.id.tv_harga_tanah_detail);
        MapView mapView = findViewById(R.id.mapView);
        TextView tvK1 = findViewById(R.id.k1_1);
        TextView tvK2 = findViewById(R.id.k2_2);
        TextView tvK3 = findViewById(R.id.k3_3);
        TextView tvK4 = findViewById(R.id.k4_4);
        TextView tvK5 = findViewById(R.id.k5_5);
        TextView tvK6 = findViewById(R.id.k6_6);
        TextView tvLuas = findViewById(R.id.tv_luas_tanah_detail);
        TextView tvUsernmae = findViewById(R.id.tv_username);
        TextView tvNoHp = findViewById(R.id.tv_no_hp);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Intent intent = getIntent();
        nama = intent.getStringExtra("nama");
        lat = intent.getDoubleExtra("latitude", lat);
        longi = intent.getDoubleExtra("longitude", longi);
        harga = intent.getDoubleExtra("harga", harga);
        no_hp = intent.getStringExtra("no_hp");
        username = intent.getStringExtra("username");
        String url = intent.getStringExtra("url");
        luas = intent.getDoubleExtra("luas", luas);
        String k1 = intent.getStringExtra("k1");
        String k2 = intent.getStringExtra("k2");
        String k3 = intent.getStringExtra("k3");
        String k5 = intent.getStringExtra("k5");
        k4 = intent.getDoubleExtra("k4", k4);
        k6 = intent.getDoubleExtra("k6", k6);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(nama);
        }

        mImages.add(url +"0.jpg");
        mImages.add(url +"1.jpg");
        mImages.add(url +"2.jpg");

        carouselView.setPageCount(mImages.size());
        carouselView.setImageListener(imageListener);

        tvNama.setText(nama);

        tvUsernmae.setText(username);
        tvNoHp.setText(no_hp);

        tvHarga.setText(""+kurensiID.format(harga));
        tvLuas.setText(luas+"");
        tvK1.setText(k1 +"");
        tvK2.setText(k2 +"");
        tvK3.setText(k3 +"");
        tvK4.setText(k4+"");
        tvK5.setText(k5 +"");
        tvK6.setText(k6+"");

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(getApplicationContext())
                    .load(mImages.get(position))
                    .error(R.drawable.ic_error)
                    .centerCrop()
                    .into(imageView);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        carouselView.pauseCarousel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carouselView.playCarousel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return false;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        DetailLokasiActivity.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/ridwanharts/cjytgnl6o073w1cnv460nfx46"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                MarkerOptions options = new MarkerOptions();
                options.title(nama);
                options.snippet(kurensiID.format(harga));
                options.position(new LatLng(lat, longi));
                mapboxMap.addMarker(options);

                LatLng selectedLocationLatLng = new LatLng(lat,longi);
                CameraPosition newCameraPosition = new CameraPosition.Builder()
                        .target(selectedLocationLatLng)
                        .build();
                mapboxMap.easeCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));

            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
