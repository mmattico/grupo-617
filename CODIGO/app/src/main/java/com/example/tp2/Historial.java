package com.example.tp2;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Historial extends AppCompatActivity {

    private String usuario;
    private final int NO_JUGO_ANTES = 0;
    private Handler handCarHist;
    private final int MSJ_HISTORIAL = 1;
    private  CargarHistorial hiloCarg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        usuario = getIntent().getStringExtra("USUARIO");
        //// VUELVO AL TEXTVIEW SCROLLEABLE
        ((TextView) (findViewById(R.id.txtListSensorEv))).setMovementMethod(new ScrollingMovementMethod());
        ((TextView) (findViewById(R.id.txtListSensorEv))).setText("Cargando Historial");
        /// INICIO EL HILO QUE ME CARGA EL HISTORIAL EN UN STRING
        hiloCarg=new CargarHistorial();
        hiloCarg.start();
        //// GENERO EL HANDLER QUE SE OCUPARA DE SETTEAR EL HISTORIAL CUANDO YA SE HALLA GENERADO EL HISTORIAL
        handCarHist = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSJ_HISTORIAL) {
                    ((TextView) (findViewById(R.id.txtListSensorEv))).setText((String) msg.obj);
                }
            }
        };

    }

    private class CargarHistorial extends Thread {
        @Override
        public void run() {
            String historial = "";
            super.run();
            /// ACCEDO AL SHARED PREFERENCE Y CARGO LOS REGISTROS ( LOS DATOS ESTAN EN FORMATO "KEY", "VALUE" EN SHEAREDPREF Y LA KEY ES EL NUMERO DE REGISTRO )
            SharedPreferences preferences = getSharedPreferences(usuario, Context.MODE_PRIVATE);
            int cantReg = preferences.getInt("NREG", NO_JUGO_ANTES);
            if (cantReg == NO_JUGO_ANTES)
                historial = "NO HAY REGISTROS TODAVIA";
            else {
                for (int i = cantReg; i >= 1; i--)
                    historial += "\n" + preferences.getString("" + i, "");
            }
            /// GENERO EL MENSAGE PARA ENVIAR AL HANDLER /////////////////////
            Message mensaje = new Message();
            mensaje.what = MSJ_HISTORIAL;
            mensaje.obj = historial;
            Historial.this.handCarHist.sendMessage(mensaje);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try
        {
            hiloCarg.stop();
        }catch (Exception ex)
        {

        }

    }
}
