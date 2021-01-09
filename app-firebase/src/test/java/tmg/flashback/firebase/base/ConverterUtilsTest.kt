package tmg.flashback.firebase.base

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.format.DateTimeParseException
import tmg.flashback.firebase.base.ConverterUtils.fromDate
import tmg.flashback.firebase.base.ConverterUtils.fromTime
import tmg.flashback.firebase.testutils.BaseTest

internal class ConverterUtilsTest: BaseTest() {

    //region Date

    @ParameterizedTest(name = "ConverterUtils fromDate {0} is valid")
    @CsvSource(
            "2020-1-2",
            "1999-01-01",
            "2023-12-31"
    )
    fun `ConverterUtils fromDate doesnt throw exception for invalid`(dateString: String) {
        assertDoesNotThrow {
            fromDate(dateString)
        }
    }

    @ParameterizedTest(name = "ConverterUtils fromDate {0} is invalid")
    @CsvSource(
            "2020-1-32",
            "1999-13-01",
            "2023-12-32",
            "something-random"
    )
    fun `ConverterUtils fromDate throws exception for invalid`(dateString: String) {
        assertThrows(DateTimeParseException::class.java) {
            fromDate(dateString)
        }
    }

    //endregion

    //region Time

    @ParameterizedTest(name = "ConverterUtils fromTime {0} is valid")
    @CsvSource(
            "23:59:00",
            "23:12",
            "12:00:00",
            "12:00:00Z"
    )
    fun `ConverterUtils fromTime doesnt throw exception for valid`(timeString: String) {
        assertDoesNotThrow {
            fromTime(timeString)
        }
    }

    @ParameterizedTest(name = "ConverterUtils fromTime {0} is invalid")
    @CsvSource(
            "12",
            "12:30:82",
            "null",
            "something-random"
    )
    fun `ConverterUtils fromTime throws exception for invalid`(timeString: String) {
        assertThrows(DateTimeParseException::class.java) {
            fromTime(timeString)
        }
    }

    //endregion


}