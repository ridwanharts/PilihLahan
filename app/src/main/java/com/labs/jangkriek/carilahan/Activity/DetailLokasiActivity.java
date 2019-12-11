package com.labs.jangkriek.carilahan.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.labs.jangkriek.carilahan.R;

public class DetailLokasiActivity extends AppCompatActivity {

    private String nama;
    private double lat, longi, harga;

    private TextView tvNama, tvLat, tvLongi, tvHarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lokasi);

        tvNama = findViewById(R.id.tv_nama_lokasi_detail);
        tvLat = findViewById(R.id.tv_detail_lat);
        tvLongi = findViewById(R.id.tv_detail_longi);
        tvHarga = findViewById(R.id.tv_harga_tanah_detail);

        Intent intent = getIntent();
        nama = intent.getStringExtra("nama");
        lat = intent.getDoubleExtra("latitude", lat);
        longi = intent.getDoubleExtra("longitude", longi);
        harga = intent.getDoubleExtra("harga", harga);

        tvNama.setText(nama);
        tvLat.setText(""+lat);
        tvLongi.setText(""+longi);
        tvHarga.setText(""+harga);

    }


}
