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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.robinfinch.journal.app.persistence.AuthorContract;
import com.robinfinch.journal.app.persistence.TitleContract;
import com.robinfinch.journal.app.ui.DetailsFragment;
import com.robinfinch.journal.app.ui.NamedObjectView;
import com.robinfinch.journal.app.util.Parser;
import com.robinfinch.journal.domain.Author;
import com.robinfinch.journal.domain.Title;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robinfinch.journal.app.util.Constants.ARG_SELECTED_ID;
import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * Title details fragment.
 *
 * @author Mark Hoogenboom
 */
public class TitleFragment extends DetailsFragment {

    private static final int LOAD_TITLE = 1;
    private static final int LOAD_AUTHOR = 2;
    private static final int UPDATE_TITLE = 3;
    private static final int DELETE_TITLE = 4;

    private static final int REQUEST_SELECT_AUTHOR = 1;

    public static TitleFragment newInstance(Uri uri) {
        TitleFragment fragment = new TitleFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.title_title)
    protected EditText titleView;

    @InjectView(R.id.title_author)
    protected NamedObjectView<Author> authorView;

    @InjectView(R.id.title_year)
    protected EditText yearView;

    private Title title;

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
        View view = inflater.inflate(R.layout.title_fragment, container, false);
        ButterKnife.inject(this, view);

        authorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAuthor();
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
                    case LOAD_TITLE:
                        uri = getArguments().getParcelable(ARG_URI);
                        return new CursorLoader(
                                getActivity(),
                                uri,
                                TitleContract.COLS, null, null, null);

                    case LOAD_AUTHOR:
                        uri = args.getParcelable(ARG_URI);
                        return new CursorLoader(
                                getActivity(),
                                uri,
                                AuthorContract.COLS, null, null, null);

                    default:
                        return null;
                }
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                if (cursor.moveToFirst()) {
                    switch (loader.getId()) {
                        case LOAD_TITLE:
                            title = Title.from(cursor, TitleContract.NAME + "_");

                            CharSequence t = title.getTitle();
                            titleView.setText(t);

                            authorView.setObject(title.getAuthor());

                            CharSequence year = title.getYear();
                            yearView.setText(year);
                            break;

                        case LOAD_AUTHOR:
                            Author author = Author.from(cursor, "");

                            authorView.setObject(author);
                    }
                } else {
                    onLoaderReset(loader);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                switch (loader.getId()) {
                    case LOAD_TITLE:
                        title = null;
                        break;

                    case LOAD_AUTHOR:
                        if (authorView != null) {
                            authorView.setObject(null);
                        }
                        break;
                }
            }
        };

        queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {

            @Override
            public void onDeleteComplete(int token, Object cookie, int result) {
                parent.onTitleDeleted();
            }
        };

        getLoaderManager().initLoader(LOAD_TITLE, null, loaderCallbacks);
    }

    @OnClick(R.id.title_select)
    public void select() {
        parent.onTitleSelected(title.getId());
    }

    @Override
    public void update() {
        if (title != null) {
            title.resetChanged();

            String t = Parser.parseText(titleView.getText());
            title.setTitle(t);

            Author author = authorView.getObject();
            title.setAuthor(author);

            String year = Parser.parseText(yearView.getText());
            title.setYear(year);

            if (title.hasChanged()) {
                Uri uri = getArguments().getParcelable(ARG_URI);

                ContentValues values = title.toValues();
                queryHandler.startUpdate(UPDATE_TITLE, null, uri, values, null, null);
            }
        }
    }

    @Override
    public void delete() {
        Uri uri = getArguments().getParcelable(ARG_URI);

        queryHandler.startDelete(DELETE_TITLE, null, uri, Long.toString(title.getRemoteId()), null);
    }

    private void selectAuthor() {
        Intent intent = new Intent(getActivity(), AuthorListActivity.class);
        startActivityForResult(intent, REQUEST_SELECT_AUTHOR);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_AUTHOR:
                onAuthorActivityResult(resultCode, data);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onAuthorActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            long authorId = data.getLongExtra(ARG_SELECTED_ID, 0L);
            if (authorView.getObjectId() != authorId) {
                if (authorId == 0L) {
                    authorView.setObject(null);
                } else {
                    Bundle args = new Bundle();
                    args.putParcelable(ARG_URI, AuthorContract.ITEM_URI_TYPE.uri(authorId));
                    getLoaderManager().initLoader(LOAD_AUTHOR, args, loaderCallbacks);
                }
            }
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
        void onTitleSelected(long id);
        void onTitleDeleted();
    }
}
