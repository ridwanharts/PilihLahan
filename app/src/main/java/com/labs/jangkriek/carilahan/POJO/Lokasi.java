package com.labs.jangkriek.carilahan.POJO;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class Lokasi {

    //tips jangan lupa samakan dengan nama variabel server dengna lokal pojo

    private int id;
    private String nama;
    private Double longitude;
    private Double latitude;
    private int status;
    private String waktu;
    private LatLng lokasi;


    private Double dayaDukungTanah;
    private Double ketersediaanAir;
    private Double kemiringanLereng;
    private Double aksebilitas;
    private Double perubahanLahan;
    private Double kerawananBencana;
    private Double jarakKeBandara;
    private Double jumlah;
    private Bitmap bitmap;
    private String gambar;
    private byte[] image;

    public Lokasi (){

    }
/*    public Lokasi(int id, String nama, double latitude, double longitude,
                  double dayaDukungTanah, double ketersediaanAir, double kemiringanLereng, double aksebilitas,
                  double perubahanLahan, double kerawananBencana, double jarakKeBandara, int status){
        this.id = id;
        this.nama = nama;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dayaDukungTanah = dayaDukungTanah;
        this.ketersediaanAir = ketersediaanAir;
        this.kemiringanLereng = kemiringanLereng;
        this.aksebilitas = aksebilitas;
        this.perubahanLahan = perubahanLahan;
        this.kerawananBencana = kerawananBencana;
        this.jarakKeBandara = jarakKeBandara;
        this.status = status;
    }*/

    public Lokasi(int id, String nama, double latitude, double longitude,
                  double dayaDukungTanah, double ketersediaanAir, double kemiringanLereng, double aksebilitas,
                  double perubahanLahan, double kerawananBencana, double jarakKeBandara, int status, Bitmap bitmap){
        this.id = id;
        this.nama = nama;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dayaDukungTanah = dayaDukungTanah;
        this.ketersediaanAir = ketersediaanAir;
        this.kemiringanLereng = kemiringanLereng;
        this.aksebilitas = aksebilitas;
        this.perubahanLahan = perubahanLahan;
        this.kerawananBencana = kerawananBencana;
        this.jarakKeBandara = jarakKeBandara;
        this.status = status;
        this.bitmap = bitmap;
    }

    public Lokasi(int id, String nama, double latitude, double longitude,
                  double dayaDukungTanah, double ketersediaanAir, double kemiringanLereng, double aksebilitas,
                  double perubahanLahan, double kerawananBencana, double jarakKeBandara, int status, String gambar){
        this.id = id;
        this.nama = nama;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dayaDukungTanah = dayaDukungTanah;
        this.ketersediaanAir = ketersediaanAir;
        this.kemiringanLereng = kemiringanLereng;
        this.aksebilitas = aksebilitas;
        this.perubahanLahan = perubahanLahan;
        this.kerawananBencana = kerawananBencana;
        this.jarakKeBandara = jarakKeBandara;
        this.status = status;
        this.gambar = gambar;
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

    public Double getDayaDukungTanah() {
        return dayaDukungTanah;
    }

    public void setDayaDukungTanah(Double dayaDukungTanah) {
        this.dayaDukungTanah = dayaDukungTanah;
    }

    public Double getKetersediaanAir() {
        return ketersediaanAir;
    }

    public void setKetersediaanAir(Double ketersediaanAir) {
        this.ketersediaanAir = ketersediaanAir;
    }

    public Double getKemiringanLereng() {
        return kemiringanLereng;
    }

    public void setKemiringanLereng(Double kemiringanLereng) {
        this.kemiringanLereng = kemiringanLereng;
    }

    public Double getAksebilitas() {
        return aksebilitas;
    }

    public void setAksebilitas(Double aksebilitas) {
        this.aksebilitas = aksebilitas;
    }

    public Double getPerubahanLahan() {
        return perubahanLahan;
    }

    public void setPerubahanLahan(Double perubahanLahan) {
        this.perubahanLahan = perubahanLahan;
    }

    public Double getKerawananBencana() {
        return kerawananBencana;
    }

    public void setKerawananBencana(Double kerawananBencana) {
        this.kerawananBencana = kerawananBencana;
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
}