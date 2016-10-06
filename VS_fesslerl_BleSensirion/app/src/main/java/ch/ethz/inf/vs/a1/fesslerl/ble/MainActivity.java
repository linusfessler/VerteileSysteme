package ch.ethz.inf.vs.a1.fesslerl.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter btAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    private final static String LOG_LOC = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = bluetoothManager.getAdapter();

        Log.d(LOG_LOC, "### btAdapter created ###");

        // Check if Bluetooth is available (!=null) and enabled (isEnabled())
        if(btAdapter != null){
            // Bluetooth is available
            if(!btAdapter.isEnabled()){
                // Bluetooth is not enabled. Ask user for permission
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            }
            else{
                Log.d(LOG_LOC, "### Bluetooth already enabled ###");
            }
        }
        else{
            Log.d(LOG_LOC, "### Bluetooth not available. ###");
        }
    }
}
