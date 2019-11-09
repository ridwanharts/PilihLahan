package com.labs.jangkriek.carilahan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.labs.jangkriek.carilahan.Activity.DetailHistoryActivity;
import com.labs.jangkriek.carilahan.POJO.RankingLokasi;
import com.labs.jangkriek.carilahan.R;

import java.util.List;

public class DetailHistoryAdapter extends RecyclerView.Adapter<DetailHistoryAdapter.MyViewHolder> {

    private Context context;
    private List<RankingLokasi> rankingLokasiList;

    public DetailHistoryAdapter(Context context, List<RankingLokasi> rankingLokasiList){
        this.context = context;
        this.rankingLokasiList = rankingLokasiList;
    }

    @NonNull
    @Override
    public DetailHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_rank_lokasi, parent, false);
        return new DetailHistoryAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailHistoryAdapter.MyViewHolder holder, int position) {

        RankingLokasi rankingLokasi = rankingLokasiList.get(position);
        int pos = position+1;

        holder.rank.setText("Rank "+pos);
        holder.namaRankLokasi.setText(rankingLokasi.getNama());
        holder.id.setText("id "+rankingLokasi.getId());
        holder.latitude.setText(String.valueOf(rankingLokasi.getLatitude()));
        holder.longitude.setText(String.valueOf(rankingLokasi.getLongitude()));
        holder.jumlah.setText(String.valueOf(rankingLokasi.getJumlah()));

        holder.setItemClickListener(new DetailHistoryActivity.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return rankingLokasiList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView namaRankLokasi;
        public TextView rank;
        public TextView id;
        public TextView latitude;
        public TextView longitude;
        public TextView jumlah;
        CardView singleCard;
        DetailHistoryActivity.ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View view) {
            super(view);
            namaRankLokasi = view.findViewById(R.id.tv_nama_lokasi);
            id = view.findViewById(R.id.tv_id_rank);
            rank = view.findViewById(R.id.tv_no_rank);
            latitude = view.findViewById(R.id.tv_rank_latitude);
            longitude = view.findViewById(R.id.tv_rank_longitude);
            jumlah = view.findViewById(R.id.tv_rank_jumlah);
            singleCard = view.findViewById(R.id.cardview_rank_lokasi);
            singleCard.setOnClickListener(this);
        }

        public void setItemClickListener(DetailHistoryActivity.ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getLayoutPosition());
        }
    }
}
