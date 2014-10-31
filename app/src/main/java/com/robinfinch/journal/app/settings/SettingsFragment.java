package com.robinfinch.journal.app.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.robinfinch.journal.app.R;

/**
 * Settings fragment.
 *
 * @author Mark Hoogenboom
 */
public class SettingsFragment extends PreferenceFragment {

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

         addPreferencesFromResource(R.xml.settings);
    }
}
