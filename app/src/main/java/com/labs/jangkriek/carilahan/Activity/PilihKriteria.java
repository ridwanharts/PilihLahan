package com.labs.jangkriek.carilahan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.labs.jangkriek.carilahan.Adapter.PrioritasKriteriaAdapter;
import com.labs.jangkriek.carilahan.POJO.Prioritas;
import com.labs.jangkriek.carilahan.R;

import java.util.ArrayList;
import java.util.Collections;

import static com.labs.jangkriek.carilahan.Activity.MainActivity.getUsername;
import static com.labs.jangkriek.carilahan.MetodeActivity.getListLokasitoProcess;

public class PilihKriteria extends AppCompatActivity implements PrioritasKriteriaAdapter.ItemClickListener {

    private CheckBox cbDayaDukungTanah, cbKetersediaanAir, cbKemiringanLereng, cbAksebilitas;
    private CheckBox cbPerubahanLahan, cbKerawananBencana, cbJarakKeBandara;
    private Boolean k1=false,k2=false,k3=false,k4=false,k5=false,k6=false,k7=false;

    private Button btnProses, btnDialogPilihOtomatis, btnDialogPilihManual, btnPilih;

    private TextView tvJumlahInput, tvError, tvDaya, tvLereng, tvAir, tvAksebilitas,tvHarga, tvBencana, tvBandara;
    private TextView tvK1,tvK2,tvK3,tvK4,tvK5,tvK6,tvK7;

    private LinearLayout inputManual, rg1,rg2,rg3,rg4,rg5,rg6,rg7;

    static int jumlahInput = 0;

    public static ArrayList<Prioritas> prioritas = new ArrayList<Prioritas>();
    private static double matriksNilaiKriteria [][];

    private final int OTOMATIS = 301;
    private final int MANUAL = 303;
    private int inputTipe = 0;

