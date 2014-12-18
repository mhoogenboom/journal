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

import com.robinfinch.journal.app.persistence.CourseContract;
import com.robinfinch.journal.app.ui.DetailsFragment;
import com.robinfinch.journal.app.util.Parser;
import com.robinfinch.journal.domain.Course;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * Course details fragment.
 *
 * @author Mark Hoogenboom
 */
public class CourseFragment extends DetailsFragment {

    private static final int LOAD_COURSE = 1;
    private static final int UPDATE_COURSE = 2;
    private static final int DELETE_COURSE = 3;

    public static CourseFragment newInstance(Uri uri) {
        CourseFragment fragment = new CourseFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.course_name)
    protected EditText nameView;

    private Course course;

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
        View view = inflater.inflate(R.layout.course_fragment, container, false);
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
                        CourseContract.COLS, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                if (cursor.moveToFirst()) {
                    course = Course.from(cursor);

                    CharSequence name = course.getName();
                    nameView.setText(name);
                } else {
                    onLoaderReset(loader);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                course = null;
            }
        };

        queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {

            @Override
            public void onDeleteComplete(int token, Object cookie, int result) {
                parent.onCourseDeleted();
            }
        };

        getLoaderManager().initLoader(LOAD_COURSE, null, loaderCallbacks);
    }

    @OnClick(R.id.course_select)
    public void select() {
        parent.onCourseSelected(course.getId());
    }

    @Override
    public void update() {
        if (course != null) {
            course.resetChanged();

            String name = Parser.parseText(nameView.getText());
            course.setName(name);

            if (course.hasChanged()) {
                Uri uri = getArguments().getParcelable(ARG_URI);

                ContentValues values = course.toValues();
                queryHandler.startUpdate(UPDATE_COURSE, null, uri, values, null, null);
            }
        }
    }

    @Override
    public void delete() {
        Uri uri = getArguments().getParcelable(ARG_URI);

        queryHandler.startDelete(DELETE_COURSE, null, uri, Long.toString(course.getRemoteId()), null);
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
        void onCourseSelected(long id);
        void onCourseDeleted();
    }
}
