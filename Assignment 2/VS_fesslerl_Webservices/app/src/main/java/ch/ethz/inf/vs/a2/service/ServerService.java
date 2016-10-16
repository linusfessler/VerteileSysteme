package ch.ethz.inf.vs.a2.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.lang.reflect.Method;

/**
 * Created by linus on 16.10.2016.
 */

public class ServerService extends Service {

    public static final int PORT = 8088;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isHotSpotOn())
            toggleHotSpot();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isHotSpotOn())
            toggleHotSpot();
    }

    public static boolean isRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if (ServerService.class.getName().equals(service.service.getClassName()))
                return true;
        return false;
    }

    private boolean isHotSpotOn() {
        WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean toggleHotSpot() {
        WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wificonfiguration = null;
        try {
            // if WiFi is on, turn it off
            if (isHotSpotOn())
                wifimanager.setWifiEnabled(false);
            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifimanager, wificonfiguration, !isHotSpotOn()); // Doesn't work, needs some permission I think
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
