package com.example.tp2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tp2.retrofit.RequestRegistroLog;
import com.example.tp2.retrofit.ServicePOST;

public class MainActivity extends AppCompatActivity {

    private String password;
    private String email;
    Intent iServiceEvento;

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
        /// GUARDO LOS VALORES DE LOS COMPONENTES///////////////////////
        cargar_campos();
        ////  VALIDO SI LOS DATOS INGRESADOS SON CORRECTOS
        if(  validarCampos()  ) {
            /// VALIDO SI HAY INTERNET /////////////////////
            if (validar_internet()) {
                /// CARGO EL MAIL Y CONTRASEÑA EN UNA ESTRUCTURA
                RequestRegistroLog userData = new RequestRegistroLog();
                userData.setEmail(this.email);
                userData.setPassword(this.password);

                ServicePOST comunicacionApiRest = new ServicePOST(this);
                comunicacionApiRest.enviarLogin(userData, MainActivity.this);

            }
        }

    }

    private boolean validarCampos() {

        if(  password.length()== 0 )
        {
            Toast.makeText(this, "Complete el campo CONTRASEÑA", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(  email.length() == 0 )
        {
            Toast.makeText(this, "Complete la el campo Email", Toast.LENGTH_SHORT).show();
            return false;
        }

        return  true;
    }


    private void cargar_campos()
    {
        password = ((EditText)findViewById(R.id.txtPass)).getText().toString().trim();
        email =  ((EditText)findViewById(R.id.txtEmailPrincipal)).getText().toString().trim();
    }

    private boolean validar_internet ()
    {
        ConnectivityManager admin = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = admin.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());

    }

    public void iniciar_Servicio_Reg_Eventos_E_Ir_Menu_Principal() {

        ServicePOST comunicacionApiRest = new ServicePOST(this);
        comunicacionApiRest.registrarEvento("el usuario "+email+" ha hecho login", "Login");

        Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
        intent.putExtra("USUARIO", email);
        startActivity(intent);
    }
}
