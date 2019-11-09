package com.labs.jangkriek.carilahan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.labs.jangkriek.carilahan.Activity.DetailLokasiActivity;
import com.labs.jangkriek.carilahan.Activity.LokasiActivity;
import com.labs.jangkriek.carilahan.Database.DbLokasi;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.POJO.Respon;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.RegisterApi;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LokasiAdapter extends RecyclerView.Adapter<LokasiAdapter.MyViewHolder> {

    private Context context;
    private List<Lokasi> lokasiList;
    private MapboxMap map;
    private static final String URL = "https://ridwanharts.000webhostapp.com/";
    private DbLokasi dbLokasi;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView namaLokasi;
        public TextView latitude;
        public TextView longitude;
        ImageView cekUpload;
        ImageButton ibUpload;
        CardView singleCard;
        LinearLayout linearLayoutDetailLokasi;
        LokasiActivity.ItemClickListener itemClickListener;

        public MyViewHolder(View view) {
            super(view);
            namaLokasi = view.findViewById(R.id.tv_nama_lokasi);
            latitude = view.findViewById(R.id.tv_latitude);
            longitude = view.findViewById(R.id.tv_longitude);
            cekUpload = view.findViewById(R.id.iv_cek_upload);
            ibUpload = view.findViewById(R.id.btn_refresh_upload);
            singleCard = view.findViewById(R.id.cardview_lokasi);
            linearLayoutDetailLokasi = view.findViewById(R.id.admin_detail_lokasi);
            singleCard.setOnClickListener(this);
        }

        public void setClickListener(LokasiActivity.ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getLayoutPosition());
        }
    }


    public LokasiAdapter(Context context, List<Lokasi> lokasiList, MapboxMap mapBoxMap) {
        this.context = context;
        this.lokasiList = lokasiList;
        this.map = mapBoxMap;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_lokasi, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LokasiAdapter.MyViewHolder holder, int position) {
        dbLokasi = new DbLokasi(context);
        Lokasi namaLokasi = lokasiList.get(position);

        holder.cekUpload.setImageResource(R.drawable.icons8_checked_grey);

        if (namaLokasi.getStatus()==1){
            holder.cekUpload.setImageResource(R.drawable.icons8_checked);
        }else {
            holder.cekUpload.setImageResource(R.drawable.icons8_checked_grey);
        }
        holder.namaLokasi.setText(namaLokasi.getNama());
        holder.latitude.setText(String.valueOf(namaLokasi.getLatitude()));
        holder.longitude.setText(String.valueOf(namaLokasi.getLongitude()));
        //Log.e("b", lokasiList.get(position).getLokasi()+"");
        holder.setClickListener(new LokasiActivity.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LatLng selectedLocationLatLng = lokasiList.get(position).getLokasi();
                Toast.makeText(context,"Lokasi : "+lokasiList.get(position).getNama(), Toast.LENGTH_SHORT).show();

                CameraPosition newCameraPosition = new CameraPosition.Builder()
                        .target(selectedLocationLatLng)
                        .build();
                map.easeCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));
            }
        });

        holder.ibUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertUser(namaLokasi.getNama(), namaLokasi.getLatitude(), namaLokasi.getLongitude(),
                        namaLokasi.getDayaDukungTanah(),namaLokasi.getKetersediaanAir(),namaLokasi.getKemiringanLereng(),
                        namaLokasi.getAksebilitas(),namaLokasi.getPerubahanLahan(),namaLokasi.getKerawananBencana(),namaLokasi.getJarakKeBandara(),
                        1);
                holder.cekUpload.setImageResource(R.drawable.icons8_checked);
                namaLokasi.setStatus(1);
                dbLokasi.updateLokasiStatus(namaLokasi);
                lokasiList.set(position, namaLokasi);

            }
        });

        holder.linearLayoutDetailLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailLokasiActivity.class);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lokasiList.size();
    }

    private void insertUser(String namaLokasi, double lat, double longi,
                            double ddTanah, double kAir, double kLereng, double aksebilitas,
                            double pLahan, double kBencana, double jBandara, int a){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("", "message : "+message);
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
        Call<Respon> call = api.insert_lokasi(
                namaLokasi,lat,longi,
                ddTanah,kAir,kLereng,
                aksebilitas,pLahan,kBencana,jBandara,a
        );
        call.enqueue(new Callback<Respon>() {
            @Override
            public void onResponse(Call<Respon> call, retrofit2.Response<Respon> response) {

                String value = response.body().getValue();
                String message = response.body().getMessage();
                if(value.equals("1")){
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Respon> call, Throwable t) {
                Toast.makeText(context, "Jaringan Error", Toast.LENGTH_SHORT).show();
                Log.d("cek lagi", ""+call);
            }
        });
    }

}
