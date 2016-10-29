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
    }

    public void onRetrieveChatLogClicked(View v) {
        Button button = ((Button) v);
        button.setEnabled(false);
        button.setText(R.string.btn_retrieving_chat_log);

        if (retrieveChatLog != null)
            retrieveChatLog.cancel(true);
        retrieveChatLog = new RetrieveChatLog();
        retrieveChatLog.execute(this);
    }

    public String getUsername() {
        return username;
    }

    public String getUuid() {
        return uuid;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    // Used to print error messages as dialog.
    private void errorDiag(String message){
        Log.d(LOG_TAG, "#### ERROR: " + message);
    }
}
