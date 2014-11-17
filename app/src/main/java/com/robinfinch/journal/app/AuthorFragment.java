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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.robinfinch.journal.app.persistence.AuthorContract;
import com.robinfinch.journal.app.ui.DetailsFragment;
import com.robinfinch.journal.app.util.Parser;
import com.robinfinch.journal.domain.Author;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * Author details fragment.
 *
 * @author Mark Hoogenboom
 */
public class AuthorFragment extends DetailsFragment {

    private static final int LOAD_AUTHOR = 1;
    private static final int UPDATE_AUTHOR = 2;
    private static final int DELETE_AUTHOR = 3;

    public static AuthorFragment newInstance(Uri uri) {
        AuthorFragment fragment = new AuthorFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.author_name)
    protected EditText nameView;

    private Author author;

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
        View view = inflater.inflate(R.layout.author_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
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
                        AuthorContract.COLS, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                if (cursor.moveToFirst()) {
                    author = Author.from(cursor, "");

                    CharSequence name = author.getName();
                    nameView.setText(name);
                } else {
                    onLoaderReset(loader);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                author = null;
            }
        };

        queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {

            @Override
            public void onUpdateComplete(int token, Object cookie, int result) {

            }

            @Override
            public void onDeleteComplete(int token, Object cookie, int result) {
                parent.onAuthorDeleted();
            }
        };

        getLoaderManager().initLoader(LOAD_AUTHOR, null, loaderCallbacks);
    }

    @OnClick(R.id.author_select)
    public void select() {
        parent.onAuthorSelected(author.getId());
    }

    @Override
    public void update() {
        if (author != null) {
            author.resetChanged();

            String name = Parser.parseText(nameView.getText());
            author.setName(name);

            if (author.hasChanged()) {
                Uri uri = getArguments().getParcelable(ARG_URI);

                ContentValues values = author.toValues();
                queryHandler.startUpdate(UPDATE_AUTHOR, null, uri, values, null, null);
            }
        }
    }

    @Override
    public void delete() {
        Uri uri = getArguments().getParcelable(ARG_URI);

        queryHandler.startDelete(DELETE_AUTHOR, null, uri, Long.toString(author.getRemoteId()), null);
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
        void onAuthorSelected(long id);
        void onAuthorDeleted();
    }
}
