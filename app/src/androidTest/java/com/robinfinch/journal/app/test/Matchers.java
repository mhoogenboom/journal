package com.robinfinch.journal.app.test;

import android.view.View;
import android.widget.Spinner;

import com.robinfinch.journal.app.ui.NamedObjectView;
import com.robinfinch.journal.domain.NamedObject;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Collection of matchers.
 *
 * @author Mark Hoogenboom
 */
public class Matchers {

    public static Matcher<View> withSelectedItem(final Matcher<Object> matcher) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with selected item: ");
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (view instanceof Spinner) {
                    Spinner spinner = (Spinner) view;
                    return matcher.matches(spinner.getSelectedItem());
                }
                return false;
            }
        };
    }

    public static Matcher<View> withNamedObject(final Matcher<NamedObject> matcher) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with named object: ");
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (view instanceof NamedObjectView) {
                    NamedObjectView nov = (NamedObjectView)  view;
                    return matcher.matches(nov.getObject());
                }
                return false;
            }
        };
    }

    public static Matcher<NamedObject> withName(final Matcher<String> matcher) {
        return new TypeSafeMatcher<NamedObject>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with name: ");
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(NamedObject obj) {
                return matcher.matches(obj.getName());
            }
        };
    }
}
