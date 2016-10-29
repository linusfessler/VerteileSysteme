package ch.ethz.inf.vs.a3.udpclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import ch.ethz.inf.vs.a3.message.Message;
import ch.ethz.inf.vs.a3.message.MessageTypes;

public class ChatActivity extends AppCompatActivity {

    private String username;
    private String uuid;
    private InetAddress ipAddress;
    private int port;
    private DatagramSocket socket;

    private static RetrieveChatLog retrieveChatLog;

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

        setTitle("Distributed Chat, connected as " + username);

        socket = UDPClient.getSocket();

        /*if (retrieveChatLog != null && retrieveChatLog.getStatus() != AsyncTask.Status.FINISHED) {
            Log.d("###################", "#################");
            Button button = ((Button) findViewById(R.id.btn_retrieve_chat_log));
            button.setEnabled(false);
            button.setText(R.string.btn_retrieving_chat_log);
        }*/
    }

    public void onRetrieveChatLogClicked(View v) {
        Button button = ((Button) v);
        button.setEnabled(false);
        button.setText(R.string.btn_retrieving_chat_log);

        // Create message buffer
        byte[] buf = new byte[NetworkConsts.PAYLOAD_SIZE];
        try {
            buf = new Message(username, uuid, null, MessageTypes.RETRIEVE_CHAT_LOG, null).json.getBytes();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Build packet
        DatagramPacket packet = new DatagramPacket(buf, 0, buf.length, ipAddress, port);

        try {
            socket.send(packet);
        } catch (IOException e) {
            errorDiag("Could not send registration message.");
        }

        if (retrieveChatLog != null)
            retrieveChatLog.cancel(true);
        retrieveChatLog = new RetrieveChatLog();
        retrieveChatLog.execute(this);
    }

    // Used to print error messages as dialog.
    private void errorDiag(String message){
        Log.d(LOG_TAG, "#### ERROR: " + message);
    }
}
