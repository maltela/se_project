package de.fh_muenster.xbankandroid;

import android.preference.PreferenceActivity;
import android.os.Bundle;


public class PrefsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.xbankprefs);
    }
}
