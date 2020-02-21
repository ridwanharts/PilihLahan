package com.labs.jangkriek.carilahan.Activity.Users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.labs.jangkriek.carilahan.Adapter.KelolaLahankuAdapter;
import com.labs.jangkriek.carilahan.Database.DbSavePencarian;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.POJO.Users;
import com.labs.jangkriek.carilahan.R;

import java.util.ArrayList;
import java.util.List;

public class DetailHistoryActivity extends AppCompatActivity {

    private DbSavePencarian dbSavePencarian;
    private KelolaLahankuAdapter detailHistoryAdapter;
    private List<Lokasi> rankingLokasiList = new ArrayList<>();
    private RecyclerView recyclerView;
    private static final String URL = "https://ridwanharts.000webhostapp.com/";
    private static List<Users> userList;

    String waktu, nama;
    String idgroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);

        Intent i = getIntent();
        idgroup = i.getStringExtra("id_group");
        waktu = i.getStringExtra("waktu");

        Log.e("","id_group : "+idgroup);
        Log.e("","waktu : "+waktu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getSupportActionBar() == null){
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(idgroup);
        }

        //Toast.makeText(getApplicationContext(), ""+idgroup+" - "+waktu+" - "+dbRangkingLokasi.getRankLokasiCount(), Toast.LENGTH_SHORT).show();
        dbSavePencarian = new DbSavePencarian(this);
        rankingLokasiList.addAll(dbSavePencarian.getDetailSaveLokasi(idgroup, waktu));
        initRecylerView();

    }

    private void initRecylerView() {
        recyclerView = findViewById(R.id.rv_list_saved_lokasi_detil);
        detailHistoryAdapter = new KelolaLahankuAdapter(this, rankingLokasiList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(detailHistoryAdapter);
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
