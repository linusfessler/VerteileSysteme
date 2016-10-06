package ch.ethz.inf.vs.a1.fesslerl.antitheft;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAlarmToggleDrawable(AntiTheftService.isEnabled);
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

        // Start service if already has permission
        if (permissionCheck == PackageManager.PERMISSION_GRANTED)
            startService();
        // Else ask user to get permission
        else {
            // Should we show an explanation?
            /*if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WAKE_LOCK)) {*/
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showExplanation();
            /*} else {
                // No explanation needed, we can request the permission.
                requestPermission();
            }*/
        }
    }

    private void startService() {
        startService(new Intent(MainActivity.this, AntiTheftService.class));
        setAlarmToggleDrawable(true);
    }

    private void stopService() {
        stopService(new Intent(MainActivity.this, AntiTheftService.class));
        setAlarmToggleDrawable(false);
    }

    private void setAlarmToggleDrawable(boolean isEnabled) {
        if (mAlarmToggle == null)
            mAlarmToggle = (ImageButton) findViewById(R.id.alarm_toggle);
        mAlarmToggle.setImageResource(isEnabled ? R.drawable.locked : R.drawable.unlocked);
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_permission_request))
                .setMessage(getString(R.string.message_permission_request))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission();
                    }
                });
        builder.create().show();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WAKE_LOCK},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ;//startService();
    }

    // Start settings activity
    public boolean onSettingsClicked(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        return true;
    }
}
