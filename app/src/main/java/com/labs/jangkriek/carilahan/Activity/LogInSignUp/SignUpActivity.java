package com.labs.jangkriek.carilahan.Activity.LogInSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.labs.jangkriek.carilahan.Activity.ApiInterface;
import com.labs.jangkriek.carilahan.Activity.MainActivity;
import com.labs.jangkriek.carilahan.ApiClient;
import com.labs.jangkriek.carilahan.POJO.Admin;
import com.labs.jangkriek.carilahan.PrefConfig;
import com.labs.jangkriek.carilahan.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    TextView tvRegister, tvBackLogin;
    EditText etUsername, etEmail, etPassword;

    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        prefConfig = new PrefConfig(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        tvRegister = findViewById(R.id.tv_register);
        tvBackLogin = findViewById(R.id.back_login_activity);
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        tvBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsername != null){
                    Call<Admin> call = apiInterface.register(
                            etUsername.getText().toString(),
                            etEmail.getText().toString(),
                            etPassword.getText().toString());
                    call.enqueue(new Callback<Admin>() {
                        @Override
                        public void onResponse(Call<Admin> call, Response<Admin> response) {
                            Toast.makeText(getApplicationContext(), "Sukses mendaftar", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Call<Admin> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Jaringan Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(), "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(this, LoginActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        finish();
    }
}
