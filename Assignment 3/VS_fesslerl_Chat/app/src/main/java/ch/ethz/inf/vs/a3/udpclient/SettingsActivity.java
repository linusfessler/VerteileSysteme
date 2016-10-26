package ch.ethz.inf.vs.a3.udpclient;

import android.preference.PreferenceActivity;
import android.os.Bundle;

public class SettingsActivity extends PreferenceActivity {

    public static final String PREF_IP = "pref_ip";
    public static final String PREF_PORT = "pref_port";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Create method that listens to change of settings and checks if it is valic
        // TODO: Implement a button in the SettingsActivity to reset all settings to default

        // TODO: Method is deprecated
        addPreferencesFromResource(R.xml.preferences);
    }

    // This static method checks if a string is a valid IP address
    public static boolean checkIp(String ip){

        // Check if ip is nonnull and nonempty
        if(ip == null || ip.isEmpty())
            return false;

        // Check if ip consists of 4 separate numbers separated by "."
        String[] nums = ip.split("\\.");
        if(nums.length != 4)
            return false;

        // Check if string was split on beginning or end (dot at beginning or end)
        if(ip.startsWith("\\.") || ip.endsWith("\\."))
            return false;

        // Check each byte of ip if it is in valid range (0 to 255)
        for(String n: nums){
            if(Integer.parseInt(n) > 255 || Integer.parseInt(n) < 0)
                return false;
        }

        return true;
    }

    public static boolean checkPort(String port){

        // Try parsing th String to an integer. If it does not work, its not a valid port.
        try{
            int portnum = Integer.parseInt(port);
            return checkPort(portnum);
        }catch (NumberFormatException e){
            return false;
        }
    }

    // This static method checks if an integer is a valid port number
    public static boolean checkPort(int port){

        // only needs to check if port is within unsigned 16bit range
        if(port < 0 || port > 65535)
            return false;
        else
            return true;

    }
}
