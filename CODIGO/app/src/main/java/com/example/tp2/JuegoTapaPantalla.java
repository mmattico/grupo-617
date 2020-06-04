package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class JuegoTapaPantalla extends AppCompatActivity {

    public SensorManager sensorManag;
    public Sensor sensor;
    public SensorEventListener sensorListener;
    public static final int MILISEGUNDOS_EN_SEGUNDO = 1000;
    private long tiempoDeInicio;
    private boolean gameOn=false;
    private boolean pantallaEstabaTapada=false;
    private int segundosTranscurridos;
    private String usuario;
    private final int NO_JUGO_ANTES=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_tapa_pantalla);
        usuario = getIntent().getStringExtra("USUARIO");
    }

    public void iniciarJuego ( View view )
    {
        ///// POR SI TOCA 2 VECES EL BOTON INICIO O QUIERE RIENICIAR EL JUEGO
        if( gameOn)
        {
            sensorManag.unregisterListener(sensorListener);
            Toast.makeText(this, "Ok reiniciemos el juego.", Toast.LENGTH_SHORT).show();
        }
        //// SE BUSCA EL SENSOR DE PROXIMIDAD

        sensorManag=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensor= sensorManag.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        /// SIN NO HAY SENSOR DE PROXIMIDAD SE LO INFORMA CON UN MENSAJE Y SE SALE
        if(sensor == null)
        {
            Toast.makeText(this, "No se pudo encontrar un sensor de proximidad, el cual es necesario para jugar.", Toast.LENGTH_SHORT);
            return;
        }
        //// SE INFORMA QUE ES LO QUE SE DEBE HACER CUANDO HAY CAMBIOS EN EL SENSOR DE PROXIMIDAD
        sensorListener= new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                //// SI EL SENSOR PUDO DETECTAR UN OBJETO APROXIMANDOSE
                if( event.values[0] < sensor.getMaximumRange() )
                {
                    //// SI LA PANTALLA NO FUE TAPADA ANTERIORMENTE -> GUARDO EL MOMENTO DE INICIO DEL JUEGO
                    if( !pantallaEstabaTapada )
                    {
                        tiempoDeInicio= SystemClock.uptimeMillis();
                        pantallaEstabaTapada=true;
                        getWindow().getDecorView().setBackgroundColor(Color.RED);
                    }
                }
                else
                {
                    //// SI LA PANTALLA ESTABA TAPADA Y AHORA NO LO ESTA --> CALCULO LOS SEGUNDOS TRANSCURRIDOS Y DESTRUYO EL LISTENER DEL SENSOR
                    if( pantallaEstabaTapada )
                    {
                        segundosTranscurridos= pasarMilisegundoASegundo(SystemClock.uptimeMillis() - tiempoDeInicio);
                        pantallaEstabaTapada=false;
                        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                        sensorManag.unregisterListener(sensorListener);

                    }
                }
                guardarInfoEnSharedPreference(event.values[0]);
            }
            /// NO ES NECESARIO TOCARLO PERO LA IMPLEMENTACION ME PIDE QUE POR LO MENOS LO DECLARE
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        /// REGISTRO EL LISTENER PARA QUE EMPIESE A ESCUCHAR
        sensorManag.registerListener(sensorListener, sensor, MILISEGUNDOS_EN_SEGUNDO/6);
        //// FLAG PARA SABER QUE SE ESTA PREPARADO PARA JUGAR ///////
        gameOn=true;
    }

    /// AL TOCAR EL BOTON DE RESULTADO ME INFORMA SI GANE O PERDI Y RESETEA LOS VALORES POR DEFAULT
    public void calcularResultado ( View view )
    {
        
        String strPrediccion= ( (TextView) findViewById(R.id.txtPredSeg) ).getText().toString();
        if( strPrediccion.length() == 0 )
            Toast.makeText(this, "Debe ingresar su prediccion para saber el resultado.", Toast.LENGTH_SHORT);
        else
        {
            try {
                int numPred= Integer.parseInt(strPrediccion);
                if( numPred == segundosTranscurridos )
                    Toast.makeText(this, "!!!!! GANASTE !!!!!!!!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "SEGUI PARTICIPANDO. LA RESPUESTA CORRECTA ERA: "+segundosTranscurridos, Toast.LENGTH_LONG).show();
                volverValoresADefault();
            }catch ( Exception x)
            {
                Toast.makeText(this, "TENES QUE INGRESAR UN NUMEERO ENTERO", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /// PASA DE MILISEGUNDOS A SEGUNDOS  LA ENTRADA
    private int pasarMilisegundoASegundo( long milisegundos )
    {
        return (int)(milisegundos/MILISEGUNDOS_EN_SEGUNDO);
    }

    /// RESTABLECE LOS VALORES DE CAMPOS A DEFAULT COMO ASI TAMBIEN VARIABLES INTERNAS DEL JUEGO
    private void volverValoresADefault( )
    {
        sensorManag.unregisterListener(sensorListener);
        gameOn=false;
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        pantallaEstabaTapada=false;
        ((TextView)findViewById(R.id.txtPredSeg)).setText("");
        segundosTranscurridos=0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        volverValoresADefault();
    }

    private void guardarInfoEnSharedPreference ( float valorDelSensor  )
    {
        SharedPreferences preferences = getSharedPreferences( usuario, Context.MODE_PRIVATE);
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
        editor.putString( ""+cantReg, "SENSOR: PROXIMIDAD--> VALOR="+valorDelSensor);
        editor.commit();
    }
}
