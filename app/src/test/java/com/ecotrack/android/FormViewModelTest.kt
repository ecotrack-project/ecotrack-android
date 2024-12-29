package com.ecotrack.android
import org.junit.After
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ecotrack.android.ui.form.FormViewModel
import com.ecotrack.android.ui.form.FormSubmissionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class FormViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: FormViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Set the main dispatcher for tests
        viewModel = FormViewModel()
    }

    @Test
    fun `test initial form status is null`() {
        assertNull(viewModel.formSubmissionStatus.value)
    }

    @Test
    fun `test welcome text is not empty`() {
        assertNotNull(viewModel.text.value)
        assertTrue(viewModel.text.value!!.isNotEmpty())
    }

    @Test
    fun `test update text function`() {
        val newText = "Test Message"
        viewModel.updateText(newText)
        assertEquals(newText, viewModel.text.value)
    }

    @Test
    fun `test form submission with invalid input`() = runTest {
        viewModel.submitForm(null, "", "")
        assertTrue(viewModel.formSubmissionStatus.value is FormSubmissionStatus.Error)
    }

    @Test
    fun `test form submission with valid input`() = runTest {
        viewModel.submitForm(1L, "test@email.com", "Test description")
        advanceUntilIdle() // Wait for coroutines to complete
        assertTrue(viewModel.formSubmissionStatus.value is FormSubmissionStatus.Loading)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}