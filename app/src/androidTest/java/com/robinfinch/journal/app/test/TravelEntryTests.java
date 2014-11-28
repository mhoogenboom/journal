package com.robinfinch.journal.app.test;

import com.robinfinch.journal.app.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.*;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.clearText;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static com.robinfinch.journal.app.test.Matchers.*;

/**
 * Travel entry related tests.
 *
 * @author Mark Hoogenboom
 */
public class TravelEntryTests extends JournalEntryTests {

    public void testCrud() {
        navigateToList("Travel");

        createItem();
        updateItem();
        deleteItem();

        // onData().check(doesNotExist());
    }

    @Override
    protected void createItem() {
        onView(withId(R.id.entity_add)).perform(click());

        onView(withText("Travel Details")).check(matches(isDisplayed()));
        onView(withId(R.id.travelentry_dayoftravel)).check(matches(withText(today())));
        onView(withId(R.id.travelentry_away)).check(matches(withSelectedItem(allOf(instanceOf(String.class), is("From")))));
        onView(withId(R.id.travelentry_place)).check(matches(withText("")));
        onView(withId(R.id.travelentry_dayoftravel)).perform(clearText(), typeText("1/2/2010"));

        // pressBack();
    }

    @Override
    protected void updateItem() {
        // onData().perform(click());

        onView(withText("Travel Details")).check(matches(isDisplayed()));
        onView(withId(R.id.travelentry_dayoftravel)).check(matches(withText("1/2/2010")));
        onView(withId(R.id.travelentry_away)).check(matches(withSelectedItem(allOf(instanceOf(String.class), is("From")))));
        onView(withId(R.id.travelentry_place)).check(matches(withText("")));
        onView(withId(R.id.travelentry_dayoftravel)).perform(clearText(), typeText("2/2/2010"));
        onView(withId(R.id.travelentry_away)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("To"))).perform(click());
        onView(withId(R.id.travelentry_place)).perform(clearText(), typeText("London"));

        // pressBack();
    }

    @Override
    protected void deleteItem() {
        // onData().perform(click());

        onView(withText("Travel Details")).check(matches(isDisplayed()));
        onView(withId(R.id.travelentry_dayoftravel)).check(matches(withText("2/2/2010")));
        onView(withId(R.id.travelentry_away)).check(matches(withSelectedItem(allOf(instanceOf(String.class), is("To")))));
        onView(withId(R.id.travelentry_place)).check(matches(withText("London")));

        onView(withId(R.id.entity_delete)).perform(click());
    }
}
