package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
    }

    public void irATapaPantallas (View view)
    {
        Intent llamador = new Intent(this, JuegoTapaPantalla.class);
        startActivity(llamador);
    }

    public void irARollingColor (View view)
    {
        Intent llamador = new Intent(this, JuegoRoll.class);
        startActivity(llamador);
    }

    public void irAHistorial (View view)
    {
        Intent llamador = new Intent(this, Historial.class);
        startActivity(llamador);
    }
}
