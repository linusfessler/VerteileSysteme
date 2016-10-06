package ch.ethz.inf.vs.a1.fesslerl.antitheft;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by linus on 04.10.2016.
 */

public class AntiTheftService extends Service implements AlarmCallback {

    public static boolean isEnabled = false;

    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private final int mNotificationId = 1;

    private SpikeMovementDetector mDetector;
    private PowerManager.WakeLock mWakeLock;

    private SensorManager mSensorManager;
    private Sensor mSensor2;

    private SharedPreferences mPreferences;

    private Ringtone mRingtone;
    private AudioAttributes mAttributes;

    private Toast mToast;

    @Override
    public void onCreate() {
        // Build ongoing notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.small_icon)
                .setContentTitle(getString(R.string.title_notification))
                .setContentText(getString(R.string.text_notification))
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this, MainActivity.class), 0))
                .setOngoing(true);
        mNotification = builder.build();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Make ringtone use alarm volume instead of ringtone volume
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
        isEnabled = true;

        // Get sensitivity and additional sensor type from preferences
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int sensitivity = (int)Double.parseDouble(mPreferences.getString(SettingsActivity.SENSITIVITY, "0"));
        int sensor2Type = Integer.parseInt(mPreferences.getString(SettingsActivity.SENSOR2TYPE, "0"));

        // Enable spike movement detection
        mDetector = new SpikeMovementDetector(this, sensitivity);

        // Register default sensor listener
        mSensorManager.registerListener(mDetector, mSensorManager.getDefaultSensor(
                Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL);

        // Register additional sensor listener if selected in preferences
        mSensor2 = mSensorManager.getDefaultSensor(sensor2Type);
        if (mSensor2 != null)
            mSensorManager.registerListener(mDetector, mSensor2, SensorManager.SENSOR_DELAY_NORMAL);

        // Start ongoing notification
        mNotificationManager.notify(mNotificationId, mNotification);

        // Acquire partial WakeLock to let service continue when screen is turned off
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WakeLock");
        mWakeLock.acquire();

        // Show toast
        showToast();

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        isEnabled = false;

        // Unregister default sensor listener
        mSensorManager.unregisterListener(mDetector, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));

        // Unregister additional sensor listener if selected in preferences
        if (mSensor2 != null)
            mSensorManager.unregisterListener(mDetector, mSensor2);

        // Release partial WakeLock
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }

        // Stop notification
        mNotificationManager.cancel(mNotificationId);

        // Stop ringtone if playing
        if (mRingtone != null && mRingtone.isPlaying())
            mRingtone.stop();

        // Show toast
        showToast();
    }

    @Override
    public void onDelayStarted() {
        // Unregister default sensor listener
        mSensorManager.unregisterListener(mDetector, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));

        // Unregister additional sensor listener if selected in preferences
        if (mSensor2 != null)
            mSensorManager.unregisterListener(mDetector, mSensor2);

        // Get delay from preferences
        int delay = (int)Double.parseDouble(mPreferences.getString(SettingsActivity.DELAY, "0"));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                try {
                    if (isEnabled) {
                        /*if (mRingtone != null && mRingtone.isPlaying())
                            mRingtone.stop();*/

                        // Get ringtone from preferences
                        String ringtoneString = mPreferences.getString(SettingsActivity.ALARM, "DEFAULT_SOUND");
                        mRingtone = RingtoneManager.getRingtone(getApplicationContext(), Uri.parse(ringtoneString));
                        // Set volume to ringtone volume
                        mRingtone.setAudioAttributes(mAttributes);
                        mRingtone.play();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000 * delay);
    }

    void showToast() {
        if (mToast != null)
            mToast.cancel();
        mToast = Toast.makeText(this, "Anti Theft Service " + (isEnabled ? "started" : "stopped"), Toast.LENGTH_SHORT);
        mToast.show();
    }
}
