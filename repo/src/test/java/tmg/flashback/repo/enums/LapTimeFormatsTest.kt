package tmg.flashback.repo.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class LapTimeFormatsTest {

    @ParameterizedTest
    @CsvSource(
            "00:000,true",
            "59:999,true",
            "23:012,true",
            "1:012,true",
            "9:012,true",
            "0:012,true",
            "103,false",
            "60:000,false",
            "34:12,false",
            "1:34:120,false",
            "oo:02,false"
    )
    fun `LapTimeFormats regex for seconds matches seconds properly`(lapTime: String, expectedIsValid: Boolean) {

        assertEquals(LapTimeFormats.SECOND_MILLIS.regex.matches(lapTime), expectedIsValid)
    }

    @ParameterizedTest
    @CsvSource(
            "1:00:000,true",
            "59:59:999,true",
            "3:09:012,true",
            "4:00:012,true",
            "1:34:120,true",
            "60:59:999,false",
            "84:23:012,false",
            "1:1:012,false",
            "103,false",
            "60:000,false",
            "34:12,false",
            "oo:02,false"
    )
    fun `LapTimeFormats regex for minutes matches minutes properly`(lapTime: String, expectedIsValid: Boolean) {

        assertEquals(LapTimeFormats.MIN_SECOND_MILLIS.regex.matches(lapTime), expectedIsValid)
    }

    @ParameterizedTest
    @CsvSource(
            "1:03:00:000,true",
            "23:02:59:999,true",
            "04:21:10:012,true",
            "18:00:00:000,true",
            "9:59:59:999,true",
            "1:20:00:012,true",
            "01:03:00:000,true",
            "9:99:99:999,false",
            "23:62:59:999,false",
            "24:00:00:000,false",
            "23:012,false",
            "60:000,false",
            "103,false",
            "34:12,false",
            "1:34:120,false",
            "oo:02,false"
    )
    fun `LapTimeFormats regex for hours matches hours properly`(lapTime: String, expectedIsValid: Boolean) {

        assertEquals(LapTimeFormats.HOUR_MIN_SECOND_MILLIS.regex.matches(lapTime), expectedIsValid)
    }
}