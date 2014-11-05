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
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.robinfinch.journal.app.adapter.JournalEntryListAdapter;
import com.robinfinch.journal.app.persistence.WalkEntryContract;
import com.robinfinch.journal.app.util.Utils;
import com.robinfinch.journal.domain.WalkEntry;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * List of walk entries fragment.
 *
 * @author Mark Hogenboom
 */
public class WalkEntryListFragment extends Fragment {

    private static final int LOAD_WALK_ENTRIES = 1;
    private static final int INSERT_WALK_ENTRY = 2;

    public static WalkEntryListFragment newInstance() {
        WalkEntryListFragment fragment = new WalkEntryListFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.walkentry_list)
    protected ExpandableListView list;

    private JournalEntryListAdapter adapter;

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

        adapter = new JournalEntryListAdapter(new CursorAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = LayoutInflater.from(context).inflate(R.layout.walkentry_list_item, parent, false);

                WalkEntryViewHolder viewHolder = new WalkEntryViewHolder(view);
                view.setTag(viewHolder);

                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                WalkEntry walkEntry = WalkEntry.from(cursor);

                WalkEntryViewHolder viewHolder = (WalkEntryViewHolder) view.getTag();
                viewHolder.bind(walkEntry);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.walkentry_list_fragment, container, false);
        ButterKnife.inject(this, view);

        list.setAdapter(adapter);

        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Cursor cursor = (Cursor) adapter.getChild(groupPosition, childPosition);
                if (cursor == null) {
                    return false;
                } else {
                    int i = cursor.getColumnIndexOrThrow(BaseColumns._ID);
                    WalkEntryListFragment.this.parent.onWalkEntryItemSelected(WalkEntryContract.ITEM_URI_TYPE.uri(cursor.getLong(i)));
                    return false;
                }
            }
        });

        return view;
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

    @OnClick(R.id.walkentry_add)
    public void addWalkEntry() {
        ContentValues initialValues = new ContentValues();
        initialValues.put(WalkEntryContract.COL_DAY_OF_ENTRY, Utils.getToday());
        initialValues.put(WalkEntryContract.COL_LOCATION, "");

        queryHandler.startInsert(INSERT_WALK_ENTRY, null, WalkEntryContract.DIR_URI_TYPE.uri(), initialValues);
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
        void onWalkEntryItemSelected(Uri uri);
    }
}
