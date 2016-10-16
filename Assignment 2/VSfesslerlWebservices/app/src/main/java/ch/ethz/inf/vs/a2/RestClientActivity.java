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
        RawHttpSensor sensor1 = new RawHttpSensor("vslab.inf.ethz.ch",8081,"/sunspots/Spot2/sensors/temperature");
        sensor1.registerListener(this);
        TextSensor sensor2 = new TextSensor("http://vslab.inf.ethz.ch:8081/sunspots/Spot1/sensors/temperature");
        sensor2.registerListener(this);
        JsonSensor sensor3 = new JsonSensor("http://vslab.inf.ethz.ch:8081/sunspots/Spot1/sensors/temperature");
        sensor3.registerListener(this);

        //TODO: Müssen wir alle Werte anzeigen? Wenn ja, wie? OnReceiveValue unterscheidet nicht zwischen sensoren.
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
