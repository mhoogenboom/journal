package com.robinfinch.journal.app;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;

import com.robinfinch.journal.app.ui.DetailsActivity;

import static com.robinfinch.journal.app.util.Constants.ARG_SELECTED_ID;

/**
 * Course details activity.
 *
 * @author Mark Hoogenboom
 */
public class CourseActivity extends DetailsActivity implements CourseFragment.Parent {

    @Override
    protected Fragment newFragmentFor(Uri uri) {
        return CourseFragment.newInstance(uri);
    }

    @Override
    public void onCourseSelected(long id) {
        Intent result = new Intent();
        result.putExtra(ARG_SELECTED_ID, id);

        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onCourseDeleted() {
        finish();
    }
}
