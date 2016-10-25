package ch.ethz.inf.vs.a3.udpclient;

import java.net.Socket;

/**
 * Created by linus on 25.10.2016.
 */

public class UDPClient {

    private static Socket socket;

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket){
        UDPClient.socket = socket;
    }

    private UDPClient() {}
}
