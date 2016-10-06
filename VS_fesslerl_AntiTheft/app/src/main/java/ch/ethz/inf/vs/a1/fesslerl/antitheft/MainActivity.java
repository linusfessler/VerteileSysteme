package ch.ethz.inf.vs.a1.fesslerl.antitheft;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final int PERMISSION_REQUEST_CODE = 1;

    private ImageButton mAlarmToggle;
    private UnlockReceiver mUnlockReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAlarmToggleDrawable(AntiTheftService.isEnabled);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (AntiTheftService.isEnabled)
            stopService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    public void onAlarmToggleClicked(View view) {
        // Stop service if it is enabled
        if (AntiTheftService.isEnabled) {
            stopService();
            return;
        }

        // Check permission
        int permissionCheck  = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WAKE_LOCK);

        // Start service if it already has permission for wake lock
        if (permissionCheck == PackageManager.PERMISSION_GRANTED)
            startService();
        // Else request permission
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WAKE_LOCK},
                    PERMISSION_REQUEST_CODE);
        }
    }

    public void startService() {
        startService(new Intent(MainActivity.this, AntiTheftService.class));
        setAlarmToggleDrawable(true);

        // Register unlock receiver to disable service when device is unlocked
        mUnlockReceiver = new UnlockReceiver(this);
        registerReceiver(mUnlockReceiver, new IntentFilter("android.intent.action.USER_PRESENT"));
    }

    public void stopService() {
        stopService(new Intent(MainActivity.this, AntiTheftService.class));
        setAlarmToggleDrawable(false);

        // Unregister unlock receiver
        unregisterReceiver(mUnlockReceiver);
    }

    private void setAlarmToggleDrawable(boolean isEnabled) {
        if (mAlarmToggle == null)
            mAlarmToggle = (ImageButton) findViewById(R.id.alarm_toggle);
        mAlarmToggle.setImageResource(isEnabled ? R.drawable.locked : R.drawable.unlocked);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            startService();
    }

    // Start settings activity
    public boolean onSettingsClicked(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        return true;
    }
}
