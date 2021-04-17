package com.labs.jangkriek.carilahan.Utils;

import com.labs.jangkriek.carilahan.POJO.ResponUsers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/register_users.php")
    Call<ResponUsers>register(
            @Query("username") String username,
            @Query("email") String email,
            @Query("password")String password,
            @Query("no_hp")String no_hp
    );

    @GET("/login_users.php")
    Call<ResponUsers>login(
            @Query("username") String username,
            @Query("password")String password
    );



}
