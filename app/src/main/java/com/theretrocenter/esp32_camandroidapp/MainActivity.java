package com.theretrocenter.esp32_camandroidapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;

import com.theretrocenter.esp32_camandroidapp.RemoteWIFICar.RemoteWIFICar;
import com.theretrocenter.esp32_camandroidapp.utilities.Preferences;

public class MainActivity extends AppCompatActivity {

    ProgressDialog dialog;
    private String lastCommand = "";
    private String remoteWIFICarIP = "";
    private RemoteWIFICar remoteWIFICar = new RemoteWIFICar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set permissions for non secure http calls
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Set Instance preference
        Preferences preferences = Preferences.getInstance(MainActivity.this);

        // Set main layout
        setContentView(R.layout.activity_main);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String command = "";

         switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                command = "turnleft";
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                command = "turnright";
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                command = "forward";
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                command = "backward";
                break;
            case KeyEvent.KEYCODE_BUTTON_A:
                 command = "claxonon";
                 break;
            case KeyEvent.KEYCODE_BUTTON_X:
                 command = "lighton";
                 break;
            case KeyEvent.KEYCODE_BUTTON_B:
                 command = "lightoff";
                 break;
            default:
                command = "stop";
                break;
         }

        // Execute action only when the command changes
        if (command != lastCommand) {
            remoteWIFICar.executeAction(command, remoteWIFICarIP);
            Log.i("Execute command", command);
        }

        // Set the last command
        lastCommand = command;

        Log.i("XXXXXXXX", String.valueOf(keyCode));

        return super.onKeyUp(keyCode, event);
    }
}