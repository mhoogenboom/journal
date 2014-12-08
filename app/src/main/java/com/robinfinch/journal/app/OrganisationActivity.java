package com.robinfinch.journal.app;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;

import com.robinfinch.journal.app.ui.DetailsActivity;

import static com.robinfinch.journal.app.util.Constants.ARG_SELECTED_ID;

/**
 * Organisation details activity.
 *
 * @organisation Mark Hoogenboom
 */
public class OrganisationActivity extends DetailsActivity implements OrganisationFragment.Parent {

    @Override
    protected Fragment newFragmentFor(Uri uri) {
        return OrganisationFragment.newInstance(uri);
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
        finish();
    }
}
