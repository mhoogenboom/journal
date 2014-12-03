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
 * Read entry related tests.
 *
 * @author Mark Hoogenboom
 */
public class ReadEntryTests extends JournalEntryTests {

    public void testCrud() {
        navigateToList("Read");

        createItem();
        updateItem();
        deleteItem();

        // onData().check(doesNotExist());
    }

    @Override
    protected void createItem() {
        onView(withId(R.id.entity_add)).perform(click());

        onView(withText("Details")).check(matches(isDisplayed()));
        onView(withId(R.id.readentry_dayread)).check(matches(withText(today())));
        onView(withId(R.id.readentry_title)).check(matches(withNamedObject(org.hamcrest.Matchers.<NamedObject>nullValue())));
        onView(withId(R.id.readentry_part)).check(matches(withText("")));
        onView(withId(R.id.readentry_dayread)).perform(clearText(), typeText("1/2/2010"));

        // pressBack();
    }

    @Override
    protected void updateItem() {
        // onData().perform(click());

        onView(withText("Details")).check(matches(isDisplayed()));
        onView(withId(R.id.readentry_dayread)).check(matches(withText("1/2/2010")));
        onView(withId(R.id.readentry_title)).check(matches(withNamedObject(org.hamcrest.Matchers.<NamedObject>nullValue())));
        onView(withId(R.id.readentry_part)).check(matches(withText("")));
        onView(withId(R.id.readentry_dayread)).perform(clearText(), typeText("2/2/2010"));
        setTitle();
        onView(withId(R.id.readentry_part)).perform(clearText(), typeText("ch 1"));

        // pressBack();
    }

    private void setTitle() {
        onView(withId(R.id.readentry_title)).perform(click());
        onView(withId(R.id.entity_add)).perform(click());
        onView(withId(R.id.title_title)).perform(typeText("Testing with Espresso"));
        onView(withId(R.id.title_select)).perform(click());
    }

    @Override
    protected void deleteItem() {
        // onData().perform(click());

        onView(withText("Details")).check(matches(isDisplayed()));
        onView(withId(R.id.readentry_dayread)).check(matches(withText("02/02/2010")));
        onView(withId(R.id.readentry_title)).check(matches(withNamedObject(withName(is("Testing with Espresso")))));
        onView(withId(R.id.readentry_part)).check(matches(withText("ch 1")));

        onView(withId(R.id.entity_delete)).perform(click());
    }
}
