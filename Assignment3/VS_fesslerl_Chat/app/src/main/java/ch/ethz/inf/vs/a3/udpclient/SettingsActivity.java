package ch.ethz.inf.vs.a3.udpclient;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: Method is deprecated
        addPreferencesFromResource(R.xml.preferences);
    }
}
