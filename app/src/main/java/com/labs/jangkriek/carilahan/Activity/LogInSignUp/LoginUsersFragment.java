package com.labs.jangkriek.carilahan.Activity.LogInSignUp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.labs.jangkriek.carilahan.POJO.ResponUsers;
import com.labs.jangkriek.carilahan.Utils.ApiInterface;
import com.labs.jangkriek.carilahan.Activity.MainActivity;
import com.labs.jangkriek.carilahan.Utils.ApiClient;
import com.labs.jangkriek.carilahan.PrefConfig;
import com.labs.jangkriek.carilahan.R;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginUsersFragment extends Fragment {

    private TextView tvSignUp, tvLogin;
    private EditText etUsernameLogin, etPasswordLogin;
    public static ApiInterface apiInterface;
    private Context context;
    private RelativeLayout rlLoading;

    public LoginUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the bubble_info for this fragment
        View v = inflater.inflate(R.layout.fragment_login_users, container, false);

        apiInterface = ApiClient.getApiClientWithLog().create(ApiInterface.class);

        tvSignUp = v.findViewById(R.id.tv_signup_admin);
        tvLogin = v.findViewById(R.id.tv_login_admin);
        etUsernameLogin = v.findViewById(R.id.username_login);
        etPasswordLogin = v.findViewById(R.id.password_login);
        rlLoading = v.findViewById(R.id.rv_loading);

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
                rlLoading.setVisibility(View.VISIBLE);
                if (etUsernameLogin.getText() == null || etPasswordLogin.getText() == null){
                    if (etUsernameLogin == null){
                        Toast.makeText(getApplicationContext(), "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    }
                    if (etPasswordLogin == null){
                        Toast.makeText(getApplicationContext(), "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    }

                }else {

                    Call<ResponUsers> c = apiInterface.login(
                            etUsernameLogin.getText().toString(),
                            etPasswordLogin.getText().toString()
                    );
                    c.enqueue(new Callback<ResponUsers>() {
                        @Override
                        public void onResponse(Call<ResponUsers> call, Response<ResponUsers> response) {
                            String value = response.body().getValue();
                            String message = response.body().getMessage();
                            int idUser = response.body().getId_user();
                            if(value.equals("0")){
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                rlLoading.setVisibility(View.INVISIBLE);
                            }else {
                                PrefConfig.setLoggedInUser(getActivity(), idUser);
                                PrefConfig.setLoggedInStatus(getActivity(),true);
                                PrefConfig.setUsernameLogin(getActivity(), message);
                                PrefConfig.setTypeLogin(getActivity(), "USER");
                                Toast.makeText(getActivity(), idUser+"", Toast.LENGTH_SHORT).show();
                                rlLoading.setVisibility(View.INVISIBLE);
                                Intent i = new Intent(getActivity(), MainActivity.class);
                                startActivity(i);
                                getActivity().finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponUsers> call, Throwable t) {
                            Toast.makeText(getActivity(), "Jaringan Error", Toast.LENGTH_SHORT).show();
                            rlLoading.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

}
