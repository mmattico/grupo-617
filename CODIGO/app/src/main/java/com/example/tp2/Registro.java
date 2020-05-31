package com.example.tp2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

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
            ///// AGUANTE CHIKA
                HttpURLConnection urlConnection = null;

                try {

                    DatosUsr body = new DatosUsr( this.nombre, this.apellido, Integer.valueOf(this.dni), this.email, this.password1, Integer.valueOf(this.comision), Integer.valueOf(this.grupo));
                   // URL urlRegistrar = new URL("https://so-unlam.net.ar/api/api/register");
                    Gson gson = new Gson();

                    String json = gson.toJson(body);

                  //  JSONObject jsonWS = new JSONObject();
                   // jsonWS.put("url", urlRegistrar);
                   // jsonWS.put("body", json);
                    JSONObject jsonWS = new JSONObject(json);
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    urlConnection = (HttpURLConnection) new URL("http://so-unlam.net.ar/api/api/register").openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setConnectTimeout(5000);

                    DataOutputStream wr = new DataOutputStream((urlConnection.getOutputStream()));
                    wr.write(jsonWS.toString().getBytes("UTF-8"));
                    wr.flush();
                    wr.close();

                    String response = urlConnection.getResponseMessage();
                    //getResponse(urlConnection);
            } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


    }
    }

    private boolean validar_internet ()
    {
        ConnectivityManager admin = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = admin.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());

    }

    public static StringBuffer getResponse(HttpURLConnection urlConnection) throws IOException {
        InputStream inStream = null;
        inStream = urlConnection.getInputStream();
        BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = bReader.readLine()) != null) {
            response.append(inputLine);
        }

        bReader.close();
        inStream.close();
        return response;
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
