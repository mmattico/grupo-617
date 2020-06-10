package com.example.tp2;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.tp2.retrofit.ServicePOST;

import java.util.LinkedList;
import java.util.Queue;

public class ServiceEvento extends IntentService {

    static boolean ejecutando = true;
    static Queue<String> descripciones = new LinkedList<String>(), type_events = new LinkedList<String>();

    public ServiceEvento() {
        super("ServiceEvento");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        while (ejecutando) {

            while (!descripciones.isEmpty() && !type_events.isEmpty()) {

                String descripcion = descripciones.poll();
                String type_event = type_events.poll();

                ServicePOST comunicacionApiRest = new ServicePOST();
                comunicacionApiRest.registrarEvento(descripcion, type_event);

            }
        }
        ejecutando = true;
    }

    public static void agregarEvento(String descripcion, String type_event) {
        descripciones.add(descripcion);
        type_events.add(type_event);
    }

    public static void detener() {
        ejecutando = false;
    }
}
