package com.robinfinch.journal.app.settings;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.robinfinch.journal.app.R;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Settings activity.
 *
 * @author Mark Hoogenboom
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        Log.d(LOG_TAG, "Create settings activity");

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        Fragment fragment = SettingsFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.container_details, fragment).commit();
    }
}
