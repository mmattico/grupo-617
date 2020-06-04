package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class Historial extends AppCompatActivity {

    private String usuario;
    private String historial="";
    private final int NO_JUGO_ANTES=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        usuario = getIntent().getStringExtra("USUARIO");
        cargarHistorial();
    }

    private void cargarHistorial() {

        SharedPreferences preferences = getSharedPreferences( usuario, Context.MODE_PRIVATE);
        int cantReg=preferences.getInt("NREG", NO_JUGO_ANTES );
        if ( cantReg== NO_JUGO_ANTES)
        {
            ((TextView)(findViewById(R.id.txtListSensorEv))).setText("NO HAY REGISTROS TODAVIA");
        }
        else
        {
            for (int i=1 ; i<= cantReg ; i++ )
            {
                historial+="\n"+preferences.getString(""+i, "NO PUDE LEER");
            }
            ((TextView)(findViewById(R.id.txtListSensorEv))).setText( historial);
        }
    }
}
