package com.labs.jangkriek.carilahan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.labs.jangkriek.carilahan.Activity.DetailLokasiActivity;
import com.labs.jangkriek.carilahan.Activity.KelolaLahankuActivity;
import com.labs.jangkriek.carilahan.Database.DbLokasi;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.R;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

public class KelolaLahankuAdapter extends RecyclerView.Adapter<KelolaLahankuAdapter.MyViewHolder> {

    private Context context;
    private List<Lokasi> lokasiList;
    private MapboxMap map;
    private static final String URL = "https://ridwanharts.000webhostapp.com/";
    private DbLokasi dbLokasi;
    private boolean kondisi=true;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView namaLokasi;
        public TextView latitude;
        public TextView longitude;
        ImageView cekUpload;
        ImageButton ibUpload;
        ImageView ivLahan;
        RelativeLayout relativeLayout;
        CardView singleCard;
        LinearLayout linearLayoutDetailLokasi;
        KelolaLahankuActivity.ItemClickListener itemClickListener;

        public MyViewHolder(View view) {
            super(view);
            namaLokasi = view.findViewById(R.id.tv_nama_lokasi);
            latitude = view.findViewById(R.id.tv_latitude);
            longitude = view.findViewById(R.id.tv_longitude);
            cekUpload = view.findViewById(R.id.iv_cek_upload);
            ibUpload = view.findViewById(R.id.btn_refresh_upload);
            ivLahan = view.findViewById(R.id.image_lahan);
            relativeLayout = view.findViewById(R.id.rv_loading);
            singleCard = view.findViewById(R.id.cardview_lokasi);
            linearLayoutDetailLokasi = view.findViewById(R.id.admin_detail_lokasi);
            singleCard.setOnClickListener(this);
        }

        public void setClickListener(KelolaLahankuActivity.ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getLayoutPosition());
        }
    }


    public KelolaLahankuAdapter(Context context, List<Lokasi> lokasiList, MapboxMap mapBoxMap) {
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
    public void onBindViewHolder(KelolaLahankuAdapter.MyViewHolder holder, int position) {
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
        holder.ivLahan.setImageBitmap(namaLokasi.getBitmap());
        //Log.e("b", lokasiList.get(position).getLokasi()+"");
        holder.setClickListener(new KelolaLahankuActivity.ItemClickListener() {
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

                holder.relativeLayout.setVisibility(View.VISIBLE);
                /*insertUser(namaLokasi.getNama(), namaLokasi.getLatitude(), namaLokasi.getLongitude(),
                        namaLokasi.getDayaDukungTanah(),namaLokasi.getKetersediaanAir(),namaLokasi.getKemiringanLereng(),
                        namaLokasi.getAksebilitas(),namaLokasi.getPerubahanLahan(),namaLokasi.getKerawananBencana(),namaLokasi.getJarakKeBandara(),
                        1, namaLokasi.getBitmap());*/
                if (kondisi){
                    holder.cekUpload.setImageResource(R.drawable.icons8_checked);
                    namaLokasi.setStatus(1);
                }else {
                    holder.cekUpload.setImageResource(R.drawable.icons8_checked_grey);
                    namaLokasi.setStatus(0);
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // Hide your View after 3 seconds
                        holder.relativeLayout.setVisibility(View.INVISIBLE);
                    }
                }, 3000);

                dbLokasi.updateLokasiStatus(namaLokasi);
                lokasiList.set(position, namaLokasi);

            }
        });

        /*holder.linearLayoutDetailLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double lat = namaLokasi.getLatitude();
                double longi = namaLokasi.getLongitude();
                double harga = namaLokasi.getPerubahanLahan();

                Bitmap bitmap = namaLokasi.getBitmap();
                String nama = namaLokasi.getNama();

                Intent i = new Intent(context, DetailLokasiActivity.class);
                i.putExtra("nama", nama);
                i.putExtra("latitude", lat);
                i.putExtra("longitude", longi);
                i.putExtra("harga", harga);
                //i.putExtra("nama", nama);
                context.startActivity(i);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return lokasiList.size();
    }

    /*private void insertUser(String namaLokasi, double lat, double longi,
                            double ddTanah, double kAir, double kLereng, double aksebilitas,
                            double pLahan, double kBencana, double jBandara, int a, Bitmap bitmap){

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

        String encodedBitmap = ImageUtils.bitmapToBase64String(bitmap, 100);

        RegisterApi api = retrofit.create(RegisterApi.class);
        Call<Respon> call = api.insert_lokasi(
                namaLokasi,hargaLahan, lat,longi,
                ddTanah,kAir,kLereng,
                aksebilitas,kBencana,jBandara, a, encodedBitmap
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
                kondisi = false;
                Log.d("cek lagi", ""+call);
            }
        });
    }*/

    private boolean mKondisi(boolean k){

        return k;
    }

}