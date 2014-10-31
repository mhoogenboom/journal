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
 * List of courses activity.
 *
 * @author Mark Hoogenboom
 */
public class CourseListActivity extends BaseActivity implements
        CourseListFragment.Parent,
        CourseFragment.Parent {

    private static final int REQUEST_SELECT_COURSE = 1;

    private boolean singlePaneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        Log.d(LOG_TAG, "Create course list activity");

        singlePaneLayout = (findViewById(R.id.container_details) == null);

        if (!singlePaneLayout) {
            showDetails(EmptyFragment.newInstance());
        }

        Fragment fragment = CourseListFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.container_list, fragment).commit();
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

    private void showDetails(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.container_details, fragment).commit();
    }
}
