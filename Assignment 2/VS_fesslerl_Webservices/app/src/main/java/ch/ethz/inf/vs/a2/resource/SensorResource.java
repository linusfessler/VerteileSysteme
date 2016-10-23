package ch.ethz.inf.vs.a2.resource;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.net.URI;

import ch.ethz.inf.vs.a2.http.HttpResponse;
import ch.ethz.inf.vs.a2.http.ParsedRequest;

/**
 * Created by johannes on 19.10.16.
 */

public class SensorResource extends Resource implements SensorEventListener {

    Sensor sensor;
    SensorManager mgr;
    SensorTypes types;
    float[] values;

    public SensorResource(URI uri, Sensor sensor, SensorManager mgr) {
        super(uri);
        this.sensor = sensor;
        this.mgr = mgr;
        this.types = new SensorTypesImpl();
    }

    @Override
    protected String get(ParsedRequest request) {
        mgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        while (values == null) {
            try {
                Thread.sleep(5);
            } catch (Exception e){}
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<h3>").append(sensor.getName()).append("</h3>");
        sb.append("<ul>");
        for (int i = 0; i < values.length ; ++i)
            sb.append("<li>Sensor Value "+i+": ").append(values[i]).append(" ").append(types.getUnitString(sensor.getType())).append("</li>");
        sb.append("</ul>");
        sb.append("<a href='/'>Return to root</a>");
        return HttpResponse.generateResponse("200 OK", sb.toString());
    }

    @Override
    protected String post(ParsedRequest request) {
        return HttpResponse.generateErrorResponse("405 Method Not Allowed", "Post not defined for Sensor Resource.");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("###MySensorListener", "Got new values.");
        mgr.unregisterListener(this);
        values = event.values.clone();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
