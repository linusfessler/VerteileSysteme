package ch.ethz.inf.vs.a2.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Formatter;

import ch.ethz.inf.vs.a2.R;
import ch.ethz.inf.vs.a2.server.ServerService;

public class ServerActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static String ipAddress;
    private static final int port = 8088;
    private static final int PERMISSIONS_REQUEST_CODE = 0;

    private Button mToggleServerStateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSIONS_REQUEST_CODE);
            }
        }

        // Get IP Address
        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        ipAddress = android.text.format.Formatter.formatIpAddress(ip);

        // Set IP Text
        Formatter formatter = new Formatter();
        String ipString = formatter.format(getString(R.string.ip), getIpAddress()).toString();
        TextView txtIp = (TextView) findViewById(R.id.txt_ip);
        txtIp.setText(ipString);

        // Set Port Text
        formatter = new Formatter();
        String port = formatter.format(getString(R.string.port), Integer.toString(getPort())).toString();
        TextView txtPort = (TextView) findViewById(R.id.txt_port);
        txtPort.setText(port);

        // Set Button Text
        mToggleServerStateButton = (Button) findViewById(R.id.btn_toggle_server_state);
        mToggleServerStateButton.setText(getString(!ServerService.isRunning(this) ?
                R.string.start_server : R.string.stop_server));
    }

    public void toggleServerState(View v) {
        if (!ServerService.isRunning(this))
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

    public static String getIpAddress() {
        return ipAddress;
    }

    public static int getPort() {
        return port;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}