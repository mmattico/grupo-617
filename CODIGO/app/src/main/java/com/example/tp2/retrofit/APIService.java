package com.example.tp2.retrofit;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIService {

    @POST("login")
    Call<ResponseLogin> login(@Body RequestRegistroLog user);

    @POST("register")
    Call<ResponseRegistro> register(@Body RequestRegistroLog user);

    @POST("event")
    Call<ResponseEvento> sendEvent(@Header("token") String token,@Body RequestEvento sensorEvent);
}
