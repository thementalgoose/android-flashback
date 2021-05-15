package tmg.flashback.statistics.ui.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

internal class DateUtilsTest {

    @ParameterizedTest(name = "Born on {0} with now being {1} means person is {2} years old")
    @CsvSource(
        "1995-01-01,2021-01-01,26",
        "2010-01-01,2010-01-01,0",
        "2010-01-01,2010-12-31,0",
        "2010-01-01,2011-01-01,1",
        "1969-01-01,2011-01-02,42"
    )
    fun `date age calculation shows correct age`(birthday: String, now: String, expectedAge: Int) {
        val birthdayDate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val nowDate = LocalDate.parse(now, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        assertEquals(expectedAge, birthdayDate.age(nowDate))
    }
}