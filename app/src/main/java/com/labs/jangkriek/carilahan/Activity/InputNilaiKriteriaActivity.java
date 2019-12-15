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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.labs.jangkriek.carilahan.POJO.Kriteria;
import com.labs.jangkriek.carilahan.Adapter.KriteriaAdapter;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Utils.Utils;

import java.util.ArrayList;

public class InputNilaiKriteriaActivity extends AppCompatActivity {

    ListView inputKriteriaList;
    ArrayList<Kriteria> listKriteria;
    int jumlahKriteria;
    private Boolean k1=false,k2=false,k3=false,k4=false,k5=false,k6=false,k7=false;
    ImageView ivCloseInput;
    Button btnCheck, btnLihatTabel, btnCloseLihatTabel, btnProsesFuzzy;
    TextView tvCek, tvLihatTabel, tvNilaiLamdaMax, tvNilaiCI, tvNilaiCR;

    private static double matriksNilaiKriteria [][];
    double matriksVektorPrioritas [][];
    double matriksHasilVP [];

    private final int OTOMATIS = 301;
    private final int MANUAL = 303;

    boolean cek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_nilai_kriteria);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getSupportActionBar() == null){
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent i = getIntent();
        jumlahKriteria = i.getIntExtra("jumlah_input", jumlahKriteria);
        k1 = i.getBooleanExtra("k1", k1);
        k2 = i.getBooleanExtra("k2", k2);
        k3 = i.getBooleanExtra("k3", k3);
        k4 = i.getBooleanExtra("k4", k4);
        k5 = i.getBooleanExtra("k5", k5);
        k6 = i.getBooleanExtra("k6", k6);
        k7 = i.getBooleanExtra("k7", k7);
        matriksVektorPrioritas = new double[jumlahKriteria][jumlahKriteria];
        matriksHasilVP = new double[jumlahKriteria];
        Log.i("a",""+k1);
        Toast.makeText(getApplicationContext(),""+k1, Toast.LENGTH_SHORT).show();

        ivCloseInput = findViewById(R.id.iv_close);
        inputKriteriaList =findViewById(R.id.list_input_kriteria);
        tvCek = findViewById(R.id.tv_cek);
        tvNilaiLamdaMax = findViewById(R.id.tv_nilai_lamdamax);
        tvNilaiCI = findViewById(R.id.tv_nilai_ci);
        tvNilaiCR = findViewById(R.id.tv_nilai_cr);

        btnLihatTabel = findViewById(R.id.btn_lihat_tabel);
        btnCheck = findViewById(R.id.btn_cek);
        btnProsesFuzzy = findViewById(R.id.btn_proses_fuzzy);

        listKriteria = getListItemData();
        KriteriaAdapter adapter = new KriteriaAdapter(this, listKriteria);
        inputKriteriaList.setAdapter(adapter);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils a = new Utils();

                inisiasiPerbandingan();

                hitungVektorPrioritas();

                Log.i("Nilai k1", " " + a.formatDecimal(matriksNilaiKriteria[0][0]) + " " + a.formatDecimal(matriksNilaiKriteria[0][1]) + " " + a.formatDecimal(matriksNilaiKriteria[0][2]) + " " );
                Log.i("Nilai k2", " " + a.formatDecimal(matriksNilaiKriteria[1][0]) + " " + a.formatDecimal(matriksNilaiKriteria[1][1]) + " " + a.formatDecimal(matriksNilaiKriteria[1][2]) + " " );
                Log.i("Nilai k3", " " + a.formatDecimal(matriksNilaiKriteria[2][0]) + " " + a.formatDecimal(matriksNilaiKriteria[2][1]) + " " + a.formatDecimal(matriksNilaiKriteria[2][2]) + " " );
                //Log.i("Nilai k4", " " + a.formatDecimal(matriksNilaiKriteria[3][0]) + " " + a.formatDecimal(matriksNilaiKriteria[3][1]) + " " + a.formatDecimal(matriksNilaiKriteria[3][2]) + " " + a.formatDecimal(matriksNilaiKriteria[3][3]));

                hitungNilaiAx();

            }
        });

        btnProsesFuzzy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InputNilaiKriteriaActivity.this, HitungFuzzyAHP.class);
                inisiasiPerbandingan();
                cek = true;
                i.putExtra(getString(R.string.INPUT_TIPE), getString(R.string.INPUT_MANUAL));
                i.putExtra("cek", cek);
                i.putExtra("k1", k1);
                i.putExtra("k2", k2);
                i.putExtra("k3", k3);
                i.putExtra("k4", k4);
                i.putExtra("k5", k5);
                i.putExtra("k6", k6);
                i.putExtra("k7", k7);
                startActivityForResult(i, 101);
            }
        });

        btnLihatTabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }

        });

        ivCloseInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static double[][] getMatriksNilaiKriteria(){
        return matriksNilaiKriteria;
    }

    public void inisiasiPerbandingan(){
        int k=0;
        matriksNilaiKriteria = new double[jumlahKriteria][jumlahKriteria];
        for (int i = 0; i < matriksNilaiKriteria.length; i++) {
            for (int j = 0; j < matriksNilaiKriteria.length; j++) {
                if (i == j) {
                    matriksNilaiKriteria[i][j] = 1.0;
                }

                if (i < j) {
                    matriksNilaiKriteria[i][j] = listKriteria.get(k).getNilai();
                    matriksNilaiKriteria[j][i] = 1 / matriksNilaiKriteria[i][j];
                    //Log.i("Nilai Perbandingan", "" + i + " : " + j + " = " + listKriteria.get(k).getNilai());
                    k++;
                }

            }
        }
    }

    public void hitungVektorPrioritas (){

        double jumlahKolomMatriks []=new double[jumlahKriteria];
        double jumlahBarisMatriksVP []=new double[jumlahKriteria];
        //Utils a = new Utils();

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
            matriksHasilVP[i]=jumlahBarisMatriksVP[i]/3;
        }

        /*for (int j = 0; j < matriksNilaiKriteria.length; j++) {
            Log.i("Nilai Jumlah Kolom",  " " + a.formatDecimal(jumlahKolomMatriks[j]));
        }

        for (int i = 0; i < matriksNilaiKriteria.length; i++) {
            for (int j = 0; j < matriksNilaiKriteria.length; j++) {
                Log.i("Matriks VP", " " + a.formatDecimal(matriksVektorPrioritas[i][j]));
            }
        }*/
    }

    public void hitungNilaiAx (){

        double matriksAx[]=new double[matriksNilaiKriteria.length];
        double lamda[]=new double[matriksNilaiKriteria.length];
        double lamdaMaks = 0, totalLamda=0;
        Utils a = new Utils();

        for (int i = 0; i < matriksNilaiKriteria.length; i++) {
            double total = 0;
            for (int k = 0; k < matriksHasilVP.length; k++) {
                total = total + (matriksNilaiKriteria[i][k] * matriksHasilVP[k]);

            }
            matriksAx[i] = total;
            Log.i("Hasil Matriks Ax",  i+ " " + a.formatDecimal(matriksAx[i]));
        }


        for (int i = 0; i < matriksAx.length; i++) {
            lamda[i] = (matriksAx[i] / matriksHasilVP[i]);
            totalLamda = totalLamda + lamda[i];
        }
        lamdaMaks = totalLamda/matriksAx.length;
        Log.i("Lamda Maks",  " " + a.formatDecimal(lamdaMaks));
        hitungKonsistensi(lamdaMaks);

        tvNilaiLamdaMax.setText(String.valueOf(lamdaMaks));

    }

    public void hitungKonsistensi(double lamdaMaks){

        double ConsistencyIndex = (lamdaMaks - jumlahKriteria) / (jumlahKriteria-1);
        double RandomIndeks[] ={0.00, 0.00, 0.52, 0.89, 1.11, 1.25, 1.35, 1.40, 1.45, 1.49};
        double ConsistencyRatio = ConsistencyIndex / RandomIndeks[jumlahKriteria-1];
        Utils a = new Utils();

        String ketKonsistensi = null;
        if(ConsistencyRatio < 0.1000){
            ketKonsistensi = "Konsisten";
        }else {
            ketKonsistensi = "Tidak Konsisten";
        }

        Log.i("Nilai Consistency Ratio",  " " + a.formatDecimal(ConsistencyRatio));
        tvNilaiCI.setText(String.valueOf(ConsistencyIndex));
        tvNilaiCR.setText(String.valueOf(ConsistencyRatio));
        tvCek.setText(""+ketKonsistensi);

    }

    private ArrayList<Kriteria> getListItemData(){
        Boolean tempK1=k1,tempK2=k2,tempK3=k3,tempK4=k4,tempK5=k5,tempK6=k6,tempK7=k7;

        double nilaiKriteria [][] = new double[jumlahKriteria][jumlahKriteria];
        ArrayList<Kriteria> listViewItems = new ArrayList<Kriteria>();
        String sk[] = new String[jumlahKriteria];

        for (int i=0;i<nilaiKriteria.length;i++) {
            if (tempK1) {
                sk[i]="Daya Dukung Tanah";
                tempK1 = false; i++;
            }
            if (tempK2) {
                sk[i]="Ketersediaan Air";
                tempK2 = false; i++;
            }
            if (tempK3) {
                sk[i]="Kemiringan Lereng";
                tempK3 = false; i++;
            }
            if (tempK4) {
                sk[i]="Aksebilitas";
                tempK4 = false; i++;
            }
            if (tempK5) {
                sk[i]="Perubahan Lahan";
                tempK5 = false; i++;
            }
            if (tempK6) {
                sk[i]="Kerawanan Bencana";
                tempK6 = false; i++;
            }
            if (tempK7) {
                sk[i]="Jarak ke Bandara";
                tempK7 = false; i++;
            }
        }

        for (int i = 0; i < nilaiKriteria.length; i++) {
            for (int j = 0; j < nilaiKriteria.length; j++) {
                if (i < j) {
                    listViewItems.add(new Kriteria(""+sk[i],""+sk[j]));
                }

            }
        }

        return listViewItems;
    }

    private void showCustomDialog() {

        Utils a = new Utils();

        //before inflating the custom alert dialog bubble_info, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_box, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(InputNilaiKriteriaActivity.this);

        tvLihatTabel = dialogView.findViewById(R.id.tv_isi_tabel);
        btnCloseLihatTabel = dialogView.findViewById(R.id.btn_close_lihat_tabel);

        inisiasiPerbandingan();

        tvLihatTabel.setText("");

        for (int i = 0; i < matriksNilaiKriteria.length; i++) {
            for (int j = 0; j < matriksNilaiKriteria.length; j++) {
                if(j==matriksNilaiKriteria.length-1){
                    tvLihatTabel.append(a.formatDecimal(matriksNilaiKriteria[i][j]) + "\n");
                }else{
                    tvLihatTabel.append(a.formatDecimal(matriksNilaiKriteria[i][j]) + "\t\t");
                    //tvLihatTabel.setText("\n");
                }

            }
        }



        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);


        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btnCloseLihatTabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

}
