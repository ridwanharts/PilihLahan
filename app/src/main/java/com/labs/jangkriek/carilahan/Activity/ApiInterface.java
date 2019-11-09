package com.labs.jangkriek.carilahan.Activity;

import com.labs.jangkriek.carilahan.POJO.Admin;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/register_admin.php")
    Call<Admin>register(
            @Query("username") String username,
            @Query("email") String email,
            @Query("password")String password
    );

    @GET("/login_admin.php")
    Call<Admin>login(
            @Query("username") String username,
            @Query("password")String password
    );



}
