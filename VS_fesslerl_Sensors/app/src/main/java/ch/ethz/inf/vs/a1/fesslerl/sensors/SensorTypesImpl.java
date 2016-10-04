package ch.ethz.inf.vs.a1.fesslerl.sensors;

import android.hardware.Sensor;

public class SensorTypesImpl implements SensorTypes {
    public int getNumberValues(int sensorType){
        switch(sensorType){
            case Sensor.TYPE_LINEAR_ACCELERATION:
            case Sensor.TYPE_GRAVITY:
            case Sensor.TYPE_GYROSCOPE:
            case Sensor.TYPE_MAGNETIC_FIELD:
            case Sensor.TYPE_ACCELEROMETER:
            case Sensor.TYPE_ROTATION_VECTOR:
                return 3;
            case Sensor.TYPE_PROXIMITY:
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
            case Sensor.TYPE_RELATIVE_HUMIDITY:
            case Sensor.TYPE_PRESSURE:
            case Sensor.TYPE_LIGHT:
                return 1;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                return 4;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                return 6;
        }
		return 0;
    }

    public String getUnitString(int sensorType){
        switch(sensorType){
            case Sensor.TYPE_LINEAR_ACCELERATION:
            case Sensor.TYPE_GRAVITY:
            case Sensor.TYPE_ACCELEROMETER:
                return "m/s^2";
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
            case Sensor.TYPE_MAGNETIC_FIELD:
                return "microT";
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
            case Sensor.TYPE_GYROSCOPE:
                return "rad/s";
            case Sensor.TYPE_LIGHT:
                return "lx";
            case Sensor.TYPE_PRESSURE:
                return "hPa";
            case Sensor.TYPE_PROXIMITY:
                return "cm";
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
            case Sensor.TYPE_ROTATION_VECTOR:
                return "no unit";
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                return "%";
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                return "°C";

        }
       return null;
    }
}
