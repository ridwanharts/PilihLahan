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
import android.view.Menu;
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
import com.labs.jangkriek.carilahan.PrefConfig;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.labs.jangkriek.carilahan.mainViewFragment.GuestHomeFragment.getListLokasitoProcessGuest;
import static com.labs.jangkriek.carilahan.mainViewFragment.UsersHomeFragment.getListLokasitoProcess;

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

    private double matriksVektorPrioritas [][];
    private double matriksHasilVP [];
    private List<Double> listVektorPrioritas = new ArrayList<>();

    ///
    double[][] matriksInputKriteria = null;
    double nilaiS[][] = null;
    private static double nilaiBobot [];
    private static double nilaiBobotAHP [];

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
            listVektorPrioritas.add(0.0);
        }
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rv_prioritas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PrioritasKriteriaAdapter(this, listKriteria, listVektorPrioritas);
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
                //Collections.swap(listVektorPrioritas, viewHolder.getAdapterPosition(), target.getAdapterPosition());
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
        if (PrefConfig.getTypeLogin(getApplicationContext()).equals("GUEST")){
            tvJumlahDataLokasi.setText("" + getListLokasitoProcessGuest().size());
        }

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
                    i.putExtra(getString(R.string.metode), "FAHP");
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

                    hitungFuzzySysntheticExtent();
                    hitungMinimumFuzzySynthetic();

                    Intent i = new Intent(PilihKriteria.this, RankingActivity.class);
                    i.putExtra(getString(R.string.INPUT_TIPE), getString(R.string.INPUT_OTOMATIS));
                    i.putExtra(getString(R.string.metode), "FAHP");
                    i.putExtra("k1", k1);
                    i.putExtra("k2", k2);
                    i.putExtra("k3", k3);
                    i.putExtra("k4", k4);
                    i.putExtra("k5", k5);
                    i.putExtra("k6", k6);
                    i.putExtra("k7", k7);
                    startActivityForResult(i, 102);
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
                listVektorPrioritas.clear();
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
                    inisiasiPerbandingan();
                    //prosesPrioritas();
                    hitungVektorPrioritas();
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
        double[] input3 = {2, 3, 1};
        double[] input4 = {3, 5, 7, 2, 5, 2};
        double[] input5 = {2, 6, 5, 7, 3, 5, 7, 4, 3, 2};
        double[] input6 = {3, 5, 3, 3, 5, 2, 5, 7, 6, 4, 2, 3, 1, 3, 1};
        double[] input7 = {3, 4, 5, 5, 8, 9, 3, 6, 5, 7, 5, 2, 2, 3, 5, 4, 2, 4, 2, 2, 1};
        if (jumlahInput == 3){return input3;}
        if (jumlahInput == 4){return input4;}
        if (jumlahInput == 5){return input5;}
        if (jumlahInput == 6){return input6;}
        if (jumlahInput == 7){return input7;}
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu_pilih_kriteria, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        if (item.getItemId()==R.id.metode_ahp){

            inisiasiPerbandingan();
            prosesPrioritas();
            for (int i=0;i<prioritas.size();i++){
                prioritas.get(i).setNilai(nilaiBobotAHP[i]);
                Log.e(""+prioritas.get(i).getId()+" "+prioritas.get(i).getKriteria()," "+prioritas.get(i).getNilai());
            }

            Intent i = new Intent(PilihKriteria.this, RankingActivity.class);
            i.putExtra(getString(R.string.INPUT_TIPE), getString(R.string.INPUT_OTOMATIS));
            i.putExtra(getString(R.string.metode), "AHP");
            i.putExtra("k1", k1);
            i.putExtra("k2", k2);
            i.putExtra("k3", k3);
            i.putExtra("k4", k4);
            i.putExtra("k5", k5);
            i.putExtra("k6", k6);
            i.putExtra("k7", k7);
            startActivityForResult(i, 102);
            Toast.makeText(getApplicationContext(),"Metode AHP", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId()==R.id.fahp_perhitungan){
            inisiasiPerbandingan();
            prosesPrioritas();

            hitungFuzzySysntheticExtent();
            hitungMinimumFuzzySynthetic();

            Intent i = new Intent(PilihKriteria.this, HitungFuzzyAHP.class);
            i.putExtra(getString(R.string.INPUT_TIPE), getString(R.string.INPUT_OTOMATIS));
            i.putExtra("k1", k1);
            i.putExtra("k2", k2);
            i.putExtra("k3", k3);
            i.putExtra("k4", k4);
            i.putExtra("k5", k5);
            i.putExtra("k6", k6);
            i.putExtra("k7", k7);
            startActivityForResult(i, 102);
            Toast.makeText(getApplicationContext(),"Detail Perhitungan FAHP"+prioritas.size(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClick(View view, int position) {

    }

    //
    //
    //
    //  prioritas atau Perhitungan metode AHP
    //
    //

    public void hitungVektorPrioritas (){

        double jumlahKolomMatriks []=new double[jumlahInput];
        double jumlahBarisMatriksVP []=new double[jumlahInput];
        matriksVektorPrioritas = new double[jumlahInput][jumlahInput];
        matriksHasilVP = new double[jumlahInput];
        nilaiBobotAHP = new double[jumlahInput];
        //menentukan jumlah kolom tabel matriks nilai kriteria
        for (int i = 0; i < matriksNilaiKriteria.length; i++) {
            for (int j = 0; j < matriksNilaiKriteria.length; j++) {

                jumlahKolomMatriks[i] = jumlahKolomMatriks[i] + matriksNilaiKriteria[j][i];
                //Log.i("Nilai Jumlah Kolom",  j+" : "+i+" = "+ a.formatDecimal(jumlahKolomMatriks[i]));
            }
        }
        //menentukan nilai matriks vektor prioritas (matriksVP dibagi jumlah kolom)
        for (int i = 0; i < matriksNilaiKriteria.length; i++) {
            for (int j = 0; j < matriksNilaiKriteria.length; j++) {
                matriksVektorPrioritas[i][j] = matriksNilaiKriteria[i][j] / jumlahKolomMatriks[j];
            }
        }
        //menentukan jumlah baris matriks vektor prioritas
        for (int i = 0; i < matriksVektorPrioritas.length; i++) {
            for (int j = 0; j < matriksVektorPrioritas.length; j++) {
                jumlahBarisMatriksVP[i] = jumlahBarisMatriksVP[i] + matriksVektorPrioritas[i][j];
            }
            matriksHasilVP[i]=jumlahBarisMatriksVP[i]/jumlahInput;
            listVektorPrioritas.add(matriksHasilVP[i]);
            nilaiBobotAHP[i]=matriksHasilVP[i];
        }

        Utils a = new Utils();
        for (int j = 0; j < matriksNilaiKriteria.length; j++) {
            Log.i("Nilai Jumlah Kolom",  " " + a.formatDecimal(jumlahKolomMatriks[j]));
        }

        for (int i = 0; i < matriksNilaiKriteria.length; i++) {
            for (int j = 0; j < matriksNilaiKriteria.length; j++) {
                Log.i("Matriks VP", "K"+ i + a.formatDecimal(matriksVektorPrioritas[i][j]));
            }
        }

    }


    ///
    ///
    //      Perhitungan Fuzzy AHP
    ///
    ///

    private void hitungFuzzySysntheticExtent(){
        matriksInputKriteria = matriksNilaiKriteria;
        nilaiS = new double[matriksInputKriteria.length][matriksInputKriteria.length];

        double jumlahTempL=0, jumlahTempM=0, jumlahTempU=0, tempL=0, tempM=0, tempU=0;
        double jumlahL[][] = new double[matriksInputKriteria.length][matriksInputKriteria.length];
        double jumlahM[][] = new double[matriksInputKriteria.length][matriksInputKriteria.length];
        double jumlahU[][] = new double[matriksInputKriteria.length][matriksInputKriteria.length];


        //mengubah skala ahp ke TFN
        for(int i = 0; i< matriksInputKriteria.length; i++){
            tempL = 0;
            tempM = 0;
            tempU = 0;
            for (int j = 0; j < matriksInputKriteria[0].length; j++) {

                if(matriksInputKriteria[i][j]==1.0) {
                    //Log.i("Matriks A  ", "ke "+ i+"-"+j + "= " + matriksInputKriteria[i][j]);
                    for(int k=0;k<3;k++){
                        if(k==0){
                            jumlahTempL = jumlahTempL + 1;
                            tempL = tempL+1;
                        }if(k==1){
                            jumlahTempM = jumlahTempM + matriksInputKriteria[i][j];
                            tempM = tempM + matriksInputKriteria[i][j];
                        }if(k==2){
                            jumlahTempU = jumlahTempU + 1;
                            tempU = tempU+1;
                        }
                        jumlahL[i][k]=tempL;
                        jumlahM[i][k]=tempM;
                        jumlahU[i][k]=tempU;
                    }
                }

                if(matriksInputKriteria[i][j]>1.0){
                    //Log.i("Matriks A  ", "ke "+ i+"-"+j + "= " + matriksInputKriteria[i][j]);
                    for(int k=0;k<3;k++){
                        if(k==0){
                            jumlahTempL = jumlahTempL + (matriksInputKriteria[i][j] - 1);
                            tempL = tempL +  (matriksInputKriteria[i][j] - 1);
                        }if(k==1){
                            jumlahTempM = jumlahTempM + (matriksInputKriteria[i][j]);
                            tempM = tempM +  (matriksInputKriteria[i][j]);
                        }if(k==2){
                            jumlahTempU = jumlahTempU + (matriksInputKriteria[i][j] + 1);
                            tempU = tempU +  (matriksInputKriteria[i][j] + 1);
                        }
                        jumlahL[i][k]=tempL;
                        jumlahM[i][k]=tempM;
                        jumlahU[i][k]=tempU;
                    }
                }if(matriksInputKriteria[i][j]<1.0){
                    //Log.i("Matriks A  ", "ke "+ i+"-"+j + "= " + matriksInputKriteria[i][j]);
                    for(int k=0;k<3;k++){
                        if(k==0){
                            jumlahTempL = jumlahTempL + (1/((1/ matriksInputKriteria[i][j]) + 1));
                            tempL = tempL +  (1/((1/ matriksInputKriteria[i][j]) + 1));
                        }if(k==1){
                            jumlahTempM = jumlahTempM + (matriksInputKriteria[i][j]);
                            tempM = tempM +  (matriksInputKriteria[i][j]);
                        }if(k==2){
                            jumlahTempU = jumlahTempU + (1/((1/ matriksInputKriteria[i][j]) - 1));
                            tempU = tempU +  (1/((1/ matriksInputKriteria[i][j]) - 1));
                        }
                        jumlahL[i][k]=tempL;
                        jumlahM[i][k]=tempM;
                        jumlahU[i][k]=tempU;
                    }
                }
            }
        }

        //print tabel TFN
        Utils a = new Utils();
        for (int i = 0; i < jumlahL.length; i++) {
            for (int j = 0; j < 2; j++) {
                if(j==0){
                    if(i==0){
                        Log.e("","Kriteria "+"x"+""+"\t\t\t"+"L"+"\t\t\t\t\t"+"M"+"\t\t\t\t"+"U"+"\n");
                    }
                    Log.e("","Kriteria "+i+"\t\t\t"+a.formatDecimal(jumlahL[i][j]) + "\t\t" +a.formatDecimal(jumlahM[i][j+1]) + "\t\t" + a.formatDecimal(jumlahU[i][j+2]) + "\n");
                }else{
                    Log.e("","");
                    //tvLihatTabel.setText("\n");
                }
            }
        }
        Log.e("","Jumlah "+ "\t\t\t\t" +formatDecimal(jumlahTempL)+ "\t\t" +formatDecimal(jumlahTempM)+ "\t\t" +formatDecimal(jumlahTempU));


        for (int i = 0; i < matriksInputKriteria.length; i++) {
            for (int j = 0; j < matriksInputKriteria[0].length; j++) {
                if (i == 0) {
                    jumlahL[j][i] = jumlahL[j][i] * (1 / jumlahTempU);
                }if(i == 1){
                    jumlahM[j][i] = jumlahM[j][i] * (1 / jumlahTempM);
                }if(i == 2){
                    jumlahU[j][i] = jumlahU[j][i] * (1 / jumlahTempL);
                }
            }
        }

        //print nilai S
        for (int i = 0; i < jumlahL.length; i++) {
            for (int j = 0; j < 2; j++) {
                if(j==0){
                    if(i==0){
                        Log.e("","Nilai S"+"x"+""+"\t\t\t"+"L"+"\t\t\t\t\t"+"M"+"\t\t\t\t"+"U"+"\n");
                    }
                    Log.e("","Nilai S"+i+"\t\t\t"+a.formatDecimal(jumlahL[i][j]) + "\t\t" +a.formatDecimal(jumlahM[i][j+1]) + "\t\t" + a.formatDecimal(jumlahU[i][j+2]) + "\n");
                }else{
                    Log.e("","");
                }
            }
        }

        //01 : 11 dan 01 : 21    s1:s2 dan s1:s3
        //11 : 01 dan 11 : 21    s2:s1 dan s2:s2
        //21 : 01 dan 21 : 11    s3:s1 dan s3:s2

        for (int i = 0; i < matriksInputKriteria.length; i++) {
            for (int j = 0; j < matriksInputKriteria[0].length; j++) {
                if(i==j){
                    nilaiS[i][j]=100;
                }else if(jumlahM[i][1]>=jumlahM[j][1]){
                    nilaiS[i][j]=1;
                }else if(jumlahL[j][0]>=jumlahU[i][2]){
                    nilaiS[i][j]=0;
                }else {
                    nilaiS[i][j]=(jumlahL[j][0]-jumlahU[i][2])/((jumlahM[i][1]-jumlahU[i][2])-(jumlahM[j][1]-jumlahL[j][0]));
                }
            }
        }

        for (int i = 0; i < nilaiS.length; i++) {
            for (int j = 0; j < nilaiS.length; j++) {

                if(j==nilaiS.length-1){
                    Log.e("","\t"+ a.formatDecimal(nilaiS[i][j]) + "\n");
                }else{
                    if(i==0 && j==0){
                        Log.e("","Tingkat Kemungkinan"+"\n");
                        for (int k=0;k<nilaiS.length;k++){
                            Log.e("","\t"+"S"+k+"\t\t");
                        }
                        Log.e("","\n");
                    }
                    Log.e("","\t"+ a.formatDecimal(nilaiS[i][j]) + "\t");
                }

            }
        }
    }

    private void hitungMinimumFuzzySynthetic(){
        Utils a = new Utils();
        double nilaiMinimum[]= new double [matriksInputKriteria[0].length];

        for(int i = 0; i< matriksInputKriteria.length; i++){
            nilaiMinimum[i] = 100;
            for(int j = 0; j< matriksInputKriteria[0].length; j++){
                if(nilaiS[i][j]<nilaiMinimum[i]){
                    nilaiMinimum[i] = nilaiS[i][j];
                }
            }
        }

        //print nilai minimum
        for (int j = 0; j < matriksInputKriteria[0].length; j++) {
            if (j == 0) {
                Log.e("","Nilai Minimum S" + "x" + "" + "\t\t\t" + "Nilai" + "\n");
            }
            Log.e("","Nilai Minimum S" + j + "\t\t\t" + a.formatDecimal(nilaiMinimum[j]) + "\n");
        }

        double nilaiNormalisasi[] = new double[nilaiMinimum.length];
        double total = 0;

        for(int i=0;i<nilaiMinimum.length;i++){
            total = total + nilaiMinimum[i];
        }
        nilaiBobot = nilaiNormalisasi;
        for(int i=0;i<nilaiNormalisasi.length;i++){
            nilaiNormalisasi[i]=nilaiMinimum[i]/total;
            nilaiBobot[i]=nilaiNormalisasi[i];
        }

        //print nilai minimum
        for (int j = 0; j < nilaiNormalisasi.length; j++) {
            if (j == 0) {
                Log.e("","Nilai Normalisasi K" + "x" + "" + "\t\t\t" + "Nilai" + "\n");
            }
            Log.e("","Nilai Normalisasi K" + j + "\t\t\t" + a.formatDecimal(nilaiNormalisasi[j]) + "\n");
        }
        for (int i=0;i<prioritas.size();i++){
            prioritas.get(i).setNilai(nilaiBobot[i]);
            Log.e(""+prioritas.get(i).getId()+""+prioritas.get(i).getKriteria()," "+prioritas.get(i).getNilai());
        }
        //tvHasilNormalisasi.setText("W(A) : "+formatDecimal(nilaiNormalisasi[0])+", "+formatDecimal(nilaiNormalisasi[1])+", "+formatDecimal(nilaiNormalisasi[2]));

    }

    public static double[] getVektorBobot(){
        return nilaiBobot;
    }

    public static double[] getVektorBobotAhp(){
        return nilaiBobotAHP;
    }

    public static ArrayList<Prioritas> getPrioritasKriteriaList(){
        return prioritas;
    }

    private String formatDecimal(double bilangan){
        return String.format("%.6f", bilangan);
    }
}