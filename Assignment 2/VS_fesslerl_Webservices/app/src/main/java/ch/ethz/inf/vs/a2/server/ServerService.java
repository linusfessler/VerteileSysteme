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
import java.util.HashMap;
import java.util.Map;

import ch.ethz.inf.vs.a2.http.HttpResponse;
import ch.ethz.inf.vs.a2.http.ParsedRequest;
import ch.ethz.inf.vs.a2.resource.Resource;
import ch.ethz.inf.vs.a2.resource.RootResource;
import ch.ethz.inf.vs.a2.resource.SensorResource;

/**
 * Created by linus on 16.10.2016.
 */

public class ServerService extends Service {
    private static final String LOGGING_TAG = "###ServerService";
    public static final int PORT = 8088;


    private Thread serverThread;
    private ServerSocket serverSocket;

    private Map<String, Resource> resourceMap;
    private RootResource rootResource;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        resourceMap = new HashMap<>();
        rootResource = new RootResource();

        SensorManager manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        resourceMap.put("/", rootResource);

        try {
            for(Sensor s : manager.getSensorList(Sensor.TYPE_ALL))
                addResource("/"+ s.getName().replace(" ","_"), new SensorResource(s, manager));
        } catch (URISyntaxException e){
            e.printStackTrace();
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
                    //Do Nothing. Server was stopped.
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

    private void close(){
        if(serverSocket != null){
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleClientRequest(Socket socket){
        Log.d(LOGGING_TAG, "got request.");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ParsedRequest request = parseRequest(in);

            //forward request
            String response = resourceMap.containsKey(request.path) ? resourceMap.get(request.path).handleRequest(request) : HttpResponse.generateErrorResponse("No such resource");

            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.print(response);
            out.flush();

            socket.close();
        } catch (Exception e){
            Log.d(LOGGING_TAG, "Error while handling client request.");
            e.printStackTrace();
        }
    }

    private ParsedRequest parseRequest(BufferedReader in) throws IOException{

        ParsedRequest request = parseFirstLine(in.readLine());
        parseHeader(in, request);

        return request;
    }

    private ParsedRequest parseFirstLine(String line) throws IOException{

        if(line != null){
            String[] splitLine = line.split("\\s");
            return new ParsedRequest(splitLine[0], splitLine[1]);
        }
        throw new IOException("Invalid HTTP request: Unable to parse first line.");
    }

    private void parseHeader(BufferedReader in, ParsedRequest request) throws IOException{
        String line;
        while ((line = in.readLine()) != null) {
            if(line.isEmpty())
                break;
            String[] split = line.split("\\s");
            request.addHeader(split[0].substring(0,split[0].length() - 1),split[1]);
        }
    }

    private void addResource(String path, Resource resource) throws URISyntaxException{
        resourceMap.put(path, resource);
        rootResource.addResource(new URI(path));
    }
}


