package com.labs.jangkriek.carilahan.mainViewFragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.labs.jangkriek.carilahan.Activity.SemuaLahanActivity;
import com.labs.jangkriek.carilahan.Database.DbLokasi;
import com.labs.jangkriek.carilahan.Database.DbSavePencarian;
import com.labs.jangkriek.carilahan.Activity.Users.HistoriActivity;
import com.labs.jangkriek.carilahan.MetodeActivity;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.POJO.PointLatLong;
import com.labs.jangkriek.carilahan.POJO.Respon;
import com.labs.jangkriek.carilahan.POJO.Users;
import com.labs.jangkriek.carilahan.PrefConfig;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Activity.Users.KelolaLahankuActivity;
import com.labs.jangkriek.carilahan.Activity.PilihKriteria;
import com.labs.jangkriek.carilahan.Utils.RegisterApi;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersHomeFragment extends Fragment {

    private DbSavePencarian dbSavePencarian;
    private DbLokasi dbLokasi;
    private CardView cvCari, cvHistory, cvKelolaLahanku, cvSemuaLahan;
    private TextView tvCountHistory, tvUsernameHome;
    private static PrefConfig prefConfig;

    //load data
    private static List<Lokasi> lokasiListFromServer = new ArrayList<>();
    private static List<PointLatLong> userLatLongListFromServer = new ArrayList<>();
    private static HashMap<String, List<Point>> dataPointHash = new HashMap<String, List<Point>>();
    ArrayList<String> listCreatedAt = new ArrayList<String>();
    List<List<Point>> POINTS = new ArrayList<>();
    private RelativeLayout rvLoading;
    private static final String URL = "https://ridwanharts.000webhostapp.com/";
    private static List<Users> userList = new ArrayList<Users>();


    public UsersHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_users, container, false);

        String username = PrefConfig.getUsernameLogin(getContext());

        cvCari = v.findViewById(R.id.cv_child_cari_lahan);
        cvHistory = v.findViewById(R.id.cv_child_history);
        cvKelolaLahanku = v.findViewById(R.id.cv_child_lahanku);
        cvSemuaLahan = v.findViewById(R.id.cv_child_view_all_lahan);

        tvCountHistory = v.findViewById(R.id.tv_count_history);
        tvUsernameHome = v.findViewById(R.id.username_home);
        tvUsernameHome.setText(username);

        rvLoading = v.findViewById(R.id.rv_loading);
        rvLoading.setVisibility(View.INVISIBLE);

        dbLokasi = new DbLokasi(getActivity());
        dbSavePencarian = new DbSavePencarian(getActivity());

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
                loadDataLokasiFromServer();
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

    public static List<Lokasi> getListLokasitoProcess() {
        return lokasiListFromServer;
    }

    public static List<PointLatLong> getListPointtoProcess() {
        return userLatLongListFromServer;
    }

    public static List<Users> userListtoProcess(){
        return userList;
    }

    private void loadDataLokasiFromServer() {
        rvLoading.setVisibility(View.VISIBLE);
        lokasiListFromServer.clear();
        userLatLongListFromServer.clear();
        listCreatedAt.clear();
        POINTS.clear();
        dataPointHash.clear();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("", "message : " + message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        RegisterApi api = retrofit.create(RegisterApi.class);
        Call<Respon> call = api.view_all_lokasi();
        call.enqueue(new Callback<Respon>() {
            @Override
            public void onResponse(Call<Respon> call, Response<Respon> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                if (value.equals("1")) {
                    Call<Respon> callUser = api.view_user();
                    callUser.enqueue(new Callback<Respon>() {
                        @Override
                        public void onResponse(Call<Respon> call, Response<Respon> responseUserList) {
                            String value = responseUserList.body().getValue();
                            if (value.equals("1")){
                                userList = responseUserList.body().getUsersList();
                                Log.e("status", "berhasil"+userList.size());
                                //load data lokasi after sukses load user list
                                lokasiListFromServer = response.body().getLokasiList();
                                userLatLongListFromServer = response.body().getPoint();
                                if (lokasiListFromServer.size() != 0) {
                                    if (userLatLongListFromServer.size() != 0) {
                                        rvLoading.setVisibility(View.INVISIBLE);
                                    }
                                    Toast.makeText(getActivity(), "Data lahan berhasil didownload " + lokasiListFromServer.size(), Toast.LENGTH_SHORT).show();
                                    rvLoading.setVisibility(View.INVISIBLE);
                                    Intent i = new Intent(getActivity(), PilihKriteria.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getActivity(), "Anda belum menambahkan data lahan " + message, Toast.LENGTH_SHORT).show();
                                    Log.e("message ", message);
                                    rvLoading.setVisibility(View.INVISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Respon> call, Throwable t) {
                            Log.e("error", "gagal");
                        }
                    });

                } else {
                    //Toast.makeText(getApplicationContext(),"Anda belum memiliki lahan"+message, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Respon> call, Throwable t) {
                Toast.makeText(getActivity(), "Koneksi internet bermasalah" + t, Toast.LENGTH_SHORT).show();
                rvLoading.setVisibility(View.INVISIBLE);

            }
        });
    }



}
