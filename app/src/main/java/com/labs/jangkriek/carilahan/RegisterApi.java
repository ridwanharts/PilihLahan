package com.labs.jangkriek.carilahan;

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
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("dayaDukungTanah") double dayaDukungTanah,
            @Field("ketersediaanAir") double ketersediaanAir,
            @Field("kemiringanLereng") double kemiringanLereng,
            @Field("aksebilitas") double aksebilitas,
            @Field("perubahanLahan") double perubahanLahan,
            @Field("kerawananBencana") double kerawananBencana,
            @Field("jarakKeBandara") double jarakKeBandara,
            @Field("status") int status
    );

    @GET("/view_lokasi.php")
    Call<Respon> view();

    @FormUrlEncoded
    @POST("/delete_lokasi.php")
    Call<Respon> delete(
            @Field("nama") String nama,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude
    );
}
