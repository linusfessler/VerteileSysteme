package ch.ethz.inf.vs.a1.fesslerl.antitheft;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by linus on 04.10.2016.
 */

public class UnlockReceiver extends BroadcastReceiver {

    private MainActivity mActivity;

    public UnlockReceiver(MainActivity activity) {
        super();
        this.mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager.isKeyguardSecure())
            mActivity.stopService();
    }
}
