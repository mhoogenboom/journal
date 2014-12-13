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

import com.robinfinch.journal.app.applications.ApplicationsModule;
import com.robinfinch.journal.app.persistence.ApplicationContract;
import com.robinfinch.journal.app.persistence.ApplicationEntryContract;
import com.robinfinch.journal.app.ui.ExpandableListFragment;
import com.robinfinch.journal.app.ui.adapter.JournalEntryListAdapter;
import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.domain.ApplicationEntry;
import com.robinfinch.journal.domain.workflow.Action;
import com.robinfinch.journal.domain.workflow.Workflow;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;


/**
 * List of application entries fragment.
 *
 * @author Mark Hogenboom
 */
public class ApplicationEntryListFragment extends ExpandableListFragment {

    private static final int LOAD_APPLICATION_ENTRIES = 1;
    private static final int INSERT_APPLICATION = 2;

    public static ApplicationEntryListFragment newInstance() {
        ApplicationEntryListFragment fragment = new ApplicationEntryListFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Inject
    Workflow workflow;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    private AsyncQueryHandler queryHandler;

    private Parent parent;

    @Override
    protected int getHeaderResId() {
        return R.string.applicationentries;
    }

    @Override
    protected int getAddButtonResId() {
        return R.string.application_add;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (Parent) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ObjectGraph.create(
                new ContextModule(getActivity().getApplicationContext()),
                new ApplicationsModule()
        ).inject(this);

        adapter = new JournalEntryListAdapter(new CursorAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = LayoutInflater.from(context).inflate(R.layout.applicationentry_list_item, parent, false);

                ViewHolder viewHolder = new ViewHolder(workflow, view);
                view.setTag(viewHolder);

                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                ApplicationEntry entry = ApplicationEntry.from(cursor, ApplicationEntryContract.NAME + "_");

                ViewHolder viewHolder = (ViewHolder) view.getTag();
                viewHolder.bind(entry);
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
                        ApplicationEntryContract.DIR_URI_TYPE.uri(),
                        ApplicationEntryContract.COLS, null, null, ApplicationEntryContract.COL_DAY_OF_ENTRY + " ASC");
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
                parent.onApplicationItemSelected(uri);
            }
        };

        getLoaderManager().initLoader(LOAD_APPLICATION_ENTRIES, null, loaderCallbacks);
    }

    @Override
    protected void add() {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ApplicationContract.COL_STATE_ID, workflow.getInitialState().getId());

        queryHandler.startInsert(INSERT_APPLICATION, null, ApplicationContract.DIR_URI_TYPE.uri(), initialValues);
    }

    @Override
    protected void select(Cursor cursor) {
        int i = cursor.getColumnIndexOrThrow(ApplicationEntryContract.COL_APPLICATION_ID);
        select(cursor.getLong(i));
    }

    @Override
    protected void select(long id) {
        parent.onApplicationItemSelected(ApplicationContract.ITEM_URI_TYPE.uri(id));
    }

    @Override
    public void onDetach() {
        parent = null;
        super.onDetach();
    }

    public interface Parent {
        void onApplicationItemSelected(Uri uri);
    }

    static class ViewHolder {

        @InjectView(R.id.applicationentry_dayofentry)
        protected TextView dayOfEntryView;

        @InjectView(R.id.applicationentry_application)
        protected TextView applicationView;

        @InjectView(R.id.applicationentry_action)
        protected TextView actionView;

        private final Workflow workflow;

        public ViewHolder(Workflow workflow, View view) {
            ButterKnife.inject(this, view);
            this.workflow = workflow;
        }

        public void bind(ApplicationEntry entry) {
            dayOfEntryView.setText(Formatter.formatDay(entry.getDayOfEntry()));

            applicationView.setText(Formatter.formatNamedObject(entry.getApplication()));

            Action action = workflow.getAction(entry.getActionId());
            if (action == null) {
                actionView.setText("");
            } else {
                actionView.setText(action.getDescription());
            }
        }
    }
}
