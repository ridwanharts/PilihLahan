package com.labs.jangkriek.carilahan.POJO;

public class Prioritas {
    int id;
    String kriteria;
    double nilai;

    public Prioritas (int id, double nilai){
        this.id = id;
        this.nilai = nilai;
    }

    public Prioritas (int id, String kriteria){
        this.id = id;
        this.kriteria = kriteria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKriteria() {
        return kriteria;
    }

    public void setKriteria(String kriteria) {
        this.kriteria = kriteria;
    }

    public double getNilai() {
        //ri
        return nilai;
    }

    public void setNilai(double nilai) {
        this.nilai = nilai;
    }
}
