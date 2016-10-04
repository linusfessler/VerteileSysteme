package sensors.nethz.a1.vs.inf.ethz.ch.sensors;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String SENSOR_TYPE = "sensor_type";

    private SensorManager sensorManager;
    private List<Sensor> sensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        SensorAdapter adapter = new SensorAdapter(this, sensors);

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, SensorActivity.class);
                    intent.putExtra(SENSOR_TYPE, sensors.get(position).getType());
                    startActivity(intent);
                }
            }
        );
    }
}
