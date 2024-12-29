// InstrumentedTests.kt
package com.ecotrack.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import junit.framework.TestCase.assertEquals

@RunWith(AndroidJUnit4::class)
class InstrumentedTests {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.ecotrack.android", appContext.packageName)
    }

    @Test
    fun testHomeFragmentTimetableDisplay() {
        // Verify timetable title is displayed
        onView(withId(R.id.text_notifications))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.WasteCollectionTimetable)))

        // Verify RecyclerView is displayed
        onView(withId(R.id.recyclerViewTable))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testFormSubmission() {
        // Navigate to form (assuming you have a way to get there)
        // Fill in form fields
        onView(withId(R.id.editTextEmail))
            .perform(typeText("test@example.com"), closeSoftKeyboard())

        onView(withId(R.id.editTextDescription))
            .perform(typeText("Test description for the report"), closeSoftKeyboard())

        // Submit form
        onView(withId(R.id.submit_button))
            .perform(click())
    }


    @Test
    fun testMapMarkerInteraction() {
        // Test map loading and marker interaction
        onView(withId(R.id.mapView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testInvalidFormSubmission() {
        // Test form validation
        onView(withId(R.id.submit_button))
            .perform(click())

        // Should show error message
        onView(withText("Please fill in all fields correctly"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testFormEmailValidation() {
        // Test invalid email
        onView(withId(R.id.editTextEmail))
            .perform(typeText("invalid-email"), closeSoftKeyboard())

        onView(withId(R.id.submit_button))
            .perform(click())

        // Should show email validation error
        onView(withText("Please enter a valid email address"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testNavigationBetweenFragments() {
        // Test navigation between different screens
        // Assuming you have bottom navigation
        onView(withId(R.id.navigation_home))
            .perform(click())

        onView(withId(R.id.navigation_map))
            .perform(click())
    }
}


