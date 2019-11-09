package com.labs.jangkriek.carilahan.POJO;

import android.widget.Button;

import java.io.Serializable;

public class Kriteria implements Serializable {

    private String kriteria1;
    private String kriteria2;
    private double nilai;

    public double getNilai() {
        return nilai;
    }

    public void setNilai(double nilai) {
        this.nilai = nilai;
    }

    private String editTextValue;

    public String getEditTextValue() {
        return editTextValue;
    }

    public void setEditTextValue(String editTextValue) {
        this.editTextValue = editTextValue;
    }

    public Kriteria (){}

    public Kriteria (String k1, String k2){
        this.kriteria1 = k1;
        this.kriteria2 = k2;
    }

    public String getKriteria1() {
        return kriteria1;
    }

    public void setKriteria1(String kriteria1) {
        this.kriteria1 = kriteria1;
    }

    public String getKriteria2() {
        return kriteria2;
    }

    public void setKriteria2(String kriteria2) {
        this.kriteria2 = kriteria2;
    }
}
