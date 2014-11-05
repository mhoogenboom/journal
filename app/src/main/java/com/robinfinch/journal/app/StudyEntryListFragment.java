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
import android.widget.CursorAdapter;
import android.widget.ExpandableListView;

import com.robinfinch.journal.app.adapter.JournalEntryListAdapter;
import com.robinfinch.journal.app.persistence.StudyEntryContract;
import com.robinfinch.journal.app.util.Utils;
import com.robinfinch.journal.domain.StudyEntry;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * List of study entries fragment.
 *
 * @author Mark Hogenboom
 */
public class StudyEntryListFragment extends Fragment {

    private static final int LOAD_STUDY_ENTRIES = 1;
    private static final int INSERT_STUDY_ENTRY = 2;

    public static StudyEntryListFragment newInstance() {
        StudyEntryListFragment fragment = new StudyEntryListFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.studyentry_list)
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
                View view = LayoutInflater.from(context).inflate(R.layout.studyentry_list_item, parent, false);

                StudyEntryViewHolder viewHolder = new StudyEntryViewHolder(view);
                view.setTag(viewHolder);

                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                StudyEntry entry = StudyEntry.from(cursor, StudyEntryContract.NAME + "_");

                StudyEntryViewHolder viewHolder = (StudyEntryViewHolder) view.getTag();
                viewHolder.bind(entry);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.studyentry_list_fragment, container, false);
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
                    StudyEntryListFragment.this.parent.onStudyEntryItemSelected(StudyEntryContract.ITEM_URI_TYPE.uri(cursor.getLong(i)));
                    return true;
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
                        StudyEntryContract.DIR_URI_TYPE.uri(),
                        StudyEntryContract.COLS, null, null, StudyEntryContract.COL_DAY_OF_ENTRY + " ASC");
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
                parent.onStudyEntryItemSelected(uri);
            }
        };

        getLoaderManager().initLoader(LOAD_STUDY_ENTRIES, null, loaderCallbacks);
    }

    @OnClick(R.id.studyentry_add)
    public void addStudyEntry() {
        ContentValues initialValues = new ContentValues();
        initialValues.put(StudyEntryContract.COL_DAY_OF_ENTRY, Utils.getToday());

        queryHandler.startInsert(INSERT_STUDY_ENTRY, null, StudyEntryContract.DIR_URI_TYPE.uri(), initialValues);
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
        void onStudyEntryItemSelected(Uri uri);
    }
}
