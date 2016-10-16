package ch.ethz.inf.vs.a2.sensor;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.ethz.inf.vs.a2.sensor.AbstractSensor;

/**
 * Created by johannes on 15.10.16.
 */

public class JsonSensor extends AbstractSensor {

    private final String LOGGING_TAG = "###JsonSensor";
    private final String urlString;

    public JsonSensor(String url){
        this.urlString = url;
    }

    @Override
    public String executeRequest() throws Exception {
        String result;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Connection", "close");
        try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = in.readLine()) != null )
                sb.append(line);
            result = sb.toString();
        }
        return result;
    }

    @Override
    public double parseResponse(String response) {
        double res = 0.0;
        try {
            JSONObject json = new JSONObject(response);
            res = json.getDouble("value");
        } catch (Exception e){
            Log.d(LOGGING_TAG, "Error parsing JSON-Response: " + e.getMessage());
        }
        return res;
    }
}
