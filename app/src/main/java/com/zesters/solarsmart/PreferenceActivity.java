package com.zesters.solarsmart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.widget.EditText;

/**
 * Created by Angelo on 14/12/2016.
 */

public class PreferenceActivity extends android.preference.PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        EditTextPreference ip = (EditTextPreference) findPreference(getString(R.string.ip_preference));
        EditTextPreference port = (EditTextPreference) findPreference(getString(R.string.port_preference));
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        ip.setText(prefs.getString(getString(R.string.ip_preference),""));
        port.setText(prefs.getString(getString(R.string.port_preference), "10001"));
    }
}
