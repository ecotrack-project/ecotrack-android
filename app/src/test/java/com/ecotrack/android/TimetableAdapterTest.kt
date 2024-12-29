package com.ecotrack.android

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.ecotrack.android.ui.home.TimetableAdapter
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TimetableAdapterTest {
    @Mock
    private lateinit var mockView: View

    private val testData = listOf(
        listOf("Tipo", "L", "M", "M", "G", "V", "S"),
        listOf("Umido", "✅", "☐", "☐", "✅", "☐", "☐")
    )

    // Using simple integers instead of actual colors
    private val testColorMap = mapOf(
        "Umido" to 0xFF123456.toInt(),  // Using hex integers instead of Color.parseColor
        "Indiff." to 0xFF234567.toInt(),
        "Plast." to 0xFF345678.toInt(),
        "Carta" to 0xFF456789.toInt(),
        "Vetro" to 0xFF567890.toInt()
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `test adapter item count`() {
        val adapter = TimetableAdapter(testData, testColorMap)
        assertEquals(2, adapter.itemCount)
    }

    @Test
    fun `test data row size matches header size`() {
        val headerSize = testData[0].size
        testData.forEach { row ->
            assertEquals("Each row should have same number of columns as header",
                headerSize, row.size)
        }
    }

    @Test
    fun `test valid color mapping for waste types`() {
        testData.drop(1).forEach { row ->
            val wasteType = row[0]
            if (testColorMap.containsKey(wasteType)) {
                assertNotNull("Waste type should have a color mapping",
                    testColorMap[wasteType])
            }
        }
    }

    @Test
    fun `test header row format`() {
        val headerRow = testData[0]
        assertEquals("Tipo", headerRow[0])
        assertEquals("L", headerRow[1])
        assertEquals("M", headerRow[2])
        assertEquals("M", headerRow[3])
        assertEquals("G", headerRow[4])
        assertEquals("V", headerRow[5])
        assertEquals("S", headerRow[6])
    }

    @Test
    fun `test data row format`() {
        val dataRow = testData[1]
        assertTrue("First column should be waste type", dataRow[0].isNotEmpty())
        for (i in 1..6) {
            assertTrue("Column $i should contain either ✅ or ☐",
                dataRow[i] in listOf("✅", "☐"))
        }
    }
}