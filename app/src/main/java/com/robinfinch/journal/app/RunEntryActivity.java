package com.robinfinch.journal.app;

import android.app.Fragment;
import android.net.Uri;

import com.robinfinch.journal.app.ui.DetailsActivity;

/**
 * Run entry details activity.
 *
 * @author Mark Hoogenboom
 */
public class RunEntryActivity extends DetailsActivity implements RunEntryFragment.Parent {

    @Override
    protected Fragment newFragmentFor(Uri uri) {
        return RunEntryFragment.newInstance(uri);
    }

    @Override
    public void onRunEntryDeleted() {
        finish();
    }
}
