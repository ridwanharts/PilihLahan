package com.labs.jangkriek.carilahan.Utils;

import com.labs.jangkriek.carilahan.POJO.Respon;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RegisterApi {

    @FormUrlEncoded
    @POST("/insert_lokasi.php")
    Call<Respon> insert_lokasi(
            @Field("nama") String nama,
            @Field("hargaLahan") double hargaLahan,
            @Field("luasLahan") double luasLahan,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("dayaDukungTanah") String dayaDukungTanah,
            @Field("ketersediaanAir") String ketersediaanAir,
            @Field("kemiringanLereng") String kemiringanLereng,
            @Field("aksebilitas") double aksebilitas,
            @Field("kerawananBencana") String kerawananBencana,
            @Field("jarakKeBandara") double jarakKeBandara,
            @Field("created_at") String created_at,
            @Field("id_user") int id_user,
            @Field("gambar") String gambar
    );

    @FormUrlEncoded
    @POST("/insert_latlong.php")
    Call<Respon> insert_latlong(
            @Field("no_point") int no_point,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("id_user") int id_user,
            @Field("created_at") String created_at
    );

    @GET("/view_lokasi.php")
    Call<Respon> view();

    @GET("/get_user_latlong.php")
    Call<Respon> get_user_latlong();


    @FormUrlEncoded
    @POST("/delete_lokasi.php")
    Call<Respon> delete(
            @Field("nama") String nama,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude
    );
}
