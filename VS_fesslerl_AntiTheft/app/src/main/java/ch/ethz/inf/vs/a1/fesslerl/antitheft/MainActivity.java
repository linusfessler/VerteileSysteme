package ch.ethz.inf.vs.a1.fesslerl.antitheft;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageButton mAlarmToggle;
    private Toast mToast;

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
        if (!AntiTheftService.isEnabled)
            startService(new Intent(MainActivity.this, AntiTheftService.class));
        else
            stopService(new Intent(MainActivity.this, AntiTheftService.class));

        AntiTheftService.isEnabled = !AntiTheftService.isEnabled;

        if (mToast != null)
            mToast.cancel();
        mToast = Toast.makeText(MainActivity.this, "Anti Theft Service " + (AntiTheftService.isEnabled ? "started" : "stopped"), Toast.LENGTH_SHORT);
        mToast.show();

        setAlarmToggleDrawable(AntiTheftService.isEnabled);
    }

    void setAlarmToggleDrawable(boolean isEnabled) {
        getAlarmToggle().setImageResource(isEnabled ? R.drawable.locked : R.drawable.unlocked);
    }

    ImageButton getAlarmToggle() {
        if (mAlarmToggle == null)
            mAlarmToggle = (ImageButton) findViewById(R.id.alarm_toggle);
        return mAlarmToggle;
    }

    // Start settings activity
    public boolean onSettingsClicked(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        return true;
    }
}
