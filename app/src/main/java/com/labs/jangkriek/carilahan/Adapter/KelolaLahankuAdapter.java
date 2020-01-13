package com.labs.jangkriek.carilahan.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.labs.jangkriek.carilahan.Activity.Users.KelolaLahankuActivity;
import com.labs.jangkriek.carilahan.Activity.Users.TambahLahanActivity;
import com.labs.jangkriek.carilahan.Database.DbLokasi;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.POJO.Respon;
import com.labs.jangkriek.carilahan.POJO.Users;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Utils.RegisterApi;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.labs.jangkriek.carilahan.Activity.MainActivity.getIdUser;
import static com.labs.jangkriek.carilahan.Activity.MainActivity.loginType;
import static com.labs.jangkriek.carilahan.Activity.SemuaLahanActivity.getIsSemuaLahan;
import static com.labs.jangkriek.carilahan.Activity.SemuaLahanActivity.userList;
import static com.labs.jangkriek.carilahan.Adapter.SaveAdapter.userListDetailHistory;

public class KelolaLahankuAdapter extends RecyclerView.Adapter<KelolaLahankuAdapter.MyViewHolder> {

    private Context context;
    private List<Lokasi> lokasiList;
    private List<Users> userList;
    private MapboxMap map;
    private static final String URL = "https://ridwanharts.000webhostapp.com/";
    private DbLokasi dbLokasi;
    private boolean kondisi = true;
    private static RegisterApi apiInterface;
    private static RelativeLayout rvLoading;
    private boolean history = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView namaLokasi, pemilikLahan;
        TextView hargaLahan;
        TextView luasLahan;
        ImageButton ibDelete;
        ImageView ivLahan;
        CardView singleCard;
        LinearLayout linearLayoutDetailLokasi;
        KelolaLahankuActivity.ItemClickListener itemClickListener;

