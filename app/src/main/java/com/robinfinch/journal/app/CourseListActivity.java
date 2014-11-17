package com.robinfinch.journal.app;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.robinfinch.journal.app.ui.BaseActivity;
import com.robinfinch.journal.app.ui.EmptyFragment;
import com.robinfinch.journal.app.ui.ListActivity;

import static com.robinfinch.journal.app.util.Constants.ARG_SELECTED_ID;
import static com.robinfinch.journal.app.util.Constants.ARG_URI;
import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * List of courses activity.
 *
 * @author Mark Hoogenboom
 */
public class CourseListActivity extends ListActivity implements
        CourseListFragment.Parent,
        CourseFragment.Parent,
        EmptyFragment.Parent {

    private static final int REQUEST_SELECT_COURSE = 1;

    @Override
    protected Fragment newFragment() {
        return CourseListFragment.newInstance();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_COURSE:
                onCourseActivityResult(resultCode, data);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onCourseActivityResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            long id = data.getLongExtra(ARG_SELECTED_ID, 0L);
            onCourseSelected(id);
        }
    }

    @Override
    public void onCourseItemSelected(Uri uri) {
        if (singlePaneLayout) {
            Intent intent = new Intent(this, CourseActivity.class)
                    .putExtra(ARG_URI, uri);
            startActivityForResult(intent, REQUEST_SELECT_COURSE);
        } else {
            showDetails(CourseFragment.newInstance(uri));
        }
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
        showDetails(EmptyFragment.newInstance());
    }

}
