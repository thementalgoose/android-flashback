package tmg.flashback.statistics.extensions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class IntExtensionsTest {


    @ParameterizedTest(name = "{0}.hoursAndMins = {1} hrs and {2} mins")
    @CsvSource(
        "0,0,0",
        "3600,1,0",
        "3599,0,59",
        "1740,0,29",
        "86399,23,59",
        "86400,24,0",
        "-2382,0,0"
    )
    fun `hoursAndMins returns hours and mins value assuming original is seconds`(seconds: Int, expectedHours: Int, expectedMinutes: Int) {

        val (hours, minutes) = seconds.secondsToHHmm
        assertEquals(expectedHours, hours)
        assertEquals(expectedMinutes, minutes)
    }
}