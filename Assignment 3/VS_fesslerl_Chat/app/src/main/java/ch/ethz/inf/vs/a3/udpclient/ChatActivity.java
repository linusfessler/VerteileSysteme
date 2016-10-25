package ch.ethz.inf.vs.a3.udpclient;

import android.net.Network;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.PriorityQueue;

import ch.ethz.inf.vs.a3.message.Message;
import ch.ethz.inf.vs.a3.message.MessageTypes;

public class ChatActivity extends AppCompatActivity {

    private String username;
    private String uuid;
    InetAddress ipAddress;
    int port;
    private DatagramSocket socket;

    private TextView usernameText;

    private final static String LOG_TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        username = getIntent().getStringExtra(MainActivity.USERNAME);
        uuid = getIntent().getStringExtra(MainActivity.UUID);
        try {
            ipAddress = InetAddress.getByName(getIntent().getStringExtra(MainActivity.IP));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        port = getIntent().getIntExtra(MainActivity.PORT, NetworkConsts.UDP_PORT);

        usernameText = (TextView) findViewById(R.id.txt_username);
        usernameText.setText(username);

        socket = UDPClient.getSocket();
    }

    public void onRetrieveChatLogClicked(View v) {
        // Create Register Packet message
        byte[] buf = new byte[NetworkConsts.PAYLOAD_SIZE];
        try {
            buf = new Message(username, uuid, null, MessageTypes.RETRIEVE_CHAT_LOG, null).json.getBytes();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Build packet
        DatagramPacket registerPacket = new DatagramPacket(buf, 0, buf.length, ipAddress, port);

        try {
            socket.send(registerPacket);
        } catch (IOException e) {
            errorDiag("Could not send registration message.");
        }

        new RetrieveChatLog().execute(this);
    }

    // Used to print error messages as dialog.
    private void errorDiag(String message){
        Log.d(LOG_TAG, "#### ERROR: " + message);
    }
}
