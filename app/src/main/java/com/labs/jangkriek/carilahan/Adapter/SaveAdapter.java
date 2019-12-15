package com.labs.jangkriek.carilahan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.labs.jangkriek.carilahan.Activity.Users.KelolaLahankuActivity;
import com.labs.jangkriek.carilahan.POJO.SavePencarian;
import com.labs.jangkriek.carilahan.Activity.Users.DetailHistoryActivity;
import com.labs.jangkriek.carilahan.R;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import java.util.List;

public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.MyViewHolder> {

    private Context context;
    private List<SavePencarian> savePencarians;
    private MapboxMap map;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView namaSaved;
        public TextView waktu;
        public TextView id;
        public TextView latitude;
        public TextView longitude;
        public Button btnSeeDetail;
        CardView singleCard;
        KelolaLahankuActivity.ItemClickListener itemClickListener;

        public MyViewHolder(View view) {
            super(view);
            namaSaved = view.findViewById(R.id.tv_nama_save_lokasi);
            id = view.findViewById(R.id.tv_id_save);
            waktu = view.findViewById(R.id.tv_save_date);
            latitude = view.findViewById(R.id.tv_save_latitude);
            longitude = view.findViewById(R.id.tv_save_longitude);
            btnSeeDetail = view.findViewById(R.id.btn_see_detail);
            singleCard = view.findViewById(R.id.cardview_save_lokasi);
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


    public SaveAdapter(Context context, List<SavePencarian> savePencarians) {
        this.context = context;
        this.savePencarians = savePencarians;
    }

    @Override
    public SaveAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_save_pencarian, parent, false);
        return new SaveAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SaveAdapter.MyViewHolder holder, int position) {
        SavePencarian savePencarian = savePencarians.get(position);
        int pos = position+1;

        holder.namaSaved.setText(savePencarian.getNama());
        holder.waktu.setText(savePencarian.getWaktu());
        holder.id.setText("id "+savePencarian.getIdGroup());
        holder.latitude.setText(String.valueOf(savePencarian.getLatitude()));
        holder.longitude.setText(String.valueOf(savePencarian.getLongitude()));
        holder.setClickListener(new KelolaLahankuActivity.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {


            }
        });
        holder.btnSeeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailHistoryActivity.class);
                i.putExtra("idgroup", savePencarian.getIdGroup());
                i.putExtra("waktu", savePencarian.getWaktu());
                i.putExtra("nama", savePencarian.getNama());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return savePencarians.size();
    }
}
