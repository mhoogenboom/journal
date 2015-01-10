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

import com.robinfinch.journal.app.persistence.CourseContract;
import com.robinfinch.journal.app.persistence.StudyEntryContract;
import com.robinfinch.journal.app.ui.DetailsFragment;
import com.robinfinch.journal.app.ui.NamedObjectView;
import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.app.util.Parser;
import com.robinfinch.journal.domain.Course;
import com.robinfinch.journal.domain.StudyEntry;

import java.util.Date;

import butterknife.InjectView;

import static com.robinfinch.journal.app.util.Constants.ARG_SELECTED_ID;
import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * Study entry details fragment.
 *
 * @author Mark Hoogenboom
 */
public class StudyEntryFragment extends DetailsFragment<StudyEntry> {

    private static final int LOAD_STUDY_ENTRY = 1;
    private static final int LOAD_COURSE = 2;
    private static final int UPDATE_STUDY_ENTRY = 3;

    private static final int REQUEST_SELECT_COURSE = 1;

    public static StudyEntryFragment newInstance(Uri uri) {
        StudyEntryFragment fragment = new StudyEntryFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.studyentry_dayofstudy)
    protected EditText dayOfEntryView;

    @InjectView(R.id.studyentry_course)
    protected NamedObjectView<Course> courseView;

    @InjectView(R.id.studyentry_description)
    protected EditText descriptionView;

    private Parent parent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (Parent) activity;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.studyentry_fragment;
    }

    @Override
    protected void initListeners() {
        courseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCourse();
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
                    case LOAD_STUDY_ENTRY:
                        uri = getArguments().getParcelable(ARG_URI);
                        return new CursorLoader(
                                getActivity(),
                                uri,
                                StudyEntryContract.COLS, null, null, null);

                    case LOAD_COURSE:
                        uri = args.getParcelable(ARG_URI);
                        return new CursorLoader(
                                getActivity(),
                                uri,
                                CourseContract.COLS, null, null, null);

                    default:
                        return null;
                }
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                if (cursor.moveToFirst()) {
                    switch (loader.getId()) {
                        case LOAD_STUDY_ENTRY:
                            onStudyEntryLoaded(cursor);
                            break;

                        case LOAD_COURSE:
                            onCourseLoaded(cursor);
                            break;
                    }
                } else {
                    onLoaderReset(loader);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                switch (loader.getId()) {
                    case LOAD_STUDY_ENTRY:
                        entity = null;
                        break;

                    case LOAD_COURSE:
                        if (courseView != null) {
                            courseView.setObject(null);
                        }
                        break;
                }
            }
        };

        queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {

            @Override
            public void onDeleteComplete(int token, Object cookie, int result) {
                parent.onStudyEntryDeleted();
            }
        };

        getLoaderManager().initLoader(LOAD_STUDY_ENTRY, null, loaderCallbacks);
    }

    private void onStudyEntryLoaded(Cursor cursor) {
        entity = StudyEntry.from(cursor);

        CharSequence dayOfEntry = Formatter.formatDayForInput(entity.getDayOfEntry());
        dayOfEntryView.setText(dayOfEntry);

        courseView.setObject(entity.getCourse());

        CharSequence description = entity.getDescription();
        descriptionView.setText(description);

        setShareText(entity.toShareString());
    }

    private void onCourseLoaded(Cursor cursor) {
        Course course = Course.from(cursor);

        courseView.setObject(course);
    }

    @Override
    public void update() {
        if (entity != null) {
            entity.resetChanged();

            Date dayOfEntry = Parser.parseDay(dayOfEntryView.getText());
            entity.setDayOfEntry(dayOfEntry);

            Course course = courseView.getObject();
            entity.setCourse(course);

            String description = Parser.parseText(descriptionView.getText());
            entity.setDescription(description);

            if (entity.hasChanged()) {
                Uri uri = getArguments().getParcelable(ARG_URI);

                ContentValues values = entity.toValues();
                queryHandler.startUpdate(UPDATE_STUDY_ENTRY, null, uri, values, null, null);
            }
        }
    }

    private void selectCourse() {
        Intent intent = new Intent(getActivity(), CourseListActivity.class);
        startActivityForResult(intent, REQUEST_SELECT_COURSE);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_COURSE:
                onCourseActivityResult(resultCode, data);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onCourseActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            long courseId = data.getLongExtra(ARG_SELECTED_ID, 0L);
            if (courseView.getObjectId() != courseId) {
                if (courseId == 0L) {
                    courseView.setObject(null);
                } else {
                    Bundle args = new Bundle();
                    args.putParcelable(ARG_URI, CourseContract.ITEM_URI_TYPE.uri(courseId));
                    getLoaderManager().initLoader(LOAD_COURSE, args, loaderCallbacks);
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
        void onStudyEntryDeleted();
    }
}
