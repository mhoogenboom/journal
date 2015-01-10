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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.robinfinch.journal.app.persistence.TravelEntryContract;
import com.robinfinch.journal.app.ui.DetailsFragment;
import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.app.util.Parser;
import com.robinfinch.journal.domain.TravelEntry;

import java.util.Date;

import butterknife.InjectView;

import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * Travel entry details fragment.
 *
 * @author Mark Hoogenboom
 */
public class TravelEntryFragment extends DetailsFragment<TravelEntry> {

    private static final int LOAD_TRAVEL_ENTRY = 1;
    private static final int UPDATE_TRAVEL_ENTRY = 2;

    public static TravelEntryFragment newInstance(Uri uri) {
        TravelEntryFragment fragment = new TravelEntryFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.travelentry_dayoftravel)
    protected EditText dayOfEntryView;

    @InjectView(R.id.travelentry_away)
    protected Spinner awayView;

    @InjectView(R.id.travelentry_place)
    protected EditText placeView;

    private Parent parent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (Parent) activity;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.travelentry_fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add(getText(R.string.travelentry_away));
        adapter.add(getText(R.string.travelentry_back));

        awayView.setAdapter(adapter);

        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Uri uri = getArguments().getParcelable(ARG_URI);
                return new CursorLoader(
                        getActivity(),
                        uri,
                        TravelEntryContract.COLS, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                if (cursor.moveToFirst()) {
                   onTravelEntryLoaded(cursor);
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
                parent.onTravelEntryDeleted();
            }
        };

        getLoaderManager().initLoader(LOAD_TRAVEL_ENTRY, null, loaderCallbacks);
    }

    private void onTravelEntryLoaded(Cursor cursor) {
        entity = TravelEntry.from(cursor);

        CharSequence dayOfEntry = Formatter.formatDayForInput(entity.getDayOfEntry());
        dayOfEntryView.setText(dayOfEntry);

        awayView.setSelection(entity.isAway() ? 0 : 1);

        CharSequence place = entity.getPlace();
        placeView.setText(place);

        setShareText(entity.toShareString());
    }

    @Override
    public void update() {
        if (entity != null) {
            entity.resetChanged();

            Date dayOfEntry = Parser.parseDay(dayOfEntryView.getText());
            entity.setDayOfEntry(dayOfEntry);

            CharSequence direction = (CharSequence) awayView.getSelectedItem();
            entity.setAway(getText(R.string.travelentry_away).equals(direction));

            String place = Parser.parseText(placeView.getText());
            entity.setPlace(place);

            if (entity.hasChanged()) {
                Uri uri = getArguments().getParcelable(ARG_URI);

                ContentValues values = entity.toValues();
                queryHandler.startUpdate(UPDATE_TRAVEL_ENTRY, null, uri, values, null, null);
            }
        }
    }

    @Override
    public void onDetach() {
        parent = null;
        super.onDetach();
    }

    public interface Parent {
        void onTravelEntryDeleted();
    }
}
