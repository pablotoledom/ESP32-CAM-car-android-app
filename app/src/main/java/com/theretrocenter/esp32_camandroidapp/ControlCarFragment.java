package com.theretrocenter.esp32_camandroidapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.longdo.mjpegviewer.MjpegView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class ControlCarFragment extends Fragment {

    private MainActivity MainActivity = new MainActivity();
    private MjpegView viewer;
    private Boolean ligthOn = false;
    private String lastCommand = "";

    Preferences preferences = Preferences.getInstance(MainActivity);

    private void carExecuteAction(String command) {
        // Get IP
        //Preferences preferences = Preferences.getInstance(MainActivity);
        String remoteWIFICarIP = preferences.getData("RemoteWIFICarIP");

        // Create http cliient object to send request to server
        HttpClient Client = new DefaultHttpClient();

        // Create URL string
        String URL = "http://" + remoteWIFICarIP + "/control?command=car&value=" + command;

        Log.i("httpget", URL);

        try {
            String SetServerString = "";

            // Create Request to server and get response
            HttpGet httpget = new HttpGet(URL);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            SetServerString = Client.execute(httpget, responseHandler);

            // Show response on activity
            Log.i("httpget", SetServerString);

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void moveCar(Integer x, Integer y) {
        String command = "";

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

        if (command != lastCommand) {
            carExecuteAction(command);
            Log.i("command XXXXXXXX", command);
        }

        lastCommand = command;

    }

    public void showCam() {
        //Preferences preferences = Preferences.getInstance(MainActivity);
        String RemoteWIFICarIP = preferences.getData("RemoteWIFICarIP");

        viewer = (MjpegView) getView().findViewById(R.id.mjpegview);
        viewer.setMode(MjpegView.MODE_FIT_WIDTH);
        viewer.setAdjustHeight(true);
        viewer.setSupportPinchZoomAndPan(true);
        viewer.setUrl("http://" + RemoteWIFICarIP + ":81/stream");
        viewer.startStream();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
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
        String carUIControl = preferences.getData("CarUIControl");

        if (carUIControl.equals("joystick")) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.controlcar_joystick, container, false);
        }

        return inflater.inflate(R.layout.controlcar_buttons, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String carUIControl = preferences.getData("CarUIControl");

        view.findViewById(R.id.light).setBackgroundResource(R.drawable.ic_light_off);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        showCam();

        if (carUIControl.equals("joystick")) {
            final JoystickView joystickRight = (JoystickView) getView().findViewById(R.id.joystickView_right);
            joystickRight.setOnMoveListener(new JoystickView.OnMoveListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onMove(int angle, int strength) {
                    moveCar(joystickRight.getNormalizedX(), joystickRight.getNormalizedY());
                }
            });
        } else {
            view.findViewById(R.id.upButton).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // Pressed
                        carExecuteAction("forward");
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        // Released
                        // v.setBackgroundResource(R.drawable.ic_megaphone_opacity);
                        carExecuteAction("stop");
                    }
                    return true;
                }
            });

            view.findViewById(R.id.downButton).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // Pressed
                        carExecuteAction("backward");
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        // Released
                        // v.setBackgroundResource(R.drawable.ic_megaphone_opacity);
                        carExecuteAction("stop");
                    }
                    return true;
                }
            });

            view.findViewById(R.id.leftButton).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // Pressed
                        carExecuteAction("turnleft");
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        // Released
                        // v.setBackgroundResource(R.drawable.ic_megaphone_opacity);
                        carExecuteAction("stop");
                    }
                    return true;
                }
            });

            view.findViewById(R.id.rightButton).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // Pressed
                        carExecuteAction("turnright");
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        // Released
                        // v.setBackgroundResource(R.drawable.ic_megaphone_opacity);
                        carExecuteAction("stop");
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
                    carExecuteAction("claxonon");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Released
                    v.setBackgroundResource(R.drawable.ic_megaphone_opacity);
                    carExecuteAction("claxonoff");
                }
                return true;
            }
        });

        view.findViewById(R.id.light).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ligthOn) {
                    carExecuteAction("lightoff");
                    view.setBackgroundResource(R.drawable.ic_light_off_opacity);
                    ligthOn = false;
                } else {
                    carExecuteAction("lighton");
                    view.setBackgroundResource(R.drawable.ic_light_on);
                    ligthOn = true;
                }
            }
        });

        view.findViewById(R.id.config).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    NavHostFragment.findNavController(ControlCarFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);

                    //when user leaves application
                    viewer.stopStream();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }
}