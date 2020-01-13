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
import com.labs.jangkriek.carilahan.Database.DbLokasi;
import com.labs.jangkriek.carilahan.Database.DbSavePencarian;
import com.labs.jangkriek.carilahan.Activity.Users.HistoriActivity;
import com.labs.jangkriek.carilahan.MetodeActivity;
import com.labs.jangkriek.carilahan.PrefConfig;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Activity.Users.KelolaLahankuActivity;
import com.labs.jangkriek.carilahan.Activity.PilihKriteria;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersHomeFragment extends Fragment {

    private DbSavePencarian dbSavePencarian;
    private DbLokasi dbLokasi;
    private CardView cvCari, cvHistory, cvKelolaLahanku, cvSemuaLahan;
    private TextView tvCountLokasi, tvCountHistory, tvUsernameHome;
    private static PrefConfig prefConfig;


    public UsersHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_users, container, false);

        prefConfig = new PrefConfig(getContext());

        cvCari = v.findViewById(R.id.cv_child_cari_lahan);
        cvHistory = v.findViewById(R.id.cv_child_history);
        cvKelolaLahanku = v.findViewById(R.id.cv_child_lahanku);
        cvSemuaLahan = v.findViewById(R.id.cv_child_view_all_lahan);

        tvCountLokasi = v.findViewById(R.id.tv_count_lokasi);
        tvCountHistory = v.findViewById(R.id.tv_count_history);
        tvUsernameHome = v.findViewById(R.id.username_home);
        tvUsernameHome.setText(prefConfig.readName());

        dbLokasi = new DbLokasi(getActivity());
        dbSavePencarian = new DbSavePencarian(getActivity());

        tvCountLokasi.setText(""+dbLokasi.getLokasiCount());
        tvCountHistory.setText(""+dbSavePencarian.getRankLokasiCount());

        dbLokasi.getLokasiCount();

        ButtonClick();

        return v;
    }

    private void ButtonClick(){
        cvSemuaLahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SemuaLahanActivity.class);
                startActivity(i);
            }
        });

        cvCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MetodeActivity.class);
                startActivity(i);
            }
        });

        cvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), HistoriActivity.class);
                startActivity(i);
            }
        });

        cvKelolaLahanku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), KelolaLahankuActivity.class);
                startActivity(i);
            }
        });
    }

}
