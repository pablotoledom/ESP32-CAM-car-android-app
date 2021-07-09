package com.theretrocenter.esp32_camandroidapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ProgressDialog dialog=new ProgressDialog(MainActivity.this);
        dialog.setMessage("Searching your Remote WIFI Car on the network. \n\nPlease wait a few minutes.");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

        new android.os.Handler(Looper.getMainLooper()).postDelayed(
        new Runnable() {
            public void run() {
                Log.i("tag", "This'll run 300 milliseconds later");

                String localIP = getIPAddress(true);
                RemoteWifiCarIP RemoteWifiCarIP = new RemoteWifiCarIP();
                String WIFICarIP = RemoteWifiCarIP.finderIP(localIP, dialog, MainActivity.this);

                // Save detected IP
                Log.i("Remote WIFI Car IP", WIFICarIP);

                setContentView(R.layout.activity_main);

            }
        },1000);
    }
}