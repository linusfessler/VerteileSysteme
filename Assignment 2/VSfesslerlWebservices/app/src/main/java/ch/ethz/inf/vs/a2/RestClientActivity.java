package ch.ethz.inf.vs.a2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import ch.ethz.inf.vs.a2.sensor.SensorListener;

public class RestClientActivity extends AppCompatActivity implements SensorListener{
    private final String LOGGING_TAG = "###RestClientActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_client);
    }

    @Override
    public void onReceiveSensorValue(double value) {
        Log.d(LOGGING_TAG, "onReceiveSensorValue: " + value);

    }

    @Override
    public void onReceiveMessage(String message) {
        Log.d(LOGGING_TAG, "OnReceiveMessage: " + message);
    }
}
