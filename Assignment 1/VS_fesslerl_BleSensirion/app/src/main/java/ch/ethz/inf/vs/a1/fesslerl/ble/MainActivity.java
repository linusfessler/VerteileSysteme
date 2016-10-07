package ch.ethz.inf.vs.a1.fesslerl.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter btAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    private final static String LOG_LOC = "MainActivity";
    private final static int MAX_SCAN_TIME = 10000;
    private ScanCallback scanCallback;                          // TODO
    private Handler handler;                                    // Use this handler to schedule a runnable that stops the Le scanning after MAX_SCAN_TIME


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
            Log.d(LOG_LOC, "### Bluetooth is available ###");
            if(!btAdapter.isEnabled()){
                // Bluetooth is not enabled. Ask user for permission
                Log.d(LOG_LOC, "### Bluetooth is not enabled ###");
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BT);
                // Bluetooth enabled and ready
                // Scan for Bluetooth devices

                scanForDevices();

            }
            else{
                Log.d(LOG_LOC, "### Bluetooth already enabled ###");
            }
        }
        else{
            Log.d(LOG_LOC, "### Bluetooth not available. ###");
        }
    }

    private void scanForDevices(){

        final BluetoothLeScanner scanner = btAdapter.getBluetoothLeScanner();
        List<ScanFilter> scanFilters= new ArrayList<ScanFilter>();

//        ScanFilter.Builder builder = new ScanFilter.Builder();
//        builder.setServiceUuid(SensirionSHT31UUIDS.UUID_TEMPERATURE_SERVICE);   //???
//        scanFilters.add(builder.build());


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scanner.stopScan(scanCallback);
            }
        }, MAX_SCAN_TIME);

        scanner.startScan(scanCallback);
//        scanner.startScan(scanFilters, ScanSettings.CALLBACK_TYPE_FIRST_MATCH, scanCallback);

    }


}
