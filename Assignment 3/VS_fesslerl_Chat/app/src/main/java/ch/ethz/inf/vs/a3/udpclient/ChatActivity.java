package ch.ethz.inf.vs.a3.udpclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.net.DatagramSocket;
import java.util.PriorityQueue;

import ch.ethz.inf.vs.a3.message.Message;

public class ChatActivity extends AppCompatActivity {

    private String username;
    private String uuid;
    private DatagramSocket socket;
    private PriorityQueue<Message> buffer = new PriorityQueue<Message>();

    private TextView usernameText;

    private final static String LOG_TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        username = getIntent().getStringExtra(MainActivity.USERNAME);
        uuid = getIntent().getStringExtra(MainActivity.UUID);

        usernameText = (TextView) findViewById(R.id.text_username);
        usernameText.setText(username);
    }

    // TODO: Implement dialog creation for displaying error messages
    // Used to print error messages as dialog.
    private void errorDiag(String message){
        Log.d(LOG_TAG, "#### ERROR: " + message);
    }
}
