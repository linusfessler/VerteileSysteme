package ch.ethz.inf.vs.a1.fesslerl.sensors;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorManager sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        final List<Sensor> sensors = sensorMgr.getSensorList(Sensor.TYPE_ALL);
        final List<String> sensorNames = new ArrayList<>();
        for(Sensor s: sensors)
            sensorNames.add(s.getName());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,sensorNames);
        ListView listView = (ListView) findViewById(R.id.lv_sensors);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, SensorActivity.class);
                intent.putExtra(getString(R.string.intent_extra_sensorname), position);
                startActivity(intent);
            }


        });

    }
}
