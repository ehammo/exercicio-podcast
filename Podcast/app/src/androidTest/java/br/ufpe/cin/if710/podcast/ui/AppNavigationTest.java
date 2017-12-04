package br.ufpe.cin.if710.podcast.ui;

import android.preference.Preference;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.squareup.haha.perflib.Main;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.ufpe.cin.if710.podcast.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.base.Verify.verify;
import static android.support.test.espresso.matcher.PreferenceMatchers.withKey;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

/**
 * Created by eduardo on 04/12/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AppNavigationTest {

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     *
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickOnSettingsItem_ShowsSettingsScreen() {
//        fail("Implement step 9");
//        Click on options menu
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        //Start settings Activity
        onView(withText(R.string.action_settings)).perform(click());
//      Check that settings Activity was opened.
        onView(withText(R.string.pref_desc)).check(matches(isCompletelyDisplayed()));
    }



    @Test
    public void clickPodcastItem_ShowsDetailScreen() {

//        //Check if there is at least a PodcastItem and Click it

        onData(anything()).inAdapterView(withId(R.id.items)).atPosition(0).
                onChildView(withId(R.id.item_title)).
                check(matches(withText("Ciência e Pseudociência")));

        onData(anything()).inAdapterView(withId(R.id.items)).atPosition(0).perform(click());
        //Check if DetailActivity is shown
        onView(withId(R.id.title_tv)).check(matches(withText("Ciência e Pseudociência")));

    }

}