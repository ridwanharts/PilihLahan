package com.labs.jangkriek.carilahan.POJO;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class Lokasi {

    //tips jangan lupa samakan dengan nama variabel server dengna lokal pojo

    private int id;
    private String nama;
    private int id_user;
    private double hargaLahan;
    private double luasLahan;
    private Double longitude;
    private Double latitude;
    private int status;
    private String created_at;
    private String waktu;
    private LatLng lokasi;

    private String dayaDukungTanah;
    private String ketersediaanAir;
    private String kemiringanLereng;
    private String kerawananBencana;

    private Double aksebilitas;
    private Double jarakKeBandara;
    private Double jumlah;
    private Bitmap bitmap;
    private String gambar;
    private byte[] image;

    public Lokasi (){

    }

    public Lokasi(int id, String nama, double hargaLahan, double luasLahan, double latitude, double longitude,
                  String dayaDukungTanah, String ketersediaanAir, String kemiringanLereng, double aksebilitas,
                  String kerawananBencana, double jarakKeBandara, String created_at, int id_user, String gambar){
        this.id = id;
        this.nama = nama;
        this.hargaLahan = hargaLahan;
        this.luasLahan = luasLahan;

        this.latitude = latitude;
        this.longitude = longitude;
        this.dayaDukungTanah = dayaDukungTanah;
        this.ketersediaanAir = ketersediaanAir;
        this.kemiringanLereng = kemiringanLereng;
        this.aksebilitas = aksebilitas;
        this.kerawananBencana = kerawananBencana;
        this.jarakKeBandara = jarakKeBandara;

        this.created_at = created_at;
        this.id_user = id_user;
        this.gambar = gambar;
    }

    public String getDayaDukungTanah() {
        return dayaDukungTanah;
    }

    public void setDayaDukungTanah(String dayaDukungTanah) {
        this.dayaDukungTanah = dayaDukungTanah;
    }

    public String getKetersediaanAir() {
        return ketersediaanAir;
    }

    public void setKetersediaanAir(String ketersediaanAir) {
        this.ketersediaanAir = ketersediaanAir;
    }

    public String getKemiringanLereng() {
        return kemiringanLereng;
    }

    public void setKemiringanLereng(String kemiringanLereng) {
        this.kemiringanLereng = kemiringanLereng;
    }

    public String getKerawananBencana() {
        return kerawananBencana;
    }

    public void setKerawananBencana(String kerawananBencana) {
        this.kerawananBencana = kerawananBencana;
    }

    public double getLuasLahan() {
        return luasLahan;
    }

    public void setLuasLahan(double luasLahan) {
        this.luasLahan = luasLahan;
    }

    public double getHargaLahan() {
        return hargaLahan;
    }

    public void setHargaLahan(double hargaLahan) {
        this.hargaLahan = hargaLahan;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Double getAksebilitas() {
        return aksebilitas;
    }

    public void setAksebilitas(Double aksebilitas) {
        this.aksebilitas = aksebilitas;
    }

    public Double getJarakKeBandara() {
        return jarakKeBandara;
    }

    public void setJarakKeBandara(Double jarakKeBandara) {
        this.jarakKeBandara = jarakKeBandara;
    }

    public void setLokasi(LatLng lokasi) {
        this.lokasi = lokasi;
    }

    public Double getJumlah() {
        return jumlah;
    }

    public void setJumlah(Double jumlah) {
        this.jumlah = jumlah;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public LatLng getLokasi() {
        LatLng a = new LatLng(this.latitude, this.longitude);
        return a;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}