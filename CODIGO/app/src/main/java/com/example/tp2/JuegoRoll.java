package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class JuegoRoll extends AppCompatActivity {

    private SensorManager sensorManag;
    private Sensor sensor;
    private SensorEventListener sensorListener;
    private boolean gameOn = false;
    private int NINGUNO = -1;
    private int PRIM_CUAD = 0;
    private int SEG_CUAD = 1;
    private int TERC_CUAD = 2;
    private int CUART_CUAD = 3;
    private int cuadranteAnterior;
    private int cuadranteActual;
    public static final int MILISEGUNDOS_EN_SEGUNDO = 1000;
    private int contadorDeCambiosDeColor = 0;
    private long tiempoDeInicio;
    private final int TIEMPO_LIMITE_SEG= 15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_roll);
    }

    ///// ACCIONES DE LOS BOTONES /////////////////////////////////////////////

    public void iniciarJuego(View view) {
        ///// POR SI TOCA 2 VECES EL BOTON INICIO O QUIERE RIENICIAR EL JUEGO

        if( gameOn)
        {
            volverValoresADefault();
            Toast.makeText(this, "Ok reiniciamos el juego.", Toast.LENGTH_SHORT).show();
        }

        //// SE BUSCA EL ACELEROMETRO //////////////////////////////////////
        tiempoDeInicio= SystemClock.uptimeMillis();
        sensorManag = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManag.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        /// SIN NO HAY ACELEROMETRO SE LO INFORMA CON UN MENSAJE Y SE SALE
        if (sensor == null) {
            Toast.makeText(this, "No se pudo encontrar un acelerometro, el cual es necesario para jugar.", Toast.LENGTH_SHORT);
            return;
        }

        //// SE GENERA QUE ACCIONES SE DEBE HACER CUANDO HAY CAMBIOS EN EL ACELEROMETRO
        sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x= event.values[0];
                float z= event.values[2];
                int cuadranteCalcu = calcularCuadrante(x, z);
                if( cuadranteCalcu == NINGUNO)
                {
                    return;
                }
                cuadranteActual= cuadranteCalcu;
                //// LA PRIMERA LECTUARA DEL SENSOR COMIENZA EL JUEGO ///////////
                if( gameOn == false )
                {
                    cuadranteAnterior =cuadranteCalcu;
                    gameOn= true;
                    setPantallaDeColor();
                    tiempoDeInicio= SystemClock.uptimeMillis();
                    return;
                }
                ////// SI SE ACABO EL TIEMPO //////////////////
                long tiempoDeJuegoEnSegundos= (SystemClock.uptimeMillis()- tiempoDeInicio)/MILISEGUNDOS_EN_SEGUNDO;
                if( tiempoDeJuegoEnSegundos >= TIEMPO_LIMITE_SEG  )
                {
                    getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                    Toast.makeText(JuegoRoll.this, "SE HAN CUMPLIDO LOS 15 SEGUNDOS. CUANTAS VUELTAS DIO?", Toast.LENGTH_LONG).show();
                    sensorManag.unregisterListener(sensorListener);
                    return;
                }
                ///////
                if( cuadranteAnterior != cuadranteActual )
                {
                    cuadranteAnterior = cuadranteActual;
                    contadorDeCambiosDeColor++;
                    setPantallaDeColor();
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManag.registerListener(sensorListener, sensor, MILISEGUNDOS_EN_SEGUNDO/32);
    }

    public void calcularResultado ( View view )
    {
        if( gameOn== false )
        {
            Toast.makeText(JuegoRoll.this, "TODAVIA NO HA INICIADO EL JUEGO", Toast.LENGTH_SHORT).show();
            return;
        }
        long tiempoDeJuegoEnSegundos= (SystemClock.uptimeMillis()- tiempoDeInicio)/MILISEGUNDOS_EN_SEGUNDO;
        if( tiempoDeJuegoEnSegundos < TIEMPO_LIMITE_SEG  )
        {
            Toast.makeText(JuegoRoll.this, "TODAVIA NO SE HAN CUMPLIDO LOS 15 SEGUNDOS", Toast.LENGTH_LONG).show();
            return;
        }

        String cadPredCambios = ( (TextView)findViewById(R.id.txtCamColPredic) ).getText().toString();
        if( cadPredCambios.length() == 0)
        {
            Toast.makeText(JuegoRoll.this, "NO HA INGRESADO LA CANTIDAD DE CAMBIOS DE COLORES", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
              int predCantCambios= Integer.parseInt(cadPredCambios);
              if( predCantCambios == contadorDeCambiosDeColor)
                  Toast.makeText(JuegoRoll.this, "!!!!!! HA GANADO !!!!!!", Toast.LENGTH_LONG).show();
              else
                  Toast.makeText(JuegoRoll.this, "HA PERDIDO. EL CELULAR HA CAMBIADO "+ contadorDeCambiosDeColor +" VECES DE COLOR", Toast.LENGTH_LONG).show();
              volverValoresADefault();
        }catch (Exception x)
        {
            Toast.makeText(JuegoRoll.this, "DEBE INGRESAR UN NUMERO ENTERO DE CAMBIOS DE COLOR", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        volverValoresADefault();
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

    }

    //// FUNCIONES COMPLEMENTARIAS ///////////////////////////////////////////////
    private int calcularCuadrante( float x, float z )
    {
        if( esta_prim_cuad( x, z ) )
        {
            return PRIM_CUAD;
        }
        if( esta_seg_cuad( x, z ) )
        {
            return SEG_CUAD;
        }
        if( esta_tercer_cuad( x, z ) )
        {
            return TERC_CUAD;
        }
        if( esta_cuarto_cuad( x, z ) )
            return CUART_CUAD;
        return NINGUNO;
    }

    private void setPantallaDeColor()
    {
        if( cuadranteActual== PRIM_CUAD )
            getWindow().getDecorView().setBackgroundColor(Color.GREEN);
        else if( cuadranteActual== SEG_CUAD )
            getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
            else if( cuadranteActual== TERC_CUAD )
            getWindow().getDecorView().setBackgroundColor(Color.RED);
                else if ( cuadranteActual == CUART_CUAD  )
                        getWindow().getDecorView().setBackgroundColor(Color.BLUE);

    }


    private void volverValoresADefault ()
    {
        gameOn=false;
        contadorDeCambiosDeColor =0;
        sensorManag.unregisterListener(sensorListener);
        ((TextView) findViewById(R.id.txtCamColPredic) ).setText("");
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
    }


    private boolean esta_prim_cuad ( float x, float z )
    {
        return  x<0 && z>0;
    }
    private boolean esta_seg_cuad ( float x, float z )
    {
        return  x<0 && z<0;
    }
    private boolean esta_tercer_cuad ( float x, float z )
    {
        return  x>0 && z<0;
    }
    private boolean esta_cuarto_cuad ( float x, float z )
    {
        return  x>0 && z>0;
    }

}
