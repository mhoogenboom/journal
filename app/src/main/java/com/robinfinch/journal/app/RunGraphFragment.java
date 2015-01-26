package com.robinfinch.journal.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.robinfinch.journal.app.persistence.JournalEntryContract;
import com.robinfinch.journal.app.persistence.RunEntryContract;
import com.robinfinch.journal.app.ui.ExpandableListFragment;
import com.robinfinch.journal.app.ui.GraphView;
import com.robinfinch.journal.app.ui.adapter.JournalEntryListAdapter;
import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.app.util.Function;
import com.robinfinch.journal.app.util.Utils;
import com.robinfinch.journal.domain.RunEntry;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.robinfinch.journal.app.util.Utils.alias;

/**
 * Graph of run entries.
 *
 * @author Mark Hoogenboom
 */
public class RunGraphFragment extends Fragment {

    private static final int LOAD_RUN_ENTRIES = 1;

    public static RunGraphFragment newInstance() {
        RunGraphFragment fragment = new RunGraphFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.graph)
    protected GraphView graphView;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    private Parent parent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (Parent) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        graphView.setXAxisLabel(new Function<Long, CharSequence>() {
            public CharSequence apply(Long x) {
                return Formatter.formatDayForInput(new Date(x));
            }
        });

        graphView.setYAxisLabel(new Function<Long, CharSequence>() {
            public CharSequence apply(Long y) {
                return Formatter.formatPace(y.intValue());
            }
        });

        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                return new CursorLoader(
                        getActivity(),
                        RunEntryContract.DIR_URI_TYPE.uri(),
                        RunEntryContract.COLS, null, null, RunEntryContract.COL_DAY_OF_ENTRY + " ASC");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                RunEntry[] window = new RunEntry[5];

                if (cursor.moveToFirst()) {
                    do {
                        int i = 1;
                        for (; i < window.length; i++) {
                            window[i - 1] = window[i];
                        }
                        window[i - 1] = RunEntry.from(cursor);

                        if (window[0] != null) {
                            int avg = (window[0].getAvgPace() * 1
                                    + window[1].getAvgPace() * 2
                                    + window[2].getAvgPace() * 4
                                    + window[3].getAvgPace() * 2
                                    + window[4].getAvgPace() * 1) / 10;

                            long day = TimeUnit.DAYS.toDays(window[2].getDayOfEntry().getTime());

                            graphView.add(day, avg);
                        }
                    } while (cursor.moveToNext());
                }
                graphView.invalidate();
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                if (graphView != null) {
                    graphView.clear();
                }
            }
        };

        getLoaderManager().initLoader(LOAD_RUN_ENTRIES, null, loaderCallbacks);
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
    }
}
