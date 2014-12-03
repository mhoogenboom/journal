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
 * Walk entry related tests.
 *
 * @author Mark Hoogenboom
 */
public class WalkEntryTests extends JournalEntryTests {

    public void testCrud() {
        navigateToList("Walked");

        createItem();
        updateItem();
        deleteItem();

        // onData().check(doesNotExist());
    }

    @Override
    protected void createItem() {
        onView(withId(R.id.entity_add)).perform(click());

        onView(withText("Details")).check(matches(isDisplayed()));
        onView(withId(R.id.walkentry_dayofwalk)).check(matches(withText(today())));
        onView(withId(R.id.walkentry_location)).check(matches(withText("")));
        onView(withId(R.id.walkentry_dayofwalk)).perform(clearText(), typeText("1/2/2010"));

        // pressBack();
    }

    @Override
    protected void updateItem() {
        // onData().perform(click());

        onView(withText("Details")).check(matches(isDisplayed()));
        onView(withId(R.id.walkentry_dayofwalk)).check(matches(withText("1/2/2010")));
        onView(withId(R.id.walkentry_location)).check(matches(withText("")));
        onView(withId(R.id.walkentry_dayofwalk)).perform(clearText(), typeText("2/2/2010"));
        onView(withId(R.id.walkentry_location)).perform(typeText("Woodwalton Fen"));

        // pressBack();
    }

    @Override
    protected void deleteItem() {
        // onData().perform(click());

        onView(withText("Details")).check(matches(isDisplayed()));
        onView(withId(R.id.walkentry_dayofwalk)).check(matches(withText("2/2/2010")));
        onView(withId(R.id.walkentry_location)).check(matches(withText("Woodwalton Fen")));

        onView(withId(R.id.entity_delete)).perform(click());
    }
}
