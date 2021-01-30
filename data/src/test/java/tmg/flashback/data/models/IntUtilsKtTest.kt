package tmg.flashback.data.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.data.utils.extendTo
import tmg.flashback.data.utils.hoursAndMins
import tmg.flashback.data.utils.toMaxIfZero

class IntUtilsKtTest {

    @ParameterizedTest(name = "{0}.toMaxIfZero() == {1}")
    @CsvSource(
        "1,false",
        "-1,false",
        ",true",
        "0,true"
    )
    fun `IntUtils toMaxIfZero wrapping integer to max value if it's null or zero`(value: Int?, expectedToBeIntMax: Boolean) {

        val expectedValue: Int = if (expectedToBeIntMax) Int.MAX_VALUE else value!!

        assertEquals(expectedValue, value.toMaxIfZero())
    }

    @ParameterizedTest(name = "{0}.extendTo({1}) == {2}")
    @CsvSource(
        "0,2,00",
        "1,3,001",
        "02,1,2",
        "213,5,00213"
    )
    fun `IntUtils extendTo extends characters with '0'`(value: Int, extension: Int, expectedValue: String) {

        assertEquals(expectedValue, value.extendTo(extension))
    }

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
    fun `IntUtils hoursAndMins returns hours and mins value assuming original is seconds`(seconds: Int, expectedHours: Int, expectedMinutes: Int) {

        val (hours, minutes) = seconds.hoursAndMins
        assertEquals(expectedHours, hours)
        assertEquals(expectedMinutes, minutes)
    }
}