package ch.ethz.inf.vs.a1.fesslerl.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;


import com.jjoe64.graphview.GraphView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private GraphContainer graphContainer;
    private SensorManager sensorMgr;
    private Sensor sensor;
    private String unit = "";
    private int numVals = 0;
    private long startTime;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        Log.d("###sensor","start");
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        final List<Sensor> sensors = sensorMgr.getSensorList(Sensor.TYPE_ALL);
        sensor = sensors.get(getIntent().getIntExtra(getString(R.string.intent_extra_sensorname), 0));

        SensorTypes types = new SensorTypesImpl();
        unit = types.getUnitString(sensor.getType());
        numVals = types.getNumberValues(sensor.getType());

        Calendar calendar = Calendar.getInstance();
        startTime = calendar.getTime().getTime();

        TextView sensorName = (TextView) findViewById(R.id.text_sensor_name);
        sensorName.setText(sensor.getName());

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graphContainer = new MyGraphContainer(graph,unit,numVals);
    }

    public GraphContainer getGraphContainer() {
        return graphContainer;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("###sensor","register Listener");
        sensorMgr.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("###sensor","unregister Listener");
        sensorMgr.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        DecimalFormat format = new DecimalFormat("+#0.0000000;-#0.0000000");
        StringBuilder sb = new StringBuilder();
        float[] vals = new float[numVals];

        for(int i = 0; i < numVals; ++i) {
            sb.append(format.format(event.values[i])).append(" ").append(unit).append("\n");
            vals[i] = event.values[i];
        }
        TextView txtVals = (TextView) findViewById(R.id.text_sensor_values);
        txtVals.setText(sb.toString());
        try {
            getGraphContainer().addValues((Calendar.getInstance().getTime().getTime() - startTime) / 1000., vals);
        } catch (IllegalArgumentException e) {
            System.err.println("IllegalArgumentException: " + e.getMessage());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do not do anything
    }
}


