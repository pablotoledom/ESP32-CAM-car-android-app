package com.theretrocenter.esp32_camandroidapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.view.KeyEvent;

import com.theretrocenter.esp32_camandroidapp.RemoteWIFICar.KeyManage;
import com.theretrocenter.esp32_camandroidapp.utilities.Preferences;
import com.theretrocenter.esp32_camandroidapp.utilities.SingletonListener;

public class MainActivity extends AppCompatActivity {
    private KeyManage keyManage = new KeyManage();
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set permissions for non secure http calls
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Set Instance preference
        preferences = Preferences.getInstance(MainActivity.this);

        SingletonListener.initInstance();

        // Set main layout
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String isControlCar = preferences.getData("controlCar");

        // Prevents on execute key events outside control car view
        if (isControlCar.equals("true")) {
            // Send key events to KeyManage car service
            keyManage.onKeyDown(keyCode, event, MainActivity.this);
            return true;
        }

        // Propagate events in other views
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        String isControlCar = preferences.getData("controlCar");

        // Prevents on execute key events outside control car view
        if (isControlCar.equals("true")) {
            // Send key events to KeyManage car service
            keyManage.onKeyUp(keyCode, event, MainActivity.this);
            return true;
        }

        // Propagate events in other views
        return super.onKeyUp(keyCode, event);
    }
}