package com.robinfinch.journal.app;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

import com.robinfinch.journal.app.persistence.OrganisationContract;
import com.robinfinch.journal.app.ui.DetailsFragment;
import com.robinfinch.journal.app.util.Parser;
import com.robinfinch.journal.domain.Organisation;

import butterknife.InjectView;
import butterknife.OnClick;

import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * Organisation details fragment.
 *
 * @organisation Mark Hoogenboom
 */
public class OrganisationFragment extends DetailsFragment<Organisation> {

    private static final int LOAD_ORGANISATION = 1;
    private static final int UPDATE_ORGANISATION = 2;

    public static OrganisationFragment newInstance(Uri uri) {
        OrganisationFragment fragment = new OrganisationFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.organisation_name)
    protected EditText nameView;

    private Parent parent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (Parent) activity;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.organisation_fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Uri uri = getArguments().getParcelable(ARG_URI);
                return new CursorLoader(
                        getActivity(),
                        uri,
                        OrganisationContract.COLS, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                if (cursor.moveToFirst()) {
                    onOrganisationLoaded(cursor);
                } else {
                    onLoaderReset(loader);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                entity = null;
            }
        };

        queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {

            @Override
            public void onDeleteComplete(int token, Object cookie, int result) {
                parent.onOrganisationDeleted();
            }
        };

        getLoaderManager().initLoader(LOAD_ORGANISATION, null, loaderCallbacks);
    }

    private void onOrganisationLoaded(Cursor cursor) {
        entity = Organisation.from(cursor);

        CharSequence name = entity.getName();
        nameView.setText(name);

        setShareText(entity.toShareString());
    }

    @OnClick(R.id.organisation_select)
    public void select() {
        parent.onOrganisationSelected(entity.getId());
    }

    @Override
    public void update() {
        if (entity != null) {
            entity.resetChanged();

            String name = Parser.parseText(nameView.getText());
            entity.setName(name);

            if (entity.hasChanged()) {
                Uri uri = getArguments().getParcelable(ARG_URI);

                ContentValues values = entity.toValues();
                queryHandler.startUpdate(UPDATE_ORGANISATION, null, uri, values, null, null);
            }
        }
    }

    @Override
    public void onDetach() {
        parent = null;
        super.onDetach();
    }

    public interface Parent {
        void onOrganisationSelected(long id);
        void onOrganisationDeleted();
    }
}
