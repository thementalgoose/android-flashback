package tmg.flashback.constants

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.constants.App.currentYear
import tmg.flashback.testutils.BaseTest

internal class Formula1Test: BaseTest() {

    @ParameterizedTest(name = "Max points in {0} season is {1}")
    @CsvSource(
            "2020,25",
            "2010,25",
            "2009,10",
            "1991,10",
            "1990,8",
            "1950,8"
    )
    fun `maxPoints by season returns correct amount of points`(season: Int, expectedPoints: Int) {

        assertEquals(expectedPoints, Formula1.maxPointsBySeason(season))
    }

    @Test
    fun `maxPoints by current year returns correct amounts of points`() {

        assertEquals(25, Formula1.maxPointsBySeason(currentYear))
    }
}