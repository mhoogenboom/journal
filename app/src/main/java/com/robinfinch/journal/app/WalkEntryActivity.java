package com.robinfinch.journal.app;

import android.app.Fragment;
import android.net.Uri;

import com.robinfinch.journal.app.ui.DetailsActivity;

/**
 * Walk entry details activity.
 *
 * @author Mark Hoogenboom
 */
public class WalkEntryActivity extends DetailsActivity implements WalkEntryFragment.Parent {

    @Override
    protected Fragment newFragmentFor(Uri uri) {
        return WalkEntryFragment.newInstance(uri);
    }

    @Override
    public void onWalkEntryDeleted() {
        finish();
    }
}
