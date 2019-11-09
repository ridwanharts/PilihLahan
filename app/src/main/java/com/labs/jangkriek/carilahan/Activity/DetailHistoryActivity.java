package com.labs.jangkriek.carilahan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.labs.jangkriek.carilahan.Adapter.DetailHistoryAdapter;
import com.labs.jangkriek.carilahan.Database.DbRangkingLokasi;
import com.labs.jangkriek.carilahan.POJO.RankingLokasi;
import com.labs.jangkriek.carilahan.R;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class DetailHistoryActivity extends AppCompatActivity {

    private DbRangkingLokasi dbRangkingLokasi;
    private DetailHistoryAdapter detailHistoryAdapter;
    private List<RankingLokasi> rankingLokasiList = new ArrayList<>();
    private RecyclerView recyclerView;

    String waktu, nama;
    int idgroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);

        Intent i = getIntent();
        idgroup = i.getIntExtra("idgroup",idgroup);
        nama = i.getStringExtra("nama");
        waktu = i.getStringExtra("waktu");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getSupportActionBar() == null){
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(nama);
        }

        dbRangkingLokasi = new DbRangkingLokasi(this);
        Toast.makeText(getApplicationContext(), ""+idgroup+" - "+waktu+" - "+dbRangkingLokasi.getRankLokasiCount(), Toast.LENGTH_SHORT).show();
        rankingLokasiList.addAll(dbRangkingLokasi.getAllRankLokasi(idgroup, waktu));
        initRecylerView();

    }

    private void initRecylerView() {
        recyclerView = findViewById(R.id.rv_list_saved_lokasi_detil);
        detailHistoryAdapter = new DetailHistoryAdapter(this, rankingLokasiList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new SlideInUpAnimator());
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
