package ch.ethz.inf.vs.a2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ch.ethz.inf.vs.a2.R;
import ch.ethz.inf.vs.a2.sensor.SensorFactory;

public class SoapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soap);
    }

    public void onXmlButtonClick(View v) {
        Intent intent = new Intent(this, SensorActivity.class);
        intent.putExtra(SensorActivity.EXTRA_SENSOR_TYPE, SensorFactory.Type.XML);
        startActivity(intent);
    }

    public void onSoapButtonClick(View v) {
        Intent intent = new Intent(this, SensorActivity.class);
        intent.putExtra(SensorActivity.EXTRA_SENSOR_TYPE, SensorFactory.Type.SOAP);
        startActivity(intent);
    }
}
