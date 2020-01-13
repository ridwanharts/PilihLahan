package com.labs.jangkriek.carilahan.Activity.Users;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.labs.jangkriek.carilahan.Activity.MainActivity;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.Adapter.KelolaLahankuAdapter;
import com.labs.jangkriek.carilahan.Database.DbLokasi;
import com.labs.jangkriek.carilahan.POJO.PointLatLong;
import com.labs.jangkriek.carilahan.POJO.Respon;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Utils.RegisterApi;
import com.labs.jangkriek.carilahan.Utils.RecyclerTouchListener;
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
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.io.IOException;
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

import static com.labs.jangkriek.carilahan.Activity.MainActivity.getIdUser;
import static com.labs.jangkriek.carilahan.Activity.MainActivity.getUsername;
import static com.labs.jangkriek.carilahan.Activity.MainActivity.idUser;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOutlineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

/**
 * Use a recyclerview with a Mapbox map to easily explore content all on one screen
 */
public class KelolaLahankuActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private static final String URL = "https://ridwanharts.000webhostapp.com/";
    private static final String SYMBOL_ICON_ID = "SYMBOL_ICON_ID";
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private static final int GALLERY_REQUEST_CODE = 7;
    public MapboxMap mapboxMap;
    private MapView mapView;
    private FeatureCollection lokasiCollection;
    private ImageView ivCekUpload;
    private Boolean reset;
    private Style mStyle;
    private KelolaLahankuAdapter mAdapter;

    //private List<Lokasi> lokasiList = new ArrayList<>();
    private List<Lokasi> lokasiListFromServer = new ArrayList<>();
    private List<Lokasi> lokasiListUser = new ArrayList<>();
    private List<PointLatLong> userLatLongListFromServer = new ArrayList<>();
    private static HashMap<String, List<Point>> dataPointHash = new HashMap<String, List<Point>>();
    ArrayList<String> listCreatedAt = new ArrayList<String>();
    List<List<Point>> POINTS = new ArrayList<>();
    List<Point> OUTER_POINTS = new ArrayList<>();

    private DbLokasi db;

    private String lat, longi;
    private Context context;

    private ImageView ivUploadGambar;

    private RelativeLayout rvLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.access_token));

        setContentView(R.layout.activity_kelola_lahanku);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getSupportActionBar() == null){
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Kelola Lahanku");
        }

        FloatingActionButton fabOpAddLocation = findViewById(R.id.op_add_location);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        ivUploadGambar = findViewById(R.id.iv_upload_lokasi);
        rvLoading = findViewById(R.id.rv_loading);
        reset = false;

        fabOpAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showNoteDialog(false, null, -1);
                double latitude = -7.8855268466;
                double longitude = 110.062440920;
                Intent i = new Intent(getApplicationContext(), TambahLahanActivity.class);
                i.putExtra("is_edit", false);
                i.putExtra("latitude", latitude);
                i.putExtra("longitude", longitude);
                //i.putExtra("nama", nama);
                startActivity(i);

            }
        });

    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        KelolaLahankuActivity.this.mapboxMap = mapboxMap;
        //new Style.Builder().fromUri("mapbox://styles/ridwanharts/cjytgnl6o073w1cnv460nfx46"
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/ridwanharts/cjytgnl6o073w1cnv460nfx46"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mStyle = style;
                loadDataLokasiFromServer();
            }
        });
    }

    private void loadDataLokasiFromServer(){
        reset = true;
        rvLoading.setVisibility(View.VISIBLE);
        lokasiListFromServer.clear();
        userLatLongListFromServer.clear();
        lokasiListUser.clear();
        listCreatedAt.clear();
        POINTS.clear();
        dataPointHash.clear();

        if (mAdapter != null){
            mAdapter.clear();
        }

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
        Call<Respon> call = api.view_lokasi_user(getIdUser());
        call.enqueue(new Callback<Respon>() {
            @Override
            public void onResponse(Call<Respon> call, Response<Respon> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                if (value.equals("1")){
                    lokasiListFromServer = response.body().getLokasiList();
                    userLatLongListFromServer = response.body().getPoint();
                    if (lokasiListFromServer.size() != 0){
                        if (userLatLongListFromServer.size() != 0){
                            rvLoading.setVisibility(View.INVISIBLE);
                            initDataLokasiCollection();
                            initMarkerIcons(mStyle);
                            initRecyclerviewLatlong();
                            initFillLayer(mStyle);
                        }
                        Toast.makeText(getApplicationContext(),"Data lahan berhasil didownload "+lokasiListFromServer.size(), Toast.LENGTH_SHORT).show();
                        rvLoading.setVisibility(View.INVISIBLE);
                    }else{
                        Toast.makeText(getApplicationContext(),"Anda belum menambahkan data lahan "+message, Toast.LENGTH_SHORT).show();
                        Log.e("message ", message);
                        rvLoading.setVisibility(View.INVISIBLE);
                    }

                }else {
                    //Toast.makeText(getApplicationContext(),"Anda belum memiliki lahan"+message, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Respon> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Koneksi internet bermasalah"+t, Toast.LENGTH_SHORT).show();
                rvLoading.setVisibility(View.INVISIBLE);

            }
        });
    }

    private void initDataLokasiCollection() {

        LatLng[] lokasi = new LatLng[lokasiListFromServer.size()];

        for (int i=0;i<lokasiListFromServer.size();i++){

            //if data lahan sama dgn id user
            if (lokasiListFromServer.get(i).getId_user() == getIdUser()){
                //get data created at disimpan ke list
                lokasiListUser.add(lokasiListFromServer.get(i));
                listCreatedAt.add(lokasiListFromServer.get(i).getCreated_at());
            }
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

        lokasiCollection = FeatureCollection.fromFeatures(new Feature[] {});
        List<Feature> lokasiList = new ArrayList<>();
        for(int i=0;i<lokasiListFromServer.size();i++) {
            if (lokasiListFromServer.get(i).getId_user() == getIdUser()) {
                lokasi[i] = new LatLng(lokasiListFromServer.get(i).getLatitude(), lokasiListFromServer.get(i).getLongitude());
                lokasiList.add(Feature.fromGeometry(Point.fromLngLat(lokasi[i].getLongitude(), lokasi[i].getLatitude())));
            }
        }
        lokasiCollection = FeatureCollection.fromFeatures(lokasiList);

    }

    private void initFillLayer(@NonNull Style loadedMapStyle) {

        if (reset){
            loadedMapStyle.removeLayer("layer-id11");
            loadedMapStyle.removeSource("source-id11");
        }
        Log.e("size", listCreatedAt.size()+"");
        for (int i=0;i<listCreatedAt.size();i++){
            POINTS.add(dataPointHash.get(listCreatedAt.get(i)));
        }

        loadedMapStyle.addSource(new GeoJsonSource("source-id11", Polygon.fromLngLats(POINTS)));
        loadedMapStyle.addLayerBelow(new FillLayer("layer-id11", "source-id11").withProperties(
                fillOpacity(0.4f),
                fillColor(Color.parseColor("#f74e4e"))
                ), LAYER_ID
        );

    }

    private void initMarkerIcons(@NonNull Style loadedMapStyle) {

        if (reset){
            loadedMapStyle.removeLayer(LAYER_ID);
            loadedMapStyle.removeSource(SOURCE_ID);
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
    public boolean onMapClick(@NonNull LatLng point) {

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    public void initRecyclerviewLatlong(){
        RecyclerView recyclerView = findViewById(R.id.rv_list_lokasi);
        mAdapter = new KelolaLahankuAdapter(this, lokasiListUser, mapboxMap);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        //snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {


            }

            @Override
            public void onLongClick(View view, int position) {
                //showActionsDialog(position);
            }
        }));
        mAdapter.notifyDataSetChanged();
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                
            } else {
                Toast.makeText(KelolaLahankuActivity.this, "Please give your permission.", Toast.LENGTH_LONG).show();
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
        Intent a = new Intent(this, MainActivity.class);
        a.putExtra("LOGIN", getUsername());
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        if (item.getItemId()==R.id.refresh_menu){
            if (mStyle.isFullyLoaded()) {
                rvLoading.setVisibility(View.VISIBLE);
                loadDataLokasiFromServer();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

        /*private void showActionsDialog(final int position) {
        CharSequence[] colors = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, lokasiList.get(position), position);
                } else {
                    deleteLokasi(position);
                }
            }
        });
        builder.show();
    }*/

    /*private void showNoteDialog(final boolean shouldUpdate, final Lokasi lokasi, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.bubble_info.dialog_lokasi, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(KelolaLahankuActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.et_nama_lokasi);
        TextView etLatitude = view.findViewById(R.id.et_latitude);
        TextView etLongitude = view.findViewById(R.id.et_longitude);
        EditText etDayaDukungTanah = view.findViewById(R.id.et_dd_tanah);
        EditText etKetersediaanAir = view.findViewById(R.id.et_k_air);
        EditText etKemiringanLereng = view.findViewById(R.id.et_k_lereng);
        EditText etAksebilitas = view.findViewById(R.id.et_aksebilitas);
        EditText etPerubahanLahan = view.findViewById(R.id.et_p_lahan);
        EditText etKerawananBencana = view.findViewById(R.id.et_k_bencana);
        EditText etJarakBandara = view.findViewById(R.id.et_j_bandara);

        ImageView ivUploadGambar = view.findViewById(R.id.iv_upload_lokasi);
        ivUploadGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (shouldUpdate && lokasi != null) {
            inputNote.setText(lokasi.getUsername());

        }
        etLatitude.setText(tvLatitudeLokasi.getText());
        etLongitude.setText(tvLongitudeLokasi.getText());

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                double tempDDTanah = Double.parseDouble(etDayaDukungTanah.getText().toString());
                double tempKAir = Double.parseDouble(etKetersediaanAir.getText().toString());
                double tempKLereng = Double.parseDouble(etKemiringanLereng.getText().toString());
                double tempAksebilitas = Double.parseDouble(etAksebilitas.getText().toString());
                double tempPLahan = Double.parseDouble(etPerubahanLahan.getText().toString());
                double tempKBencana = Double.parseDouble(etKerawananBencana.getText().toString());
                double tempJBandara = Double.parseDouble(etJarakBandara.getText().toString());

                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(KelolaLahankuActivity.this, "!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && lokasi != null) {
                    // update note by it's idg
                    updateLokasi(inputNote.getText().toString(), position);
                } else {
                    // create new note

                    createLokasi(inputNote.getText().toString(), Double.parseDouble(lat), Double.parseDouble(longi),
                            tempDDTanah,tempKAir,tempKLereng,
                            tempAksebilitas,tempPLahan,tempKBencana,tempJBandara,
                            0, imageBitmap);

                }
            }
        });
    }*/

    /*private void createLokasi(String namaLokasi, double lat, double longi,
                              double ddTanah, double kAir, double kLereng, double aksebilitas,
                              double pLahan, double kBencana, double jBandara, int a, Bitmap bitmap) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertLokasi(namaLokasi, lat, longi, ddTanah, kAir, kLereng, aksebilitas, pLahan, kBencana, jBandara, a, bitmap);

        // get the newly inserted note from db
        //Lokasi n = db.getLokasi(id);

        *//*if (n != null) {
            // adding new note to array list at 0 position
            lokasiList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyNotes();
        }*//*
    }*/

    /*private void updateLokasi(String note, int position) {
        Lokasi n = lokasiList.get(position);
        // updating note text
        n.setUsername(note);

        // updating note in db
        db.updateLokasi(n);

        // refreshing the list
        lokasiList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }*/

    /*private void deleteLokasi(int position) {

        // delete pada database server
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NotNull String message) {
                //Log.d("", "message : "+message);
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
        Call<Respon> call = api.delete(
                lokasiList.get(position).getUsername(),
                lokasiList.get(position).getLatitude(),
                lokasiList.get(position).getLongitude()

        );
        call.enqueue(new Callback<Respon>() {
            @Override
            public void onResponse(Call<Respon> call, retrofit2.Response<Respon> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                if(value.equals("1")){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Respon> call, Throwable t) {
                Toast.makeText(context, "Jaringan Error", Toast.LENGTH_SHORT).show();
                //Log.d("cek lagi", ""+call);
            }
        });

        // deleting the note from db lokal
        db.deleteLokasi(lokasiList.get(position));

        // removing the note from the list
        lokasiList.remove(position);
        mAdapter.notifyItemRemoved(position);



        toggleEmptyNotes();
    }*/


}