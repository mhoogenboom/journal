package com.robinfinch.journal.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.robinfinch.journal.app.persistence.WalkEntryContract;
import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.app.util.Parser;
import com.robinfinch.journal.domain.WalkEntry;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * Walk entry details fragment.
 *
 * @author Mark Hoogenboom
 */
public class WalkEntryFragment extends Fragment {

    private static final int LOAD_WALK_ENTRY = 1;
    private static final int UPDATE_WALK_ENTRY = 2;
    private static final int DELETE_WALK_ENTRY = 3;

    public static WalkEntryFragment newInstance(Uri uri) {
        WalkEntryFragment fragment = new WalkEntryFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.walkentry_dayofwalk)
    protected EditText dayOfWalkView;

    @InjectView(R.id.walkentry_location)
    protected EditText locationView;

    private WalkEntry entry;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    private AsyncQueryHandler queryHandler;

    private Parent parent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (Parent) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.walkentry_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.CATEGORY_CONTAINER, R.id.walkentry_delete, 0, R.string.walkentry_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                Uri uri = getArguments().getParcelable(ARG_URI);

                return new CursorLoader(
                        getActivity(),
                        uri,
                        WalkEntryContract.COLS, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                if (cursor.moveToFirst()) {
                    entry = WalkEntry.from(cursor);

                    CharSequence dayOfEntry = Formatter.formatDayForInput(entry.getDayOfEntry());
                    dayOfWalkView.setText(dayOfEntry);

                    CharSequence location = entry.getLocation();
                    locationView.setText(location);
                } else {
                    onLoaderReset(loader);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                entry = null;
            }
        };

        queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {

            @Override
            public void onUpdateComplete(int token, Object cookie, int result) {

            }

            @Override
            public void onDeleteComplete(int token, Object cookie, int result) {
                parent.onWalkEntryDeleted();
            }
        };

        getLoaderManager().initLoader(LOAD_WALK_ENTRY, null, loaderCallbacks);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.walkentry_delete:
                deleteWalkEntry();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateWalkEntry() {
        if (entry != null) {
            entry.resetChanged();

            Date dayOfEntry = Parser.parseDay(dayOfWalkView.getText());
            entry.setDayOfEntry(dayOfEntry);

            String location = locationView.getText().toString();
            entry.setLocation(location);

            if (entry.hasChanged()) {
                Uri uri = getArguments().getParcelable(ARG_URI);

                ContentValues values = entry.toValues();
                queryHandler.startUpdate(UPDATE_WALK_ENTRY, null, uri, values, null, null);
            }
        }
    }

    @OnClick(R.id.walkentry_delete)
    public void deleteWalkEntry() {
        Uri uri = getArguments().getParcelable(ARG_URI);

        queryHandler.startDelete(DELETE_WALK_ENTRY, null, uri, Long.toString(entry.getRemoteId()), null);
    }

    @Override
    public void onPause() {
        updateWalkEntry();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        parent = null;
        super.onDetach();
    }

    public interface Parent {
        void onWalkEntryDeleted();
    }
}
