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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.robinfinch.journal.app.persistence.RunEntryContract;
import com.robinfinch.journal.app.ui.DetailsFragment;
import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.app.util.Parser;
import com.robinfinch.journal.domain.RunEntry;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * Run entry details fragment.
 *
 * @author Mark Hoogenboom
 */
public class RunEntryFragment extends DetailsFragment {

    private static final int LOAD_RUN_ENTRY = 1;
    private static final int UPDATE_RUN_ENTRY = 2;
    private static final int DELETE_RUN_ENTRY = 3;

    public static RunEntryFragment newInstance(Uri uri) {
        RunEntryFragment fragment = new RunEntryFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.runentry_dayofrun)
    protected EditText dayOfRunView;

    @InjectView(R.id.runentry_distance)
    protected EditText distanceView;

    @InjectView(R.id.runentry_timetaken)
    protected EditText timeTakenView;

    private RunEntry entry;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    private AsyncQueryHandler queryHandler;

    private Parent parent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (Parent) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.runentry_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
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
                        RunEntryContract.COLS, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                if (cursor.moveToFirst()) {
                    entry = RunEntry.from(cursor);

                    CharSequence dayOfEntry = Formatter.formatDayForInput(entry.getDayOfEntry());
                    dayOfRunView.setText(dayOfEntry);

                    CharSequence distance = Formatter.formatDistanceForInput(entry.getDistance());
                    distanceView.setText(distance);

                    CharSequence timeTaken = Formatter.formatTime(entry.getTimeTaken());
                    timeTakenView.setText(timeTaken);
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
                parent.onRunEntryDeleted();
            }
        };

        getLoaderManager().initLoader(LOAD_RUN_ENTRY, null, loaderCallbacks);
    }

    @Override
    public void update() {
        if (entry != null) {
            entry.resetChanged();

            Date dayOfEntry = Parser.parseDay(dayOfRunView.getText());
            entry.setDayOfEntry(dayOfEntry);

            int distance = Parser.parseDistance(distanceView.getText());
            entry.setDistance(distance);

            int timeTaken = Parser.parseTime(timeTakenView.getText());
            entry.setTimeTaken(timeTaken);

            if (entry.hasChanged()) {
                Uri uri = getArguments().getParcelable(ARG_URI);

                ContentValues values = entry.toValues();
                queryHandler.startUpdate(UPDATE_RUN_ENTRY, null, uri, values, null, null);
            }
        }
    }

    @Override
    public void delete() {
        Uri uri = getArguments().getParcelable(ARG_URI);

        queryHandler.startDelete(DELETE_RUN_ENTRY, null, uri, Long.toString(entry.getRemoteId()), null);
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
        void onRunEntryDeleted();
    }
}
