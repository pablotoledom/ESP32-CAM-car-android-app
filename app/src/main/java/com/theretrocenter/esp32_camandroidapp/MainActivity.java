package com.theretrocenter.esp32_camandroidapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;

import com.theretrocenter.esp32_camandroidapp.RemoteWIFICar.RemoteWIFICar;
import com.theretrocenter.esp32_camandroidapp.utilities.IPAddress;

public class MainActivity extends AppCompatActivity {

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Set permissions for non secure http calls
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Loading dialog while finding for IP
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Searching your Remote WIFI Car on the network. \n\nPlease wait a few minutes.");
            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog.show();

            // Timeout one second
            new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                public void run() {
                    try {
                        // Get local IP
                        IPAddress ipAddress = new IPAddress();
                        String localIP = ipAddress.getLocalIP(true);

                        // Get Remote WIFI car IP
                        RemoteWIFICar remoteWifiCar = new RemoteWIFICar();
                        String WIFICarIP = remoteWifiCar.finderIP(localIP, dialog, MainActivity.this);
                        Log.i("Remote WIFI Car IP", WIFICarIP);

                        // Set main layout
                        setContentView(R.layout.activity_main);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
            },1000);

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
            // Dismiss dialog when rotate display
            dialog.dismiss();
        } catch(Exception ex) {
            //ex.printStackTrace();
        }
        super.onSaveInstanceState(outState);
    }
}