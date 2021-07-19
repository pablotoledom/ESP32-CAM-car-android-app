package com.theretrocenter.esp32_camandroidapp.utilities;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class JSONParser {
    static JSONObject jsonOb = null;
    static JSONArray jarray = null;

    // Constructor
    public JSONParser(){

    }

    // Get Json data from URL method
    public JSONArray GetJSONfromUrl(String url){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        String serverResponseString = "";

        try {
            // HTTP call
            HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            serverResponseString = client.execute(httpget, responseHandler);

        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }

        try {
            // Parse string to json object
            jsonOb = new JSONObject(serverResponseString);
            // Extract result object
            jarray = jsonOb.optJSONArray("result");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return jarray;
    }
}
