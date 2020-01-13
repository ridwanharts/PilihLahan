package com.labs.jangkriek.carilahan.mainViewFragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.labs.jangkriek.carilahan.Activity.SemuaLahanActivity;
import com.labs.jangkriek.carilahan.Activity.Users.HistoriActivity;
import com.labs.jangkriek.carilahan.Activity.PilihKriteria;
import com.labs.jangkriek.carilahan.Database.DbLokasi;
import com.labs.jangkriek.carilahan.Database.DbSavePencarian;
import com.labs.jangkriek.carilahan.Database.DbUserLokasi;
import com.labs.jangkriek.carilahan.MetodeActivity;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Activity.UserLokasiActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuestHomeFragment extends Fragment {

    private DbSavePencarian dbSavePencarian;
    private DbLokasi dbLokasi;
    private DbUserLokasi dbUserLokasi;
    private CardView cvHitung, cvLocation;
    private TextView tvCountLokasi;

    public GuestHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_guest, container, false);

        cvHitung = v.findViewById(R.id.cv_user_child_top2);
        cvLocation = v.findViewById(R.id.cv_user_child_location);

        tvCountLokasi = v.findViewById(R.id.tv_user_count_lokasi);


        dbLokasi = new DbLokasi(getActivity());
        dbUserLokasi = new DbUserLokasi(getActivity());
        dbSavePencarian = new DbSavePencarian(getActivity());

        tvCountLokasi.setText(""+dbUserLokasi.getLokasiCount());

        cvHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MetodeActivity.class);
                startActivity(i);
            }
        });

        cvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SemuaLahanActivity.class);
                startActivity(i);
            }
        });

        // Inflate the bubble_info for this fragment
        return v;
    }

}
