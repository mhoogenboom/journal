package com.robinfinch.journal.app;

import android.app.Activity;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.robinfinch.journal.app.persistence.JournalEntryContract;
import com.robinfinch.journal.app.persistence.RunEntryContract;
import com.robinfinch.journal.app.ui.ExpandableListFragment;
import com.robinfinch.journal.app.ui.adapter.JournalEntryListAdapter;
import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.app.util.Utils;
import com.robinfinch.journal.domain.RunEntry;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.robinfinch.journal.app.util.Utils.alias;

/**
 * List of run entries fragment.
 *
 * @author Mark Hoogenboom
 */
public class RunEntryListFragment extends ExpandableListFragment {

    private static final int LOAD_RUN_ENTRIES = 1;
    private static final int INSERT_RUN_ENTRY = 2;

    public static RunEntryListFragment newInstance() {
        RunEntryListFragment fragment = new RunEntryListFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    private AsyncQueryHandler queryHandler;

    private Parent parent;

    @Override
    protected int getHeaderResId() {
        return R.string.runentries;
    }

    @Override
    protected int getAddButtonResId() {
        return R.string.entity_add;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (Parent) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new JournalEntryListAdapter(new CursorAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = LayoutInflater.from(context).inflate(R.layout.runentry_list_item, parent, false);

                ViewHolder viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);

                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                RunEntry runEntry = RunEntry.from(cursor);

                ViewHolder viewHolder = (ViewHolder) view.getTag();
                viewHolder.bind(runEntry);
            }
        }, alias(RunEntryContract.NAME, RunEntryContract.COL_DAY_OF_ENTRY));

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                return new CursorLoader(
                        getActivity(),
                        RunEntryContract.DIR_URI_TYPE.uri(getActivity()),
                        RunEntryContract.COLS, null, null, RunEntryContract.COL_DAY_OF_ENTRY + " ASC");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                adapter.swapCursor(cursor);

                if (adapter.getGroupCount() > 0) {
                    list.expandGroup(adapter.getGroupCount() - 1);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                adapter.swapCursor(null);
            }
        };

        queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {

            @Override
            public void onInsertComplete(int token, Object cookie, Uri uri) {
                parent.onRunEntryItemSelected(uri);
            }
        };

        getLoaderManager().initLoader(LOAD_RUN_ENTRIES, null, loaderCallbacks);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.add(Menu.CATEGORY_CONTAINER, R.id.runentry_graph, 3, R.string.runentry_graph)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.runentry_graph:
                parent.onGraphRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void add() {
        ContentValues initialValues = new ContentValues();
        initialValues.put(JournalEntryContract.COL_DAY_OF_ENTRY, Utils.getDefaultDayOfEntry(getActivity()));

        queryHandler.startInsert(INSERT_RUN_ENTRY, null, RunEntryContract.DIR_URI_TYPE.uri(getActivity()), initialValues);
    }

    @Override
    protected void select(long id) {
        parent.onRunEntryItemSelected(RunEntryContract.ITEM_URI_TYPE.uri(getActivity(), id));
    }

    @Override
    public void onDetach() {
        parent = null;
        super.onDetach();
    }

    public interface Parent {
        void onRunEntryItemSelected(Uri uri);

        void onGraphRequested();
    }

    static class ViewHolder {

        @InjectView(R.id.runentry_dayofrun)
        protected TextView dayOfEntryView;

        @InjectView(R.id.runentry_distance)
        protected TextView distanceView;

        @InjectView(R.id.runentry_timetaken)
        protected TextView timeTakenView;

        @InjectView(R.id.runentry_avgpace)
        protected TextView avgPaceView;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void bind(RunEntry entry) {
            dayOfEntryView.setText(Formatter.formatDay(entry.getDayOfEntry()));
            distanceView.setText(Formatter.formatDistance(entry.getDistance()));
            timeTakenView.setText(Formatter.formatTime(entry.getTimeTaken()));
            avgPaceView.setText(Formatter.formatPace(entry.getAvgPace()));
        }
    }
}
