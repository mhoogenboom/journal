package com.robinfinch.journal.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.robinfinch.journal.app.applications.ApplicationsModule;
import com.robinfinch.journal.app.persistence.ApplicationContract;
import com.robinfinch.journal.app.persistence.ApplicationEntryContract;
import com.robinfinch.journal.app.persistence.OrganisationContract;
import com.robinfinch.journal.app.persistence.RecruiterContract;
import com.robinfinch.journal.app.ui.NamedObjectView;
import com.robinfinch.journal.app.util.Parser;
import com.robinfinch.journal.app.util.Utils;
import com.robinfinch.journal.domain.Application;
import com.robinfinch.journal.domain.Organisation;
import com.robinfinch.journal.domain.Recruiter;
import com.robinfinch.journal.domain.workflow.Action;
import com.robinfinch.journal.domain.workflow.State;
import com.robinfinch.journal.domain.workflow.Workflow;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;

import static com.robinfinch.journal.app.util.Constants.ARG_SELECTED_ID;
import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * Application details fragment.
 *
 * @author Mark Hoogenboom
 */
public class ApplicationFragment extends Fragment {

    private static final int LOAD_APPLICATION = 1;
    private static final int LOAD_RECRUITER = 2;
    private static final int LOAD_CLIENT = 3;
    private static final int UPDATE_APPLICATION = 4;
    private static final int UPDATE_APPLICATION_STATE = 5;
    private static final int INSERT_APPLICATION_ENTRY = 5;

    private static final int REQUEST_SELECT_RECRUITER = 1;
    private static final int REQUEST_SELECT_CLIENT = 2;

    private static final int MENU_GROUP_WORKFLOW_ACTIONS = 100;

    public static ApplicationFragment newInstance(Uri uri) {
        ApplicationFragment fragment = new ApplicationFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.application_recruiter)
    protected NamedObjectView<Recruiter> recruiterView;

    @InjectView(R.id.application_client)
    protected NamedObjectView<Organisation> clientView;

    @InjectView(R.id.application_start)
    protected EditText startView;

    @InjectView(R.id.application_rate)
    protected EditText rateView;

    @InjectView(R.id.application_state)
    protected TextView stateView;

    @Inject
    Workflow workflow;

    private Application application;

    private Collection<Action> actions;

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
        setHasOptionsMenu(true);

