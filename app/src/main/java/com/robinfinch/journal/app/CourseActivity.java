package com.robinfinch.journal.app;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import static com.robinfinch.journal.app.util.Constants.ARG_SELECTED_ID;
import static com.robinfinch.journal.app.util.Constants.ARG_URI;
import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Course details activity.
 *
 * @author Mark Hoogenboom
 */
public class CourseActivity extends BaseActivity implements CourseFragment.Parent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        Uri uri = getIntent().getParcelableExtra(ARG_URI);

        Log.d(LOG_TAG, "Create course activity for " + uri);

        Fragment fragment = CourseFragment.newInstance(uri);
        getFragmentManager().beginTransaction().replace(R.id.container_details, fragment).commit();
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
