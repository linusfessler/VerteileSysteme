package ch.ethz.inf.vs.a2.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ch.ethz.inf.vs.a2.R;
import ch.ethz.inf.vs.a2.sensor.SensorFactory;

public class RestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);
    }

    public void onRawHttpButtonClick(View v) {
        Intent intent = new Intent(this, SensorActivity.class);
        intent.putExtra(SensorActivity.EXTRA_SENSOR_TYPE, SensorFactory.Type.RAW_HTTP);
        startActivity(intent);
    }

    public void onTextButtonClick(View v) {
        Intent intent = new Intent(this, SensorActivity.class);
        intent.putExtra(SensorActivity.EXTRA_SENSOR_TYPE, SensorFactory.Type.TEXT);
        startActivity(intent);
    }

    public void onJsonButtonClick(View v) {
        Intent intent = new Intent(this, SensorActivity.class);
        intent.putExtra(SensorActivity.EXTRA_SENSOR_TYPE, SensorFactory.Type.JSON);
        startActivity(intent);
    }
}
