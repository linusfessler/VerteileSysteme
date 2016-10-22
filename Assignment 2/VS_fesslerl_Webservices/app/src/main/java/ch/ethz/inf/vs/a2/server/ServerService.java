package ch.ethz.inf.vs.a2.server;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;

import ch.ethz.inf.vs.a2.http.ParsedRequest;
import ch.ethz.inf.vs.a2.resource.RootResource;
import ch.ethz.inf.vs.a2.resource.SensorResource;

/**
 * Created by linus on 16.10.2016.
 */

public class ServerService extends Service {

    public static final int PORT = 8088;

    private Thread serverThread;
    private ServerSocket serverSocket;
    private RootResource rootResource;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            rootResource = new RootResource(new URI("/"));
            SensorManager manager = (SensorManager) getSystemService(SENSOR_SERVICE);
            for(Sensor s : manager.getSensorList(Sensor.TYPE_ALL))
                rootResource.addResource(new SensorResource(new URI(rootResource.getUri().toString() + s.getName().replace(" ", "_")), s, manager));
        } catch (URISyntaxException e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(PORT);

                    while(true) {
                        final Socket clientSocket = serverSocket.accept();
                        (new Thread(new Runnable() {
                            @Override
                            public void run() {
                                handleClientRequest(clientSocket);
                            }
                        })).start();
                    }
                } catch (SocketException sockException){
                    // Do Nothing. Server was stopped.
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        serverThread.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        close();
    }

    public static boolean isRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if (ServerService.class.getName().equals(service.service.getClassName()))
                return true;
        return false;
    }

    private void close() {
        if(serverSocket != null){
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleClientRequest(Socket socket) {
        Log.d(getClass().getName(), "Got request.");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ParsedRequest request = parseRequest(in);

            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.print(rootResource.handleRequest(request));
            out.flush();

            socket.close();
        } catch (Exception e){
            Log.d(getClass().getName(), e.getMessage());
        }
    }

    private ParsedRequest parseRequest(BufferedReader in) throws IOException {

        ParsedRequest request = parseFirstLine(in.readLine());
        parseHeader(in, request);

        return request;
    }

    private ParsedRequest parseFirstLine(String line) throws IOException {
        if (line != null){
            String[] splitLine = line.split("\\s");
            try {
                return new ParsedRequest(splitLine[0], new URI(splitLine[1]));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        throw new IOException("Invalid HTTP request: First line is null.");
    }

    private void parseHeader(BufferedReader in, ParsedRequest request) throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            if(line.isEmpty())
                break;
            String[] split = line.split("\\s");
            request.addHeader(split[0].substring(0,split[0].length() - 1),split[1]);
        }
    }
}


