package com.robinfinch.journal.app;

import android.app.Fragment;
import android.net.Uri;

import com.robinfinch.journal.app.ui.DetailsActivity;

/**
 * Travel entry details activity.
 *
 * @author Mark Hoogenboom
 */
public class TravelEntryActivity extends DetailsActivity implements TravelEntryFragment.Parent {

    @Override
    protected Fragment newFragmentFor(Uri uri) {
        return TravelEntryFragment.newInstance(uri);
    }

    @Override
    public void onTravelEntryDeleted() {
        finish();
    }
}
