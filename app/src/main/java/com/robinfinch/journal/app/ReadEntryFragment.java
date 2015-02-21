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

import com.robinfinch.journal.app.persistence.ReadEntryContract;
import com.robinfinch.journal.app.persistence.TitleContract;
import com.robinfinch.journal.app.ui.DetailsFragment;
import com.robinfinch.journal.app.ui.NamedObjectView;
import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.app.util.Parser;
import com.robinfinch.journal.domain.ReadEntry;
import com.robinfinch.journal.domain.Title;

import java.util.Date;

import butterknife.InjectView;

import static com.robinfinch.journal.app.util.Constants.ARG_SELECTED_ID;
import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * Read entry details fragment.
 *
 * @author Mark Hoogenboom
 */
public class ReadEntryFragment extends DetailsFragment<ReadEntry> {

    private static final int LOAD_READ_ENTRY = 1;
    private static final int LOAD_TITLE = 2;
    private static final int UPDATE_READ_ENTRY = 3;

    private static final int REQUEST_SELECT_TITLE = 1;

    public static ReadEntryFragment newInstance(Uri uri) {
        ReadEntryFragment fragment = new ReadEntryFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.readentry_dayread)
    protected EditText dayOfEntryView;

    @InjectView(R.id.readentry_title)
    protected NamedObjectView<Title> titleView;

    @InjectView(R.id.readentry_part)
    protected EditText partView;

    private Parent parent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (Parent) activity;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.readentry_fragment;
    }

    @Override
    protected void initListeners() {
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTitle();
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
                    case LOAD_READ_ENTRY:
                        uri = getArguments().getParcelable(ARG_URI);
                        return new CursorLoader(
                                getActivity(),
                                uri,
                                ReadEntryContract.COLS, null, null, null);

                    case LOAD_TITLE:
                        uri = args.getParcelable(ARG_URI);
                        return new CursorLoader(
                                getActivity(),
                                uri,
                                TitleContract.COLS, null, null, null);

                    default:
                        return null;
                }
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                if (cursor.moveToFirst()) {
                    switch (loader.getId()) {
                        case LOAD_READ_ENTRY:
                            onReadEntryLoaded(cursor);
                            break;

                        case LOAD_TITLE:
                            onTitleLoaded(cursor);
                            break;
                    }
                } else {
                    onLoaderReset(loader);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                switch (loader.getId()) {
                    case LOAD_READ_ENTRY:
                        entity = null;
                        break;

                    case LOAD_TITLE:
                        if (titleView != null) {
                            titleView.setObject(null);
                        }
                        break;
                }
            }
        };

        queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {

            @Override
            public void onDeleteComplete(int token, Object cookie, int result) {
                parent.onReadEntryDeleted();
            }
        };

        getLoaderManager().initLoader(LOAD_READ_ENTRY, null, loaderCallbacks);
    }

    private void onReadEntryLoaded(Cursor cursor) {
        entity = ReadEntry.from(cursor);

        CharSequence dayOfEntry = Formatter.formatDayForInput(entity.getDayOfEntry());
        dayOfEntryView.setText(dayOfEntry);

        titleView.setObject(entity.getTitle());

        CharSequence part = entity.getPart();
        partView.setText(part);

        setShareText(entity.toShareString());
    }

    private void onTitleLoaded(Cursor cursor) {
        Title title = Title.from(cursor);
        titleView.setObject(title);
    }

    @Override
    public void update() {
        if (entity != null) {
            entity.resetChanged();

            Date dayOfEntry = Parser.parseDay(dayOfEntryView.getText());
            entity.setDayOfEntry(dayOfEntry);

            Title title = titleView.getObject();
            entity.setTitle(title);

            String part = Parser.parseText(partView.getText());
            entity.setPart(part);

            if (entity.hasChanged()) {
                Uri uri = getArguments().getParcelable(ARG_URI);

                ContentValues values = entity.toValues();
                queryHandler.startUpdate(UPDATE_READ_ENTRY, null, uri, values, null, null);
            }
        }
    }

    private void selectTitle() {
        Intent intent = new Intent(getActivity(), TitleListActivity.class);
        startActivityForResult(intent, REQUEST_SELECT_TITLE);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_TITLE:
                onTitleActivityResult(resultCode, data);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onTitleActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            long titleId = data.getLongExtra(ARG_SELECTED_ID, 0L);
            if (titleView.getObjectId() != titleId) {
                if (titleId == 0L) {
                    titleView.setObject(null);
                } else {
                    Bundle args = new Bundle();
                    args.putParcelable(ARG_URI, TitleContract.ITEM_URI_TYPE.uri(getActivity(), titleId));
                    getLoaderManager().initLoader(LOAD_TITLE, args, loaderCallbacks);
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
        void onReadEntryDeleted();
    }
}
