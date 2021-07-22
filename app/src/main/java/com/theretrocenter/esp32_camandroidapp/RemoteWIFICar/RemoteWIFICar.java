package com.theretrocenter.esp32_camandroidapp.RemoteWIFICar;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.theretrocenter.esp32_camandroidapp.utilities.JSONParser;
import com.theretrocenter.esp32_camandroidapp.utilities.Preferences;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RemoteWIFICar {
    public static final String TAG_SSID = "ssid";
    public static final String TAG_SIGNAL = "signal";
    public static final String TAG_SECURITY = "security";

    private Boolean networkPing(String IP) {
        if (IP.equals("")) {
            return false;
        }

        // Create http client object to send request to server
        HttpClient Client = new DefaultHttpClient();
        Client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
        Client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

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

            // *********** Mock IP server ********
            if (true) {
                dialog.hide();
                String mockIP = "192.168.1.135:8080";
                preferences.saveData("RemoteWIFICarIP", mockIP);
                return mockIP;
            }

            // Find WIFI Car IP
            String[] parts = hostIP.split("\\.",-1);
            String baseIPScan = parts[0] + "." + parts[1] + "." + parts[2] + ".";


            // First test: Saved IP
            String SavedIP = preferences.getData("RemoteWIFICarIP");
            Boolean testSavedIP = networkPing(SavedIP);

            if (testSavedIP) {
                dialog.hide();
                return SavedIP;
            } else {
                // Second test: Default IP Car
                Boolean carDefaultIP = networkPing("192.168.4.1");

                if (carDefaultIP) {
                    dialog.hide();
                    preferences.saveData("RemoteWIFICarIP", "192.168.4.1");
                    return "192.168.4.1";
                }

                // Third test: Loop finder IP in network
                for (Integer endIP = 1 ; endIP <= 255 ; endIP++) {
                // for (Integer endIP = 1 ; endIP <= 2 ; endIP++) {
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

                // Hidde loading message
                dialog.hide();
            }

            return baseIPScan;
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return "ERROR";
    }

    public void executeAction(String command, String remoteWIFICarIP) {
        try {
            // Create URL string
            String saveSettingsURL = "http://" + remoteWIFICarIP + "/control?command=car&value=" + command;

            // HTTP json Call
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.GetJSONfromUrl(saveSettingsURL);

            // Show response on activity
            Log.i("httpget", json.toString());

            //JSONObject c = json.getJSONObject(0);


            /*// Create http cliient object to send request to server
            HttpClient Client = new DefaultHttpClient();
            Client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 500);
            Client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 500);

            // Create URL string
            String URL = "http://" + remoteWIFICarIP + "/control?command=car&value=" + command;

            Log.i("Action URL", URL);

            // Create Request to server and get response
            HttpGet httpget = new HttpGet(URL);
            String serverResponseString = "";
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            serverResponseString = Client.execute(httpget, responseHandler);

            // Show response on activity
            Log.i("Action response", serverResponseString);*/

        } catch(Exception ex) {
            // ex.printStackTrace();
        }
    }

    public ArrayList<HashMap<String, String>>  getWIFIList (String remoteWIFICarIP) {
        ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();

        try {
            // Create URL string
            String wifiListURL = "http://" + remoteWIFICarIP + "/wifilist/";

            // HTTP json Call
            JSONParser jParser = new JSONParser();
            JSONObject jsonArray = jParser.GetJSONfromUrl(wifiListURL);
            JSONArray json = jsonArray.optJSONArray("result");

            Log.d("json", json.toString());

            for (int i=0; i< json.length(); i++){
                try {
                    JSONObject c = json.getJSONObject(i);

                    // Crear Listado de datos que irán a la vista
                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_SSID, c.getString(TAG_SSID));
                    map.put(TAG_SIGNAL, c.getString(TAG_SIGNAL));
                    map.put(TAG_SECURITY, c.getString(TAG_SECURITY));
                    jsonlist.add(map);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return jsonlist;
    }

    public void saveWIFISettings(String ssidB64, String passB64, String UIControl, String remoteWIFICarIP) {
        try {
            // Create URL string
            String saveSettingsURL = "http://" + remoteWIFICarIP + "/control?command=saveconfig&ssid=" + ssidB64 + "&password=" + passB64 + "&usercontrol=" + UIControl;

            // HTTP json Call
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.GetJSONfromUrl(saveSettingsURL);

            // Show response on activity
            Log.i("httpget", json.toString());


        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
