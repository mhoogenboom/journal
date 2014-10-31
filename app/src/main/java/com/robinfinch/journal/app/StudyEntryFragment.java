package com.robinfinch.journal.app;

import android.app.Activity;
import android.app.Fragment;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.robinfinch.journal.app.persistence.CourseContract;
import com.robinfinch.journal.app.persistence.StudyEntryContract;
import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.app.util.Parser;
import com.robinfinch.journal.domain.Course;
import com.robinfinch.journal.domain.StudyEntry;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robinfinch.journal.app.util.Constants.ARG_SELECTED_ID;
import static com.robinfinch.journal.app.util.Constants.ARG_URI;

/**
 * Study entry details fragment.
 *
 * @author Mark Hoogenboom
 */
public class StudyEntryFragment extends Fragment {

    private static final int LOAD_STUDY_ENTRY = 1;
    private static final int LOAD_COURSE = 2;
    private static final int UPDATE_STUDY_ENTRY = 3;
    private static final int DELETE_STUDY_ENTRY = 4;

    private static final int REQUEST_SELECT_COURSE = 1;

    public static StudyEntryFragment newInstance(Uri uri) {
        StudyEntryFragment fragment = new StudyEntryFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.studyentry_dayofstudy)
    protected EditText dayOfStudyView;

    @InjectView(R.id.studyentry_course)
    protected CourseView courseView;

    @InjectView(R.id.studyentry_description)
    protected EditText descriptionView;

    private StudyEntry entry;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    private AsyncQueryHandler queryHandler;

    private Parent parent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (Parent) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.studyentry_fragment, container, false);
        ButterKnife.inject(this, view);

        courseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCourse();
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.CATEGORY_CONTAINER, R.id.studyentry_delete, 0, R.string.studyentry_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
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
                            entry = StudyEntry.from(cursor, StudyEntryContract.NAME + "_");

                            CharSequence dayOfEntry = Formatter.formatDayForInput(entry.getDayOfEntry());
                            dayOfStudyView.setText(dayOfEntry);

                            courseView.setCourse(entry.getCourse());

                            CharSequence description = entry.getDescription();
                            descriptionView.setText(description);
                            break;

                        case LOAD_COURSE:
                            Course course = Course.from(cursor, "");

                            courseView.setCourse(course);
                    }
                } else {
                    onLoaderReset(loader);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                switch (loader.getId()) {
                    case LOAD_STUDY_ENTRY:
                        entry = null;
                        break;

                    case LOAD_COURSE:
                        if (courseView != null) {
                            courseView.setCourse(null);
                        }
                        break;
                }
            }
        };

        queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {

            @Override
            public void onUpdateComplete(int token, Object cookie, int result) {

            }

            @Override
            public void onDeleteComplete(int token, Object cookie, int result) {
                parent.onStudyEntryDeleted();
            }
        };

        getLoaderManager().initLoader(LOAD_STUDY_ENTRY, null, loaderCallbacks);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.studyentry_delete:
                deleteStudyEntry();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateStudyEntry() {
        if (entry != null) {
            entry.resetChanged();

            Date dayOfEntry = Parser.parseDay(dayOfStudyView.getText());
            entry.setDayOfEntry(dayOfEntry);

            Course course = courseView.getCourse();
            entry.setCourse(course);

            String description = descriptionView.getText().toString();
            entry.setDescription(description);

            if (entry.hasChanged()) {
                Uri uri = getArguments().getParcelable(ARG_URI);

                ContentValues values = entry.toValues();
                queryHandler.startUpdate(UPDATE_STUDY_ENTRY, null, uri, values, null, null);
            }
        }
    }

    @OnClick(R.id.studyentry_delete)
    public void deleteStudyEntry() {
        Uri uri = getArguments().getParcelable(ARG_URI);

        queryHandler.startDelete(DELETE_STUDY_ENTRY, null, uri, Long.toString(entry.getRemoteId()), null);
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
            if (courseView.getCourseId() != courseId) {
                if (courseId == 0L) {
                    courseView.setCourse(null);
                } else {
                    Bundle args = new Bundle();
                    args.putParcelable(ARG_URI, CourseContract.ITEM_URI_TYPE.uri(courseId));
                    getLoaderManager().initLoader(LOAD_COURSE, args, loaderCallbacks);
                }
            }
        }
    }

    @Override
    public void onPause() {
        updateStudyEntry();
        super.onPause();
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
        void onStudyEntryDeleted();
    }
}
