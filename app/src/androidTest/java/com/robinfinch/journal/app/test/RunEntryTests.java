package com.robinfinch.journal.app.test;

import com.robinfinch.journal.app.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.clearText;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

/**
 * Run entry related tests.
 *
 * @author Mark Hoogenboom
 */
public class RunEntryTests extends JournalEntryTests {

    public void testCrud() {
        navigateToList("Runs");

        createItem();
        updateItem();
        deleteItem();

        // onData().check(doesNotExist());
    }

    @Override
    protected void createItem() {
        onView(withId(R.id.entity_add)).perform(click());

        onView(withText("Run Details")).check(matches(isDisplayed()));
        onView(withId(R.id.runentry_dayofrun)).check(matches(withText(today())));
        onView(withId(R.id.runentry_distance)).check(matches(withText("0")));
        onView(withId(R.id.runentry_timetaken)).check(matches(withText("0:00")));
        onView(withId(R.id.runentry_dayofrun)).perform(clearText(), typeText("1/2/2010"));

        // pressBack();
    }

    @Override
    protected void updateItem() {
        // onData().perform(click());

        onView(withText("Run Details")).check(matches(isDisplayed()));
        onView(withId(R.id.runentry_dayofrun)).check(matches(withText("1/2/2010")));
        onView(withId(R.id.runentry_distance)).check(matches(withText("0")));
        onView(withId(R.id.runentry_timetaken)).check(matches(withText("0:00")));
        onView(withId(R.id.runentry_dayofrun)).perform(clearText(), typeText("2/2/2010"));
        onView(withId(R.id.runentry_distance)).perform(clearText(), typeText("5000"));
        onView(withId(R.id.runentry_timetaken)).perform(clearText(), typeText("25:00"));

        // pressBack();
    }

    @Override
    protected void deleteItem() {
        // onData().perform(click());

        onView(withText("Run Details")).check(matches(isDisplayed()));
        onView(withId(R.id.runentry_dayofrun)).check(matches(withText("2/2/2010")));
        onView(withId(R.id.runentry_distance)).check(matches(withText("5000")));
        onView(withId(R.id.runentry_timetaken)).check(matches(withText("25:00")));

        onView(withId(R.id.entity_delete)).perform(click());
    }
}
