package ch.ethz.inf.vs.a2;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.ethz.inf.vs.a2.sensor.AbstractSensor;

/**
 * Created by johannes on 15.10.16.
 */

public class TextSensor extends AbstractSensor {
    private final String LOGGING_TAG = "###TextSensor";

    private final String urlString;

    public TextSensor(String url){
        this.urlString = url;
    }

    @Override
    public String executeRequest() throws Exception {
        String result = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "text/plain");
            connection.setRequestProperty("Connection", "close");
            try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                result = in.readLine();
            }

        }catch (Exception e){
            Log.d(LOGGING_TAG, e.getMessage());
            throw e;
        }
        return result;
    }

    @Override
    public double parseResponse(String response) {
        return Double.parseDouble(response);
    }
}
