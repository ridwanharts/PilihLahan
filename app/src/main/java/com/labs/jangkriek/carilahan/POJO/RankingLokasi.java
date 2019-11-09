package com.labs.jangkriek.carilahan.POJO;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class RankingLokasi {

    private int id;
    private int idGroup;
    private String nama;
    private Double longitude;
    private Double latitude;
    private Double jumlah;
    private String waktu;
    private LatLng lokasi;

    private Double dayaDukungTanah;
    private Double ketersediaanAir;
    private Double kemiringanLereng;
    private Double aksebilitas;
    private Double perubahanLahan;
    private Double kerawananBencana;
    private Double jarakKeBandara;

    public RankingLokasi(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RankingLokasi(int id, int idgroup, String nama, double latitude, double longitude, double jumlah, String waktu){
        this.id = id;
        this.idGroup = idgroup;
        this.nama = nama;
        this.latitude = latitude;
        this.longitude = longitude;
        this.jumlah = jumlah;
        this.waktu = waktu;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public LatLng getLokasi() {
        LatLng a = new LatLng(this.latitude, this.longitude);
        return a;
    }

    public void setLokasi(LatLng lokasi) {
        this.lokasi = lokasi;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public Double getJumlah() {
        return jumlah;
    }

    public void setJumlah(Double jumlah) {
        this.jumlah = jumlah;
    }
}
