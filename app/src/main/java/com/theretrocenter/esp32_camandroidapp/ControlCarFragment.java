package com.theretrocenter.esp32_camandroidapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
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
import com.theretrocenter.esp32_camandroidapp.utilities.IPAddress;
import com.theretrocenter.esp32_camandroidapp.utilities.Preferences;
import com.theretrocenter.esp32_camandroidapp.utilities.SingletonListener;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class ControlCarFragment extends Fragment {
    private MainActivity mainActivity = new MainActivity();
    private RemoteWIFICar remoteWIFICar = new RemoteWIFICar();
    private Preferences preferences = Preferences.getInstance(mainActivity);
    private MjpegView viewer;
    private String lastCommand = "";
    private Boolean ligthOn = false;
    private Boolean forwardActived = false;
    private Context context;
    private ProgressDialog loading;
    private Boolean isFindingCar = false;

    // Get saved preferences
    private String carUIControl = preferences.getData("CarUIControl");
    private String remoteWIFICarIP = preferences.getData("RemoteWIFICarIP");

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
        Log.i("Execute command", mjpegSource);
        viewer = (MjpegView) getView().findViewById(R.id.mjpegview);
        viewer.setMode(MjpegView.MODE_FIT_WIDTH);
        viewer.setAdjustHeight(true);
        viewer.setSupportPinchZoomAndPan(true);
        viewer.setUrl(mjpegSource);
        viewer.startStream();
    }

    public void connectWithCar() {
        if (!isFindingCar) {
            // Prevent a second search instance from running
            isFindingCar = true;

            // Loading dialog while finding for IP
            loading = new ProgressDialog(context);
            loading.setCancelable(true);
            loading.setMessage(getString(R.string.loadingTextFindIP));
            loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loading.show();

            // Timeout one second
            new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                public void run() {
                    try {
                        // Get local IP
                        IPAddress ipAddress = new IPAddress();
                        String localIP = ipAddress.getLocalIP(true);

                        // Get Remote WIFI car IP
                        RemoteWIFICar remoteWifiCar = new RemoteWIFICar();
                        remoteWIFICarIP = remoteWifiCar.finderIP(localIP, mainActivity);
                        Log.i("Remote WIFI Car IP", remoteWIFICarIP);

                        // Load video jpeg
                        showCam();

                        // Hidde loading message
                        loading.hide();
                        loading.dismiss();
                        isFindingCar = false;

                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
            },1000);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
            // Stop viewer when rotate display
            viewer.stopStream();
            // Dismiss dialog when rotate display
            loading.hide();
            loading.dismiss();
        } catch(Exception ex) {
            //ex.printStackTrace();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Stop viewer when rotate display
        viewer.stopStream();
        // Dismiss dialog when rotate display
        loading.hide();
        loading.dismiss();
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        // Find car in the network
        connectWithCar();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        context = container.getContext();

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

        // Set true, for stop propagation on keyDown and keyUp events in human interfaces
        preferences.saveData("controlCar", "true");

        // Find car in the network
        connectWithCar();

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
                            v.setBackgroundResource(0);
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
                            v.setBackgroundResource(0);
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

        SingletonListener mySingletonListener = SingletonListener.getInstance();
        mySingletonListener.setListener(new SingletonListener.ChangeListener() {
            @Override
            public void onChange() {
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