        ObjectGraph.create(
                new ContextModule(getActivity().getApplicationContext()),
                new ApplicationsModule()
        ).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.application_fragment, container, false);
        ButterKnife.inject(this, view);

        recruiterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectRecruiter();
            }
        });

        clientView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectClient();
            }
        });

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.removeGroup(MENU_GROUP_WORKFLOW_ACTIONS);

        if (actions != null) {
            for (Action action : actions) {
                menu.add(MENU_GROUP_WORKFLOW_ACTIONS, (int) action.getId(), 0, action.getDescription())
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actions != null) {
            for (Action action : actions) {
                if (action.getId() == item.getItemId()) {
                    return onActionSelected(action);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean onActionSelected(Action action) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ApplicationEntryContract.COL_DAY_OF_ENTRY, Utils.getToday());
        initialValues.put(ApplicationEntryContract.COL_APPLICATION_ID, application.getId());
        initialValues.put(ApplicationEntryContract.COL_ACTION_ID, action.getId());
        queryHandler.startInsert(INSERT_APPLICATION_ENTRY, action, ApplicationEntryContract.DIR_URI_TYPE.uri(), initialValues);
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                Uri uri;
                switch (id) {
                    case LOAD_APPLICATION:
                        uri = getArguments().getParcelable(ARG_URI);
                        return new CursorLoader(
                                getActivity(),
                                uri,
                                ApplicationContract.COLS, null, null, null);

                    case LOAD_RECRUITER:
                        uri = args.getParcelable(ARG_URI);
                        return new CursorLoader(
                                getActivity(),
                                uri,
                                RecruiterContract.COLS, null, null, null);

                    case LOAD_CLIENT:
                        uri = args.getParcelable(ARG_URI);
                        return new CursorLoader(
                                getActivity(),
                                uri,
                                OrganisationContract.COLS, null, null, null);

                    default:
                        return null;
                }
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                if (cursor.moveToFirst()) {
                    switch (loader.getId()) {
                        case LOAD_APPLICATION:
                            application = Application.from(cursor, ApplicationContract.NAME + "_");

                            recruiterView.setObject(application.getRecruiter());

                            clientView.setObject(application.getClient());

                            CharSequence start = application.getStart();
                            startView.setText(start);

                            CharSequence rate = application.getRate();
                            rateView.setText(rate);

                            State state = workflow.getState(application.getStateId());
                            stateView.setText(state.getDescription());

                            actions = workflow.getActions(state);
                            getActivity().invalidateOptionsMenu();
                            break;

                        case LOAD_RECRUITER:
                            Recruiter recruiter = Recruiter.from(cursor, "");

                            recruiterView.setObject(recruiter);

                        case LOAD_CLIENT:
                            Organisation client = Organisation.from(cursor, "");

                            clientView.setObject(client);
                    }
                } else {
                    onLoaderReset(loader);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                switch (loader.getId()) {
                    case LOAD_APPLICATION:
                        application = null;
                        break;

                    case LOAD_RECRUITER:
                        if (recruiterView != null) {
                            recruiterView.setObject(null);
                        }
                        break;

                    case LOAD_CLIENT:
                        if (clientView != null) {
                            clientView.setObject(null);
                        }
                        break;
                }
            }
        };

        queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {

            @Override
            public void onInsertComplete(int token, Object cookie, Uri uri) {
                State state = workflow.getState(application.getStateId());
                Action action = (Action) cookie;
                update(workflow.getNextState(state, action));
            }

            @Override
            public void onUpdateComplete(int token, Object cookie, int result) {
                if (token == UPDATE_APPLICATION_STATE) {
                    parent.onApplicationStateUpdated();
                }
            }
        };

        getLoaderManager().initLoader(LOAD_APPLICATION, null, loaderCallbacks);
    }

    public void update(State nextState) {
        if (application != null) {
            int token = UPDATE_APPLICATION;

            application.resetChanged();

            Recruiter recruiter = recruiterView.getObject();
            application.setRecruiter(recruiter);

            Organisation client = clientView.getObject();
            application.setClient(client);

            String start = Parser.parseText(startView.getText());
            application.setStart(start);

            String rate = Parser.parseText(rateView.getText());
            application.setRate(rate);

            if (nextState != null) {
                token = UPDATE_APPLICATION_STATE;
                application.setStateId(nextState.getId());
            }

            if (application.hasChanged()) {
                Uri uri = getArguments().getParcelable(ARG_URI);

                ContentValues values = application.toValues();
                queryHandler.startUpdate(token, null, uri, values, null, null);
            }
        }
    }

    private void selectRecruiter() {
        Intent intent = new Intent(getActivity(), AuthorListActivity.class); // TODO
        startActivityForResult(intent, REQUEST_SELECT_RECRUITER);
    }

    private void selectClient() {
        Intent intent = new Intent(getActivity(), OrganisationListActivity.class);
        startActivityForResult(intent, REQUEST_SELECT_CLIENT);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_RECRUITER:
                onRecruiterActivityResult(resultCode, data);
                break;

            case REQUEST_SELECT_CLIENT:
                onClientActivityResult(resultCode, data);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onRecruiterActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            long recruiterId = data.getLongExtra(ARG_SELECTED_ID, 0L);
            if (recruiterView.getObjectId() != recruiterId) {
                if (recruiterId == 0L) {
                    recruiterView.setObject(null);
                } else {
                    Bundle args = new Bundle();
                    args.putParcelable(ARG_URI, RecruiterContract.ITEM_URI_TYPE.uri(recruiterId));
                    getLoaderManager().initLoader(LOAD_RECRUITER, args, loaderCallbacks);
                }
            }
        }
    }

    private void onClientActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            long clientId = data.getLongExtra(ARG_SELECTED_ID, 0L);
            if (clientView.getObjectId() != clientId) {
                if (clientId == 0L) {
                    clientView.setObject(null);
                } else {
                    Bundle args = new Bundle();
                    args.putParcelable(ARG_URI, OrganisationContract.ITEM_URI_TYPE.uri(clientId));
                    getLoaderManager().initLoader(LOAD_CLIENT, args, loaderCallbacks);
                }
            }
        }
    }

    @Override
    public void onPause() {
        update(null);
        super.onPause();
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
        void onApplicationStateUpdated();
    }
}
