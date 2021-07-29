package com.theretrocenter.esp32_camandroidapp.utilities;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class JSONParser {
    static JSONObject jsonOb = null;

    // Constructor
    public JSONParser(){

    }

    // Get Json data from URL method
    public JSONObject GetJSONfromUrl(String url, Boolean useTimeOut){
        HttpParams httpParameters = new BasicHttpParams();
        if(useTimeOut) {
            HttpConnectionParams.setConnectionTimeout(httpParameters, 1500);
            HttpConnectionParams.setSoTimeout(httpParameters, 2000);
        }

        String serverResponseString = "";

        try {
            // HTTP call
            HttpClient client = HttpClientBuilder.create().disableAutomaticRetries().build();
            HttpGet httpget = new HttpGet(url);
            httpget.setParams(httpParameters);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            serverResponseString = client.execute(httpget, responseHandler);

        } catch(Exception ex) {
            // ex.printStackTrace();
            return null;
        }

        try {
            // Parse string to json object
            jsonOb = new JSONObject(serverResponseString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return jsonOb;
    }
}
