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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.robinfinch.journal.app.persistence.TravelEntryContract;
import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.app.util.Parser;
import com.robinfinch.journal.domain.TravelEntry;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * Travel entry details fragment.
 *
 * @author Mark Hoogenboom
 */
public class TravelEntryFragment extends Fragment {

    private static final int LOAD_TRAVEL_ENTRY = 1;
    private static final int UPDATE_TRAVEL_ENTRY = 2;
    private static final int DELETE_TRAVEL_ENTRY = 3;

    public static TravelEntryFragment newInstance(Uri uri) {
        TravelEntryFragment fragment = new TravelEntryFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.travelentry_dayoftravel)
    protected EditText dayOfTravelView;

    @InjectView(R.id.travelentry_away)
    protected Spinner awayView;

    @InjectView(R.id.travelentry_place)
    protected EditText placeView;

    private TravelEntry entry;

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
        View view = inflater.inflate(R.layout.travelentry_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.CATEGORY_CONTAINER, R.id.travelentry_delete, 0, R.string.travelentry_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
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
                    entry = TravelEntry.from(cursor);

                    CharSequence dayOfEntry = Formatter.formatDayForInput(entry.getDayOfEntry());
                    dayOfTravelView.setText(dayOfEntry);

                    awayView.setSelection(entry.isAway() ? 0 : 1);

                    CharSequence place = entry.getPlace();
                    placeView.setText(place);
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
                parent.onTravelEntryDeleted();
            }
        };

        getLoaderManager().initLoader(LOAD_TRAVEL_ENTRY, null, loaderCallbacks);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.travelentry_delete:
                deleteTravelEntry();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateTravelEntry() {
        if (entry != null) {
            entry.resetChanged();

            Date dayOfEntry = Parser.parseDay(dayOfTravelView.getText());
            entry.setDayOfEntry(dayOfEntry);

            CharSequence direction = (CharSequence) awayView.getSelectedItem();
            entry.setAway(getText(R.string.travelentry_away).equals(direction));

            String place = placeView.getText().toString();
            entry.setPlace(place);

            if (entry.hasChanged()) {
                Uri uri = getArguments().getParcelable(ARG_URI);

                ContentValues values = entry.toValues();
                queryHandler.startUpdate(UPDATE_TRAVEL_ENTRY, null, uri, values, null, null);
            }
        }
    }

    @OnClick(R.id.travelentry_delete)
    public void deleteTravelEntry() {
        Uri uri = getArguments().getParcelable(ARG_URI);

        queryHandler.startDelete(DELETE_TRAVEL_ENTRY, null, uri, Long.toString(entry.getRemoteId()), null);
    }

    @Override
    public void onPause() {
        updateTravelEntry();
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
        void onTravelEntryDeleted();
    }
}
