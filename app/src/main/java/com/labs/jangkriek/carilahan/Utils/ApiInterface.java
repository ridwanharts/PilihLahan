package com.labs.jangkriek.carilahan.Utils;

import com.labs.jangkriek.carilahan.POJO.Users;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/register_users.php")
    Call<Users>register(
            @Query("username") String username,
            @Query("email") String email,
            @Query("password")String password
    );

    @GET("/login_users.php")
    Call<Users>login(
            @Query("username") String username,
            @Query("password")String password
    );



}
