package com.labs.jangkriek.carilahan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.labs.jangkriek.carilahan.Activity.MainActivity;
import com.labs.jangkriek.carilahan.Activity.PilihKriteria;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.POJO.PointLatLong;
import com.labs.jangkriek.carilahan.POJO.Respon;
import com.labs.jangkriek.carilahan.Utils.RegisterApi;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MetodeActivity extends AppCompatActivity {

    private CardView cvFahp;

    private static final String URL = "https://pilihlahan.000webhostapp.com/";

    private static List<Lokasi> lokasiListUser = new ArrayList<>();
    private static List<Lokasi> lokasiListFromServer = new ArrayList<>();
    private static List<PointLatLong> userLatLongListFromServer = new ArrayList<>();
    private static HashMap<String, List<Point>> dataPointHash = new HashMap<String, List<Point>>();
    ArrayList<String> listCreatedAt = new ArrayList<String>();
    List<List<Point>> POINTS = new ArrayList<>();
    private RelativeLayout rvLoading;

    private static List<Lokasi> listLokasiLahan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metode);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Pilih Metode");
        }

        rvLoading = findViewById(R.id.rv_loading);
        rvLoading.setVisibility(View.INVISIBLE);

        cvFahp = findViewById(R.id.cv_metode_fahp);

        cvFahp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDataLokasiFromServer();
            }
        });

        //getListLokasitoProcess();
    }

    public static List<Lokasi> getListLokasitoProcess() {
        return lokasiListFromServer;
    }

    public static List<PointLatLong> getListPointtoProcess() {
        return userLatLongListFromServer;
    }

    private void loadDataLokasiFromServer() {
        rvLoading.setVisibility(View.VISIBLE);
        lokasiListFromServer.clear();
        userLatLongListFromServer.clear();
        listCreatedAt.clear();
        POINTS.clear();
        dataPointHash.clear();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("", "message : " + message);
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
        Call<Respon> call = api.view_all_lokasi();
        call.enqueue(new Callback<Respon>() {
            @Override
            public void onResponse(Call<Respon> call, Response<Respon> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                if (value.equals("1")) {
                    lokasiListFromServer = response.body().getLokasiList();
                    userLatLongListFromServer = response.body().getPoint();
                    if (lokasiListFromServer.size() != 0) {
                        if (userLatLongListFromServer.size() != 0) {
                            rvLoading.setVisibility(View.INVISIBLE);
                        }
                        Toast.makeText(getApplicationContext(), "Data lahan berhasil didownload " + lokasiListFromServer.size(), Toast.LENGTH_SHORT).show();
                        rvLoading.setVisibility(View.INVISIBLE);
                        Intent i = new Intent(getApplicationContext(), PilihKriteria.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Anda belum menambahkan data lahan " + message, Toast.LENGTH_SHORT).show();
                        Log.e("message ", message);
                        rvLoading.setVisibility(View.INVISIBLE);
                    }

                } else {
                    //Toast.makeText(getApplicationContext(),"Anda belum memiliki lahan"+message, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Respon> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Koneksi internet bermasalah" + t, Toast.LENGTH_SHORT).show();
                rvLoading.setVisibility(View.INVISIBLE);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(this, MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        finish();
    }
}
