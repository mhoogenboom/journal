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
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.robinfinch.journal.app.persistence.JournalEntryContract;
import com.robinfinch.journal.app.persistence.WalkEntryContract;
import com.robinfinch.journal.app.ui.ExpandableListFragment;
import com.robinfinch.journal.app.ui.adapter.JournalEntryListAdapter;
import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.app.util.Utils;
import com.robinfinch.journal.domain.WalkEntry;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.robinfinch.journal.app.util.Utils.alias;

/**
 * List of walk entries fragment.
 *
 * @author Mark Hoogenboom
 */
public class WalkEntryListFragment extends ExpandableListFragment {

    private static final int LOAD_WALK_ENTRIES = 1;
    private static final int INSERT_WALK_ENTRY = 2;

    public static WalkEntryListFragment newInstance() {
        WalkEntryListFragment fragment = new WalkEntryListFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    private AsyncQueryHandler queryHandler;

    private Parent parent;

    @Override
    protected int getHeaderResId() {
        return R.string.walkentries;
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
                View view = LayoutInflater.from(context).inflate(R.layout.walkentry_list_item, parent, false);

                ViewHolder viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);

                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                WalkEntry walkEntry = WalkEntry.from(cursor);

                ViewHolder viewHolder = (ViewHolder) view.getTag();
                viewHolder.bind(walkEntry);
            }
        }, alias(WalkEntryContract.NAME, WalkEntryContract.COL_DAY_OF_ENTRY));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                return new CursorLoader(
                        getActivity(),
                        WalkEntryContract.DIR_URI_TYPE.uri(),
                        WalkEntryContract.COLS, null, null, WalkEntryContract.COL_DAY_OF_ENTRY + " ASC");
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
                parent.onWalkEntryItemSelected(uri);
            }
        };

        getLoaderManager().initLoader(LOAD_WALK_ENTRIES, null, loaderCallbacks);
    }

    @Override
    protected void add() {
        ContentValues initialValues = new ContentValues();
        initialValues.put(JournalEntryContract.COL_DAY_OF_ENTRY, Utils.getDefaultDayOfEntry(getActivity()));
        initialValues.put(WalkEntryContract.COL_LOCATION, "");

        queryHandler.startInsert(INSERT_WALK_ENTRY, null, WalkEntryContract.DIR_URI_TYPE.uri(), initialValues);
    }

    @Override
    protected void select(long id) {
        parent.onWalkEntryItemSelected(WalkEntryContract.ITEM_URI_TYPE.uri(id));
    }

    @Override
    public void onDetach() {
        parent = null;
        super.onDetach();
    }

    public interface Parent {
        void onWalkEntryItemSelected(Uri uri);
    }

    static class ViewHolder {

        @InjectView(R.id.walkentry_dayofwalk)
        protected TextView dayOfEntryView;

        @InjectView(R.id.walkentry_description)
        protected TextView descriptionView;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void bind(WalkEntry entry) {
            dayOfEntryView.setText(Formatter.formatDay(entry.getDayOfEntry()));
            descriptionView.setText(Formatter.formatWalkDescription(entry.getLocation()));
        }
    }
}
