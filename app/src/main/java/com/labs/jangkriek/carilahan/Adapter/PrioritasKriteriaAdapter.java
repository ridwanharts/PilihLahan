package com.labs.jangkriek.carilahan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.labs.jangkriek.carilahan.R;
import com.labs.jangkriek.carilahan.Utils.Utils;

import java.util.List;

public class PrioritasKriteriaAdapter extends RecyclerView.Adapter<PrioritasKriteriaAdapter.ViewHolder> {

    private List<String> listKriteria;
    private  List<Double> listVektorPrioritas;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    // data is passed into the constructor
    public PrioritasKriteriaAdapter(Context context, List<String> listKriteria, List<Double> listVektorPrioritas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.listKriteria = listKriteria;
        this.listVektorPrioritas = listVektorPrioritas;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_kriteria, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String kriteria = listKriteria.get(position);
        double vektorPrioritas = listVektorPrioritas.get(position);
        int no = position+1;
        Utils a = new Utils();
        holder.tvNamaKriteria.setText(kriteria);
        holder.tvVektorPrioritas.setText(a.formatDecimal(vektorPrioritas*100)+"");
        if (holder.tvNamaKriteria.getText().toString().equals("Pilihan Masih Kosong")){
            holder.btnNo.setText("-");
        }else {
            holder.btnNo.setText(""+no);
        }

        holder.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext, ""+a.formatDecimal(vektorPrioritas*100), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return listKriteria.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNamaKriteria;
        TextView tvVektorPrioritas;
        Button btnNo;


        ViewHolder(View itemView) {
            super(itemView);
            tvNamaKriteria = itemView.findViewById(R.id.tv_nama_pkriteria);
            tvVektorPrioritas = itemView.findViewById(R.id.tv_vektor_prioritas);
            btnNo = itemView.findViewById(R.id.btn_no_prioritas);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return listKriteria.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
