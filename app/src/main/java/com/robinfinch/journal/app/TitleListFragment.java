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

import com.robinfinch.journal.app.persistence.AuthorContract;
import com.robinfinch.journal.app.persistence.TitleContract;
import com.robinfinch.journal.app.ui.ListFragment;
import com.robinfinch.journal.domain.Title;

/**
 * List of titles fragment.
 *
 * @title Mark Hogenboom
 */
public class TitleListFragment extends ListFragment {

    private static final int LOAD_TITLES = 1;
    private static final int INSERT_TITLE = 2;

    public static TitleListFragment newInstance() {
        TitleListFragment fragment = new TitleListFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    private AsyncQueryHandler queryHandler;

    private Parent parent;

    @Override
    protected int getHeaderResId() {
        return R.string.titles;
    }

    @Override
    protected int getAddButtonResId() {
        return R.string.title_add;
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
                View view = LayoutInflater.from(context).inflate(R.layout.title_list_item, parent, false);

                TitleViewHolder viewHolder = new TitleViewHolder(view);
                view.setTag(viewHolder);

                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                Title title = Title.from(cursor, TitleContract.NAME + "_");

                TitleViewHolder viewHolder = (TitleViewHolder) view.getTag();
                viewHolder.bind(title);
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
                        TitleContract.DIR_URI_TYPE.uri(),
                        TitleContract.COLS, null, null, AuthorContract.COL_NAME + ", " + TitleContract.COL_YEAR +  " ASC");
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
                parent.onTitleItemSelected(uri);
            }
        };

        getLoaderManager().initLoader(LOAD_TITLES, null, loaderCallbacks);
    }

    @Override
    protected void add() {
        ContentValues initialValues = new ContentValues();

        queryHandler.startInsert(INSERT_TITLE, null, TitleContract.DIR_URI_TYPE.uri(), initialValues);
    }

    @Override
    protected void select(long id) {
        parent.onTitleItemSelected(TitleContract.ITEM_URI_TYPE.uri(id));
    }

    @Override
    public void onDetach() {
        parent = null;
        super.onDetach();
    }

    public interface Parent {
        void onTitleItemSelected(Uri uri);
    }
}
