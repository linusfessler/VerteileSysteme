package ch.ethz.inf.vs.a3.udpclient;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Use method to check if IP value in settings is semantically correct (num of vals, vals, seperator)
        // TODO: Use method to check if IP value in settings is semantically correct (isNumber, valid Value)

        // TODO: Method is deprecated
        addPreferencesFromResource(R.xml.preferences);
    }
}
