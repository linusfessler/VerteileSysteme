package ch.ethz.inf.vs.a3.udpclient;

import java.net.DatagramSocket;
import java.net.Socket;

/**
 * Created by linus on 25.10.2016.
 */

public class UDPClient {

    private static DatagramSocket socket;

    public static synchronized DatagramSocket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(DatagramSocket socket){
        UDPClient.socket = socket;
    }

    private UDPClient() {}
}
