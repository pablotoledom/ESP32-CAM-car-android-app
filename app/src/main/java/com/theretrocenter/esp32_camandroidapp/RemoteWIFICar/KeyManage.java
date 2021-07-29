package com.theretrocenter.esp32_camandroidapp.RemoteWIFICar;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

import com.theretrocenter.esp32_camandroidapp.ControlCarFragment;
import com.theretrocenter.esp32_camandroidapp.R;
import com.theretrocenter.esp32_camandroidapp.utilities.Preferences;
import com.theretrocenter.esp32_camandroidapp.utilities.SingletonListener;

public class KeyManage {
    private RemoteWIFICar remoteWIFICar = new RemoteWIFICar();
    private String lastCommand = "";
    private Boolean forwardActived = false;
    private Boolean backwardActived = false;
    private Boolean turnleftActived = false;
    private Boolean turnrightActived = false;
    private Boolean lightActived = false;
    private Boolean ligthOn = false;

    public void onKeyDown(int keyCode, KeyEvent event, Context context) {
        Preferences preferences = Preferences.getInstance(context);
        String remoteWIFICarIP = preferences.getData("RemoteWIFICarIP");
        String command = "";

        // Sensing key pressed
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if(!turnleftActived) {
                    command = "turnleft";
                    turnleftActived = true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if(!turnrightActived) {
                    command = "turnright";
                    turnrightActived = true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_BUTTON_R1:
            case KeyEvent.KEYCODE_BUTTON_R2:
                if(!forwardActived) {
                    command = "forward";
                    forwardActived = true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_BUTTON_L1:
            case KeyEvent.KEYCODE_BUTTON_L2:
                if(!backwardActived) {
                    command = "backward";
                    backwardActived = true;
                }
                break;
            case KeyEvent.KEYCODE_BUTTON_A:
            case KeyEvent.KEYCODE_SPACE:
                command = "claxonon";
                break;
            case KeyEvent.KEYCODE_BUTTON_X:
            case KeyEvent.KEYCODE_Q:
                if (!lightActived) {
                    if(ligthOn) {
                        command = "lightoff";
                        ligthOn = false;
                    } else {
                        command = "lighton";
                        ligthOn = true;
                    }

                    lightActived = true;
                }

                break;
            case KeyEvent.KEYCODE_BUTTON_START:
            case KeyEvent.KEYCODE_W:
                // Change state in singleton for execute event
                SingletonListener mySingletonListener = SingletonListener.getInstance();
                mySingletonListener.setChanged(true);
                break;
        }

        // Execute action only when the command changes
        if (command != "" && command != lastCommand) {
            remoteWIFICar.executeAction(command, remoteWIFICarIP);
            Log.i("Execute command", command);
        }

        // Set the last command
        lastCommand = command;
    }

    public void onKeyUp(int keyCode, KeyEvent event, Context context) {
        Log.i("Key UP", String.valueOf(keyCode));

        Preferences preferences = Preferences.getInstance(context);
        String remoteWIFICarIP = preferences.getData("RemoteWIFICarIP");
        String command = "";

        // Sensing key released
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if(forwardActived) {
                    command = "forward";
                } else {
                    command = "stop";
                }

                turnleftActived = false;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if(forwardActived) {
                    command = "forward";
                } else {
                    command = "stop";
                }

                turnrightActived = false;
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_BUTTON_R1:
            case KeyEvent.KEYCODE_BUTTON_R2:
                command = "stop";
                forwardActived = false;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_BUTTON_L1:
            case KeyEvent.KEYCODE_BUTTON_L2:
                if(forwardActived) {
                    command = "forward";
                } else {
                    command = "stop";
                }

                backwardActived = false;
                break;
            case KeyEvent.KEYCODE_BUTTON_A:
            case KeyEvent.KEYCODE_SPACE:
                command = "claxonoff";
                break;
            case KeyEvent.KEYCODE_BUTTON_X:
            case KeyEvent.KEYCODE_Q:
                lightActived = false;
                break;
        }

        if (command != "" && command != lastCommand) {
            remoteWIFICar.executeAction(command, remoteWIFICarIP);
            Log.i("Execute command", command);
        }

        // Set the last command
        lastCommand = command;
    }
}
