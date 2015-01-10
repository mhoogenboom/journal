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
import android.view.View;
import android.widget.EditText;

import com.robinfinch.journal.app.persistence.AuthorContract;
import com.robinfinch.journal.app.persistence.TitleContract;
import com.robinfinch.journal.app.ui.DetailsFragment;
import com.robinfinch.journal.app.ui.NamedObjectView;
import com.robinfinch.journal.app.util.Parser;
import com.robinfinch.journal.domain.Author;
import com.robinfinch.journal.domain.Title;

import butterknife.InjectView;
import butterknife.OnClick;

import static com.robinfinch.journal.app.util.Constants.ARG_SELECTED_ID;
import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * Title details fragment.
 *
 * @author Mark Hoogenboom
 */
public class TitleFragment extends DetailsFragment<Title> {

    private static final int LOAD_TITLE = 1;
    private static final int LOAD_AUTHOR = 2;
    private static final int UPDATE_TITLE = 3;

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

    private Parent parent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (Parent) activity;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.title_fragment;
    }

    @Override
    protected void initListeners() {
        authorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAuthor();
            }
        });
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
                            onTitleLoaded(cursor);
                            break;

                        case LOAD_AUTHOR:
                            onAuthorLoaded(cursor);
                            break;
                    }
                } else {
                    onLoaderReset(loader);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                switch (loader.getId()) {
                    case LOAD_TITLE:
                        entity = null;
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

    private void onTitleLoaded(Cursor cursor) {
        entity = Title.from(cursor);

        CharSequence t = entity.getTitle();
        titleView.setText(t);

        authorView.setObject(entity.getAuthor());

        CharSequence year = entity.getYear();
        yearView.setText(year);

        setShareText(entity.toShareString());
    }

    private void onAuthorLoaded(Cursor cursor) {
        Author author = Author.from(cursor);

        authorView.setObject(author);
    }

    @OnClick(R.id.title_select)
    public void select() {
        parent.onTitleSelected(entity.getId());
    }

    @Override
    public void update() {
        if (entity != null) {
            entity.resetChanged();

            String t = Parser.parseText(titleView.getText());
            entity.setTitle(t);

            Author author = authorView.getObject();
            entity.setAuthor(author);

            String year = Parser.parseText(yearView.getText());
            entity.setYear(year);

            if (entity.hasChanged()) {
                Uri uri = getArguments().getParcelable(ARG_URI);

                ContentValues values = entity.toValues();
                queryHandler.startUpdate(UPDATE_TITLE, null, uri, values, null, null);
            }
        }
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
    public void onDetach() {
        parent = null;
        super.onDetach();
    }

    public interface Parent {
        void onTitleSelected(long id);
        void onTitleDeleted();
    }
}
