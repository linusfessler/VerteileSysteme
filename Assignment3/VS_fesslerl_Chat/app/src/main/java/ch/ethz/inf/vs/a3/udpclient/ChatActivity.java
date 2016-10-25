package ch.ethz.inf.vs.a3.udpclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ChatActivity extends AppCompatActivity {

    private String uname;
    private TextView textview_username;
    private DatagramSocket sock;

    private final static String IP_ADDRESS = "192.168.1.1";

    private final static String LOG_TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Log.d(LOG_TAG, "### In ChatView");

        // Get username
        uname = getIntent().getStringExtra(MainActivity.UNAME);

        Log.d(LOG_TAG, "### Username: " + uname);

        textview_username = (TextView) findViewById(R.id.text_username);
        textview_username.setText(uname);
    }

    // Implement connection establishment here.
    @Override
    protected void onStart(){
        super.onStart();
        connectToServer();
    }

    // Implement connection tear-down here.
    @Override
    protected void onStop(){
        super.onStop();
    }

    private void connectToServer(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);


        // TODO: Get port value from settings, not from constants.
        int port = NetworkConsts.UDP_PORT;


//        try{
//            port = Integer.parseInt(preferences.getString("pref_port1234", "0"));
//        }
//        catch(NumberFormatException e){
//            Log.d(LOG_TAG, "#### ERROR: Could not parse integer");
//            errorDiag(getString(R.string.error_port));
//            return;
//        }

        // TODO: Move this part of error handling to SettingsActivity class.
        // Error if port value is too small or too big
        if (port < 1025 || port > 65535){
            Log.d(LOG_TAG, "#### ERROR: Port value not in bounds.");
            errorDiag(getString(R.string.error_port));
            return;
        }

        Log.d(LOG_TAG, "### Reading preferences: Port = " + port);


        // Create new UDP Socket

        try {
            sock = new DatagramSocket(port);
        }catch(SocketException e){
            errorDiag("Could not bind socket to port " + port);
            e.printStackTrace();
        }

        try {
            sock.setSoTimeout(NetworkConsts.SOCKET_TIMEOUT);
        } catch (SocketException e) {
            errorDiag("Could not set socket timeout " + NetworkConsts.SOCKET_TIMEOUT);
        }

        byte[] buf = createRegisterPacket();

        // TODO: Get this value from settings
        String ipString = IP_ADDRESS;
        InetAddress toAddr = null;
        try {
            toAddr = InetAddress.getByName(ipString);
        } catch (UnknownHostException e) {
            errorDiag("Could not get IP Address by name " + ipString);
        }

        // Build packet
        DatagramPacket registerPacket = new DatagramPacket(buf, 0, buf.length, toAddr, port);


    }

    // TODO: Actually build a resistration packet
    private byte[] createRegisterPacket(){
        return "Placeholder".getBytes();
    }

    // TODO: Implement dialog creation for displaying error messages
    // Used to print error messages as dialog.
    private void errorDiag(String message){
        Log.d(LOG_TAG, "#### ERROR: " + message);
    }

}
