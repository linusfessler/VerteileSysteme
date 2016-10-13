package ch.ethz.inf.vs.a1.fesslerl.ble;

import android.util.Log;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by markus on 13.10.16.
 */

public class TestValueGenerator implements Runnable {

    ConnectedActivity caller;

    private final String LOG_LOC = "TestValueGenerator";


    TestValueGenerator(ConnectedActivity cl){
        caller = cl;
        Log.d(LOG_LOC, "### TestValueGenerator initialized");
    }

    @Override
    public void run(){
        Log.d(LOG_LOC, "### TestValueGenerator running");

        while (true) {
            caller.addTestValues(ThreadLocalRandom.current().nextDouble(-273.15, 1000), ThreadLocalRandom.current().nextDouble(0, 100));
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(LOG_LOC, "#### Value added");
        }
    }
}