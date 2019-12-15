package com.labs.jangkriek.carilahan.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String URL = "https://ridwanharts.000webhostapp.com/";

    public static Retrofit retrofit = null;

    public static Retrofit getApiClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
