package com.labs.jangkriek.carilahan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.labs.jangkriek.carilahan.Activity.DetailLokasiActivity;
import com.labs.jangkriek.carilahan.Activity.RankingActivity;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.POJO.Users;
import com.labs.jangkriek.carilahan.PrefConfig;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Utils.Utils;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;
import static com.labs.jangkriek.carilahan.Adapter.SaveAdapter.userListDetailHistory;
import static com.labs.jangkriek.carilahan.mainViewFragment.GuestHomeFragment.userListtoProcessGuest;
import static com.labs.jangkriek.carilahan.mainViewFragment.UsersHomeFragment.userListtoProcess;

public class RankingLokasiAdapter extends RecyclerView.Adapter<RankingLokasiAdapter.MyViewHolder> {

    private Context context;
    private List<Lokasi> rangkingLokasiList;
    private MapboxMap map;
    private List<Users> userList;
    private boolean history = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView namaRankLokasi;
        public TextView rank;
        public TextView id;
        public TextView latitude;
        public TextView longitude;
        public TextView jumlah;
        TextView pemilikLahan;
        TextView no_hp;
        TextView hargaLahan;
        TextView luasLahan;
        CardView singleCard;
        ImageView ivLahan;
        LinearLayout linearLayoutDetailLokasi;
        RankingActivity.ItemClickListener itemClickListener;

        public MyViewHolder(View view) {
            super(view);
            namaRankLokasi = view.findViewById(R.id.tv_nama_lokasi);
            rank = view.findViewById(R.id.tv_no);
            jumlah = view.findViewById(R.id.tv_jumlah);
            singleCard = view.findViewById(R.id.cardview_rank_lokasi);

            pemilikLahan = view.findViewById(R.id.tv_pemilik);
            luasLahan = view.findViewById(R.id.tv_luas_lahan);
            hargaLahan = view.findViewById(R.id.tv_harga_lahan);
            ivLahan = view.findViewById(R.id.image_lahan);
            no_hp = view.findViewById(R.id.no_hp_rank);
            linearLayoutDetailLokasi = view.findViewById(R.id.lihat_detail_lokasi);
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

        if (history){
            userList = userListDetailHistory();
        }else {
            userList = userListtoProcess();
            if (PrefConfig.getTypeLogin(context).equals("GUEST")){
                userList = userListtoProcessGuest();
            }
        }

        String username = "null";
        String loginType = PrefConfig.getTypeLogin(context);
        holder.namaRankLokasi.setText(namaLokasi.getNama());
        if (namaLokasi.getId_user() == PrefConfig.getLoggedInUser(context) && !loginType.equals("GUEST")){
            holder.pemilikLahan.setText("Lahanku");
            holder.pemilikLahan.setTextColor(context.getResources().getColor(R.color.mapboxGreen));
        }else{
            for (int i=0;i<userList.size();i++){
                if (userList.get(i).getId() == namaLokasi.getId_user()){
                    username = userList.get(i).getUsername();
                    holder.pemilikLahan.setText(username);
                    holder.no_hp.setText(String.valueOf(userList.get(i).getNo_hp()));
                }
            }

            holder.pemilikLahan.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
//
        holder.luasLahan.setText(String.valueOf(namaLokasi.getLuasLahan()));
        holder.hargaLahan.setText(String.valueOf(namaLokasi.getHargaLahan()));
        Glide.with(context)
                .load(namaLokasi.getGambar()+"0.jpg")
                .placeholder(R.drawable.ic_refresh)
                .error(R.drawable.ic_error)
                .into(holder.ivLahan);

        holder.rank.setText("Rank "+pos);
        holder.namaRankLokasi.setText(namaLokasi.getNama());

        Utils a = new Utils();
        holder.jumlah.setText(String.valueOf(a.format5(namaLokasi.getJumlah())));
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

        holder.linearLayoutDetailLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Detail Lokasi ", Toast.LENGTH_SHORT).show();
                String no_hp="";
                String username="";

                for (int i=0;i<userList.size();i++){
                    if (userList.get(i).getId() == rangkingLokasiList.get(position).getId_user()){
                        no_hp = userList.get(i).getNo_hp();
                        username = userList.get(i).getUsername();
                    }
                }
                Intent i = new Intent(context, DetailLokasiActivity.class);
                i.putExtra("is_edit", false);
                i.putExtra("nama", namaLokasi.getNama());
                i.putExtra("harga", namaLokasi.getHargaLahan());
                i.putExtra("latitude", namaLokasi.getLatitude());
                i.putExtra("longitude", namaLokasi.getLongitude());
                i.putExtra("url", namaLokasi.getGambar());

                i.putExtra("k1", namaLokasi.getDayaDukungTanah());
                i.putExtra("k2", namaLokasi.getKetersediaanAir());
                i.putExtra("k3", namaLokasi.getKemiringanLereng());
                i.putExtra("k4", namaLokasi.getAksebilitas());
                i.putExtra("k5", namaLokasi.getKerawananBencana());
                i.putExtra("k6", namaLokasi.getJarakKeBandara());
                i.putExtra("luas", namaLokasi.getLuasLahan());
                //i.putExtra("nama", nama);
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return rangkingLokasiList.size();
    }

}
