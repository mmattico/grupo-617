package com.example.tp2;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public class ServiceHistorial extends IntentService {
    private final int NO_JUGO_ANTES=0;


    public ServiceHistorial() {
        super("ServiceHistorial");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences preferences = getSharedPreferences(intent.getExtras().getString("usuario"), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int cantReg=preferences.getInt("NREG", NO_JUGO_ANTES );
        if ( cantReg== NO_JUGO_ANTES)
        {
            editor.putInt("NREG", 1);
        }
        else
        {
            cantReg++;
            editor.putInt("NREG", cantReg);
        }
        editor.putString( ""+cantReg, "SENSOR: ACELEROMETRO--> VALORES--> X="+intent.getExtras().getString("x")+" , Y="+intent.getExtras().getString("y")+" ,Z="+intent.getExtras().getString("z")+"FUNCIONA");
        editor.apply();
    }
}
