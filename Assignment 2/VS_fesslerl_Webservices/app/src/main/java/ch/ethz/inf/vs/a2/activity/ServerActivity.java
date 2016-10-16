package ch.ethz.inf.vs.a2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Formatter;

import ch.ethz.inf.vs.a2.R;
import ch.ethz.inf.vs.a2.service.ServerService;

public class ServerActivity extends AppCompatActivity {

    private static String LOG_LOC = "###ServerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        Formatter formatter = new Formatter();
        String port = formatter.format(getString(R.string.port), Integer.toString(ServerService.PORT)).toString();

        TextView textView = (TextView) findViewById(R.id.txt_port);
        textView.setText(port);

        Button button = (Button) findViewById(R.id.btn_toggle_server_state);
        button.setText(getString(!ServerService.isRunning(this) ?
                R.string.start_server : R.string.stop_server));
    }

    public void toggleServerState(View v) {
        Button button = (Button) v;
        Intent intent = new Intent(this, ServerService.class);
        Log.d(LOG_LOC, Boolean.toString(ServerService.isRunning(this)));
        if (!ServerService.isRunning(this)) {
            button.setText(getString(R.string.stop_server));
            startService(intent);
        } else {
            button.setText(getString(R.string.start_server));
            stopService(intent);
        }
    }
}