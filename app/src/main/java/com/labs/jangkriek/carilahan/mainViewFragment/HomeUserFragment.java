package com.labs.jangkriek.carilahan.mainViewFragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.labs.jangkriek.carilahan.Activity.HistoriActivity;
import com.labs.jangkriek.carilahan.Activity.PilihKriteria;
import com.labs.jangkriek.carilahan.Database.DbLokasi;
import com.labs.jangkriek.carilahan.Database.DbSavePencarian;
import com.labs.jangkriek.carilahan.Database.DbUserLokasi;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Activity.UserLokasiActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeUserFragment extends Fragment {

    private DbSavePencarian dbSavePencarian;
    private DbLokasi dbLokasi;
    private DbUserLokasi dbUserLokasi;
    private CardView cvCari, cvHitung, cvLocation;
    private TextView tvCountLokasi, tvCountHistory;

    public HomeUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_guest, container, false);

        cvCari = v.findViewById(R.id.cv_user_child_top1);
        cvHitung = v.findViewById(R.id.cv_user_child_top2);
        cvLocation = v.findViewById(R.id.cv_user_child_location);

        tvCountLokasi = v.findViewById(R.id.tv_user_count_lokasi);
        tvCountHistory = v.findViewById(R.id.tv_user_count_history);

        dbLokasi = new DbLokasi(getActivity());
        dbUserLokasi = new DbUserLokasi(getActivity());
        dbSavePencarian = new DbSavePencarian(getActivity());

        tvCountLokasi.setText(""+dbUserLokasi.getLokasiCount());
        tvCountHistory.setText(""+dbSavePencarian.getRankLokasiCount());


        cvCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), HistoriActivity.class);
                startActivity(i);
            }
        });

        cvHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PilihKriteria.class);
                startActivity(i);
            }
        });

        cvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UserLokasiActivity.class);
                startActivity(i);
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

}
