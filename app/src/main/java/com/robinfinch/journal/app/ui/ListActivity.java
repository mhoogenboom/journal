package com.robinfinch.journal.app.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.robinfinch.journal.app.R;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Base class for list activities.
 *
 * @author Mark Hoogenboom
 */
public abstract class ListActivity extends BaseActivity {

    protected boolean singlePaneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        Log.d(LOG_TAG, "Create list activity");

        singlePaneLayout = (findViewById(R.id.container_details) == null);

        if (!singlePaneLayout) {
            showDetails(EmptyFragment.newInstance());
        }

        Fragment fragment = newFragment();
        getFragmentManager().beginTransaction().replace(R.id.container_list, fragment).commit();
    }

    protected abstract Fragment newFragment();

    protected void showDetails(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.container_details, fragment).commit();
    }
}
