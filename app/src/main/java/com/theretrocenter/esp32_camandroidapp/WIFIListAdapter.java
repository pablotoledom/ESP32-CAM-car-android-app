package com.theretrocenter.esp32_camandroidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;


public class WIFIListAdapter extends ArrayAdapter<HashMap<String, String>> {

    private int resource;
    private Context context;
    private List<HashMap<String, String>> wifiList;
    private EditText ssidWifi;

    public WIFIListAdapter(
            @NonNull Context context,
            @LayoutRes int resource,
            @NonNull List<HashMap<String, String>> objects,
            @NonNull EditText ssidWIFIET) {
        super(context, resource, objects);

        // Set class vars
        this.resource = resource;
        this.context = context;
        this.wifiList = objects;
        this.ssidWifi = ssidWIFIET;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(resource, null);
        ((TextView) view.findViewById(R.id.wifiItem)).setText(wifiList.get(position).get("ssid"));

        // Parse signal string to int
        int signal = Integer.parseInt(wifiList.get(position).get("signal"));

        // Define the signal icon based on signal lost
        if (signal < -85) {
            view.findViewById(R.id.wifiSignal).setBackgroundResource(R.drawable.ic_signal_1);
        } else if (signal < -75) {
            view.findViewById(R.id.wifiSignal).setBackgroundResource(R.drawable.ic_signal_2);
        } else if (signal < -65) {
            view.findViewById(R.id.wifiSignal).setBackgroundResource(R.drawable.ic_signal_3);
        } else {
            view.findViewById(R.id.wifiSignal).setBackgroundResource(R.drawable.ic_signal_4);
        }

        // Get security string
        String security = wifiList.get(position).get("security");

        // Define the security icon
        if (security.equals("NEED-PASS")) {
            view.findViewById(R.id.wifiSecurity).setBackgroundResource(R.drawable.ic_padlock_closed);
        } else {
            view.findViewById(R.id.wifiSecurity).setBackgroundResource(R.drawable.ic_padlock_open);
        }

        // onclick WIFI list item listener
        view.findViewById(R.id.wifiItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add selected SSID to edit text
                TextView textView = (TextView) view.findViewById(R.id.wifiItem);
                String text = textView.getText().toString();
                ssidWifi.setText(text);
                ssidWifi.requestFocus();
            }
        });

        return view;
    }

    public static class UserGuideFragment {
    }
}
