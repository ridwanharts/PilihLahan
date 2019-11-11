package com.labs.jangkriek.carilahan.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.labs.jangkriek.carilahan.POJO.Prioritas;
import com.labs.jangkriek.carilahan.R;

import java.util.ArrayList;
import java.util.Arrays;

import static com.labs.jangkriek.carilahan.Activity.MainActivity.getLoginType;

public class PilihKriteria extends AppCompatActivity {

    private CheckBox cbDayaDukungTanah, cbKetersediaanAir, cbKemiringanLereng, cbAksebilitas;
    private CheckBox cbPerubahanLahan, cbKerawananBencana, cbJarakKeBandara;
    private Boolean k1=false,k2=false,k3=false,k4=false,k5=false,k6=false,k7=false;
    private RadioButton rb1k1,rb2k1,rb3k1,rb4k1,rb5k1,rb6k1,rb7k1;
    private RadioButton rb1k2,rb2k2,rb3k2,rb4k2,rb5k2,rb6k2,rb7k2;
    private RadioButton rb1k3,rb2k3,rb3k3,rb4k3,rb5k3,rb6k3,rb7k3;
    private RadioButton rb1k4,rb2k4,rb3k4,rb4k4,rb5k4,rb6k4,rb7k4;
    private RadioButton rb1k5,rb2k5,rb3k5,rb4k5,rb5k5,rb6k5,rb7k5;
    private RadioButton rb1k6,rb2k6,rb3k6,rb4k6,rb5k6,rb6k6,rb7k6;
    private RadioButton rb1k7,rb2k7,rb3k7,rb4k7,rb5k7,rb6k7,rb7k7;

    private Button btnProses, btnDialogPilihOtomatis, btnDialogPilihManual, btnPilih;

    private TextView tvJumlahInput, tvError, tvDaya, tvLereng, tvAir, tvAksebilitas,tvHarga, tvBencana, tvBandara;
    private TextView tvK1,tvK2,tvK3,tvK4,tvK5,tvK6,tvK7;

    private LinearLayout inputOtomatis, inputManual, rg1,rg2,rg3,rg4,rg5,rg6,rg7;

    static int jumlahInput = 0;

    public static ArrayList<Prioritas> prioritas = new ArrayList<Prioritas>();
    private static double matriksNilaiKriteria [][];

