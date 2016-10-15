package ch.ethz.inf.vs.a2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import ch.ethz.inf.vs.a2.http.HttpRawRequest;
import ch.ethz.inf.vs.a2.sensor.AbstractSensor;

/**
 * Created by johannes on 15.10.16.
 */

public class RawHttpSensor extends AbstractSensor {
    private final String host;
    private final int port;
    private final String path;

    public RawHttpSensor(String host, int port, String path){
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
            out.print(req.generateRequest(host,port,path));
            out.flush();
            String line =in.readLine();
            while(line != null){
                ret.append(line);
                line = in.readLine();
            }
        }

        return ret.toString();
    }

    @Override
    public double parseResponse(String response) {
        //TODO: reicht das so?
        String tmp = response.split("getterValue")[1];
        boolean writing = false;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; tmp.charAt(i) != '<'; ++i){
            if(writing)
                sb.append(tmp.charAt(i));
            if(tmp.charAt(i) == '>')
                writing = true;
        }
        return Double.parseDouble(sb.toString());
    }
}
