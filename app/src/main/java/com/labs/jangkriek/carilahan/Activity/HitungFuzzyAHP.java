package com.labs.jangkriek.carilahan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.labs.jangkriek.carilahan.POJO.Prioritas;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Utils.Utils;

import java.util.ArrayList;

public class HitungFuzzyAHP extends AppCompatActivity {

    private TextView tvHasilTfn, tvNilaiS, tvNilaiKemungkinan, tvNilaiMinimum, tvHasilNormalisasi;
    private Button btnConverttoTFN, btnRank;
    private Boolean k1=false,k2=false,k3=false,k4=false,k5=false,k6=false,k7=false;

    public static ArrayList<Prioritas> prioritas = new ArrayList<Prioritas>();
    double[][] matriksInputKriteria = null;
    double nilaiS[][] = null;
    boolean cekNow = false;
    private static double nilaiBobot [];
    private String inputTipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hitung_fuzzy_ahp);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getSupportActionBar() == null){
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Hitung Vektor Bobot");
        }

        tvHasilTfn = findViewById(R.id.hasil_tfn);
        tvNilaiS = findViewById(R.id.nilai_s);
        tvNilaiKemungkinan = findViewById(R.id.nilai_kemungkinan);
        tvNilaiMinimum = findViewById(R.id.nilai_minimum_s);
        tvHasilNormalisasi = findViewById(R.id.hasil_normalisasi);
        btnConverttoTFN = findViewById(R.id.btn_matriks_tfn);
        btnRank = findViewById(R.id.btn_rank);

        Intent i = getIntent();

        inputTipe = i.getStringExtra(getString(R.string.INPUT_TIPE));
        k1 = i.getBooleanExtra("k1", k1);
        k2 = i.getBooleanExtra("k2", k2);
        k3 = i.getBooleanExtra("k3", k3);
        k4 = i.getBooleanExtra("k4", k4);
        k5 = i.getBooleanExtra("k5", k5);
        k6 = i.getBooleanExtra("k6", k6);
        k7 = i.getBooleanExtra("k7", k7);
        Toast.makeText(getApplicationContext(),""+k1, Toast.LENGTH_SHORT).show();

        if(inputTipe.equals(getString(R.string.INPUT_MANUAL))){
            matriksInputKriteria = InputNilaiKriteriaActivity.getMatriksNilaiKriteria();
            nilaiS = new double[matriksInputKriteria.length][matriksInputKriteria.length];
            Log.i(" ", "size matriks : "+ matriksInputKriteria.length);
        }else {
            matriksInputKriteria = PilihKriteria.getMatriksNilaiKriteria();
            prioritas = PilihKriteria.getPrioritasKriteria();
            nilaiS = new double[matriksInputKriteria.length][matriksInputKriteria.length];
            Toast.makeText(getApplicationContext(), ""+prioritas.size(),Toast.LENGTH_SHORT).show();
            Log.e(" ", "size matriks : "+ matriksInputKriteria.length);
        }

        btnConverttoTFN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(matriksInputKriteria != null){
                    //finishActivity(101);

                    hitungFuzzySysntheticExtent();

                    hitungMinimumFuzzySynthetic();
                }else {
                    Toast.makeText(getApplication(),"Nilai Kriteria Masih Kosong !",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HitungFuzzyAHP.this, RankingActivity.class);
                i.putExtra(getString(R.string.INPUT_TIPE), inputTipe);
                i.putExtra("k1", k1);
                i.putExtra("k2", k2);
                i.putExtra("k3", k3);
                i.putExtra("k4", k4);
                i.putExtra("k5", k5);
                i.putExtra("k6", k6);
                i.putExtra("k7", k7);
                startActivityForResult(i, 102);
            }
        });
    }

    private void hitungFuzzySysntheticExtent(){
        double jumlahTempL=0, jumlahTempM=0, jumlahTempU=0, tempL=0, tempM=0, tempU=0;
        double jumlahL[][] = new double[matriksInputKriteria.length][matriksInputKriteria.length];
        double jumlahM[][] = new double[matriksInputKriteria.length][matriksInputKriteria.length];
        double jumlahU[][] = new double[matriksInputKriteria.length][matriksInputKriteria.length];


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
                        tvHasilTfn.append("Kriteria "+"x"+""+"\t\t\t"+"L"+"\t\t\t\t\t"+"M"+"\t\t\t\t"+"U"+"\n");
                    }
                    tvHasilTfn.append("Kriteria "+i+"\t\t\t"+a.formatDecimal(jumlahL[i][j]) + "\t\t" +a.formatDecimal(jumlahM[i][j+1]) + "\t\t" + a.formatDecimal(jumlahU[i][j+2]) + "\n");
                }else{
                    tvHasilTfn.append("");
                    //tvLihatTabel.setText("\n");
                }
            }
        }
        tvHasilTfn.append("Jumlah "+ "\t\t\t\t" +formatDecimal(jumlahTempL)+ "\t\t" +formatDecimal(jumlahTempM)+ "\t\t" +formatDecimal(jumlahTempU));


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
                        tvNilaiS.append("Nilai S"+"x"+""+"\t\t\t"+"L"+"\t\t\t\t\t"+"M"+"\t\t\t\t"+"U"+"\n");
                    }
                    tvNilaiS.append("Nilai S"+i+"\t\t\t"+a.formatDecimal(jumlahL[i][j]) + "\t\t" +a.formatDecimal(jumlahM[i][j+1]) + "\t\t" + a.formatDecimal(jumlahU[i][j+2]) + "\n");
                }else{
                    tvNilaiS.append("");
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
                    tvNilaiKemungkinan.append("\t"+ a.formatDecimal(nilaiS[i][j]) + "\n");
                }else{
                    if(i==0 && j==0){
                        tvNilaiKemungkinan.append("Tingkat Kemungkinan"+"\n");
                        for (int k=0;k<nilaiS.length;k++){
                            tvNilaiKemungkinan.append("\t"+"S"+k+"\t\t");
                        }
                        tvNilaiKemungkinan.append("\n");
                    }
                    tvNilaiKemungkinan.append("\t"+ a.formatDecimal(nilaiS[i][j]) + "\t");
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
                tvNilaiMinimum.append("Nilai Minimum S" + "x" + "" + "\t\t\t" + "Nilai" + "\n");
            }
            tvNilaiMinimum.append("Nilai Minimum S" + j + "\t\t\t" + a.formatDecimal(nilaiMinimum[j]) + "\n");
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
                tvHasilNormalisasi.append("Nilai Normalisasi K" + "x" + "" + "\t\t\t" + "Nilai" + "\n");
            }
            tvHasilNormalisasi.append("Nilai Normalisasi K" + j + "\t\t\t" + a.formatDecimal(nilaiNormalisasi[j]) + "\n");
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

    public static ArrayList<Prioritas> getPrioritasKriteriaList(){
        return prioritas;
    }

    private String formatDecimal(double bilangan){
        return String.format("%.6f", bilangan);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }


}
