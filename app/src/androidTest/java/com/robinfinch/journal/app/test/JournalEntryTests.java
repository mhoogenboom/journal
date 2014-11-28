package com.robinfinch.journal.app.test;

import android.test.ActivityInstrumentationTestCase2;

import com.robinfinch.journal.app.MainActivity;
import com.robinfinch.journal.app.R;

import java.util.Calendar;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.contrib.DrawerActions.openDrawer;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.hasSibling;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Base class for journal entry related tests.
 * NB: Espresso 1.2 is expected to support cursor adapters.
 *
 * @author Mark Hoogenboom
 */
public abstract class JournalEntryTests extends ActivityInstrumentationTestCase2<MainActivity> {

    public JournalEntryTests() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    protected void navigateToList(String name) {

        openDrawer(R.id.drawer_layout);

        onData(allOf(is(instanceOf(String.class)), is(name)))
                .inAdapterView(withId(R.id.navigation))
                .perform(click());

        onView(allOf(withText(name), hasSibling(withId(R.id.list))))
                .check(matches(isDisplayed()));
    }

    protected abstract void createItem();

    protected abstract void updateItem();

    protected abstract void deleteItem();

    protected String today() {
        Calendar c = Calendar.getInstance();
        return String.format("%1$td/%1$tm/%1$tY", c);
    }
}

