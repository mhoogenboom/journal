package com.robinfinch.journal.app;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

import com.robinfinch.journal.app.persistence.WalkEntryContract;
import com.robinfinch.journal.app.ui.DetailsFragment;
import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.app.util.Parser;
import com.robinfinch.journal.domain.WalkEntry;

import java.util.Date;

import butterknife.InjectView;

import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * Walk entry details fragment.
 *
 * @author Mark Hoogenboom
 */
public class WalkEntryFragment extends DetailsFragment<WalkEntry> {

    private static final int LOAD_WALK_ENTRY = 1;
    private static final int UPDATE_WALK_ENTRY = 2;

    public static WalkEntryFragment newInstance(Uri uri) {
        WalkEntryFragment fragment = new WalkEntryFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.walkentry_dayofwalk)
    protected EditText dayOfEntryView;

    @InjectView(R.id.walkentry_location)
    protected EditText locationView;

    private Parent parent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (Parent) activity;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.walkentry_fragment;
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
                    onWalkEntryLoaded(cursor);
                } else {
                    onLoaderReset(loader);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                entity = null;
            }
        };

        queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {

            @Override
            public void onDeleteComplete(int token, Object cookie, int result) {
                parent.onWalkEntryDeleted();
            }
        };

        getLoaderManager().initLoader(LOAD_WALK_ENTRY, null, loaderCallbacks);
    }

    private void onWalkEntryLoaded(Cursor cursor) {
        entity = WalkEntry.from(cursor);

        CharSequence dayOfEntry = Formatter.formatDayForInput(entity.getDayOfEntry());
        dayOfEntryView.setText(dayOfEntry);

        CharSequence location = entity.getLocation();
        locationView.setText(location);

        setShareText(entity.toShareString());
    }

    @Override
    public void update() {
        if (entity != null) {
            entity.resetChanged();

            Date dayOfEntry = Parser.parseDay(dayOfEntryView.getText());
            entity.setDayOfEntry(dayOfEntry);

            String location = Parser.parseText(locationView.getText());
            entity.setLocation(location);

            if (entity.hasChanged()) {
                Uri uri = getArguments().getParcelable(ARG_URI);

                ContentValues values = entity.toValues();
                queryHandler.startUpdate(UPDATE_WALK_ENTRY, null, uri, values, null, null);
            }
        }
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
