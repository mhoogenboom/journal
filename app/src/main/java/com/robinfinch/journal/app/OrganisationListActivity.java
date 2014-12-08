package com.robinfinch.journal.app;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;

import com.robinfinch.journal.app.ui.EmptyFragment;
import com.robinfinch.journal.app.ui.ListActivity;

import static com.robinfinch.journal.app.util.Constants.ARG_SELECTED_ID;
import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * List of organisations activity.
 *
 * @organisation Mark Hoogenboom
 */
public class OrganisationListActivity extends ListActivity implements
        OrganisationListFragment.Parent,
        OrganisationFragment.Parent,
        EmptyFragment.Parent {

    private static final int REQUEST_SELECT_ORGANISATION = 1;

    @Override
    protected Fragment newFragment() {
        return OrganisationListFragment.newInstance();
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_ORGANISATION:
                onOrganisationActivityResult(resultCode, data);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onOrganisationActivityResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            long id = data.getLongExtra(ARG_SELECTED_ID, 0L);
            onOrganisationSelected(id);
        }
    }

    @Override
    public void onOrganisationItemSelected(Uri uri) {
        if (singlePaneLayout) {
            Intent intent = new Intent(this, OrganisationActivity.class)
                    .putExtra(ARG_URI, uri);
            startActivityForResult(intent, REQUEST_SELECT_ORGANISATION);
        } else {
            showDetails(OrganisationFragment.newInstance(uri));
        }
    }

    @Override
    public void onOrganisationSelected(long id) {
        Intent result = new Intent();
        result.putExtra(ARG_SELECTED_ID, id);

        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onOrganisationDeleted() {
        showDetails(EmptyFragment.newInstance());
    }

}
