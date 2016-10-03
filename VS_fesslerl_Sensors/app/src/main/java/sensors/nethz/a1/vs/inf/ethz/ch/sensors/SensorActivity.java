package sensors.nethz.a1.vs.inf.ethz.ch.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private GraphContainer graphContainer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		// TODO
    }

    public GraphContainer getGraphContainer() {
        return graphContainer;
    }

    @Override
    protected void onResume() {
        super.onResume();
		// TODO
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do not do anything
    }
}
