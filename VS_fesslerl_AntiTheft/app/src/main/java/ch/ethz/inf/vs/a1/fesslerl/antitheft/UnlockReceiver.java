package ch.ethz.inf.vs.a1.fesslerl.antitheft;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.widget.ImageButton;
import android.widget.Switch;

/**
 * Created by linus on 04.10.2016.
 */

public class UnlockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity activity = (MainActivity) context;
        activity.setAlarmToggleDrawable(false);

        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager.isKeyguardSecure())
            context.stopService(new Intent(context, AntiTheftService.class));

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(SettingsActivity.IS_RUNNING, false);
        editor.commit();
    }
}
