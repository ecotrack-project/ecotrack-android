package com.ecotrack.android

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ecotrack.android.ui.map.MapViewModel
import com.ecotrack.android.ui.map.MarkerData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

@ExperimentalCoroutinesApi
class MapViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MapViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MapViewModel()
    }

    @Test
    fun `test initial markers list is empty`() = runTest {
        assertTrue(viewModel.markers.value.isNullOrEmpty())
    }

    @Test
    fun `test update user location`() = runTest {
        val testLocation = LatLng(45.0, 7.0)
        viewModel.updateUserLocation(testLocation)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(testLocation, viewModel.userLocation.value)
    }

    @Test
    fun `test add marker`() = runTest {
        val testMarker = MarkerData(
            id = 1L,
            position = LatLng(45.0, 7.0),
            fillinglevel = 50,
            trashType = "Plastica"
        )
        viewModel.addMarker(testMarker)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(listOf(testMarker), viewModel.markers.value)
    }

    @Test
    fun `test remove marker`() = runTest {
        val testMarker = MarkerData(
            id = 1L,
            position = LatLng(45.0, 7.0),
            fillinglevel = 50,
            trashType = "Plastica"
        )
        viewModel.addMarker(testMarker)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.removeMarker(testMarker)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.markers.value?.isEmpty() ?: false)
    }

    @Test
    fun `test initial user location is null`() = runTest {
        assertNull(viewModel.userLocation.value)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}