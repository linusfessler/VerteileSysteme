package ch.ethz.inf.vs.a3.udpclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.PriorityQueue;
import java.util.UUID;

import ch.ethz.inf.vs.a3.message.Message;
import ch.ethz.inf.vs.a3.message.MessageTypes;

public class ChatActivity extends AppCompatActivity {

    private String uname;
    private TextView textview_username;
    private DatagramSocket sock;
    private String uuid;
    private InetAddress toAddr;
    private int port;

    private PriorityQueue<Message> buffer = new PriorityQueue<Message>();

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

        // create UUID
        uuid = UUID.randomUUID().toString();
    }

    // Implement connection establishment here.
    @Override
    protected void onStart(){
        super.onStart();
    }

    // Implement connection tear-down here.
    @Override
    protected void onStop(){
        super.onStop();
    }

    // TODO: Implement dialog creation for displaying error messages
    // Used to print error messages as dialog.
    private void errorDiag(String message){
        Log.d(LOG_TAG, "#### ERROR: " + message);
    }
}
