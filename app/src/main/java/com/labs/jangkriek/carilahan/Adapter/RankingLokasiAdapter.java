package com.labs.jangkriek.carilahan.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.labs.jangkriek.carilahan.Activity.RankingActivity;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.R;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

public class RankingLokasiAdapter extends RecyclerView.Adapter<RankingLokasiAdapter.MyViewHolder> {

    private Context context;
    private List<Lokasi> rangkingLokasiList;
    private MapboxMap map;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView namaRankLokasi;
        public TextView rank;
        public TextView id;
        public TextView latitude;
        public TextView longitude;
        public TextView jumlah;
        CardView singleCard;
        RankingActivity.ItemClickListener itemClickListener;

        public MyViewHolder(View view) {
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

        public void setClickListener(RankingActivity.ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getLayoutPosition());
        }
    }


    public RankingLokasiAdapter(Context context, List<Lokasi> rangkingLokasiList, MapboxMap map) {
        this.context = context;
        this.rangkingLokasiList = rangkingLokasiList;
        this.map = map;
    }

    @Override
    public RankingLokasiAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_rank_lokasi, parent, false);

        return new RankingLokasiAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RankingLokasiAdapter.MyViewHolder holder, int position) {
        Lokasi namaLokasi = rangkingLokasiList.get(position);
        int pos = position+1;

        holder.rank.setText("Rank "+pos);
        holder.namaRankLokasi.setText(namaLokasi.getNama());
        holder.id.setText("id "+namaLokasi.getId());
        holder.latitude.setText(String.valueOf(namaLokasi.getLatitude()));
        holder.longitude.setText(String.valueOf(namaLokasi.getLongitude()));
        holder.jumlah.setText(String.valueOf(namaLokasi.getJumlah()));
        //Log.e("b", lokasiList.get(position).getLokasi()+"");
        holder.setClickListener(new RankingActivity.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LatLng selectedLocationLatLng = rangkingLokasiList.get(position).getLokasi();
                //Toast.makeText(context,"cek : "+rangkingLokasiList.get(position).getLokasi(), Toast.LENGTH_SHORT).show();

                BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(context).inflate(R.layout.bubble_info_rank, null);
                PopupWindow popupWindow = BubblePopupHelper.create(context, bubbleLayout);

                CameraPosition newCameraPosition = new CameraPosition.Builder()
                        .target(selectedLocationLatLng)
                        .build();
                map.easeCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));

                TextView jarakBandara, aksebilitas, jenisTanah, kAir, kLereng, kBencana;
                ImageButton ibEdit;
                bubbleLayout.setArrowDirection(ArrowDirection.BOTTOM_CENTER);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, -90);

                jarakBandara = popupWindow.getContentView().findViewById(R.id.jarak_bandara_bubble);
                aksebilitas = popupWindow.getContentView().findViewById(R.id.aksebilitas_bubble);

                jenisTanah = popupWindow.getContentView().findViewById(R.id.jenistanah_bubble);
                kAir = popupWindow.getContentView().findViewById(R.id.air_bubble);
                kLereng = popupWindow.getContentView().findViewById(R.id.lereng_bubble);
                kBencana = popupWindow.getContentView().findViewById(R.id.bencana_bubble);

                jarakBandara.setText(rangkingLokasiList.get(position).getJarakKeBandara()+"");
                aksebilitas.setText(rangkingLokasiList.get(position).getAksebilitas()+"");

                jenisTanah.setText(rangkingLokasiList.get(position).getDayaDukungTanah()+"");
                kAir.setText(rangkingLokasiList.get(position).getKetersediaanAir()+"");
                kLereng.setText(rangkingLokasiList.get(position).getKemiringanLereng()+"");
                kBencana.setText(rangkingLokasiList.get(position).getKerawananBencana()+"");
            }
        });
        /*holder.setClickListener(new KelolaLahankuActivity.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LatLng selectedLocationLatLng = rangkingLokasiList.get(position).getLokasi();
                Toast.makeText(context,"cek : "+rangkingLokasiList.get(position).getLokasi(), Toast.LENGTH_SHORT).show();

                CameraPosition newCameraPosition = new CameraPosition.Builder()
                        .target(selectedLocationLatLng)
                        .build();
                map.easeCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
