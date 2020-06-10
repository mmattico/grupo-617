package com.example.tp2.retrofit;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.tp2.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Thread.sleep;

public class ServicePOST {

    private Context context;

    public ServicePOST() {

    }

    public ServicePOST(Context context) {
        this.context = context;
    }

    public void PostRegitro(RequestRegistroLog postRegistroLogin) {

        Client restAdapter = new Client();
        APIService interfazRestApi = restAdapter.getClient().create(APIService.class);
        Call<ResponseRegistro> responseRegistroCall = interfazRestApi.register(postRegistroLogin);

        responseRegistroCall.enqueue(new Callback<ResponseRegistro>() {
            @Override
            public void onResponse(Call<ResponseRegistro> call, Response<ResponseRegistro> response) {

                if (response.body() == null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResponseError>() {
                    }.getType();
                    ResponseError errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                    Toast.makeText(context, errorResponse.getMsg(), Toast.LENGTH_LONG).show();
                } else if (response.body().getState().equals("success")) {
                    Toast.makeText(context, "Ha sido registrado correctamente", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseRegistro> call, Throwable t) {
                Toast.makeText(context, "fallo la conexion", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void enviarLogin(RequestRegistroLog postRegistroLogin, final MainActivity principal) {
        Client restAdapter = new Client();
        APIService interfazRestApi = restAdapter.getClient().create(APIService.class);

        Call<ResponseLogin> responseLoginCall = interfazRestApi.login(postRegistroLogin);
        responseLoginCall.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {

                if (response.body() == null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResponseError>() {
                    }.getType();
                    ResponseError errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                    Log.i("errorResponseMsg", errorResponse.getMsg());
                    Log.i("errorResponseState", errorResponse.getState());
                    Toast.makeText(context, errorResponse.getMsg(), Toast.LENGTH_LONG).show();

                } else if (response.body().getState().equals("success")) {
                    Toast.makeText(context, "login exitoso", Toast.LENGTH_LONG).show();
                    Token.setToken(response.body().getToken());
                    principal.irTapaPant();
                }

            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registrarEvento(final String descripcion, String type_events) {

        RequestEvento postEvento = new RequestEvento();
        postEvento.setEnv("DEV");
        postEvento.setDescription(descripcion);
        postEvento.setState("ACTIVO");
        postEvento.setType_events(type_events);

        Client restAdapter = new Client();
        APIService interfazRestApi = restAdapter.getClient().create(APIService.class);

        if(type_events.equals("Login")) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Call<ResponseEvento> responseEventoCall = interfazRestApi.sendEvent(Token.getToken(),postEvento);
        responseEventoCall.enqueue(new Callback<ResponseEvento>() {
            @Override
            public void onResponse(Call<ResponseEvento> call, Response<ResponseEvento> response) {
                if (response.body() == null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResponseError>() {
                    }.getType();
                    ResponseError errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                    Log.i("mensajeError", errorResponse.getMsg());
                } else if (response.body().getState().equals("success"))
                    Log.i("mensajeSuccess",descripcion+ "exito");
                else{
                    Log.i("mensajeFallo","fallo "+descripcion);
                }

            }

            @Override
            public void onFailure(Call<ResponseEvento> call, Throwable t) {
                Log.d("Reg error: ", t.getMessage());
                Toast.makeText(context.getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}