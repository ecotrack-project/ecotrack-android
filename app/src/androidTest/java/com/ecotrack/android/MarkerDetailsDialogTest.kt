package com.ecotrack.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


// Custom test for Dialog interaction
@RunWith(AndroidJUnit4::class)
class MarkerDetailsDialogTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testMarkerDetailsDialog() {
        // Assuming we can trigger the dialog
        // Test dialog content
        onView(withId(R.id.trashTypeTextView))
            .check(matches(isDisplayed()))

        onView(withId(R.id.fillingLevelTextView))
            .check(matches(isDisplayed()))

        // Test dialog buttons
        onView(withId(R.id.find_path))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.send_report))
            .check(matches(isDisplayed()))
            .perform(click())
    }
}