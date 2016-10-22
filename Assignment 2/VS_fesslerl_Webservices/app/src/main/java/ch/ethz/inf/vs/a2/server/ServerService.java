package ch.ethz.inf.vs.a2.server;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.Vibrator;
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
import java.util.Map;

import ch.ethz.inf.vs.a2.activity.ServerActivity;
import ch.ethz.inf.vs.a2.http.HttpResponse;
import ch.ethz.inf.vs.a2.http.ParsedRequest;
import ch.ethz.inf.vs.a2.resource.FlashlightResource;
import ch.ethz.inf.vs.a2.resource.Resource;
import ch.ethz.inf.vs.a2.resource.RootResource;
import ch.ethz.inf.vs.a2.resource.SensorResource;
import ch.ethz.inf.vs.a2.resource.VibratorResource;

/**
 * Created by linus on 16.10.2016.
 */

public class ServerService extends Service {

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
            for (Sensor s : manager.getSensorList(Sensor.TYPE_ALL))
                rootResource.addResource(new SensorResource(new URI(rootResource.getUri().toString() + s.getName().replace(" ", "_")), s, manager));

            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            rootResource.addResource(new VibratorResource((new URI(rootResource.getUri().toString() + "Vibrator_(Actuator)")), vibrator));

            boolean hasFlashLight = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) &&
                    getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
            rootResource.addResource(new FlashlightResource((new URI(rootResource.getUri().toString() + "Flashlight_(Actuator)")), hasFlashLight));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(ServerActivity.getPort());

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
            ParsedRequest request = parseRequest(in, socket);

            String response;
            if (rootResource.getUri().toString().equals(request.uri.toString()))
                response = rootResource.handleRequest(request);
            else {
                Resource resource = rootResource.getResource(request.uri);
                response = resource != null ?
                        resource.handleRequest(request) :
                        HttpResponse.generateErrorResponse("404 Not Found", "Resource not found");
            }

            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.print(response);
            out.flush();

            if (socket.isConnected())
                socket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private ParsedRequest parseRequest(BufferedReader in, Socket socket) throws IOException {
        ParsedRequest request = parseFirstLine(in.readLine());
        parseHeader(in, request);
        parseContent(in, request);

        return request;
    }

    private ParsedRequest parseFirstLine(String line) throws IOException {
        if (line != null) {
            Log.d("###############", line);
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
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            Log.d("###############", line);
            String[] split = line.split("\\s");
            request.addHeader(split[0].substring(0,split[0].length() - 1),split[1]);
        }
    }

    private void parseContent(BufferedReader in, ParsedRequest request) throws IOException {
        if (!request.method.equals("POST") || !request.header.containsKey("Content-Length"))
            return;

        int length = Integer.parseInt(request.header.get("Content-Length"));
        char[] cbuf = new char[length];
        in.read(cbuf, 0, length);

        String content = new String(cbuf);
        String[] split = content.split("&");
        for (String s : split) {
            String[] subSplit = s.split("=");
            request.addContent(subSplit[0], subSplit[1]);
        }
    }
}


