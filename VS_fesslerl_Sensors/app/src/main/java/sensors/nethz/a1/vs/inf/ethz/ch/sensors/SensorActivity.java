package sensors.nethz.a1.vs.inf.ethz.ch.sensors;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private int numValues;
    private String unit;

    private TextView textView;
    private GraphContainer graphContainer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            int sensorType = (int) bundle.getInt(MainActivity.SENSOR_TYPE);
            sensor = sensorManager.getDefaultSensor(sensorType);

            textView = (TextView) findViewById(R.id.sensor_name);
            textView.setText(sensor.getName());

            SensorTypesImpl sensorTypes = new SensorTypesImpl(sensorManager);
            numValues = sensorTypes.getNumberValues(sensorType);
            unit = sensorTypes.getUnitString(sensorType);
        }
    }

    public GraphContainer getGraphContainer() {
        return graphContainer;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, sensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        numValues = event.values.length;
        textView.setText(sensor.getName());
        for (int i = 0; i < numValues; i++)
            textView.setText(textView.getText() + System.getProperty("line.separator") + event.values[i] + " " + unit);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do not do anything
    }
}
