package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Registro extends AppCompatActivity {
    //// VARIABLES GLOBALES //////////////////////////////////////////////
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String password1;
    private String password2;
    private String comision;
    private String grupo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    public void registrarme ( View view)
    {
        /// GUARDO LOS VALORES DE LOS COMPONENTES///////////////////////
        cargar_campos();
        ////  VALIDO SI LOS DATOS INGRESADOS SON CORRECTOS
        if(  validarCampos()  )
        {
            /// VALIDO SI HAY INTERNET /////////////////////
            if( validar_internet() )
            {

            }
        }


    }

    private boolean validar_internet ()
    {
        ConnectivityManager admin = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = admin.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());

    }


    private void cargar_campos()
    {
        nombre = ((EditText)findViewById(R.id.txtNom)).getText().toString().trim();
        apellido = ((EditText)findViewById(R.id.txtApe)).getText().toString().trim();
        email = ((EditText)findViewById(R.id.txtEmail)).getText().toString().trim();
        password1 = ((EditText)findViewById(R.id.txtPass1)).getText().toString().trim();
        password2 = ((EditText)findViewById(R.id.txtPass2)).getText().toString().trim();
        dni =  ((EditText)findViewById(R.id.txtDni)).getText().toString().trim();
        comision = ((EditText)findViewById(R.id.txtComision)).getText().toString().trim();
        grupo = ((EditText)findViewById(R.id.txtGrupo)).getText().toString().trim();
    }

    boolean validarCampos()
    {
        /// Chequeo Nombre no vacio
        if(  nombre.length()== 0 )
        {
            Toast.makeText(this, "Complete el campo Nombre", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(  apellido.length()== 0 )
        {
            Toast.makeText(this, "Complete el campo Apellido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(  email.length()== 0 )
        {
            Toast.makeText(this, "Complete el campo Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(  password1.length()== 0 )
        {
            Toast.makeText(this, "Complete el campo CONTRASEÑA", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(  password2.length()== 0 )
        {
            Toast.makeText(this, "Complete la repeticion de contraseña", Toast.LENGTH_SHORT).show();
            return false;
        }
        if( ! password1.equals( password2) )
        {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(  dni.length() == 0 )
        {
            Toast.makeText(this, "Complete la el campo DNI", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(  comision.length() == 0 )
        {
            Toast.makeText(this, "Complete la el campo DNI", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(  grupo.length() == 0 )
        {
            Toast.makeText(this, "Complete la el campo DNI", Toast.LENGTH_SHORT).show();
            return false;
        }
        return  true;
    }


}
