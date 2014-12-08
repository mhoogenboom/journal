package com.robinfinch.journal.app;

import android.app.Fragment;
import android.net.Uri;

import com.robinfinch.journal.app.ui.DetailsActivity;

/**
 * Application details activity.
 *
 * @author Mark Hoogenboom
 */
public class ApplicationActivity extends DetailsActivity implements ApplicationFragment.Parent {

    @Override
    protected Fragment newFragmentFor(Uri uri) {
        return ApplicationFragment.newInstance(uri);
    }

    @Override
    public void onApplicationStateUpdated() {
        finish();
    }
}
