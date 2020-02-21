package com.labs.jangkriek.carilahan.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.labs.jangkriek.carilahan.Activity.Users.KelolaLahankuActivity;
import com.labs.jangkriek.carilahan.Database.DbSavePencarian;
import com.labs.jangkriek.carilahan.POJO.Lokasi;
import com.labs.jangkriek.carilahan.POJO.Respon;
import com.labs.jangkriek.carilahan.POJO.SavePencarian;
import com.labs.jangkriek.carilahan.Activity.Users.DetailHistoryActivity;
import com.labs.jangkriek.carilahan.POJO.Users;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Utils.RegisterApi;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.MyViewHolder> {

    private Context context;
    private List<String> waktu;
    private List<String> idGroup;
    private List<String> metode;
    private List<String> kriteria;
    private MapboxMap map;
    private SimpleDateFormat newFormatHour = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat formatHour = new SimpleDateFormat("HHmmss");
    private DbSavePencarian dbSavePencarian;
    Date date = null;

    private static final String URL = "https://ridwanharts.000webhostapp.com/";
    private static List<Users> userList;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView waktu, tvKriteria;
        public Button btnSeeDetail;
        LinearLayout linAHP, linFAHP;
        CardView singleCard;
        private RelativeLayout rvLoading;
        KelolaLahankuActivity.ItemClickListener itemClickListener;
        ImageButton ibDelete;


        public MyViewHolder(View view) {
            super(view);
            linAHP = view.findViewById(R.id.lin_ahp);
            linFAHP = view.findViewById(R.id.lin_fahp);
            waktu = view.findViewById(R.id.tv_save_date);
            btnSeeDetail = view.findViewById(R.id.btn_see_detail);
            tvKriteria = view.findViewById(R.id.tv_kriteria_use);
            singleCard = view.findViewById(R.id.cardview_save_lokasi);
            singleCard.setOnClickListener(this);
            rvLoading = view.findViewById(R.id.rv_loading);
            ibDelete = view.findViewById(R.id.btn_delete);
        }

        public void setClickListener(KelolaLahankuActivity.ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getLayoutPosition());
        }
    }


    public SaveAdapter(Context context, List<String> waktu, List<String> idGroup, List<String> metode, List<String> kriteria) {
        this.context = context;
        this.waktu = waktu;
        this.idGroup = idGroup;
        this.metode = metode;
        this.kriteria = kriteria;
    }

    @Override
    public SaveAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_save_pencarian, parent, false);
        return new SaveAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SaveAdapter.MyViewHolder holder, int position) {

        dbSavePencarian = new DbSavePencarian(context);

        try {
            date = formatHour.parse(idGroup.get(position).substring(0,5));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null){
            holder.waktu.setText(waktu.get(position)+" - "+""+newFormatHour.format(date)+ " WIB");
        }
        if (metode.get(position).equals("AHP")){
            holder.linAHP.setVisibility(View.VISIBLE);
        }else {
            holder.linFAHP.setVisibility(View.VISIBLE);
        }

        holder.tvKriteria.setText(kriteria.get(position));

        holder.setClickListener(new KelolaLahankuActivity.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {


            }
        });
        holder.btnSeeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.rvLoading.setVisibility(View.VISIBLE);
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.e("", "message : "+message);
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
                Call<Respon> call = api.view_user();
                call.enqueue(new Callback<Respon>() {
                    @Override
                    public void onResponse(Call<Respon> call, Response<Respon> response) {
                        String value = response.body().getValue();
                        if (value.equals("1")){
                            userList = response.body().getUsersList();
                            //Log.e("status", "berhasil user list "+userList.size());
                            Intent i = new Intent(context, DetailHistoryActivity.class);
                            i.putExtra("id_group", idGroup.get(position));
                            i.putExtra("waktu", waktu.get(position));
                            context.startActivity(i);
                            holder.rvLoading.setVisibility(View.INVISIBLE);
                            Log.e("status", "id  "+idGroup.get(position));
                        }
                    }

                    @Override
                    public void onFailure(Call<Respon> call, Throwable t) {
                        Log.e("error", "gagal");
                        holder.rvLoading.setVisibility(View.VISIBLE);
                    }
                });


            }
        });

        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle("Hapus Lahan ");
                builder.setMessage("Apakah anda yakin menghapus hasil pencarian ini ?");
                builder.setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // deleting the note from db lokal
                                dbSavePencarian.deleteLokasi(idGroup.get(position));

                                // removing from the list
                                idGroup.remove(position);
                                waktu.remove(position);
                                notifyItemRemoved(position);
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
    }

    @Override
    public int getItemCount() {
        return waktu.size();
    }

    public static List<Users> userListDetailHistory(){
        return userList;
    }

}
