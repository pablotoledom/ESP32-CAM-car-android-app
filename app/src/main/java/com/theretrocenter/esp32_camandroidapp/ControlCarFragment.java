package com.theretrocenter.esp32_camandroidapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.longdo.mjpegviewer.MjpegView;
import com.theretrocenter.esp32_camandroidapp.RemoteWIFICar.RemoteWIFICar;
import com.theretrocenter.esp32_camandroidapp.utilities.Preferences;
import io.github.controlwear.virtual.joystick.android.JoystickView;

public class ControlCarFragment extends Fragment {

    private MainActivity mainActivity = new MainActivity();
    private RemoteWIFICar remoteWIFICar = new RemoteWIFICar();
    private Preferences preferences = Preferences.getInstance(mainActivity);
    private MjpegView viewer;
    private String lastCommand = "";
    private Boolean ligthOn = false;
    private Boolean forwardActived = false;

    // Get saved preferences
    private String carUIControl = preferences.getData("CarUIControl");
    private String remoteWIFICarIP = preferences.getData("RemoteWIFICarIP");
    private String sawUserGuide = preferences.getData("SawUserGuide");

    public void moveCar(Integer x, Integer y) {
        String command = "";

        // Determine movement based on the index of movement of axes
        if (x > 75) {
            command = "turnright";
        } else if (x < 25) {
            command = "turnleft";
        } else if (y > 75) {
            command = "backward";
        } else if (y < 25) {
            command = "forward";
        } else {
            command = "stop";
        }

        // Execute action only when the command changes
        if (command != lastCommand) {
            remoteWIFICar.executeAction(command, remoteWIFICarIP);
            Log.i("Execute command", command);
        }

        // Set the last command
        lastCommand = command;
    }

    public void showCam() {
        // Set jpeg video url to viewer
        String mjpegSource = "http://" + remoteWIFICarIP + ":81/stream";
        //String mjpegSource = "http://" + remoteWIFICarIP + "/stream/index.mjpeg";
        Log.i("Execute command", mjpegSource);
        viewer = (MjpegView) getView().findViewById(R.id.mjpegview);
        viewer.setMode(MjpegView.MODE_FIT_WIDTH);
        viewer.setAdjustHeight(true);
        viewer.setSupportPinchZoomAndPan(true);
        viewer.setUrl(mjpegSource);
        viewer.startStream();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
            // Stop viewer when rotate display
            viewer.stopStream();
        } catch(Exception ex) {
            //ex.printStackTrace();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Determine UI control layout
        if (carUIControl.equals("joystick")) {
            // Inflate the joystick layout for this fragment
            return inflater.inflate(R.layout.controlcar_joystick, container, false);
        }

        // Inflate the buttons layout for this fragment
        return inflater.inflate(R.layout.controlcar_buttons, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load video jpeg
        showCam();

        if (carUIControl.equals("joystick")) {
            // Execute car movements for a Joystick event
            final JoystickView joystickRight = (JoystickView) getView().findViewById(R.id.joystickView_right);
            joystickRight.setOnMoveListener(new JoystickView.OnMoveListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onMove(int angle, int strength) {
                    moveCar(joystickRight.getNormalizedX(), joystickRight.getNormalizedY());
                }
            });
        } else {
            // Execute car movements for a buttons events
            view.findViewById(R.id.upButton).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // Pressed
                        remoteWIFICar.executeAction("forward", remoteWIFICarIP);
                        v.setBackgroundResource(R.drawable.ic_circle);
                        forwardActived = true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        // Released
                        v.setBackgroundResource(0);
                        remoteWIFICar.executeAction("stop", remoteWIFICarIP);
                        forwardActived = false;
                    }
                    return true;
                }
            });

            view.findViewById(R.id.downButton).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // Pressed
                        remoteWIFICar.executeAction("backward", remoteWIFICarIP);
                        v.setBackgroundResource(R.drawable.ic_circle);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        // Released
                        v.setBackgroundResource(0);
                        remoteWIFICar.executeAction("stop", remoteWIFICarIP);
                    }
                    return true;
                }
            });

            view.findViewById(R.id.leftButton).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // Pressed
                        remoteWIFICar.executeAction("turnleft", remoteWIFICarIP);
                        v.setBackgroundResource(R.drawable.ic_circle);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        // Released
                        if (forwardActived) {
                            remoteWIFICar.executeAction("forward", remoteWIFICarIP);
                        } else {
                            v.setBackgroundResource(0);
                            remoteWIFICar.executeAction("stop", remoteWIFICarIP);
                        }
                    }
                    return true;
                }
            });

            view.findViewById(R.id.rightButton).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // Pressed
                        remoteWIFICar.executeAction("turnright", remoteWIFICarIP);
                        v.setBackgroundResource(R.drawable.ic_circle);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        // Released
                        if (forwardActived) {
                            remoteWIFICar.executeAction("forward", remoteWIFICarIP);
                        } else {
                            v.setBackgroundResource(0);
                            remoteWIFICar.executeAction("stop", remoteWIFICarIP);
                        }
                    }
                    return true;
                }
            });
        }

        view.findViewById(R.id.claxon).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Pressed
                    v.setBackgroundResource(R.drawable.ic_megaphone_pressed);
                    remoteWIFICar.executeAction("claxonon", remoteWIFICarIP);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Released
                    v.setBackgroundResource(R.drawable.ic_megaphone_opacity);
                    remoteWIFICar.executeAction("claxonoff", remoteWIFICarIP);
                }
                return true;
            }
        });

        view.findViewById(R.id.light_cam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ligthOn) {
                    remoteWIFICar.executeAction("lightoff", remoteWIFICarIP);
                    view.setBackgroundResource(0);
                    ligthOn = false;
                } else {
                    remoteWIFICar.executeAction("lighton", remoteWIFICarIP);
                    view.setBackgroundResource(R.drawable.ic_light_on);
                    ligthOn = true;
                }
            }
        });

        view.findViewById(R.id.config).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Go to configuration fragment layout
                    NavHostFragment.findNavController(ControlCarFragment.this)
                            .navigate(R.id.action_ControlCarFragment_to_ConfigurationFragment);

                    // Stop the viewer
                    viewer.stopStream();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }
}