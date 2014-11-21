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

import com.robinfinch.journal.app.persistence.TravelEntryContract;
import com.robinfinch.journal.app.ui.ExpandableListFragment;
import com.robinfinch.journal.app.ui.adapter.JournalEntryListAdapter;
import com.robinfinch.journal.app.util.Utils;
import com.robinfinch.journal.domain.TravelEntry;


/**
 * List of travel entries fragment.
 *
 * @author Mark Hogenboom
 */
public class TravelEntryListFragment extends ExpandableListFragment {

    private static final int LOAD_TRAVEL_ENTRIES = 1;
    private static final int INSERT_TRAVEL_ENTRY = 2;

    public static TravelEntryListFragment newInstance() {
        TravelEntryListFragment fragment = new TravelEntryListFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    private AsyncQueryHandler queryHandler;

    private Parent parent;

    @Override
    protected int getLayoutResId() {
        return R.layout.travelentry_list_fragment;
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
                View view = LayoutInflater.from(context).inflate(R.layout.travelentry_list_item, parent, false);

                TravelEntryViewHolder viewHolder = new TravelEntryViewHolder(view);
                view.setTag(viewHolder);

                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TravelEntry travelEntry = TravelEntry.from(cursor);

                TravelEntryViewHolder viewHolder = (TravelEntryViewHolder) view.getTag();
                viewHolder.bind(travelEntry);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                return new CursorLoader(
                        getActivity(),
                        TravelEntryContract.DIR_URI_TYPE.uri(),
                        TravelEntryContract.COLS, null, null, TravelEntryContract.COL_DAY_OF_ENTRY + " ASC");
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
                parent.onTravelEntryItemSelected(uri);
            }
        };

        getLoaderManager().initLoader(LOAD_TRAVEL_ENTRIES, null, loaderCallbacks);
    }

    @Override
    protected void add() {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TravelEntryContract.COL_DAY_OF_ENTRY, Utils.getToday());
        initialValues.put(TravelEntryContract.COL_PLACE, "");

        queryHandler.startInsert(INSERT_TRAVEL_ENTRY, null, TravelEntryContract.DIR_URI_TYPE.uri(), initialValues);
    }

    @Override
    protected void select(long id) {
        parent.onTravelEntryItemSelected(TravelEntryContract.ITEM_URI_TYPE.uri(id));
    }

    @Override
    public void onDetach() {
        parent = null;
        super.onDetach();
    }

    public interface Parent {
        void onTravelEntryItemSelected(Uri uri);
    }
}
