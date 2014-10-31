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
import android.widget.ListView;

import com.robinfinch.journal.app.persistence.RunEntryContract;
import com.robinfinch.journal.app.util.Utils;
import com.robinfinch.journal.domain.RunEntry;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * List of run entries fragment.
 *
 * @author Mark Hogenboom
 */
public class RunEntryListFragment extends Fragment {

    private static final int LOAD_RUN_ENTRIES = 1;
    private static final int INSERT_RUN_ENTRY = 2;

    public static RunEntryListFragment newInstance() {
        RunEntryListFragment fragment = new RunEntryListFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.runentry_list)
    protected ListView list;

    private CursorAdapter adapter;

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

        adapter = new CursorAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = LayoutInflater.from(context).inflate(R.layout.runentry_list_item, parent, false);

                RunEntryViewHolder viewHolder = new RunEntryViewHolder(view);
                view.setTag(viewHolder);

                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                RunEntry runEntry = RunEntry.from(cursor);

                RunEntryViewHolder viewHolder = (RunEntryViewHolder) view.getTag();
                viewHolder.bind(runEntry);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.runentry_list_fragment, container, false);
        ButterKnife.inject(this, view);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) adapter.getItem(position);
                if (cursor != null) {
                    int i = cursor.getColumnIndexOrThrow(BaseColumns._ID);
                    RunEntryListFragment.this.parent.onRunEntryItemSelected(RunEntryContract.ITEM_URI_TYPE.uri(cursor.getLong(i)));
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
                        RunEntryContract.DIR_URI_TYPE.uri(),
                        RunEntryContract.COLS, null, null, RunEntryContract.COL_DAY_OF_ENTRY + " ASC");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                adapter.swapCursor(cursor);
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

    @OnClick(R.id.runentry_add)
    public void addRunEntry() {
        ContentValues initialValues = new ContentValues();
        initialValues.put(RunEntryContract.COL_DAY_OF_ENTRY, Utils.getToday());

        queryHandler.startInsert(INSERT_RUN_ENTRY, null, RunEntryContract.DIR_URI_TYPE.uri(), initialValues);
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
        void onRunEntryItemSelected(Uri uri);
    }
}
