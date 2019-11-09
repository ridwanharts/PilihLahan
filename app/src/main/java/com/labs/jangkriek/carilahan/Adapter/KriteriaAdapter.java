package com.labs.jangkriek.carilahan.Adapter;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.labs.jangkriek.carilahan.POJO.Kriteria;
import com.labs.jangkriek.carilahan.R;

import java.util.HashMap;
import java.util.List;

public class KriteriaAdapter extends BaseAdapter {

    private List<Kriteria> kriteriaList;
    private Context context;
    public String[] current;
    public static HashMap<Integer,Double> myList = new HashMap<Integer, Double>();
    TextView kriteria1, kriteria2, nilaiKriteria;
    private int editingPosition = 0;

    public KriteriaAdapter(@NonNull Context context, List<Kriteria> list) {
        this.context = context;
        this.kriteriaList = list;

        for (int i=0;i<list.size();i++){
            myList.put(i,0.0);
        }
    }

    public View getView(final int pos, View convertView, ViewGroup parent){
        final ViewHolder holder;
        current = new String[myList.size()];

        View listItemView = convertView;
        if (listItemView==null){
            holder = new ViewHolder();
            /*listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_list_input_kriteria,parent,false);*/

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listItemView = inflater.inflate(R.layout.item_list_input_kriteria, parent, false);

            holder.etInputNilai = listItemView.findViewById(R.id.et_nilai_input);
            holder.tvNilaiKriteria = listItemView.findViewById(R.id.nilai_kriteria);
            holder.seekBar = listItemView.findViewById(R.id.seekbar);

            listItemView.setTag(holder);
        }else {
            holder = (ViewHolder)listItemView.getTag();
        }

        kriteria1 = listItemView.findViewById(R.id.kriteria1);
        kriteria2 = listItemView.findViewById(R.id.kriteria2);

        kriteria1.setText(kriteriaList.get(pos).getKriteria1());
        kriteria2.setText(kriteriaList.get(pos).getKriteria2());

        holder.etInputNilai.removeTextChangedListener(watcher);

        holder.etInputNilai.setText(myList.get(pos)+"");

        holder.etInputNilai.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editingPosition = pos;
                }

                holder.tvNilaiKriteria.setText(String.valueOf(myList.get(pos)));
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.etInputNilai.setShowSoftInputOnFocus(false);
        }

        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double[] a = {0, 0.1111, 0.125, 0.1429, 0.1667, 0.2, 0.25, 0.3333, 0.5, 1};
                if (progress == 10 || progress == 0) {
                    holder.etInputNilai.setText(String.valueOf(0));
                } else if (progress < 10) {
                    double value = a[progress];
                    holder.etInputNilai.setText(String.valueOf(value));
                } else {
                    double value = progress - 10;
                    holder.etInputNilai.setText(String.valueOf(value));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        kriteriaList.get(pos).setNilai(myList.get(pos));
        holder.etInputNilai.addTextChangedListener(watcher);

        return listItemView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private TextWatcher watcher = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            current[editingPosition] = s.toString();
            if (!s.toString().isEmpty()){
                myList.put(editingPosition, Double.parseDouble(s.toString()));

            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            current[editingPosition] = s.toString();
            if (!s.toString().isEmpty()){
                myList.put(editingPosition, Double.parseDouble(s.toString()));
            }
        }
        public void afterTextChanged(Editable s) {

            /*String value = holder.etInputNilai.getText().toString();
            double progress = Double.valueOf(value);
            if (progress == 10 || progress == 0) {
                holder.seekBar.setProgress(10);
            } else if (progress < 10) {
                holder.seekBar.setProgress(5);
            } else {
                holder.seekBar.setProgress(15);
            }*/

        }
    };

    private class ViewHolder {

        EditText etInputNilai;
        TextView tvNilaiKriteria;
        AppCompatSeekBar seekBar;
        int ref;


    }

    @Override
    public int getCount() {
        return kriteriaList.size();
    }

    @Override
    public Object getItem(int position) {

        return kriteriaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
