package com.robinfinch.journal.app.ui;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.robinfinch.journal.app.R;

import static com.robinfinch.journal.app.util.Constants.ARG_URI;
import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Base class for details activities.
 *
 * @author Mark Hoogenboom
 */
public abstract class DetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        Uri uri = getIntent().getParcelableExtra(ARG_URI);

        Log.d(LOG_TAG, "Create details activity for " + uri);

        Fragment fragment = newFragmentFor(uri);
        getFragmentManager().beginTransaction().replace(R.id.container_details, fragment).commit();
    }

    protected abstract Fragment newFragmentFor(Uri uri);
}
