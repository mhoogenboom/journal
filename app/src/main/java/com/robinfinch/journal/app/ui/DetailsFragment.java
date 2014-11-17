package com.robinfinch.journal.app.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.robinfinch.journal.app.R;

import butterknife.OnClick;

/**
 * Base class of details fragments.
 *
 * @author Mark Hoogenboom
 */
public abstract class DetailsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.CATEGORY_CONTAINER, R.id.entity_delete, 0, R.string.entity_delete)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.entity_delete:
                delete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public abstract void update();

    @OnClick(R.id.entity_delete)
    public abstract void delete();

    @Override
    public void onPause() {
        update();
        super.onPause();
    }
}
