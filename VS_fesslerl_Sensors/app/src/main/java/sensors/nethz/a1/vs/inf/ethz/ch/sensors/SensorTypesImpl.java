package sensors.nethz.a1.vs.inf.ethz.ch.sensors;

import android.hardware.Sensor;
import android.hardware.SensorManager;

public class SensorTypesImpl implements SensorTypes {

    private SensorManager sensorManager;

    public SensorTypesImpl(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    public int getNumberValues(int sensorType) {
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
            case Sensor.TYPE_GRAVITY:
            case Sensor.TYPE_LINEAR_ACCELERATION: return 3;
            case Sensor.TYPE_MAGNETIC_FIELD: return 1;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED: return 6;
            case Sensor.TYPE_GYROSCOPE: return 3;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED: return 6;
            case Sensor.TYPE_LIGHT: return 1;
            case Sensor.TYPE_PRESSURE: return 1;
            case Sensor.TYPE_PROXIMITY: return 1;
            case Sensor.TYPE_ORIENTATION: return 3;
            case Sensor.TYPE_RELATIVE_HUMIDITY: return 1;
            case Sensor.TYPE_TEMPERATURE:
            case Sensor.TYPE_AMBIENT_TEMPERATURE: return 1;
            case Sensor.TYPE_STEP_COUNTER: return 1;
            case Sensor.TYPE_STEP_DETECTOR: return 1;
            case Sensor.TYPE_SIGNIFICANT_MOTION:
            case Sensor.TYPE_STATIONARY_DETECT: return 1;
            case Sensor.TYPE_ROTATION_VECTOR:
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR: return 5;
            default: return 0;
        }
    }

    public String getUnitString(int sensorType) {
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
            case Sensor.TYPE_GRAVITY:
            case Sensor.TYPE_LINEAR_ACCELERATION: return "m/s^2";
            case Sensor.TYPE_MAGNETIC_FIELD:
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED: return "uT";
            case Sensor.TYPE_GYROSCOPE:
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED: return "radians/second";
            case Sensor.TYPE_LIGHT: return "lux";
            case Sensor.TYPE_PRESSURE: return "hPa";
            case Sensor.TYPE_PROXIMITY: return "cm";
            case Sensor.TYPE_ORIENTATION: return "°";
            case Sensor.TYPE_RELATIVE_HUMIDITY: return "%";
            case Sensor.TYPE_TEMPERATURE:
            case Sensor.TYPE_AMBIENT_TEMPERATURE: return "°C";
            case Sensor.TYPE_STEP_COUNTER: return "step";
            case Sensor.TYPE_STEP_DETECTOR: return "steps";
            case Sensor.TYPE_SIGNIFICANT_MOTION:
            case Sensor.TYPE_STATIONARY_DETECT:
            case Sensor.TYPE_ROTATION_VECTOR:
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR: return "";
            default: return "default";
        }
    }
}
