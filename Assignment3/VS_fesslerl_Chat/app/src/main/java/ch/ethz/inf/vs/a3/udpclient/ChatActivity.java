package ch.ethz.inf.vs.a3.udpclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {

    private String uname;
    private TextView textview_username;

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
}