        public MyViewHolder(View view) {
            super(view);
            namaLokasi = view.findViewById(R.id.tv_nama_lokasi);
            pemilikLahan = view.findViewById(R.id.tv_pemilik);
            luasLahan = view.findViewById(R.id.tv_luas_lahan);
            hargaLahan = view.findViewById(R.id.tv_harga_lahan);
            ibDelete = view.findViewById(R.id.btn_delete);
            ivLahan = view.findViewById(R.id.image_lahan);
            rvLoading = view.findViewById(R.id.rv_loading);
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

    public KelolaLahankuAdapter(Context context, List<Lokasi> lokasiList) {
        this.context = context;
        this.lokasiList = lokasiList;
        history = true;
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

        if (history){
            userList = userListDetailHistory();
        }else {
            userList = userList();
        }

        String username = "null";

        byte[] decodedString = Base64.decode(namaLokasi.getGambar(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        holder.namaLokasi.setText(namaLokasi.getNama());


        if (namaLokasi.getId_user() == getIdUser() && !loginType.equals("GUEST")){
            holder.pemilikLahan.setText("Lahanku");
            holder.pemilikLahan.setTextColor(context.getResources().getColor(R.color.mapboxGreen));
        }else{
            for (int i=0;i<userList.size();i++){
                if (userList.get(i).getId() == namaLokasi.getId_user()){
                    username = userList.get(i).getUsername();
                    holder.pemilikLahan.setText(username);
                }
            }

            holder.pemilikLahan.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

        holder.luasLahan.setText(String.valueOf(namaLokasi.getLuasLahan()));
        holder.hargaLahan.setText(String.valueOf(namaLokasi.getHargaLahan()));
        holder.ivLahan.setImageBitmap(decodedByte);
        //Log.e("b", lokasiList.get(position).getLokasi()+"");

        //histroy cek
        if (!history) {
            holder.setClickListener(new KelolaLahankuActivity.ItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    LatLng selectedLocationLatLng = lokasiList.get(position).getLokasi();
                    //Toast.makeText(context, "Lokasi : " + lokasiList.get(position).getUsername(), Toast.LENGTH_SHORT).show();

                    CameraPosition newCameraPosition = new CameraPosition.Builder()
                            .target(selectedLocationLatLng)
                            .build();
                    map.easeCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));
                }
            });
            if (getIsSemuaLahan() != null){
                if (getIsSemuaLahan()){
                    holder.ibDelete.setVisibility(View.INVISIBLE);
                }
            }

            holder.ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle("Hapus Lahan ");
                    builder.setMessage("Apakah anda yakin menghapus lahan "+namaLokasi.getNama());
                    builder.setPositiveButton("Ya",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    rvLoading.setVisibility(View.VISIBLE);
                                    deleteLahan(namaLokasi.getId());
                                    notifyDataSetChanged();
                                    //notifyItemRemoved(namaLokasi.getId());
                                }
                            });
                    builder.setNegativeButton("Tidak",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    notifyDataSetChanged();
                }
            });

            BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(context).inflate(R.layout.bubble_info, null);
            PopupWindow popupWindow = BubblePopupHelper.create(context, bubbleLayout);

            holder.linearLayoutDetailLokasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView jarakBandara, aksebilitas, jenisTanah, kAir, kLereng, kBencana;
                    ImageButton ibEdit;

                    LatLng selectedLocationLatLng = lokasiList.get(position).getLokasi();

                    CameraPosition newCameraPosition = new CameraPosition.Builder()
                            .target(selectedLocationLatLng)
                            .build();
                    map.easeCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));

                    bubbleLayout.setArrowDirection(ArrowDirection.BOTTOM_CENTER);
                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, -70);

                    jarakBandara = popupWindow.getContentView().findViewById(R.id.jarak_bandara_bubble);
                    aksebilitas = popupWindow.getContentView().findViewById(R.id.aksebilitas_bubble);

                    jenisTanah = popupWindow.getContentView().findViewById(R.id.jenistanah_bubble);
                    kAir = popupWindow.getContentView().findViewById(R.id.air_bubble);
                    kLereng = popupWindow.getContentView().findViewById(R.id.lereng_bubble);
                    kBencana = popupWindow.getContentView().findViewById(R.id.bencana_bubble);

                    ibEdit = popupWindow.getContentView().findViewById(R.id.btn_edit);
                    Boolean cek;
                    cek = true;
                    if (cek == getIsSemuaLahan()){
                        ibEdit.setVisibility(View.GONE);
                    }else {
                        if (ibEdit.getVisibility() == View.VISIBLE) {
                            ibEdit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setCancelable(true);
                                    builder.setTitle("Edit Lahan");
                                    builder.setMessage("\"" + namaLokasi.getNama() + "\"");
                                    builder.setPositiveButton("Ya",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent i = new Intent(context, TambahLahanActivity.class);
                                                    i.putExtra("is_edit", true);
                                                    i.putExtra("id_lahan", namaLokasi.getId());
                                                    i.putExtra("nama_lahan", namaLokasi.getNama());
                                                    i.putExtra("harga_lahan", namaLokasi.getHargaLahan());
                                                    i.putExtra("luas_lahan", namaLokasi.getLuasLahan());
                                                    i.putExtra("latitude", namaLokasi.getLatitude());
                                                    i.putExtra("longitude", namaLokasi.getLongitude());
                                                    i.putExtra("created_at", namaLokasi.getCreated_at());
                                                    i.putExtra("gambar", namaLokasi.getGambar());
                                                    context.startActivity(i);

                                                    Toast.makeText(context, "Edited Lahan " + namaLokasi.getNama(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    builder.setNegativeButton("Tidak",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });

                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });
                        }
                    }

                    jarakBandara.setText(lokasiList.get(position).getJarakKeBandara()+"");
                    aksebilitas.setText(lokasiList.get(position).getAksebilitas()+"");

                    jenisTanah.setText(lokasiList.get(position).getDayaDukungTanah()+"");
                    kAir.setText(lokasiList.get(position).getKetersediaanAir()+"");
                    kLereng.setText(lokasiList.get(position).getKemiringanLereng()+"");
                    kBencana.setText(lokasiList.get(position).getKerawananBencana()+"");

                }
            });

        }else {
            ViewGroup.LayoutParams a = holder.singleCard.getLayoutParams();
            a.width = ViewGroup.LayoutParams.MATCH_PARENT;
            holder.singleCard.setLayoutParams(a);

            holder.ibDelete.setVisibility(View.INVISIBLE);
            holder.setClickListener(new KelolaLahankuActivity.ItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Toast.makeText(context, "Lokasi History ", Toast.LENGTH_SHORT).show();

                }
            });

            holder.linearLayoutDetailLokasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Linear ", Toast.LENGTH_SHORT).show();
                }
            });
        }
        //end hustory boolean


    }

    private void deleteLahan(int id) {

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
        Call<Respon> callInsertPlot = api.delete(id);
        callInsertPlot.enqueue(new Callback<Respon>() {
            @Override
            public void onResponse(Call<Respon> call, retrofit2.Response<Respon> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                rvLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Respon> call, Throwable t) {
                Toast.makeText(context, "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                rvLoading.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lokasiList.size();
    }

    public void clear() {
        int size = lokasiList.size();
        lokasiList.clear();

        notifyItemRangeRemoved(0, size);
    }



}