package com.labs.jangkriek.carilahan.Activity.LogInSignUp.LoginFragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.labs.jangkriek.carilahan.Activity.LogInSignUp.LoginActivity;
import com.labs.jangkriek.carilahan.Activity.LogInSignUp.SignUpActivity;
import com.labs.jangkriek.carilahan.Activity.MainActivity;
import com.labs.jangkriek.carilahan.R;

import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginUserFragment extends Fragment {

    private TextView tvLogin, tvLoginAdminFr;
    private ProgressBar pb;
    private View relativeLayout;

    public LoginUserFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_user, container, false);

        tvLogin = v.findViewById(R.id.tv_login_user);
        tvLoginAdminFr = v.findViewById(R.id.tv_login_admin_fragment);
        relativeLayout = v.findViewById(R.id.relativeLayout);
        pb = v.findViewById(R.id.progressBar_login);
        relativeLayout = v.findViewById(R.id.relativeLayout);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        relativeLayout.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.VISIBLE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        i.putExtra("LOGIN","USER");
                        startActivity(i);
                        getActivity().finish();
                    }
                }, 800L);
            }
        });

        tvLoginAdminFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
