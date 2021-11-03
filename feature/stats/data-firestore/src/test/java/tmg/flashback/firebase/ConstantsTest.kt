package tmg.flashback.firebase

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.testutils.BaseTest

internal class ConstantsTest: BaseTest() {

    @ParameterizedTest(name = "getOverviewKeys({0}, {1}) = {2}")
    @CsvSource(
        "1950|2019|[1950, 1960, 1970, 1980, 1990, 2000, 2010]",
        "1950|2020|[1950, 1960, 1970, 1980, 1990, 2000, 2010, 2020]",
        "1950|2021|[1950, 1960, 1970, 1980, 1990, 2000, 2010, 2020]",
        "1950|2022|[1950, 1960, 1970, 1980, 1990, 2000, 2010, 2020]",
        "1950|2025|[1950, 1960, 1970, 1980, 1990, 2000, 2010, 2020]",
        "1950|2029|[1950, 1960, 1970, 1980, 1990, 2000, 2010, 2020]",
        "1950|2030|[1950, 1960, 1970, 1980, 1990, 2000, 2010, 2020, 2030]",
        "1950|2031|[1950, 1960, 1970, 1980, 1990, 2000, 2010, 2020, 2030]",
        delimiter = '|'
    )
    fun `getOverviewKeys returns correct set of keys`(inputStarting: Int, finishing: Int, expected: String) {
        assertEquals(expected, getOverviewKeys(inputStarting, finishing).toString())
    }

    @Test
    fun `overviewKeys returns expected array`() {
        val result = overviewKeys
        val expected = listOf(
            "season1950",
            "season1960",
            "season1970",
            "season1980",
            "season1990",
            "season2000",
            "season2010",
            "season2020"
        )
        assertEquals(expected, result)
    }
}