    private TextView tvJumlahDataLokasi;
    public PrioritasKriteriaAdapter adapter;
    private ArrayList<String> listKriteria = new ArrayList<>();
    private boolean isCreatedListview = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_kriteria);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getSupportActionBar() == null){
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Pilih Kriteria");
        }

        inisiasiButtonCheckBox();
        buttonClick();
        setListKriteria();

    }

    //drag move kriteria

    //set recyleriew list kriteria yang dipilih
    private void setListKriteria(){

        //prioritas.add(new Prioritas(count, getString(R.string.K1_DAYA_DUKUNG_TANAH)));
        isCreatedListview = true;
        if (listKriteria.isEmpty()){
            listKriteria.add("Pilihan Masih Kosong");
        }
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rv_prioritas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PrioritasKriteriaAdapter(this, listKriteria);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int move = makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
                return move;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Collections.swap(listKriteria, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                adapter.notifyDataSetChanged();
            }


        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    //inisiasi button dan checkbox
    private void inisiasiButtonCheckBox(){

        //
        //inisiasi
        //

        tvJumlahDataLokasi = findViewById(R.id.jumlah_data_lokasi);
        tvJumlahDataLokasi.setText("" + getListLokasitoProcess().size());

        btnProses = findViewById(R.id.btn_input_activity);
        btnPilih = findViewById(R.id.btn_pilih);

        inputManual = findViewById(R.id.input_manual);

        tvJumlahInput = findViewById(R.id.tv_jumlahInput);
        tvDaya = findViewById(R.id.k1_daya_dukung_tanah_t);
        tvAir = findViewById(R.id.k2_ketersediaan_air_t);
        tvLereng = findViewById(R.id.k3_kemiringan_lereng_t);
        tvAksebilitas = findViewById(R.id.k4_aksebilitas_t);
        tvHarga = findViewById(R.id.k5_perubahan_lahan_t);
        tvBencana =  findViewById(R.id.k6_kerawanan_bencana_t);
        tvBandara = findViewById(R.id.k7_jarakke_bandara_t);
        tvK1 = findViewById(R.id.k1_otomatis);
        tvK2 = findViewById(R.id.k2_otomatis);
        tvK3 = findViewById(R.id.k3_otomatis);
        tvK4 = findViewById(R.id.k4_otomatis);
        tvK5 = findViewById(R.id.k5_otomatis);
        tvK6 = findViewById(R.id.k6_otomatis);
        tvK7 = findViewById(R.id.k7_otomatis);

        rg1 = findViewById(R.id.rg_1);
        rg2 = findViewById(R.id.rg_2);
        rg3 = findViewById(R.id.rg_3);
        rg4 = findViewById(R.id.rg_4);
        rg5 = findViewById(R.id.rg_5);
        rg6 = findViewById(R.id.rg_6);
        rg7 = findViewById(R.id.rg_7);


        //
        //VISIBILITY
        //
        inputManual.setVisibility(View.INVISIBLE);

        tvDaya.setVisibility(View.INVISIBLE);
        tvAir.setVisibility(View.INVISIBLE);
        tvLereng.setVisibility(View.INVISIBLE);
        tvAksebilitas.setVisibility(View.INVISIBLE);
        tvHarga.setVisibility(View.INVISIBLE);
        tvBandara.setVisibility(View.INVISIBLE);
        tvBencana.setVisibility(View.INVISIBLE);

    }

    //set button click
    private void buttonClick(){
        
        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputTipe == MANUAL){
                    Intent i = new Intent(PilihKriteria.this, InputNilaiKriteriaActivity.class);
                    i.putExtra("jumlah_input", jumlahInput);
                    i.putExtra(getString(R.string.INPUT_TIPE),getString(R.string.INPUT_MANUAL));
                    i.putExtra("k1", k1);
                    i.putExtra("k2", k2);
                    i.putExtra("k3", k3);
                    i.putExtra("k4", k4);
                    i.putExtra("k5", k5);
                    i.putExtra("k6", k6);
                    i.putExtra("k7", k7);
                    startActivityForResult(i, 100);

                }else if (inputTipe == OTOMATIS){
                    inisiasiPerbandingan();
                    prosesPrioritas();
                    Intent i = new Intent(PilihKriteria.this, HitungFuzzyAHP.class);
                    i.putExtra(getString(R.string.INPUT_TIPE), getString(R.string.INPUT_OTOMATIS));
                    i.putExtra("k1", k1);
                    i.putExtra("k2", k2);
                    i.putExtra("k3", k3);
                    i.putExtra("k4", k4);
                    i.putExtra("k5", k5);
                    i.putExtra("k6", k6);
                    i.putExtra("k7", k7);
                    startActivityForResult(i, 100);
                }
            }
        });

        btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPilihKriteria();
            }
        });

    }

    //dialog pilih kriteria
    private void showDialogPilihKriteria() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_pilih_kriteria, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(PilihKriteria.this);
        alertDialogBuilderUserInput.setView(view);

        //cast dialog
        cbDayaDukungTanah = view.findViewById(R.id.k1_daya_dukung_tanah);
        cbKetersediaanAir = view.findViewById(R.id.k2_ketersediaan_air);
        cbKemiringanLereng = view.findViewById(R.id.k3_kemiringan_lereng);
        cbAksebilitas = view.findViewById(R.id.k4_aksebilitas);
        cbPerubahanLahan = view.findViewById(R.id.k5_perubahan_lahan);
        cbKerawananBencana = view.findViewById(R.id.k6_kerawanan_bencana);
        cbJarakKeBandara = view.findViewById(R.id.k7_jarakke_bandara);
        //cast
        btnDialogPilihOtomatis = view.findViewById(R.id.btn_ok_otomatis);
        btnDialogPilihManual = view.findViewById(R.id.btn_ok_manual);
        tvError = view.findViewById(R.id.tv_eror);

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        btnDialogPilihOtomatis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listKriteria.clear();
                k1=false;k2=false;k3=false;k4=false;k5=false;k6=false;k7=false;
                //k1Send=false;k2Send=false;k3Send=false;k4Send=false;k5Send=false;k6Send=false;k7Send=false;
                jumlahInput = 0;
                inputManual.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.INVISIBLE);

                k1 = cbDayaDukungTanah.isChecked();
                k2 = cbKetersediaanAir.isChecked();
                k3 = cbKemiringanLereng.isChecked();
                k4 = cbAksebilitas.isChecked();
                k5 = cbPerubahanLahan.isChecked();
                k6 = cbKerawananBencana.isChecked();
                k7 = cbJarakKeBandara.isChecked();

                if (k1){
                    jumlahInput++;
                    listKriteria.add(getString(R.string.K1_DAYA_DUKUNG_TANAH));
                }

                if (k2){
                    jumlahInput++;
                    listKriteria.add(getString(R.string.K2_KETERSEDIAAN_AIR));
                }

                if (k3){
                    jumlahInput++;
                    listKriteria.add(getString(R.string.K3_KEMIRINGAN_LERENG));
                }

                if (k4){
                    jumlahInput++;
                    listKriteria.add(getString(R.string.K4_AKSEBILITAS));
                }

                if (k5) {
                    jumlahInput++;
                    listKriteria.add(getString(R.string.K5_HARGA_LAHAN));
                }

                if (k6) {
                    jumlahInput++;
                    listKriteria.add(getString(R.string.K6_KERAWANAN_BENCANA));
                }

                if(k7) {
                    jumlahInput++;
                    listKriteria.add(getString(R.string.K7_JARAK_KE_BANDARA));
                }

                if (jumlahInput >= 3){
                    tvJumlahInput.setText(String.valueOf(jumlahInput));
                    inputTipe = OTOMATIS;
                    alertDialog.dismiss();
                }else {
                    //Toast.makeText(getApplicationContext(), "Pilihan kriteria kurang dari 3", Toast.LENGTH_SHORT).show();
                    tvError.setVisibility(View.VISIBLE);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Do some stuff
                                    tvError.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    };
                    thread.start(); //start the thread

                }
                adapter.notifyDataSetChanged();
            }


        });

        btnDialogPilihManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                k1=false;k2=false;k3=false;k4=false;k5=false;k6=false;k7=false;
                //k1Send=false;k2Send=false;k3Send=false;k4Send=false;k5Send=false;k6Send=false;k7Send=false;
                jumlahInput = 0;
                inputManual.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.INVISIBLE);

                k1 = cbDayaDukungTanah.isChecked();
                k2 = cbKetersediaanAir.isChecked();
                k3 = cbKemiringanLereng.isChecked();
                k4 = cbAksebilitas.isChecked();
                k5 = cbPerubahanLahan.isChecked();
                k6 = cbKerawananBencana.isChecked();
                k7 = cbJarakKeBandara.isChecked();

                if (k1){
                    tvDaya.setVisibility(View.VISIBLE);
                    jumlahInput++;
                }else {
                    tvDaya.setVisibility(View.INVISIBLE);
                }

                if (k2){
                    tvAir.setVisibility(View.VISIBLE);
                    jumlahInput++;
                }else {
                    tvAir.setVisibility(View.INVISIBLE);
                }

                if (k3){
                    tvLereng.setVisibility(View.VISIBLE);
                    jumlahInput++;
                }else{
                    tvLereng.setVisibility(View.INVISIBLE);
                }

                if (k4){
                    tvAksebilitas.setVisibility(View.VISIBLE);
                    jumlahInput++;
                }else{
                    tvAksebilitas.setVisibility(View.INVISIBLE);
                }

                if (k5) {
                    tvHarga.setVisibility(View.VISIBLE);
                    jumlahInput++;
                }else {
                    tvHarga.setVisibility(View.INVISIBLE);
                }

                if (k6) {
                    tvBencana.setVisibility(View.VISIBLE);
                    jumlahInput++;
                }else {
                    tvBencana.setVisibility(View.INVISIBLE);
                }

                if(k7) {
                    tvBandara.setVisibility(View.VISIBLE);
                    jumlahInput++;
                }else {
                    tvBandara.setVisibility(View.INVISIBLE);
                }

                if (jumlahInput >= 3){
                    tvJumlahInput.setText(String.valueOf(jumlahInput));
                    inputManual.setVisibility(View.VISIBLE);
                    inputTipe = MANUAL;
                    alertDialog.dismiss();
                }else {
                    //Toast.makeText(getApplicationContext(), "Pilihan kriteria kurang dari 3", Toast.LENGTH_SHORT).show();
                    tvError.setVisibility(View.VISIBLE);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Do some stuff
                                    tvError.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    };
                    thread.start(); //start the thread

                }

            }
        });

    }

    public void prosesPrioritas(){
        prioritas.clear();

        for (int i=0;i<listKriteria.size();i++){
            prioritas.add(new Prioritas(i, listKriteria.get(i)));
        }

        for (int i=0;i<prioritas.size();i++){
            Log.e("Prioritas ke "," "+prioritas.get(i).getId()+" : "+prioritas.get(i).getKriteria());
        }
    }

    //untuk akses nilai matriks
    public static double[][] getMatriksNilaiKriteria(){
        return matriksNilaiKriteria;
    }

    //untuk akses prioritas pilihan
    public static ArrayList<Prioritas> getPrioritasKriteria(){
        return prioritas;
    }

    //memasukkan nilai ke matriks
    public void inisiasiPerbandingan(){
        int k=0;
        matriksNilaiKriteria = new double[jumlahInput][jumlahInput];
        double[] listNilai = setNilaiKriteria();
        for (int i = 0; i < matriksNilaiKriteria.length; i++) {
            for (int j = 0; j < matriksNilaiKriteria.length; j++) {
                if (i == j) {
                    matriksNilaiKriteria[i][j] = 1.0;
                }

                if (i < j) {
                    matriksNilaiKriteria[i][j] = listNilai[k];
                    matriksNilaiKriteria[j][i] = 1 / matriksNilaiKriteria[i][j];
                    //Log.i("Nilai Perbandingan", "" + i + " : " + j + " = " + listKriteria.get(k).getNilai());
                    k++;
                }

            }
        }
    }

    //nilai kriteria untuk input otomatis
    public double[] setNilaiKriteria(){
        double[] input3 = {2, 6, 3};
        double[] input4 = {3, 5, 7, 2, 5, 2};
        double[] input5 = {5, 8, 9, 7, 5, 7, 5, 4, 3, 1};
        double[] input6 = {3, 9, 8, 5, 7, 2, 7, 5, 6, 4, 2, 3, 1, 1, 3};
        double[] input7 = {3, 4, 5, 5, 8, 9, 3, 6, 5, 7, 5, 2, 2, 3, 5, 4, 2, 4, 2, 2, 1};

        if (jumlahInput == 3){
            return input3;
        }
        if (jumlahInput == 4){
            return input4;
        }
        if (jumlahInput == 5){
            return input5;
        }
        if (jumlahInput == 6){
            return input6;
        }
        if (jumlahInput == 7){
            return input7;
        }
        return null;
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
        a.putExtra("LOGIN", getUsername());
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        finish();
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}