package com.robinfinch.journal.app;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.robinfinch.journal.app.persistence.OrganisationContract;
import com.robinfinch.journal.app.persistence.RecruiterContract;
import com.robinfinch.journal.app.ui.DetailsFragment;
import com.robinfinch.journal.app.ui.NamedObjectView;
import com.robinfinch.journal.app.util.Parser;
import com.robinfinch.journal.domain.Organisation;
import com.robinfinch.journal.domain.Recruiter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robinfinch.journal.app.util.Constants.*;

/**
 * Recruiter details fragment.
 *
 * @organisation Mark Hoogenboom
 */
public class RecruiterFragment extends DetailsFragment {

    private static final int LOAD_RECRUITER = 1;
    private static final int LOAD_ORGANISATION = 2;
    private static final int LOAD_CONTACT = 3;
    private static final int UPDATE_RECRUITER = 4;
    private static final int DELETE_RECRUITER = 5;

    private static final int REQUEST_SELECT_ORGANISATION = 1;
    private static final int REQUEST_PICK_CONTACT = 2;

    public static RecruiterFragment newInstance(Uri uri) {
        RecruiterFragment fragment = new RecruiterFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.recruiter_name)
    protected EditText nameView;

    @InjectView(R.id.recruiter_organisation)
    protected NamedObjectView<Organisation> organisationView;

    @InjectView(R.id.recruiter_phone_number)
    protected EditText phoneNumberView;

    private Recruiter recruiter;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    private AsyncQueryHandler queryHandler;

    private Parent parent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (Parent) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recruiter_fragment, container, false);
        ButterKnife.inject(this, view);

        organisationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOrganisation();
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

                Uri uri;
                switch (id) {
                    case LOAD_RECRUITER:
                        uri = getArguments().getParcelable(ARG_URI);
                        return new CursorLoader(
                                getActivity(),
                                uri,
                                RecruiterContract.COLS, null, null, null);

                    case LOAD_ORGANISATION:
                        uri = args.getParcelable(ARG_URI);
                        return new CursorLoader(
                                getActivity(),
                                uri,
                                OrganisationContract.COLS, null, null, null);

                    case LOAD_CONTACT:
                        uri = args.getParcelable(ARG_URI);

                        String[] projection = {
                                ContactsContract.Contacts.DISPLAY_NAME,
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                        };
                        return new CursorLoader(
                                getActivity(),
                                uri,
                                projection, null, null, null);

                    default:
                        return null;
                }
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                if (cursor.moveToFirst()) {
                    switch (loader.getId()) {
                        case LOAD_RECRUITER:
                            recruiter = Recruiter.from(cursor, RecruiterContract.NAME + "_");

                            CharSequence name = recruiter.getName();
                            nameView.setText(name);

                            organisationView.setObject(recruiter.getOrganisation());

                            CharSequence phoneNumber = recruiter.getPhoneNumber();
                            phoneNumberView.setText(phoneNumber);
                            break;

                        case LOAD_ORGANISATION:
                            Organisation organisation = Organisation.from(cursor, "");

                            organisationView.setObject(organisation);
                            break;

                        case LOAD_CONTACT:
                            int i = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                            if (i != -1) {
                                name = cursor.getString(i);
                                nameView.setText(name);
                            }

                            i = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            if (i != -1) {
                                phoneNumber = cursor.getString(i);
                                phoneNumberView.setText(phoneNumber);
                            }
                            break;
                    }
                } else {
                    onLoaderReset(loader);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                switch (loader.getId()) {
                    case LOAD_RECRUITER:
                        recruiter = null;
                        break;

                    case LOAD_ORGANISATION:
                        if (organisationView != null) {
                            organisationView.setObject(null);
                        }
                        break;
                }
            }
        };

        queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {

            @Override
            public void onDeleteComplete(int token, Object cookie, int result) {
                parent.onRecruiterDeleted();
            }
        };

        getLoaderManager().initLoader(LOAD_RECRUITER, null, loaderCallbacks);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.CATEGORY_CONTAINER, R.id.recruiter_pick, 0, R.string.recruiter_pick)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recruiter_pick:
                pickFromContacts();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void pickFromContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_PICK_CONTACT);
    }

    @OnClick(R.id.recruiter_select)
    public void select() {
        parent.onRecruiterSelected(recruiter.getId());
    }

    @Override
    public void update() {
        if (recruiter != null) {
            recruiter.resetChanged();

            String name = Parser.parseText(nameView.getText());
            recruiter.setName(name);

            Organisation organisation = organisationView.getObject();
            recruiter.setOrganisation(organisation);

            String phoneNumber = Parser.parseText(phoneNumberView.getText());
            recruiter.setPhoneNumber(phoneNumber);

            if (recruiter.hasChanged()) {
                Uri uri = getArguments().getParcelable(ARG_URI);

                ContentValues values = recruiter.toValues();
                queryHandler.startUpdate(UPDATE_RECRUITER, null, uri, values, null, null);
            }
        }
    }

    @Override
    public void delete() {
        Uri uri = getArguments().getParcelable(ARG_URI);

        queryHandler.startDelete(DELETE_RECRUITER, null, uri, Long.toString(recruiter.getRemoteId()), null);
    }

    private void selectOrganisation() {
        Intent intent = new Intent(getActivity(), OrganisationListActivity.class);
        startActivityForResult(intent, REQUEST_SELECT_ORGANISATION);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_ORGANISATION:
                onOrganisationActivityResult(resultCode, data);
                break;

            case REQUEST_PICK_CONTACT:
                onContactActivityResult(resultCode, data);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onOrganisationActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            long organisationId = data.getLongExtra(ARG_SELECTED_ID, 0L);
            if (organisationView.getObjectId() != organisationId) {
                if (organisationId == 0L) {
                    organisationView.setObject(null);
                } else {
                    Bundle args = new Bundle();
                    args.putParcelable(ARG_URI, OrganisationContract.ITEM_URI_TYPE.uri(organisationId));
                    getLoaderManager().initLoader(LOAD_ORGANISATION, args, loaderCallbacks);
                }
            }
        }
    }

    private void onContactActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri contactUri = data.getData();

            Log.d(LOG_TAG, "Picked contact " + contactUri);

            Bundle args = new Bundle();
            args.putParcelable(ARG_URI, contactUri);
            getLoaderManager().initLoader(LOAD_CONTACT, args, loaderCallbacks);
        }
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
        void onRecruiterSelected(long id);
        void onRecruiterDeleted();
    }
}
