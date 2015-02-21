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

import com.robinfinch.journal.app.persistence.AuthorContract;
import com.robinfinch.journal.app.ui.ListFragment;
import com.robinfinch.journal.domain.Author;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.robinfinch.journal.app.util.Utils.alias;

/**
 * List of authors fragment.
 *
 * @author Mark Hoogenboom
 */
public class AuthorListFragment extends ListFragment {

    private static final int LOAD_AUTHORS = 1;
    private static final int INSERT_AUTHOR = 2;

    public static AuthorListFragment newInstance() {
        AuthorListFragment fragment = new AuthorListFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    private AsyncQueryHandler queryHandler;

    private Parent parent;

    @Override
    protected int getHeaderResId() {
        return R.string.authors;
    }

    @Override
    protected int getAddButtonResId() {
        return R.string.author_add;
    }

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
                View view = LayoutInflater.from(context).inflate(R.layout.author_list_item, parent, false);

                ViewHolder viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);

                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                Author author = Author.from(cursor);

                ViewHolder viewHolder = (ViewHolder) view.getTag();
                viewHolder.bind(author);
            }
        };
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                return new CursorLoader(
                        getActivity(),
                        AuthorContract.DIR_URI_TYPE.uri(getActivity()),
                        AuthorContract.COLS, null, null,
                        alias(AuthorContract.NAME, AuthorContract.COL_NAME) + " ASC");
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
                parent.onAuthorItemSelected(uri);
            }
        };

        getLoaderManager().initLoader(LOAD_AUTHORS, null, loaderCallbacks);
    }

    @Override
    protected void add() {
        ContentValues initialValues = new ContentValues();

        queryHandler.startInsert(INSERT_AUTHOR, null, AuthorContract.DIR_URI_TYPE.uri(getActivity()), initialValues);
    }

    @Override
    protected void select(long id) {
        parent.onAuthorItemSelected(AuthorContract.ITEM_URI_TYPE.uri(getActivity(), id));
    }

    @Override
    public void onDetach() {
        parent = null;
        super.onDetach();
    }

    public interface Parent {
        void onAuthorItemSelected(Uri uri);
    }

    static class ViewHolder {

        @InjectView(R.id.author_name)
        protected TextView nameView;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void bind(Author author) {

            nameView.setText(author.getName());
        }
    }
}
