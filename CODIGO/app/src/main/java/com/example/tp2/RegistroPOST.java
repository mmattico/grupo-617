package com.example.tp2;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RegistroPOST extends IntentService {



    public RegistroPOST() {
        super("RegistroGET");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        HttpURLConnection urlConnection = null;

        try {
            JSONObject jsonWS = new JSONObject(intent.getExtras().getString("json"));

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

            urlConnection.connect();

            String response = urlConnection.getResponseMessage();

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
