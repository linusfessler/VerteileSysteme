package ch.ethz.inf.vs.a2.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ch.ethz.inf.vs.a2.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startTask1(View v){
        startActivity(new Intent(this, RestActivity.class));
    }

    public void startTask2(View v){
        startActivity(new Intent(this, SoapActivity.class));
    }

    public void startTask3(View v){
        startActivity(new Intent(this, MainActivity.class));
    }
}
