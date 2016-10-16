package ch.ethz.inf.vs.a2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import ch.ethz.inf.vs.a2.R;
import ch.ethz.inf.vs.a2.sensor.Sensor;
import ch.ethz.inf.vs.a2.sensor.SensorFactory;
import ch.ethz.inf.vs.a2.sensor.SensorListener;

/**
 * Created by linus on 16.10.2016.
 */

public class SensorActivity extends AppCompatActivity implements SensorListener {

    public static final String EXTRA_SENSOR_TYPE = "Sensor Type";

    private final String LOGGING_TAG = "###SensorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        SensorFactory.Type sensorType = (SensorFactory.Type) getIntent().getExtras().get(EXTRA_SENSOR_TYPE);
        Sensor sensor = SensorFactory.getInstance(sensorType);
        sensor.registerListener(this);
        sensor.getTemperature();
    }

    @Override
    public void onReceiveSensorValue(double value) {
        //if there was an error before, clear the message textview.
        TextView textMsg = (TextView) findViewById(R.id.txt_message);
        textMsg.setText("");
        //show value to the user.
        TextView textTemp = (TextView) findViewById(R.id.txt_temp);
        textTemp.setText(String.format(getString(R.string.temp_val),value));
        Log.d(LOGGING_TAG, "onReceiveSensorValue: " + value);
    }

    @Override
    public void onReceiveMessage(String message) {
        TextView textMsg = (TextView) findViewById(R.id.txt_message);
        textMsg.setText(message);
        Log.d(LOGGING_TAG, "OnReceiveMessage: " + message);
    }
}
