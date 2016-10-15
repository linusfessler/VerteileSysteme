package ch.ethz.inf.vs.a2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import ch.ethz.inf.vs.a2.sensor.SensorListener;

public class RestClientActivity extends AppCompatActivity implements SensorListener{
    private final String LOGGING_TAG = "###RestClientActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_client);
        RawHttpSensor sensor = new RawHttpSensor("vslab.inf.ethz.ch",8081,"/sunspots/Spot2/sensors/temperature");
        sensor.registerListener(this);
        sensor.getTemperature();
    }

    @Override
    public void onReceiveSensorValue(double value) {
        TextView textTemp = (TextView) findViewById(R.id.txt_temp);
        textTemp.setText(String.format(getString(R.string.temp_val),value));
        Log.d(LOGGING_TAG, "onReceiveSensorValue: " + value);

    }

    @Override
    public void onReceiveMessage(String message) {
        Log.d(LOGGING_TAG, "OnReceiveMessage: " + message);
    }
}
