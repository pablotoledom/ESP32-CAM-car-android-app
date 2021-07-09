package com.theretrocenter.esp32_camandroidapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;


public class SecondFragment extends Fragment {

    private MainActivity MainActivity = new MainActivity();

    Preferences preferences = Preferences.getInstance(MainActivity);
    String selectedUIControl = "";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String carUIControl = preferences.getData("CarUIControl");
        String ssidWIFISaved = preferences.getData("SSIDWIFI");

        EditText ssidWIFIET = view.findViewById(R.id.SSIDWIFIEditText);
        EditText passWIFIET = view.findViewById(R.id.PassWIFIEditText);
        ssidWIFIET.setText(ssidWIFISaved);

        if (carUIControl.equals("joystick")) {
            RadioButton joystickUIControl = view.findViewById(R.id.radioButtonjoystick);
            joystickUIControl.setChecked(true);
        } else {
            RadioButton buttonsUIControl = view.findViewById(R.id.radioButtonButtons);
            buttonsUIControl.setChecked(true);
        }


        RadioGroup carUIControlRadioGroup = (RadioGroup) view.findViewById(R.id.carUIControlRadioGroup);

        carUIControlRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioButtonButtons:
                        // do operations specific to this selection
                        selectedUIControl = "buttons";
                        break;
                    case R.id.radioButtonjoystick:
                        // do operations specific to this selection
                        selectedUIControl = "joystick";
                        break;
                }
            }
        });

        view.findViewById(R.id.scanWIFIBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        view.findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ssidWIFI = ssidWIFIET.getText().toString();
                String passWIFI = passWIFIET.getText().toString();

                if (!passWIFI.equals("")) {
                    preferences.saveData("SSIDWIFI", ssidWIFI);

                    String remoteWIFICarIP = preferences.getData("RemoteWIFICarIP");

                    //String testValue = "Hello, world!";
                    byte[] ssidB64 = Base64.encode(ssidWIFI.getBytes(), Base64.DEFAULT);
                    byte[] passB64 = Base64.encode(passWIFI.getBytes(), Base64.DEFAULT);

                    String ssidB64Str = new String(ssidB64);
                    String passB64Str = new String(passB64);

                    String UIControl = selectedUIControl.equals("buttons") ? "buttonstext" : selectedUIControl;

                    // Create http cliient object to send request to server
                    HttpClient Client = new DefaultHttpClient();

                    // Create URL string
                    String URL = "http://" + remoteWIFICarIP + "/control?command=saveconfig&ssid=" + ssidB64Str + "&password=" + passB64Str + "&usercontrol=" + UIControl;

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

                preferences.saveData("CarUIControl", selectedUIControl);

                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        view.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

    }
}