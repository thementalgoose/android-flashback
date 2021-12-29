package tmg.flashback.formula1.constants

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.Year

internal class Formula1Test {

    @ParameterizedTest(name = "Max points in {0} season is {1}")
    @CsvSource(
        "2021,28",
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
    fun `constructor championship starts in 1958`() {

        assertEquals(1958, Formula1.constructorChampionshipStarts)
    }

    @Test
    fun `maxPoints by current year returns correct amounts of points`() {

        assertEquals(28, Formula1.maxPointsBySeason(Year.now().value))
    }
}