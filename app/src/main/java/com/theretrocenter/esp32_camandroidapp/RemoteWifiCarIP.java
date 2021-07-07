package com.theretrocenter.esp32_camandroidapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class RemoteWifiCarIP {
    private Boolean networkPing(String IP) {
        // Create http client object to send request to server
        HttpClient Client = new DefaultHttpClient();

        // Create URL string
        String URL = "http://" + IP + "/control?command=car&value=stop";

        Log.i("URL to ping", URL);

        try {
            String SetServerString = "";

            // Create Request to server and get response
            HttpGet httpget = new HttpGet(URL);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            SetServerString = Client.execute(httpget, responseHandler);

            // Show response on activity
            Log.i("http body response", SetServerString);

            if (SetServerString.equals("OK")) {
                return true;
            }
        }
        catch(Exception ex) {
            // ex.printStackTrace();
            return false;
        }

        return false;
    }

    public String finderIP(String hostIP, ProgressDialog dialog, Context context) {
        try {
            Preferences preferences = Preferences.getInstance(context);

            // Find WIFI Car IP
            String[] parts = hostIP.split("\\.",-1);
            String baseIPScan = parts[0] + "." + parts[1] + "." + parts[2] + ".";


            String SavedIP = preferences.getData("RemoteWIFICarIP");

            Boolean testSavedIP = networkPing(SavedIP);

            if (testSavedIP) {
                dialog.hide();
                return SavedIP;
            } else {
                // for (Integer endIP = 1 ; endIP <= 255 ; endIP++) {
                for (Integer endIP = 1 ; endIP <= 2 ; endIP++) {
                    String ipForTest = baseIPScan + endIP;

                    Boolean testIP = networkPing(ipForTest);

                    Log.i("Result of test", testIP.toString());

                    if (testIP) {
                        dialog.hide();
                        Log.i("I have found the IP", endIP.toString());

                        // Save
                        preferences.saveData("RemoteWIFICarIP",ipForTest);
                        return ipForTest;
                    }
                }

                // Mock IP server
                dialog.hide();
                preferences.saveData("RemoteWIFICarIP", "192.168.1.123");
            }

            return baseIPScan;
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return "ERROR";
    }
}
