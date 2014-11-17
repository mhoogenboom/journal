package com.robinfinch.journal.app;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;

import com.robinfinch.journal.app.ui.EmptyFragment;
import com.robinfinch.journal.app.ui.ListActivity;

import static com.robinfinch.journal.app.util.Constants.ARG_SELECTED_ID;
import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * List of authors activity.
 *
 * @author Mark Hoogenboom
 */
public class AuthorListActivity extends ListActivity implements
        AuthorListFragment.Parent,
        AuthorFragment.Parent,
        EmptyFragment.Parent {

    private static final int REQUEST_SELECT_AUTHOR = 1;

    @Override
    protected Fragment newFragment() {
        return AuthorListFragment.newInstance();
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_AUTHOR:
                onAuthorActivityResult(resultCode, data);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onAuthorActivityResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            long id = data.getLongExtra(ARG_SELECTED_ID, 0L);
            onAuthorSelected(id);
        }
    }

    @Override
    public void onAuthorItemSelected(Uri uri) {
        if (singlePaneLayout) {
            Intent intent = new Intent(this, AuthorActivity.class)
                    .putExtra(ARG_URI, uri);
            startActivityForResult(intent, REQUEST_SELECT_AUTHOR);
        } else {
            showDetails(AuthorFragment.newInstance(uri));
        }
    }

    @Override
    public void onAuthorSelected(long id) {
        Intent result = new Intent();
        result.putExtra(ARG_SELECTED_ID, id);

        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onAuthorDeleted() {
        showDetails(EmptyFragment.newInstance());
    }

}
