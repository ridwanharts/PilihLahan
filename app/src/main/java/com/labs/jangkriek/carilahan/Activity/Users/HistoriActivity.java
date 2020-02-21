package com.labs.jangkriek.carilahan.Activity.Users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.labs.jangkriek.carilahan.Activity.MainActivity;
import com.labs.jangkriek.carilahan.Database.DbSavePencarian;
import com.labs.jangkriek.carilahan.Adapter.SaveAdapter;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.POJO.Respon;
import com.labs.jangkriek.carilahan.POJO.SavePencarian;
import com.labs.jangkriek.carilahan.POJO.Users;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Utils.RegisterApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoriActivity extends AppCompatActivity {

    private Button btnSymbol;
    private RecyclerView recyclerView;

    private DbSavePencarian dbSavePencarian;
    private List<Lokasi> savePencarianList = new ArrayList<>();
    private List<String> waktu = new ArrayList<>();
    private List<String> idGroup = new ArrayList<>();
    private List<String> metode = new ArrayList<>();
    private List<String> kriteria = new ArrayList<>();
    private List<String> viewIdGroup = new ArrayList<>();
    private HashMap<Integer, String> dataHash = new HashMap<>();
    private HashMap<Integer, String> dataIdGroup = new HashMap<>();
    private HashMap<Integer, String> dataMetode = new HashMap<>();
    private HashMap<Integer, String> dataKriteria = new HashMap<>();
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

        initData();

        initRecyclerView();



    }

    public void initData(){

        int count = 1;
        for (int i=0;i<savePencarianList.size()-1;i++){
            if (savePencarianList.size()==3){
                dataHash.put(count,savePencarianList.get(i).getWaktu());
                dataIdGroup.put(count,savePencarianList.get(i).getIdgroup());
                dataMetode.put(count,savePencarianList.get(i).getMetode());
                dataKriteria.put(count,savePencarianList.get(i).getKriteria());
                break;
            }else {
                if (i==0){
                    dataHash.put(count,savePencarianList.get(0).getWaktu());
                    dataIdGroup.put(count,savePencarianList.get(0).getIdgroup());
                    dataMetode.put(count,savePencarianList.get(0).getMetode());
                    dataKriteria.put(count,savePencarianList.get(0).getKriteria());
                    count++;
                }
                if (!savePencarianList.get(i).getIdgroup().equals(savePencarianList.get(i+1).getIdgroup())){
                    dataHash.put(count,savePencarianList.get(i+1).getWaktu());
                    dataIdGroup.put(count,savePencarianList.get(i+1).getIdgroup());
                    dataMetode.put(count,savePencarianList.get(i+1).getMetode());
                    dataKriteria.put(count,savePencarianList.get(i+1).getKriteria());
                    count++;
                }

            }

        }
        Log.e("Jumlah",""+savePencarianList.size());

        int j = 1;
        for (int i=0;i<dataHash.size();i++){
            Log.e("-",""+dataMetode.get(j));
            waktu.add(dataHash.get(j));
            idGroup.add(dataIdGroup.get(j));
            metode.add(dataMetode.get(j));
            kriteria.add(dataKriteria.get(j));
            j++;
        }
        //Log.e("Jumlah",""+dataHash.size());


    }

    public void initRecyclerView(){
        recyclerView = findViewById(R.id.rv_list_saved_lokasi);
        saveAdapter = new SaveAdapter(this, waktu, idGroup, metode, kriteria);
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

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
        Intent a = new Intent(this, MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        finish();
    }
}
