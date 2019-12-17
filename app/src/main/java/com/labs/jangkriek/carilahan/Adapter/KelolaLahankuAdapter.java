package com.labs.jangkriek.carilahan.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.labs.jangkriek.carilahan.Activity.Users.KelolaLahankuActivity;
import com.labs.jangkriek.carilahan.Database.DbLokasi;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.R;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.labs.jangkriek.carilahan.Activity.MainActivity.getUsername;

public class KelolaLahankuAdapter extends RecyclerView.Adapter<KelolaLahankuAdapter.MyViewHolder> {

    private Context context;
    private List<Lokasi> lokasiList;
    private MapboxMap map;
    private static final String URL = "https://ridwanharts.000webhostapp.com/";
    private DbLokasi dbLokasi;
    private boolean kondisi = true;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView namaLokasi, pemilikLahan;
        public TextView hargaLahan;
        public TextView luasLahan;
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
            pemilikLahan = view.findViewById(R.id.tv_pemilik);
            luasLahan = view.findViewById(R.id.tv_luas_lahan);
            hargaLahan = view.findViewById(R.id.tv_harga_lahan);
            cekUpload = view.findViewById(R.id.iv_cek_upload);
            ibUpload = view.findViewById(R.id.btn_refresh_upload);
            ivLahan = view.findViewById(R.id.image_lahan);
            relativeLayout = view.findViewById(R.id.rv_loading);
            singleCard = view.findViewById(R.id.cardview_lokasi);
            linearLayoutDetailLokasi = view.findViewById(R.id.lihat_detail_lokasi);
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

        if (namaLokasi.getStatus() == 1) {
            holder.cekUpload.setImageResource(R.drawable.icons8_checked);
        } else {
            holder.cekUpload.setImageResource(R.drawable.icons8_checked_grey);
        }

        byte[] decodedString = Base64.decode(namaLokasi.getGambar(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        holder.namaLokasi.setText(namaLokasi.getNama());
        holder.pemilikLahan.setText(getUsername());
        holder.luasLahan.setText(String.valueOf(namaLokasi.getLuasLahan()));
        holder.hargaLahan.setText(String.valueOf(namaLokasi.getHargaLahan()));
        holder.ivLahan.setImageBitmap(decodedByte);
        //Log.e("b", lokasiList.get(position).getLokasi()+"");
        holder.setClickListener(new KelolaLahankuActivity.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LatLng selectedLocationLatLng = lokasiList.get(position).getLokasi();
                //Toast.makeText(context, "Lokasi : " + lokasiList.get(position).getNama(), Toast.LENGTH_SHORT).show();

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
                if (kondisi) {
                    holder.cekUpload.setImageResource(R.drawable.icons8_checked);
                    namaLokasi.setStatus(1);
                } else {
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

        BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(context).inflate(R.layout.bubble_info, null);
        PopupWindow popupWindow = BubblePopupHelper.create(context, bubbleLayout);

        holder.linearLayoutDetailLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView jarakBandara, aksebilitas, jenisTanah, kAir, kLereng, kBencana;

                LatLng selectedLocationLatLng = lokasiList.get(position).getLokasi();

                CameraPosition newCameraPosition = new CameraPosition.Builder()
                        .target(selectedLocationLatLng)
                        .build();
                map.easeCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));

                bubbleLayout.setArrowDirection(ArrowDirection.BOTTOM_CENTER);
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, -40);

                jarakBandara = popupWindow.getContentView().findViewById(R.id.jarak_bandara_bubble);
                aksebilitas = popupWindow.getContentView().findViewById(R.id.aksebilitas_bubble);

                jenisTanah = popupWindow.getContentView().findViewById(R.id.jenistanah_bubble);
                kAir = popupWindow.getContentView().findViewById(R.id.air_bubble);
                kLereng = popupWindow.getContentView().findViewById(R.id.lereng_bubble);
                kBencana = popupWindow.getContentView().findViewById(R.id.bencana_bubble);

                jarakBandara.setText(lokasiList.get(position).getJarakKeBandara()+"");
                aksebilitas.setText(lokasiList.get(position).getAksebilitas()+"");

                jenisTanah.setText(lokasiList.get(position).getDayaDukungTanah()+"");
                kAir.setText(lokasiList.get(position).getKetersediaanAir()+"");
                kLereng.setText(lokasiList.get(position).getKemiringanLereng()+"");
                kBencana.setText(lokasiList.get(position).getKerawananBencana()+"");

                /*double lat = namaLokasi.getLatitude();
                double longi = namaLokasi.getLongitude();
                double harga = namaLokasi.getHargaLahan());

                Bitmap bitmap = namaLokasi.getBitmap();
                String nama = namaLokasi.getNama();

                Intent i = new Intent(context, DetailLokasiActivity.class);
                i.putExtra("nama", nama);
                i.putExtra("latitude", lat);
                i.putExtra("longitude", longi);
                i.putExtra("harga", harga);
                //i.putExtra("nama", nama);
                context.startActivity(i);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return lokasiList.size();
    }

}