package ch.ethz.inf.vs.a3.udpclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String UNAME = "USERNAME";
    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    // Execute when user klicks on Settings menu
    public void onSettingsClicked(MenuItem Item){
        Intent toSettings = new Intent(this, SettingsActivity.class);
        startActivity(toSettings);
    }

    public void onJoinClicked(View v){

        Log.d(LOG_TAG, "### Clicked Join");

        // Get username from text input
        EditText uname_input = (EditText) findViewById(R.id.uname_input);
        String uname = uname_input.getText().toString();

        // Only use first line as username
        String[] temp = uname.split("\n", 2);
        uname = temp[0];

        Intent toChat = new Intent(this, ChatActivity.class);
        toChat.putExtra(UNAME, uname);
        startActivity(toChat);
    }
}
