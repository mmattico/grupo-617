package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuPrincipal extends AppCompatActivity {

    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        usuario = getIntent().getStringExtra("USUARIO");
    }

    public void irATapaPantallas (View view)
    {
        Intent llamador = new Intent(this, JuegoTapaPantalla.class);
        llamador.putExtra("USUARIO", usuario);
        startActivity(llamador);

    }

    public void irARollingColor (View view)
    {
        Intent llamador = new Intent(this, JuegoRoll.class);
        llamador.putExtra("USUARIO", usuario);
        startActivity(llamador);
    }

    public void irAHistorial (View view)
    {
        Intent llamador = new Intent(this, Historial.class);
        llamador.putExtra("USUARIO", usuario);
        startActivity(llamador);
    }
}
