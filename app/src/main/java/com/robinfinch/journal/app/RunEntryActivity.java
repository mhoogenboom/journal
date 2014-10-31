package com.robinfinch.journal.app;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import static com.robinfinch.journal.app.util.Constants.ARG_URI;
import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Run entry details activity.
 *
 * @author Mark Hoogenboom
 */
public class RunEntryActivity extends BaseActivity implements RunEntryFragment.Parent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        Uri uri = getIntent().getParcelableExtra(ARG_URI);

        Log.d(LOG_TAG, "Create run entry activity for " + uri);

        Fragment fragment = RunEntryFragment.newInstance(uri);
        getFragmentManager().beginTransaction().replace(R.id.container_details, fragment).commit();
    }

    @Override
    public void onRunEntryDeleted() {
        finish();
    }
}
