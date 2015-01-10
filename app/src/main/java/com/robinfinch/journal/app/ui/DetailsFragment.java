package com.robinfinch.journal.app.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.robinfinch.journal.app.R;
import com.robinfinch.journal.domain.SyncableObject;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * Base class of details fragments.
 *
 * @author Mark Hoogenboom
 */
public abstract class DetailsFragment<E extends SyncableObject> extends Fragment {

    protected static final int DELETE_ENTITY = 6;

    protected E entity;

    protected LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    protected AsyncQueryHandler queryHandler;

    protected ShareActionProvider shareActionProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shareActionProvider = new ShareActionProvider(getActivity());

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        ButterKnife.inject(this, view);
        initListeners();
        return view;
    }

    protected abstract int getLayoutResId();

    protected void initListeners() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

//        menu.add(Menu.CATEGORY_CONTAINER, R.id.entity_share, 1, R.string.entity_share)
//                .setActionProvider(shareActionProvider)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        MenuItem shareItem = menu.add(Menu.CATEGORY_CONTAINER, R.id.entity_share, 1, R.string.entity_share);
        shareItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        MenuItemCompat.setActionProvider(shareItem, shareActionProvider);

        menu.add(Menu.CATEGORY_CONTAINER, R.id.entity_delete, 2, R.string.entity_delete)
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

    protected void setShareText(CharSequence shareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, shareText);

        shareActionProvider.setShareIntent(shareIntent);
    }

    public abstract void update();

    @OnClick(R.id.entity_delete)
    public void delete() {
        Uri uri = getArguments().getParcelable(ARG_URI);

        queryHandler.startDelete(DELETE_ENTITY, null, uri, Long.toString(entity.getRemoteId()), null);
    }

    @Override
    public void onPause() {
        update();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }
}
