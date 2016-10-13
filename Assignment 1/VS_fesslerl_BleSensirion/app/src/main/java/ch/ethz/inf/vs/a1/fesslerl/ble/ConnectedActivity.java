package ch.ethz.inf.vs.a1.fesslerl.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.jjoe64.graphview.GraphView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import static ch.ethz.inf.vs.a1.fesslerl.ble.SensirionSHT31UUIDS.*;

public class ConnectedActivity extends AppCompatActivity {

    BluetoothDevice device;

    BluetoothGattService humidityService;
    BluetoothGattService temperatureService;

    BluetoothGatt gatt;

    GraphView graphTemp;
    GraphView graphHum;
    LiveGraphManager gm;

    TextView textTemp;
    TextView textHum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);

        //device = getIntent().getParcelableExtra(BleGlobalConsts.DEVICE_KEY);

        // Initialize GraphViews
        graphTemp = (GraphView) findViewById(R.id.graph_temperature);
        graphHum  = (GraphView) findViewById(R.id.graph_humidity);
        gm = new LiveGraphManager(graphTemp, graphHum);

        textTemp = (TextView) findViewById(R.id.textTemp);
        textHum = (TextView) findViewById(R.id.textHum);

        gm.updateTempGraph(0, textTemp);
        gm.updateTempGraph(0, textHum);

        // For testing
        new Thread(new TestValueGenerator(100, this)).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setViewConnectionState(false);
        //connectToDevice();
    }

    private void connectToDevice(){
        gatt = device.connectGatt(this, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                switch (newState){
                    case BluetoothProfile.STATE_CONNECTED:
                        setViewConnectionState(true);
                        gatt.discoverServices();
                        break;
                    case BluetoothProfile.STATE_DISCONNECTED:
                        setViewConnectionState(false);
                        closeConnection();
                        //Try to reestablish connection
                        connectToDevice();
                        break;
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                Log.d("###connected", "discovered services.");
                humidityService = gatt.getService(UUID_HUMIDITY_SERVICE);
                temperatureService = gatt.getService(UUID_TEMPERATURE_SERVICE);
                //TODO: Works without step 10. (Overriding characteristics). Should we still implement it?
                //BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(UUID_HUMIDITY_CHARACTERISTIC,BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY, BluetoothGattCharacteristic.PERMISSION_WRITE | BluetoothGattCharacteristic.PERMISSION_READ );
                //humidityService.addCharacteristic(characteristic);
                setNotificationDescriptor(humidityService.getCharacteristic(UUID_HUMIDITY_CHARACTERISTIC));

            }


            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                //TODO: Use Graph to show values. (Step 14).
                float val = convertRawValue(characteristic.getValue());
                if(characteristic.getUuid().equals(UUID_HUMIDITY_CHARACTERISTIC)){
                    gm.updateTempGraph(val, textTemp);
                }else{
                    gm.updateTempGraph(val, textHum);
                }
                //Log.d("###connected:", (characteristic.getUuid().equals(UUID_HUMIDITY_CHARACTERISTIC) ? "Humidity: " : "Temp: ") + convertRawValue(characteristic.getValue()));
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
                if(descriptor.getCharacteristic().getUuid().equals(UUID_HUMIDITY_CHARACTERISTIC))
                    setNotificationDescriptor(temperatureService.getCharacteristic(UUID_TEMPERATURE_CHARACTERISTIC));
            }
        });
    }

    private void setNotificationDescriptor(BluetoothGattCharacteristic characteristic){
        gatt.setCharacteristicNotification(characteristic, true);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(NOTIFICATION_DESCRIPTOR_UUID);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        gatt.writeDescriptor(descriptor);
    }
    private float convertRawValue(byte[] raw) {
        ByteBuffer wrapper = ByteBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN);
        return wrapper.getFloat();
    }
    @Override
    protected void onStop() {
        super.onStop();
        //closeConnection();
    }

    private void closeConnection(){
        gatt.close();
        gatt = null;
    }

    private void setViewConnectionState(final boolean connected){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView connView = (TextView) findViewById(R.id.connectionState);
                connView.setText(connected ? getText(R.string.device_connected) : getText(R.string.device_not_connected));
            }
        });
    }

    // For testing
    public void addTestValue(final double tempVal, final double humVal) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gm.updateTempGraph(tempVal, textTemp);
                gm.updateHumGraph(humVal, textHum);
            }
        });
    }
}


