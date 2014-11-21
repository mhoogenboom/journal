package com.robinfinch.journal.app;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;

import com.robinfinch.journal.app.ui.EmptyFragment;
import com.robinfinch.journal.app.ui.ListActivity;

import static com.robinfinch.journal.app.util.Constants.ARG_SELECTED_ID;
import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * List of titles activity.
 *
 * @title Mark Hoogenboom
 */
public class TitleListActivity extends ListActivity implements
        TitleListFragment.Parent,
        TitleFragment.Parent,
        EmptyFragment.Parent {

    private static final int REQUEST_SELECT_TITLE = 1;

    @Override
    protected Fragment newFragment() {
        return TitleListFragment.newInstance();
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_TITLE:
                onTitleActivityResult(resultCode, data);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onTitleActivityResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            long id = data.getLongExtra(ARG_SELECTED_ID, 0L);
            onTitleSelected(id);
        }
    }

    @Override
    public void onTitleItemSelected(Uri uri) {
        if (singlePaneLayout) {
            Intent intent = new Intent(this, TitleActivity.class)
                    .putExtra(ARG_URI, uri);
            startActivityForResult(intent, REQUEST_SELECT_TITLE);
        } else {
            showDetails(TitleFragment.newInstance(uri));
        }
    }

    @Override
    public void onTitleSelected(long id) {
        Intent result = new Intent();
        result.putExtra(ARG_SELECTED_ID, id);

        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onTitleDeleted() {
        showDetails(EmptyFragment.newInstance());
    }

}
