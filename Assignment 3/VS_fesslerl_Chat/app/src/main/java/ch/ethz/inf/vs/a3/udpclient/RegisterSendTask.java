package ch.ethz.inf.vs.a3.udpclient;

import android.os.AsyncTask;

import java.io.IOException;
import java.lang.Void;
import java.net.DatagramPacket;

/**
 * Created by markus on 25.10.16.
 */

public class RegisterSendTask extends AsyncTask<DatagramPacket, Void, Integer> {

    @Override
    protected Integer doInBackground(DatagramPacket... dps){

        // Check if there is at least one DatagramPacket in the array
        if(dps.length <= 0){

        }

        // Check if socket is valid
        //???

//        try {
//            sock.send(dps[0]);
//        } catch (IOException e) {
//            errorDiag("Could not send registration message.");
//        }

        return (new Integer(0));
    }
}
