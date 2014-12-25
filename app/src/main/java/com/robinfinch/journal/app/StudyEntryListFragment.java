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

import com.robinfinch.journal.app.persistence.JournalEntryContract;
import com.robinfinch.journal.app.persistence.StudyEntryContract;
import com.robinfinch.journal.app.ui.ExpandableListFragment;
import com.robinfinch.journal.app.ui.adapter.JournalEntryListAdapter;
import com.robinfinch.journal.app.util.Utils;
import com.robinfinch.journal.domain.StudyEntry;

import static com.robinfinch.journal.app.util.Utils.alias;

/**
 * List of study entries fragment.
 *
 * @author Mark Hoogenboom
 */
public class StudyEntryListFragment extends ExpandableListFragment {

    private static final int LOAD_STUDY_ENTRIES = 1;
    private static final int INSERT_STUDY_ENTRY = 2;

    public static StudyEntryListFragment newInstance() {
        StudyEntryListFragment fragment = new StudyEntryListFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    private AsyncQueryHandler queryHandler;

    private Parent parent;

    @Override
    protected int getHeaderResId() {
        return R.string.studyentries;
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
                View view = LayoutInflater.from(context).inflate(R.layout.studyentry_list_item, parent, false);

                StudyEntryViewHolder viewHolder = new StudyEntryViewHolder(view);
                view.setTag(viewHolder);

                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                StudyEntry entry = StudyEntry.from(cursor);

                StudyEntryViewHolder viewHolder = (StudyEntryViewHolder) view.getTag();
                viewHolder.bind(entry);
            }
        }, alias(StudyEntryContract.NAME, StudyEntryContract.COL_DAY_OF_ENTRY));
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

    @Override
    protected void add() {
        ContentValues initialValues = new ContentValues();
        initialValues.put(JournalEntryContract.COL_DAY_OF_ENTRY, Utils.getDefaultDayOfEntry(getActivity()));

        queryHandler.startInsert(INSERT_STUDY_ENTRY, null, StudyEntryContract.DIR_URI_TYPE.uri(), initialValues);
    }

    @Override
    protected void select(long id) {
        parent.onStudyEntryItemSelected(StudyEntryContract.ITEM_URI_TYPE.uri(id));
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
