package com.robinfinch.journal.app;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import static com.robinfinch.journal.app.util.Constants.ARG_URI;
import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Travel entry details activity.
 *
 * @author Mark Hoogenboom
 */
public class TravelEntryActivity extends BaseActivity implements TravelEntryFragment.Parent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        Uri uri = getIntent().getParcelableExtra(ARG_URI);

        Log.d(LOG_TAG, "Create travel entry activity for " + uri);

        Fragment fragment = TravelEntryFragment.newInstance(uri);
        getFragmentManager().beginTransaction().replace(R.id.container_details, fragment).commit();
    }

    @Override
    public void onTravelEntryDeleted() {
        finish();
    }
}
