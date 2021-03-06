package com.labs.jangkriek.carilahan.Activity.LogInSignUp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.labs.jangkriek.carilahan.Activity.MainActivity;
import com.labs.jangkriek.carilahan.PrefConfig;
import com.labs.jangkriek.carilahan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginGuestFragment extends Fragment {

    private TextView tvLogin;
    private ProgressBar pb;
    private View relativeLayout;

    public LoginGuestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the bubble_info for this fragment
        View v = inflater.inflate(R.layout.fragment_login_guest, container, false);

        tvLogin = v.findViewById(R.id.tv_login_user);
        relativeLayout = v.findViewById(R.id.relativeLayout);
        pb = v.findViewById(R.id.progressBar_login);
        relativeLayout = v.findViewById(R.id.relativeLayout);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.VISIBLE);
                pb.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PrefConfig.setTypeLogin(getContext(), "GUEST");
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                }, 800L);
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
