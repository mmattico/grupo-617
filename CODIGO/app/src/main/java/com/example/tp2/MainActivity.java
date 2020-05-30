package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void irRegistrarme (View view)
    {
        Intent llamador = new Intent(this, Registro.class);
        startActivity(llamador);
    }


    public void loguearme ( View view )
    {

    }

    private boolean chequearInternet ()
    {
        return true;
    }

}
