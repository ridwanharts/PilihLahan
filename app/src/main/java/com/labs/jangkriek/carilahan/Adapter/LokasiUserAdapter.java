package com.labs.jangkriek.carilahan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.labs.jangkriek.carilahan.Activity.DetailLokasiActivity;
import com.labs.jangkriek.carilahan.Activity.Users.KelolaLahankuActivity;
import com.labs.jangkriek.carilahan.Database.DbUserLokasi;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.R;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

public class LokasiUserAdapter extends RecyclerView.Adapter<LokasiUserAdapter.MyViewHolder> {

    private Context context;
    private List<Lokasi> lokasiList;
    private MapboxMap map;
    private DbUserLokasi dbUserLokasi;
    private boolean history = false;

    public LokasiUserAdapter(Context context, List<Lokasi> lokasiList, MapboxMap mapboxMap) {
        this.context = context;
        this.lokasiList = lokasiList;
        this.map = mapboxMap;
    }

    public LokasiUserAdapter(Context context, List<Lokasi> lokasiList) {
        this.context = context;
        this.lokasiList = lokasiList;
        history = true;
    }

    @Override
    public LokasiUserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_list_lokasi, parent, false);


        return new LokasiUserAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LokasiUserAdapter.MyViewHolder holder, int position) {
        dbUserLokasi = new DbUserLokasi(context);
        Lokasi namaLokasi = lokasiList.get(position);

        if (namaLokasi.getStatus()==1){
            holder.cekUpload.setImageResource(R.drawable.icons8_checked);
        }
        holder.namaLokasi.setText(namaLokasi.getNama());
        holder.latitude.setText(String.valueOf(namaLokasi.getLatitude()));
        holder.longitude.setText(String.valueOf(namaLokasi.getLongitude()));
        holder.ivGambar.setImageBitmap(namaLokasi.getBitmap());
        //Log.e("b", lokasiList.get(position).getLokasi()+"");

        if (!history){
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

            holder.linearLayoutDetailLokasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailLokasiActivity.class);


                    context.startActivity(i);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return lokasiList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView namaLokasi;
        public TextView latitude;
        public TextView longitude;
        ImageView cekUpload;
        ImageView ivGambar;
        CardView singleCard;
        LinearLayout linearLayoutDetailLokasi;
        KelolaLahankuActivity.ItemClickListener itemClickListener;

        public MyViewHolder(View view) {
            super(view);
            namaLokasi = view.findViewById(R.id.tv_nama_lokasi);
            latitude = view.findViewById(R.id.tv_latitude);
            longitude = view.findViewById(R.id.tv_longitude);
            cekUpload = view.findViewById(R.id.iv_cek_upload);
            ivGambar = view.findViewById(R.id.iv_gambar);
            singleCard = view.findViewById(R.id.cardview_lokasi);
            linearLayoutDetailLokasi = view.findViewById(R.id.user_detail_lokasi);
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
}
