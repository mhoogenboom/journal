package com.robinfinch.journal.app;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;

import com.robinfinch.journal.app.ui.EmptyFragment;
import com.robinfinch.journal.app.ui.ListActivity;

import static com.robinfinch.journal.app.util.Constants.ARG_SELECTED_ID;
import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * List of recruiters activity.
 *
 * @recruiter Mark Hoogenboom
 */
public class RecruiterListActivity extends ListActivity implements
        RecruiterListFragment.Parent,
        RecruiterFragment.Parent,
        EmptyFragment.Parent {

    private static final int REQUEST_SELECT_RECRUITER = 1;

    @Override
    protected Fragment newFragment() {
        return RecruiterListFragment.newInstance();
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_RECRUITER:
                onRecruiterActivityResult(resultCode, data);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onRecruiterActivityResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            long id = data.getLongExtra(ARG_SELECTED_ID, 0L);
            onRecruiterSelected(id);
        }
    }

    @Override
    public void onRecruiterItemSelected(Uri uri) {
        if (singlePaneLayout) {
            Intent intent = new Intent(this, RecruiterActivity.class)
                    .putExtra(ARG_URI, uri);
            startActivityForResult(intent, REQUEST_SELECT_RECRUITER);
        } else {
            showDetails(RecruiterFragment.newInstance(uri));
        }
    }

    @Override
    public void onRecruiterSelected(long id) {
        Intent result = new Intent();
        result.putExtra(ARG_SELECTED_ID, id);

        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onRecruiterDeleted() {
        showDetails(EmptyFragment.newInstance());
    }

}
