package com.robinfinch.journal.app;

import android.app.Fragment;
import android.net.Uri;

import com.robinfinch.journal.app.ui.DetailsActivity;

/**
 * Study entry details activity.
 *
 * @author Mark Hoogenboom
 */
public class StudyEntryActivity extends DetailsActivity implements StudyEntryFragment.Parent {

    @Override
    protected Fragment newFragmentFor(Uri uri) {
        return StudyEntryFragment.newInstance(uri);
    }

    @Override
    public void onStudyEntryDeleted() {
        finish();
    }
}
