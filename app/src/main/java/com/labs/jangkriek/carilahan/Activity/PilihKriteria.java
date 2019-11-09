package com.labs.jangkriek.carilahan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.labs.jangkriek.carilahan.R;

import static com.labs.jangkriek.carilahan.Activity.MainActivity.getLoginType;

public class PilihKriteria extends AppCompatActivity {

    private CheckBox cbDayaDukungTanah, cbKetersediaanAir, cbKemiringanLereng, cbAksebilitas;
    private CheckBox cbPerubahanLahan, cbKerawananBencana, cbJarakKeBandara;
    private Boolean k1=false,k2=false,k3=false,k4=false,k5=false,k6=false,k7=false;

    private Button btnOkInput, btnInputNilai, btnCancel;

    private TextView tvJumlahInput;

    static int jumlahInput = 0;

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

        cbDayaDukungTanah = findViewById(R.id.k1_daya_dukung_tanah);
        cbKetersediaanAir = findViewById(R.id.k2_ketersediaan_air);
        cbKemiringanLereng = findViewById(R.id.k3_kemiringan_lereng);
        cbAksebilitas = findViewById(R.id.k4_aksebilitas);
        cbPerubahanLahan = findViewById(R.id.k5_perubahan_lahan);
        cbKerawananBencana = findViewById(R.id.k6_kerawanan_bencana);
        cbJarakKeBandara = findViewById(R.id.k7_jarakke_bandara);

        btnOkInput = findViewById(R.id.btn_ok_input);
        btnInputNilai = findViewById(R.id.btn_input_activity);
        btnCancel = findViewById(R.id.btn_cancel_input);

        tvJumlahInput = findViewById(R.id.tv_jumlahInput);

        btnOkInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumlahInput = 0;
                cbDayaDukungTanah.setEnabled(false);
                cbKetersediaanAir.setEnabled(false);
                cbKemiringanLereng.setEnabled(false);
                cbAksebilitas.setEnabled(false);
                cbPerubahanLahan.setEnabled(false);
                cbKerawananBencana.setEnabled(false);
                cbJarakKeBandara.setEnabled(false);

                if (cbDayaDukungTanah.isChecked()){
                    jumlahInput++;k1=true;
                    Toast.makeText(getApplicationContext(),""+k1, Toast.LENGTH_SHORT).show();
                }
                if (cbKetersediaanAir.isChecked()){
                    jumlahInput++;k2=true;
                    Toast.makeText(getApplicationContext(),""+k2, Toast.LENGTH_SHORT).show();
                }
                if (cbKemiringanLereng.isChecked()){
                    jumlahInput++;k3=true;}
                if (cbAksebilitas.isChecked()){
                    jumlahInput++;k4=true;}
                if (cbPerubahanLahan.isChecked()){
                    jumlahInput++;k5=true;}
                if (cbKerawananBencana.isChecked()){
                    jumlahInput++;k6=true;}
                if (cbJarakKeBandara.isChecked()){
                    jumlahInput++;k7=true;}

                tvJumlahInput.setText(String.valueOf(jumlahInput));

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbDayaDukungTanah.setEnabled(true);
                cbKetersediaanAir.setEnabled(true);
                cbKemiringanLereng.setEnabled(true);
                cbAksebilitas.setEnabled(true);
                cbPerubahanLahan.setEnabled(true);
                cbKerawananBencana.setEnabled(true);
                cbJarakKeBandara.setEnabled(true);
                jumlahInput = 0;
                k1=false;k2=false;k3=false;k4=false;
                k5=false;k6=false;k7=false;
            }
        });

        btnInputNilai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PilihKriteria.this, InputNilaiKriteriaActivity.class);
                i.putExtra("jumlah_input", jumlahInput);
                i.putExtra("k1", k1);
                i.putExtra("k2", k2);
                i.putExtra("k3", k3);
                i.putExtra("k4", k4);
                i.putExtra("k5", k5);
                i.putExtra("k6", k6);
                i.putExtra("k7", k7);
                startActivityForResult(i, 100);
            }
        });

    }

    private String formatDecimal(double bilangan){
        return String.format("%.4f", bilangan);
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