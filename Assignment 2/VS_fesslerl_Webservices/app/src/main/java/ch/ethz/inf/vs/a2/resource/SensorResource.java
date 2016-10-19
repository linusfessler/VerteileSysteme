package ch.ethz.inf.vs.a2.resource;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.text.DecimalFormat;

import ch.ethz.inf.vs.a2.http.HttpResponse;
import ch.ethz.inf.vs.a2.http.ParsedRequest;

/**
 * Created by johannes on 19.10.16.
 */

public class SensorResource extends Resource {
    Sensor sensor;
    SensorManager mgr;
    SensorTypes types;
    float[] vals;

    public SensorResource(Sensor sensor, SensorManager mgr){
        this.sensor = sensor;
        this.mgr = mgr;
        this.types = new SensorTypesImpl();
    }

    @Override
    protected String get(ParsedRequest request) {
        MySensorListener listener = new MySensorListener();
        mgr.registerListener(listener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        while(!listener.hasValue) {
            try {
                Thread.sleep(5);
            } catch (Exception e){}
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<h3>").append(sensor.getName()).append("</h3>");
        sb.append("<ul>");
        for(int i = 0; i < listener.vals.length ; ++i){
            sb.append("<li>Sensor Value "+i+": ").append(listener.vals[i]).append(" ").append(types.getUnitString(sensor.getType())).append("</li>");
        }
        sb.append("</ul>");
        sb.append("<a href='/'>Return to root</a>");
        return HttpResponse.generateHtmlResponse(sb.toString());
    }

    @Override
    protected String post(ParsedRequest request) {
        return null;
    }


    //TODO: Ich glaube nicht dass diese zusätzliche Klasse eine elegante Lösung ist, aber wie sollen wir sonst einzelne Werte vom Sensor lesen?
    class MySensorListener implements SensorEventListener {

        public boolean hasValue = false;
        public float[] vals;

        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.d("###MySensorListener", "Got new values.");
            mgr.unregisterListener(this);
            vals = event.values.clone();
            hasValue = true;

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}
