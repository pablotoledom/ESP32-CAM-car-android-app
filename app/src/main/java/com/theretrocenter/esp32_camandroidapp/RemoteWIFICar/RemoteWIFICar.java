package com.theretrocenter.esp32_camandroidapp.RemoteWIFICar;


import android.content.Context;
import android.util.Log;

import com.theretrocenter.esp32_camandroidapp.utilities.JSONParser;
import com.theretrocenter.esp32_camandroidapp.utilities.Preferences;

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

        // Create URL string
        String URL = "http://" + IP + "/control?command=car&value=stop";

        Log.i("URL to ping", URL);

        try {
            // HTTP json Call
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.GetJSONfromUrl(URL, true);
            String result = json.getString("result");

            if (result.equals("OK")) {
                return true;
            }
        }
        catch(Exception ex) {
            // ex.printStackTrace();
            return false;
        }

        return false;
    }

    public String finderIP(String hostIP, Context context) {
        try {
            Preferences preferences = Preferences.getInstance(context);

            // *********** Mock IP server ********
            if (false) {
                String mockIP = "192.168.1.32:8080";
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
                return SavedIP;
            } else {
                // Second test: Default IP Car
                Boolean carDefaultIP = networkPing("192.168.4.1");

                if (carDefaultIP) {
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
                        Log.i("I have found the IP", endIP.toString());

                        // Save
                        preferences.saveData("RemoteWIFICarIP",ipForTest);
                        return ipForTest;
                    }
                }
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
            JSONObject json = jParser.GetJSONfromUrl(saveSettingsURL, true);

            // Show response on activity
            Log.i("URL Execute action", saveSettingsURL);
            Log.i("Execute action", json.toString());

        } catch(Exception ex) {
            // ex.printStackTrace();
        }
    }

    public ArrayList<HashMap<String, String>> getWIFIList (String remoteWIFICarIP) {
        ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();

        try {
            // Create URL string
            String wifiListURL = "http://" + remoteWIFICarIP + "/wifilist";

            Log.i("URL WIFI list", wifiListURL);

            // HTTP json Call
            JSONParser jParser = new JSONParser();
            JSONObject jsonArray = jParser.GetJSONfromUrl(wifiListURL, false);
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
            JSONObject json = jParser.GetJSONfromUrl(saveSettingsURL, false);

            // Show response on activity
            Log.i("URL WIFI and settings", saveSettingsURL);
            Log.i("Save WIFI and settings", json.toString());


        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
