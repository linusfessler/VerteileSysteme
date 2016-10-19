package ch.ethz.inf.vs.a2.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.Formatter;

import ch.ethz.inf.vs.a2.R;
import ch.ethz.inf.vs.a2.server.ServerService;

public class ServerActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Button mToggleServerStateButton;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        // Set IP Text
        Formatter formatter = new Formatter();
        String ip = formatter.format(getString(R.string.ip), "192.168.1.1 (Placeholder)").toString();
        TextView txtIp = (TextView) findViewById(R.id.txt_ip);
        txtIp.setText(ip);

        // Set Port Text
        formatter = new Formatter();
        String port = formatter.format(getString(R.string.port), Integer.toString(ServerService.PORT)).toString();
        TextView txtPort = (TextView) findViewById(R.id.txt_port);
        txtPort.setText(port);

        // Set Button Text
        mToggleServerStateButton = (Button) findViewById(R.id.btn_toggle_server_state);
        mToggleServerStateButton.setText(getString(!ServerService.isRunning(this) ?
                R.string.start_server : R.string.stop_server));
    }

    public void toggleServerState(View v) {
        if (!ServerService.isRunning(this))
            /*if (!isHotSpotOn()) {
                // Request user to turn hotspot on

                if (mToast != null)
                    mToast.cancel();
                mToast = Toast.makeText(getApplicationContext(), getString(R.string.turn_on_hotspot), Toast.LENGTH_LONG);
                mToast.show();

                Intent intent = new Intent();
                intent.setClassName("com.android.settings", "com.android.settings.TetherSettings");
                startActivity(intent);
            } else*/
                startService();
        else
            stopService();
    }

    private void startService() {
        Intent intent = new Intent(this, ServerService.class);
        mToggleServerStateButton.setText(getString(R.string.stop_server));
        startService(intent);
        Log.d(getLocalClassName(), "Server started");
    }

    private void stopService() {
        Intent intent = new Intent(this, ServerService.class);
        mToggleServerStateButton.setText(getString(R.string.start_server));
        stopService(intent);
        Log.d(getLocalClassName(), "Server stopped");
    }

    private boolean isHotSpotOn() {
        WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        }
        catch (Exception e) {
            Log.e(getLocalClassName(), "", e);
        }
        return false;
    }
}