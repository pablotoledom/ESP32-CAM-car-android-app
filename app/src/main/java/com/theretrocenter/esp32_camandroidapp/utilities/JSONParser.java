package com.theretrocenter.esp32_camandroidapp.utilities;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONException;

public class JSONParser {
    static JSONObject jsonOb = null;

    // Constructor
    public JSONParser(){

    }

    // Get Json data from URL method
    public JSONObject GetJSONfromUrl(String url, Boolean useTimeOut){
        String responseText = "";
        HttpParams httpParameters = new BasicHttpParams();
        if(useTimeOut) {
            HttpConnectionParams.setConnectionTimeout(httpParameters, 2000);
            HttpConnectionParams.setSoTimeout(httpParameters, 2000);
        }

        CloseableHttpClient httpclient = HttpClientBuilder.create().disableAutomaticRetries().build();

        //String serverResponseString = "";

        try {
            // HTTP call
            HttpGet httpget = new HttpGet(url);
            httpget.setParams(httpParameters);
            CloseableHttpResponse response = httpclient.execute(httpget);

            HttpEntity entity = response.getEntity();
            responseText = EntityUtils.toString(entity);

            // Log.i("Execute command", responseText);

            try {
                httpget.abort();
            } finally {
                response.close();
            }

            try {
                // Parse string to json object
                jsonOb = new JSONObject(responseText);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

            httpclient.close();

            return jsonOb;

        } catch(Exception ex) {
            //ex.printStackTrace();
            return null;
        }
    }
}
