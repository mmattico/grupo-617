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
        ///// CREA UNA ESPECIE DE ESTRUCTURA DE COMO VA A SER LA COMUNICACION ENTRE RETROFIT Y LA API ///////////////
        Client restAdapter = new Client();
        //// SE ESTABLECEN LAS OPERACIONES QUE VA A HACER RETROFIT SOBRE LA API
        APIService interfazRestApi = restAdapter.getClient().create(APIService.class);
        ////  SE SE DEFINE LA PETRICION AL COMPLETO ///////////////////////////
        Call<ResponseLogin> responseLoginCall = interfazRestApi.login(postRegistroLogin);
        //// SE ENCOLA LA PETICION ///////////////////////////////////////////////
        responseLoginCall.enqueue(new Callback<ResponseLogin>() {
            //// FUNCION QUE SE ACTIVA CUANDO SE RECIBE LA RESPUESTA DE LA API, TANTO DE ERROR COMO SI ESTA OK
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {

                if (response.body() == null) {
                    /// SI LA PETICION DIO ERROR ENTONCES LO CAPTURAMOS Y MANDAMOS A UN LOG E INFORMAMOS POR PANTALLA
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResponseError>() {
                    }.getType();
                    ResponseError errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                    Log.i("errorResponseMsg", errorResponse.getMsg());
                    Log.i("errorResponseState", errorResponse.getState());
                    Toast.makeText(context, errorResponse.getMsg(), Toast.LENGTH_LONG).show();

                } else if (response.body().getState().equals("success")) {
                    /// SI SE PUDO COMPLETAR LA OPERACION SE ACCEDE AL MEN
                    Toast.makeText(context, "login exitoso", Toast.LENGTH_LONG).show();
                    Token.setToken(response.body().getToken());
                    principal.iniciar_Servicio_Reg_Eventos_E_Ir_Menu_Principal();
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

        /// SE PREPARAN LAS ESTRUCTURAS DE RETROFIT PARA EL MENSAJE
        Client restAdapter = new Client();
        APIService interfazRestApi = restAdapter.getClient().create(APIService.class);

        Call<ResponseEvento> responseEventoCall = interfazRestApi.sendEvent(Token.getToken(),postEvento);
        //// SE ENCOLA EL MENSAJE
        responseEventoCall.enqueue(new Callback<ResponseEvento>() {
            ///// METODO CUANDO SE EJECUTA CUANDO SE RECIBE LA RESPUESTA DEL REGISTRO DE EVENTOS
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