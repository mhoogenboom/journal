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

import com.robinfinch.journal.app.persistence.RunEntryContract;
import com.robinfinch.journal.app.ui.DetailsFragment;
import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.app.util.Parser;
import com.robinfinch.journal.domain.RunEntry;

import java.util.Date;

import butterknife.InjectView;

import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * Run entity details fragment.
 *
 * @author Mark Hoogenboom
 */
public class RunEntryFragment extends DetailsFragment<RunEntry> {

    private static final int LOAD_RUN_ENTRY = 1;
    private static final int UPDATE_RUN_ENTRY = 2;

    public static RunEntryFragment newInstance(Uri uri) {
        RunEntryFragment fragment = new RunEntryFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.runentry_dayofrun)
    protected EditText dayOfEntryView;

    @InjectView(R.id.runentry_distance)
    protected EditText distanceView;

    @InjectView(R.id.runentry_note)
    protected EditText noteView;

    @InjectView(R.id.runentry_timetaken)
    protected EditText timeTakenView;

    private Parent parent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (Parent) activity;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.runentry_fragment;
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
                    onRunEntryLoaded(cursor);
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
                parent.onRunEntryDeleted();
            }
        };

        getLoaderManager().initLoader(LOAD_RUN_ENTRY, null, loaderCallbacks);
    }

    private void onRunEntryLoaded(Cursor cursor) {
        entity = RunEntry.from(cursor);

        CharSequence dayOfEntry = Formatter.formatDayForInput(entity.getDayOfEntry());
        dayOfEntryView.setText(dayOfEntry);

        CharSequence distance = Formatter.formatDistanceForInput(entity.getDistance());
        distanceView.setText(distance);

        CharSequence note = entity.getNote();
        noteView.setText(note);

        CharSequence timeTaken = Formatter.formatTime(entity.getTimeTaken());
        timeTakenView.setText(timeTaken);

        setShareText(entity.toShareString());
    }

    @Override
    public void update() {
        if (entity != null) {
            entity.resetChanged();

            Date dayOfEntry = Parser.parseDay(dayOfEntryView.getText());
            entity.setDayOfEntry(dayOfEntry);

            int distance = Parser.parseDistance(distanceView.getText());
            entity.setDistance(distance);

            String note = Parser.parseText(noteView.getText());
            entity.setNote(note);

            int timeTaken = Parser.parseTime(timeTakenView.getText());
            entity.setTimeTaken(timeTaken);

            if (entity.hasChanged()) {
                Uri uri = getArguments().getParcelable(ARG_URI);

                ContentValues values = entity.toValues();
                queryHandler.startUpdate(UPDATE_RUN_ENTRY, null, uri, values, null, null);
            }
        }
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
