package ch.ethz.inf.vs.a1.fesslerl.antitheft;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

/**
 * Created by linus on 04.10.2016.
 */

public class AntiTheftService extends Service implements AlarmCallback {

    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private final int mNotificationId = 1;

    private SensorManager mSensorManager;
    private SpikeMovementDetector mDetector;
    private int mSensor2Type;
    private SharedPreferences mPreferences;
    private Ringtone mRingtone;
    private AudioAttributes mAttributes;

    @Override
    public void onCreate() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.small_icon)
                .setContentTitle(getString(R.string.title_notification))
                .setContentText(getString(R.string.text_notification))
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this, MainActivity.class), 0))
                .setOngoing(true);
        mNotification = builder.build();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mNotificationManager.notify(mNotificationId, mNotification);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int sensitivity = (int)Double.parseDouble(mPreferences.getString(SettingsActivity.SENSITIVITY, "0"));
        int mSensor2Type = Integer.parseInt(mPreferences.getString(SettingsActivity.SENSOR2TYPE, "0"));

        mDetector = new SpikeMovementDetector(this, sensitivity);

        mSensorManager.registerListener(mDetector, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mDetector, mSensorManager.getDefaultSensor(mSensor2Type), SensorManager.SENSOR_DELAY_NORMAL);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(mDetector, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));
        mSensorManager.unregisterListener(mDetector, mSensorManager.getDefaultSensor(mSensor2Type));
        mNotificationManager.cancel(mNotificationId);
        if (mRingtone != null)
            mRingtone.stop();
    }

    @Override
    public void onDelayStarted() {
        mSensorManager.unregisterListener(mDetector, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));

        int delay = (int)Double.parseDouble(mPreferences.getString(SettingsActivity.DELAY, "0"));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                try {
                    String ringtoneString = mPreferences.getString(SettingsActivity.ALARM, "DEFAULT_SOUND");
                    mRingtone = RingtoneManager.getRingtone(getApplicationContext(), Uri.parse(ringtoneString));
                    mRingtone.setAudioAttributes(mAttributes);
                    mRingtone.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000 * delay);
    }
}
