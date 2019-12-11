package com.labs.jangkriek.carilahan.Activity.LogInSignUp.LoginFragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.labs.jangkriek.carilahan.Activity.ApiInterface;
import com.labs.jangkriek.carilahan.Activity.LogInSignUp.SignUpActivity;
import com.labs.jangkriek.carilahan.Activity.MainActivity;
import com.labs.jangkriek.carilahan.ApiClient;
import com.labs.jangkriek.carilahan.POJO.Admin;
import com.labs.jangkriek.carilahan.PrefConfig;
import com.labs.jangkriek.carilahan.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginAdminFragment extends Fragment {

    private TextView tvSignUp, tvLogin;
    private EditText etUsernameLogin, etPasswordLogin;
    public static ApiInterface apiInterface;
    public static PrefConfig prefConfig;
    private Context context;

    public LoginAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_users, container, false);

        prefConfig = new PrefConfig(getContext());
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        tvSignUp = v.findViewById(R.id.tv_signup_admin);
        tvLogin = v.findViewById(R.id.tv_login_admin);
        etUsernameLogin = v.findViewById(R.id.username_login);
        etPasswordLogin = v.findViewById(R.id.password_login);

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SignUpActivity.class);
                startActivity(i);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (etUsernameLogin.getText() == null || etPasswordLogin.getText() == null){
                    if (etUsernameLogin == null){
                        Toast.makeText(getApplicationContext(), "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    }
                    if (etPasswordLogin == null){
                        Toast.makeText(getApplicationContext(), "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    }

                }else {

                    Call<Admin> c = apiInterface.login(
                            etUsernameLogin.getText().toString(),
                            etPasswordLogin.getText().toString()
                    );
                    c.enqueue(new Callback<Admin>() {
                        @Override
                        public void onResponse(Call<Admin> call, Response<Admin> response) {
                            String value = response.body().getValue();
                            String message = response.body().getMessage();
                            if(value.equals("0")){
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                prefConfig.writeName(message);
                                Intent i = new Intent(getActivity(), MainActivity.class);
                                i.putExtra("LOGIN","ADMIN");
                                startActivity(i);
                                getActivity().finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Admin> call, Throwable t) {
                            Toast.makeText(getActivity(), "Jaringan Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }*/

                Intent i = new Intent(getActivity(), MainActivity.class);
                i.putExtra("LOGIN","ADMIN");
                startActivity(i);
                getActivity().finish();
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

}
