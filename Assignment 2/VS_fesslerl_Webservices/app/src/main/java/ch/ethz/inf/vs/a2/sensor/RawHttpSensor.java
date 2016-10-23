package ch.ethz.inf.vs.a2.sensor;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import ch.ethz.inf.vs.a2.http.HttpRawRequest;
import ch.ethz.inf.vs.a2.http.HttpRawRequestImpl;
import ch.ethz.inf.vs.a2.sensor.AbstractSensor;

/**
 * Created by johannes on 15.10.16.
 */

public class RawHttpSensor extends AbstractSensor {
    private final String host;
    private final int port;
    private final String path;

    public RawHttpSensor(String host, int port, String path) {
        this.host = host;
        this.port = port;
        this.path = path;
    }

    @Override
    public String executeRequest() throws Exception {
        StringBuilder ret = new StringBuilder();
        try(Socket s = new Socket(host, port);
            PrintWriter out = new PrintWriter(s.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
            HttpRawRequest req = new HttpRawRequestImpl();
            out.print(req.generateRequest(host, port, path));
            out.flush();
            String line = in.readLine();
            while(line != null){
                ret.append(line);
                line = in.readLine();
            }
        }

        return ret.toString();
    }

    @Override
    public double parseResponse(String response) {
        if (!response.contains("200 OK"))
            return 0.0;

        String temp = response.split("GMTConnection: close")[1];
        return Double.parseDouble(temp);
    }
}
