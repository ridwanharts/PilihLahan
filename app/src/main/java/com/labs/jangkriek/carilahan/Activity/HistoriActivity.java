package com.labs.jangkriek.carilahan.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.labs.jangkriek.carilahan.Database.DbSavePencarian;
import com.labs.jangkriek.carilahan.Adapter.SaveAdapter;
import com.labs.jangkriek.carilahan.POJO.SavePencarian;
import com.labs.jangkriek.carilahan.R;

import java.util.ArrayList;
import java.util.List;

public class HistoriActivity extends AppCompatActivity {

    private Button btnSymbol;
    private RecyclerView recyclerView;

    private DbSavePencarian dbSavePencarian;
    private List<SavePencarian> savePencarianList = new ArrayList<>();
    private SaveAdapter saveAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histori);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getSupportActionBar() == null){
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("History Pencarian");
        }

        dbSavePencarian = new DbSavePencarian(this);
        btnSymbol = findViewById(R.id.simbol);

        savePencarianList.addAll(dbSavePencarian.getAllSaveLokasi());

        initRecyclerView();

    }

    public void initRecyclerView(){
        recyclerView = findViewById(R.id.rv_list_saved_lokasi);
        saveAdapter = new SaveAdapter(this,savePencarianList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(saveAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
