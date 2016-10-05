package ch.ethz.inf.vs.a1.fesslerl.antitheft;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;

public abstract class AbstractMovementDetector implements SensorEventListener {

    protected AlarmCallback callback;
    protected int sensitivity;

    private int mSensor2Type;

    public AbstractMovementDetector(AlarmCallback callback, int sensitivity) {
        this.callback = callback;
        this.sensitivity = sensitivity;
    }

    // Sensor monitoring
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Copy values because the event is not owned by the application
        float[] values = event.values.clone();
        if (doAlarmLogic(values))
            callback.onDelayStarted();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do not do anything
    }

    public abstract boolean doAlarmLogic(float[] values);

}
