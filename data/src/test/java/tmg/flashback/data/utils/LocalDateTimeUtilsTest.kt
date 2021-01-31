package tmg.flashback.data.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

internal class LocalDateTimeUtilsTest {

    @ParameterizedTest(name = "daysBetween {0} <> {1} = {2} days")
    @CsvSource(
            "12-01-2020,13-01-2020,1",
            "12-01-2020,12-01-2020,0",
            "12-01-2020,11-01-2020,-1",
            "12-01-2020,11-05-2020,120",
            "21-04-1993,11-01-2018,9031",
            "21-04-1993,11-01-1953,-14710"
    )
    fun `daysBetween test`(start: String, end: String, expected: Int) {
        val startDate = LocalDate.parse(start, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        val endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("dd-MM-yyyy"))

        assertEquals(expected, daysBetween(startDate, endDate))
    }

    @ParameterizedTest(name = "secondsBetween {0} <> {1} = {2} seconds")
    @CsvSource(
            "12:00:00,14:00:00,7200",
            "12:00:00,12:00:00,0",
            "12:00:00,11:00:00,-3600"
    )
    fun `secondsBetween test`(start: String, end: String, expected: Int) {
        val startDate = LocalTime.parse(start, DateTimeFormatter.ofPattern("HH:mm:ss"))
        val endDate = LocalTime.parse(end, DateTimeFormatter.ofPattern("HH:mm:ss"))

        assertEquals(expected, secondsBetween(startDate, endDate))
    }
}