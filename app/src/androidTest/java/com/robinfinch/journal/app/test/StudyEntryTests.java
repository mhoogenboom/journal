package com.robinfinch.journal.app.test;

import com.robinfinch.journal.app.R;
import com.robinfinch.journal.domain.NamedObject;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.clearText;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static com.robinfinch.journal.app.test.Matchers.withName;
import static com.robinfinch.journal.app.test.Matchers.withNamedObject;
import static org.hamcrest.Matchers.is;

/**
 * Study entry related tests.
 *
 * @author Mark Hoogenboom
 */
public class StudyEntryTests extends JournalEntryTests {

    public void testCrud() {
        navigateToList("Studied");

        createItem();
        updateItem();
        deleteItem();

        // onData().check(doesNotExist());
    }

    @Override
    protected void createItem() {
        onView(withId(R.id.entity_add)).perform(click());

        onView(withText("Details")).check(matches(isDisplayed()));
        onView(withId(R.id.studyentry_dayofstudy)).check(matches(withText(today())));
        onView(withId(R.id.studyentry_course)).check(matches(withNamedObject(org.hamcrest.Matchers.<NamedObject>nullValue())));
        onView(withId(R.id.studyentry_description)).check(matches(withText("")));
        onView(withId(R.id.studyentry_dayofstudy)).perform(clearText(), typeText("1/2/2010"));

        // pressBack();
    }

    @Override
    protected void updateItem() {
        // onData().perform(click());

        onView(withText("Details")).check(matches(isDisplayed()));
        onView(withId(R.id.studyentry_dayofstudy)).check(matches(withText("1/2/2010")));
        onView(withId(R.id.studyentry_course)).check(matches(withNamedObject(org.hamcrest.Matchers.<NamedObject>nullValue())));
        onView(withId(R.id.studyentry_description)).check(matches(withText("")));
        onView(withId(R.id.studyentry_dayofstudy)).perform(clearText(), typeText("2/2/2010"));
        setCourse();
        onView(withId(R.id.studyentry_description)).perform(clearText(), typeText("lesson 1"));

        // pressBack();
    }

    private void setCourse() {
        onView(withId(R.id.studyentry_course)).perform(click());
        onView(withId(R.id.entity_add)).perform(click());
        onView(withId(R.id.course_name)).perform(typeText("Introduction to unit testing"));
        onView(withId(R.id.course_select)).perform(click());
    }

    @Override
    protected void deleteItem() {
        // onData().perform(click());

        onView(withText("Details")).check(matches(isDisplayed()));
        onView(withId(R.id.studyentry_dayofstudy)).check(matches(withText("02/02/2010")));
        onView(withId(R.id.studyentry_course)).check(matches(withNamedObject(withName(is("Introduction to unit testing")))));
        onView(withId(R.id.studyentry_description)).check(matches(withText("lesson 1")));

        onView(withId(R.id.entity_delete)).perform(click());
    }
}