    private final int OTOMATIS = 301;
    private final int MANUAL = 303;
    private int inputTipe = 0;

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

    }

    //inisiasi button dan checkbox
    private void inisiasiButtonCheckBox(){

        //
        //inisiasi
        //
        btnProses = findViewById(R.id.btn_input_activity);
        btnPilih = findViewById(R.id.btn_pilih);

        inputOtomatis = findViewById(R.id.input_otomatis);
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

        rb1k1 = findViewById(R.id.rb1k1);
        rb2k1 = findViewById(R.id.rb2k1);
        rb3k1 = findViewById(R.id.rb3k1);
        rb4k1 = findViewById(R.id.rb4k1);
        rb5k1 = findViewById(R.id.rb5k1);
        rb6k1 = findViewById(R.id.rb6k1);
        rb7k1 = findViewById(R.id.rb7k1);

        rb1k2 = findViewById(R.id.rb1k2);
        rb2k2 = findViewById(R.id.rb2k2);
        rb3k2 = findViewById(R.id.rb3k2);
        rb4k2 = findViewById(R.id.rb4k2);
        rb5k2 = findViewById(R.id.rb5k2);
        rb6k2 = findViewById(R.id.rb6k2);
        rb7k2 = findViewById(R.id.rb7k2);

        rb1k3 = findViewById(R.id.rb1k3);
        rb2k3 = findViewById(R.id.rb2k3);
        rb3k3 = findViewById(R.id.rb3k3);
        rb4k3 = findViewById(R.id.rb4k3);
        rb5k3 = findViewById(R.id.rb5k3);
        rb6k3 = findViewById(R.id.rb6k3);
        rb7k3 = findViewById(R.id.rb7k3);

        rb1k4 = findViewById(R.id.rb1k4);
        rb2k4 = findViewById(R.id.rb2k4);
        rb3k4 = findViewById(R.id.rb3k4);
        rb4k4 = findViewById(R.id.rb4k4);
        rb5k4 = findViewById(R.id.rb5k4);
        rb6k4 = findViewById(R.id.rb6k4);
        rb7k4 = findViewById(R.id.rb7k4);

        rb1k5 = findViewById(R.id.rb1k5);
        rb2k5 = findViewById(R.id.rb2k5);
        rb3k5 = findViewById(R.id.rb3k5);
        rb4k5 = findViewById(R.id.rb4k5);
        rb5k5 = findViewById(R.id.rb5k5);
        rb6k5 = findViewById(R.id.rb6k5);
        rb7k5 = findViewById(R.id.rb7k5);
        
        rb1k6 = findViewById(R.id.rb1k6);
        rb2k6 = findViewById(R.id.rb2k6);
        rb3k6 = findViewById(R.id.rb3k6);
        rb4k6 = findViewById(R.id.rb4k6);
        rb5k6 = findViewById(R.id.rb5k6);
        rb6k6 = findViewById(R.id.rb6k6);
        rb7k6 = findViewById(R.id.rb7k6);
        
        rb1k7 = findViewById(R.id.rb1k7);
        rb2k7 = findViewById(R.id.rb2k7);
        rb3k7 = findViewById(R.id.rb3k7);
        rb4k7 = findViewById(R.id.rb4k7);
        rb5k7 = findViewById(R.id.rb5k7);
        rb6k7 = findViewById(R.id.rb6k7);
        rb7k7 = findViewById(R.id.rb7k7);

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
        inputOtomatis.setVisibility(View.INVISIBLE);
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
                    prosesPrioritas();
                    inisiasiPerbandingan();
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
                resetPrioritasPilihan();
                setVisibleRadioButton();
                k1=false;k2=false;k3=false;k4=false;k5=false;k6=false;k7=false;
                //k1Send=false;k2Send=false;k3Send=false;k4Send=false;k5Send=false;k6Send=false;k7Send=false;
                jumlahInput = 0;
                inputOtomatis.setVisibility(View.INVISIBLE);
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
                }else {
                    setInvisibleRbK1();
                }

                if (k2){
                    jumlahInput++;
                }else {
                    setInvisibleRbK2();
                }

                if (k3){
                    jumlahInput++;
                }else{
                    setInvisibleRbK3();
                }

                if (k4){
                    jumlahInput++;
                }else{
                    setInvisibleRbK4();
                }

                if (k5) {
                    jumlahInput++;
                }else {
                    setInvisibleRbK5();
                }

                if (k6) {
                    jumlahInput++;
                }else {
                    setInvisibleRbK6();
                }

                if(k7) {
                    jumlahInput++;
                }else {
                    setInvisibleRbK7();
                }

                if (jumlahInput >= 3){
                    tvJumlahInput.setText(String.valueOf(jumlahInput));
                    inputOtomatis.setVisibility(View.VISIBLE);
                    inputTipe = OTOMATIS;
                    rg1.setVisibility(View.INVISIBLE);
                    rg2.setVisibility(View.INVISIBLE);
                    rg3.setVisibility(View.INVISIBLE);
                    rg4.setVisibility(View.INVISIBLE);
                    rg5.setVisibility(View.INVISIBLE);
                    rg6.setVisibility(View.INVISIBLE);
                    rg7.setVisibility(View.INVISIBLE);

                    if (jumlahInput == 3){
                        rg1.setVisibility(View.VISIBLE);
                        rg2.setVisibility(View.VISIBLE);
                        rg3.setVisibility(View.VISIBLE);
                    }if (jumlahInput == 4){
                        rg1.setVisibility(View.VISIBLE);
                        rg2.setVisibility(View.VISIBLE);
                        rg3.setVisibility(View.VISIBLE);
                        rg4.setVisibility(View.VISIBLE);
                    }if (jumlahInput == 5){
                        rg1.setVisibility(View.VISIBLE);
                        rg2.setVisibility(View.VISIBLE);
                        rg3.setVisibility(View.VISIBLE);
                        rg4.setVisibility(View.VISIBLE);
                        rg5.setVisibility(View.VISIBLE);
                    }if (jumlahInput == 6){
                        rg1.setVisibility(View.VISIBLE);
                        rg2.setVisibility(View.VISIBLE);
                        rg3.setVisibility(View.VISIBLE);
                        rg4.setVisibility(View.VISIBLE);
                        rg5.setVisibility(View.VISIBLE);
                        rg6.setVisibility(View.VISIBLE);
                    }if (jumlahInput == 7){
                        rg1.setVisibility(View.VISIBLE);
                        rg2.setVisibility(View.VISIBLE);
                        rg3.setVisibility(View.VISIBLE);
                        rg4.setVisibility(View.VISIBLE);
                        rg5.setVisibility(View.VISIBLE);
                        rg6.setVisibility(View.VISIBLE);
                        rg7.setVisibility(View.VISIBLE);
                    }
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

            }
        });

        btnDialogPilihManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                k1=false;k2=false;k3=false;k4=false;k5=false;k6=false;k7=false;
                //k1Send=false;k2Send=false;k3Send=false;k4Send=false;k5Send=false;k6Send=false;k7Send=false;
                jumlahInput = 0;
                inputManual.setVisibility(View.INVISIBLE);
                inputOtomatis.setVisibility(View.INVISIBLE);
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

    //reset checkbox
    public void resetPrioritasPilihan(){
        rb1k1.setChecked(false);
        rb2k1.setChecked(false);
        rb3k1.setChecked(false);
        rb4k1.setChecked(false);
        rb5k1.setChecked(false);
        rb6k1.setChecked(false);
        rb7k1.setChecked(false);

        rb1k2.setChecked(false);
        rb2k2.setChecked(false);
        rb3k2.setChecked(false);
        rb4k2.setChecked(false);
        rb5k2.setChecked(false);
        rb6k2.setChecked(false);
        rb7k2.setChecked(false);

        rb1k3.setChecked(false);
        rb2k3.setChecked(false);
        rb3k3.setChecked(false);
        rb4k3.setChecked(false);
        rb5k3.setChecked(false);
        rb6k3.setChecked(false);
        rb7k3.setChecked(false);

        rb1k4.setChecked(false);
        rb2k4.setChecked(false);
        rb3k4.setChecked(false);
        rb4k4.setChecked(false);
        rb5k4.setChecked(false);
        rb6k4.setChecked(false);
        rb7k4.setChecked(false);

        rb1k5.setChecked(false);
        rb2k5.setChecked(false);
        rb3k5.setChecked(false);
        rb4k5.setChecked(false);
        rb5k5.setChecked(false);
        rb6k5.setChecked(false);
        rb7k5.setChecked(false);

        rb1k6.setChecked(false);
        rb2k6.setChecked(false);
        rb3k6.setChecked(false);
        rb4k6.setChecked(false);
        rb5k6.setChecked(false);
        rb6k6.setChecked(false);
        rb7k6.setChecked(false);

        rb1k7.setChecked(false);
        rb2k7.setChecked(false);
        rb3k7.setChecked(false);
        rb4k7.setChecked(false);
        rb5k7.setChecked(false);
        rb6k7.setChecked(false);
        rb7k7.setChecked(false);
    }

    //set visible all radio button
    public void setVisibleRadioButton(){
        tvK1.setVisibility(View.VISIBLE);
        rb1k1.setVisibility(View.VISIBLE);
        rb2k1.setVisibility(View.VISIBLE);
        rb3k1.setVisibility(View.VISIBLE);
        rb4k1.setVisibility(View.VISIBLE);
        rb5k1.setVisibility(View.VISIBLE);
        rb6k1.setVisibility(View.VISIBLE);
        rb7k1.setVisibility(View.VISIBLE);

        tvK2.setVisibility(View.VISIBLE);
        rb1k2.setVisibility(View.VISIBLE);
        rb2k2.setVisibility(View.VISIBLE);
        rb3k2.setVisibility(View.VISIBLE);
        rb4k2.setVisibility(View.VISIBLE);
        rb5k2.setVisibility(View.VISIBLE);
        rb6k2.setVisibility(View.VISIBLE);
        rb7k2.setVisibility(View.VISIBLE);

        tvK3.setVisibility(View.VISIBLE);
        rb1k3.setVisibility(View.VISIBLE);
        rb2k3.setVisibility(View.VISIBLE);
        rb3k3.setVisibility(View.VISIBLE);
        rb4k3.setVisibility(View.VISIBLE);
        rb5k3.setVisibility(View.VISIBLE);
        rb6k3.setVisibility(View.VISIBLE);
        rb7k3.setVisibility(View.VISIBLE);

        tvK4.setVisibility(View.VISIBLE);
        rb1k4.setVisibility(View.VISIBLE);
        rb2k4.setVisibility(View.VISIBLE);
        rb3k4.setVisibility(View.VISIBLE);
        rb4k4.setVisibility(View.VISIBLE);
        rb5k4.setVisibility(View.VISIBLE);
        rb6k4.setVisibility(View.VISIBLE);
        rb7k4.setVisibility(View.VISIBLE);

        tvK5.setVisibility(View.VISIBLE);
        rb1k5.setVisibility(View.VISIBLE);
        rb2k5.setVisibility(View.VISIBLE);
        rb3k5.setVisibility(View.VISIBLE);
        rb4k5.setVisibility(View.VISIBLE);
        rb5k5.setVisibility(View.VISIBLE);
        rb6k5.setVisibility(View.VISIBLE);
        rb7k5.setVisibility(View.VISIBLE);

        tvK6.setVisibility(View.VISIBLE);
        rb1k6.setVisibility(View.VISIBLE);
        rb2k6.setVisibility(View.VISIBLE);
        rb3k6.setVisibility(View.VISIBLE);
        rb4k6.setVisibility(View.VISIBLE);
        rb5k6.setVisibility(View.VISIBLE);
        rb6k6.setVisibility(View.VISIBLE);
        rb7k6.setVisibility(View.VISIBLE);

        tvK7.setVisibility(View.VISIBLE);
        rb1k7.setVisibility(View.VISIBLE);
        rb2k7.setVisibility(View.VISIBLE);
        rb3k7.setVisibility(View.VISIBLE);
        rb4k7.setVisibility(View.VISIBLE);
        rb5k7.setVisibility(View.VISIBLE);
        rb6k7.setVisibility(View.VISIBLE);
        rb7k7.setVisibility(View.VISIBLE);
    }
    
    public void setInvisibleRbK1(){
        tvK1.setVisibility(View.INVISIBLE);
        rb1k1.setVisibility(View.INVISIBLE);
        rb2k1.setVisibility(View.INVISIBLE);
        rb3k1.setVisibility(View.INVISIBLE);
        rb4k1.setVisibility(View.INVISIBLE);
        rb5k1.setVisibility(View.INVISIBLE);
        rb6k1.setVisibility(View.INVISIBLE);
        rb7k1.setVisibility(View.INVISIBLE);
    }

    public void setInvisibleRbK2(){
        tvK2.setVisibility(View.INVISIBLE);
        rb1k2.setVisibility(View.INVISIBLE);
        rb2k2.setVisibility(View.INVISIBLE);
        rb3k2.setVisibility(View.INVISIBLE);
        rb4k2.setVisibility(View.INVISIBLE);
        rb5k2.setVisibility(View.INVISIBLE);
        rb6k2.setVisibility(View.INVISIBLE);
        rb7k2.setVisibility(View.INVISIBLE);
    }

    public void setInvisibleRbK3(){
        tvK3.setVisibility(View.INVISIBLE);
        rb1k3.setVisibility(View.INVISIBLE);
        rb2k3.setVisibility(View.INVISIBLE);
        rb3k3.setVisibility(View.INVISIBLE);
        rb4k3.setVisibility(View.INVISIBLE);
        rb5k3.setVisibility(View.INVISIBLE);
        rb6k3.setVisibility(View.INVISIBLE);
        rb7k3.setVisibility(View.INVISIBLE);
    }

    public void setInvisibleRbK4(){
        tvK4.setVisibility(View.INVISIBLE);
        rb1k4.setVisibility(View.INVISIBLE);
        rb2k4.setVisibility(View.INVISIBLE);
        rb3k4.setVisibility(View.INVISIBLE);
        rb4k4.setVisibility(View.INVISIBLE);
        rb5k4.setVisibility(View.INVISIBLE);
        rb6k4.setVisibility(View.INVISIBLE);
        rb7k4.setVisibility(View.INVISIBLE);
    }

    public void setInvisibleRbK5(){
        tvK5.setVisibility(View.INVISIBLE);
        rb1k5.setVisibility(View.INVISIBLE);
        rb2k5.setVisibility(View.INVISIBLE);
        rb3k5.setVisibility(View.INVISIBLE);
        rb4k5.setVisibility(View.INVISIBLE);
        rb5k5.setVisibility(View.INVISIBLE);
        rb6k5.setVisibility(View.INVISIBLE);
        rb7k5.setVisibility(View.INVISIBLE);
    }

    public void setInvisibleRbK6(){
        tvK6.setVisibility(View.INVISIBLE);
        rb1k6.setVisibility(View.INVISIBLE);
        rb2k6.setVisibility(View.INVISIBLE);
        rb3k6.setVisibility(View.INVISIBLE);
        rb4k6.setVisibility(View.INVISIBLE);
        rb5k6.setVisibility(View.INVISIBLE);
        rb6k6.setVisibility(View.INVISIBLE);
        rb7k6.setVisibility(View.INVISIBLE);
    }

    public void setInvisibleRbK7(){
        tvK7.setVisibility(View.INVISIBLE);
        rb1k7.setVisibility(View.INVISIBLE);
        rb2k7.setVisibility(View.INVISIBLE);
        rb3k7.setVisibility(View.INVISIBLE);
        rb4k7.setVisibility(View.INVISIBLE);
        rb5k7.setVisibility(View.INVISIBLE);
        rb6k7.setVisibility(View.INVISIBLE);
        rb7k7.setVisibility(View.INVISIBLE);
    }

    public void prosesPrioritas(){
        prioritas.clear();
        int count = 1;
        cekRadioGroup1(count);
        count++;

        cekRadioGroup2(count);
        count++;

        cekRadioGroup3(count);
        count++;

        cekRadioGroup4(count);
        count++;

        cekRadioGroup5(count);
        count++;

        cekRadioGroup6(count);
        count++;

        cekRadioGroup7(count);
        count++;

        Toast.makeText(getApplicationContext(), "Count : " + count,Toast.LENGTH_SHORT).show();
        for (int i=0;i<prioritas.size();i++){
            Log.e("Prioritas ke "," "+prioritas.get(i).getId()+" : "+prioritas.get(i).getKriteria());
        }
    }

    //add array list berdasarkan prioritas
    public void cekRadioGroup1 (int count){
        if (rg1.getVisibility() == View.VISIBLE){
            if (rb1k1.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 1 Kriteria 1",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K1_DAYA_DUKUNG_TANAH)));
            }
            if (rb1k2.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 1 Kriteria 2",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K2_KETERSEDIAAN_AIR)));
            }
            if (rb1k3.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 1 Kriteria 3",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K3_KEMIRINGAN_LERENG)));
            }
            if (rb1k4.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 1 Kriteria 4",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K4_AKSEBILITAS)));
            }
            if (rb1k5.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 1 Kriteria 5",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K5_HARGA_LAHAN)));
            }
            if (rb1k6.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 1 Kriteria 6",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K6_KERAWANAN_BENCANA)));
            }
            if (rb1k7.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 1 Kriteria 7",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K7_JARAK_KE_BANDARA)));
            }
        }
    }
    public void cekRadioGroup2 (int count){
        if (rg2.getVisibility() == View.VISIBLE){
            if (rb2k1.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 1",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count,getString(R.string.K1_DAYA_DUKUNG_TANAH)));
            }
            if (rb2k2.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 2",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K2_KETERSEDIAAN_AIR)));
            }
            if (rb2k3.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 3",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K3_KEMIRINGAN_LERENG)));
            }
            if (rb2k4.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 4",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K4_AKSEBILITAS)));
            }
            if (rb2k5.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 5",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K5_HARGA_LAHAN)));
            }
            if (rb2k6.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 6",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K6_KERAWANAN_BENCANA)));
            }
            if (rb2k7.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 7",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K7_JARAK_KE_BANDARA)));
            }
        }
    }
    public void cekRadioGroup3 (int count){
        if (rg3.getVisibility() == View.VISIBLE){
            if (rb3k1.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 1",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count,getString(R.string.K1_DAYA_DUKUNG_TANAH)));
            }
            if (rb3k2.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 2",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K2_KETERSEDIAAN_AIR)));
            }
            if (rb3k3.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 3",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K3_KEMIRINGAN_LERENG)));
            }
            if (rb3k4.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 4",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K4_AKSEBILITAS)));
            }
            if (rb3k5.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 5",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K5_HARGA_LAHAN)));
            }
            if (rb3k6.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 6",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K6_KERAWANAN_BENCANA)));
            }
            if (rb3k7.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 7",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K7_JARAK_KE_BANDARA)));
            }
        }
    }
    public void cekRadioGroup4 (int count){
        if (rg4.getVisibility() == View.VISIBLE){
            if (rb4k1.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 1",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count,getString(R.string.K1_DAYA_DUKUNG_TANAH)));
            }
            if (rb4k2.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 2",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K2_KETERSEDIAAN_AIR)));
            }
            if (rb4k3.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 3",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K3_KEMIRINGAN_LERENG)));
            }
            if (rb4k4.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 4",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K4_AKSEBILITAS)));
            }
            if (rb4k5.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 5",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K5_HARGA_LAHAN)));
            }
            if (rb4k6.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 6",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K6_KERAWANAN_BENCANA)));
            }
            if (rb4k7.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 7",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K7_JARAK_KE_BANDARA)));
            }
        }
    }
    public void cekRadioGroup5 (int count){
        if (rg5.getVisibility() == View.VISIBLE) {
            if (rb5k1.isChecked()) {
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 1",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K1_DAYA_DUKUNG_TANAH)));
            }
            if (rb5k2.isChecked()) {
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 2",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K2_KETERSEDIAAN_AIR)));
            }
            if (rb5k3.isChecked()) {
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 3",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K3_KEMIRINGAN_LERENG)));
            }
            if (rb5k4.isChecked()) {
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 4",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K4_AKSEBILITAS)));
            }
            if (rb5k5.isChecked()) {
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 5",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K5_HARGA_LAHAN)));
            }
            if (rb5k6.isChecked()) {
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 6",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K6_KERAWANAN_BENCANA)));
            }
            if (rb5k7.isChecked()) {
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 7",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K7_JARAK_KE_BANDARA)));
            }
        }
        
    }
    public void cekRadioGroup6 (int count){
        if (rg6.getVisibility() == View.VISIBLE){
            if (rb6k1.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 1",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count,getString(R.string.K1_DAYA_DUKUNG_TANAH)));
            }
            if (rb6k2.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 2",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K2_KETERSEDIAAN_AIR)));
            }
            if (rb6k3.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 3",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K3_KEMIRINGAN_LERENG)));
            }
            if (rb6k4.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 4",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K4_AKSEBILITAS)));
            }
            if (rb6k5.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 5",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K5_HARGA_LAHAN)));
            }
            if (rb6k6.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 6",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K6_KERAWANAN_BENCANA)));
            }
            if (rb6k7.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 7",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K7_JARAK_KE_BANDARA)));
            }
        }
    }
    public void cekRadioGroup7 (int count){
        if (rg7.getVisibility() == View.VISIBLE){
            if (rb7k1.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 1",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count,getString(R.string.K1_DAYA_DUKUNG_TANAH)));
            }
            if (rb7k2.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 2",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K2_KETERSEDIAAN_AIR)));
            }
            if (rb7k3.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 3",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K3_KEMIRINGAN_LERENG)));
            }
            if (rb7k4.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 4",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K4_AKSEBILITAS)));
            }
            if (rb7k5.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 5",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K5_HARGA_LAHAN)));
            }
            if (rb7k6.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 6",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K6_KERAWANAN_BENCANA)));
            }
            if (rb7k7.isChecked()){
                //Toast.makeText(getApplicationContext(), "Radio Button 2 Kriteria 7",Toast.LENGTH_SHORT).show();
                prioritas.add(new Prioritas(count, getString(R.string.K7_JARAK_KE_BANDARA)));
            }
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
        a.putExtra("LOGIN", getLoginType());
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        finish();
    }
}