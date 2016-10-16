package ch.ethz.inf.vs.a2.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import ch.ethz.inf.vs.a2.R;
import ch.ethz.inf.vs.a2.sensor.JsonSensor;
import ch.ethz.inf.vs.a2.sensor.RawHttpSensor;
import ch.ethz.inf.vs.a2.sensor.SensorFactory;
import ch.ethz.inf.vs.a2.sensor.SensorListener;
import ch.ethz.inf.vs.a2.sensor.TextSensor;

public class RestClientActivity extends AppCompatActivity implements SensorListener{
    private final String LOGGING_TAG = "###RestClientActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_client);

        RawHttpSensor sensor1 = (RawHttpSensor) SensorFactory.getInstance(SensorFactory.Type.RAW_HTTP);
        TextSensor sensor2 = (TextSensor) SensorFactory.getInstance(SensorFactory.Type.TEXT);
        JsonSensor sensor3 = (JsonSensor) SensorFactory.getInstance(SensorFactory.Type.JSON);

        sensor1.registerListener(this);
        sensor2.registerListener(this);
        sensor3.registerListener(this);

        //TODO: Müssen wir alle Werte anzeigen? Wenn ja, wie? OnReceiveSensorValue unterscheidet nicht zwischen Sensoren.
        sensor1.getTemperature();
        sensor2.getTemperature();
        sensor3.getTemperature();
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
