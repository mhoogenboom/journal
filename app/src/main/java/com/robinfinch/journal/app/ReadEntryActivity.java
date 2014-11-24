package com.robinfinch.journal.app;

import android.app.Fragment;
import android.net.Uri;

import com.robinfinch.journal.app.ui.DetailsActivity;

/**
 * Read entry details activity.
 *
 * @author Mark Hoogenboom
 */
public class ReadEntryActivity extends DetailsActivity implements ReadEntryFragment.Parent {

    @Override
    protected Fragment newFragmentFor(Uri uri) {
        return ReadEntryFragment.newInstance(uri);
    }

    @Override
    public void onReadEntryDeleted() {
        finish();
    }
}
