package tmg.flashback.formula1.constants

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.Year

internal class Formula1Test {

    @ParameterizedTest(name = "Max points in {0} season is {1}")
    @CsvSource(
        "2024,26",
        "2023,26",
        "2021,26",
        "2020,26",
        "2010,26",
        "2009,11",
        "1991,11",
        "1990,8",
        "1950,8"
    )
    fun `maxPoints by season returns correct amount of points`(season: Int, expectedPoints: Int) {

        assertEquals(expectedPoints, Formula1.maxDriverPointsBySeason(season))
    }

    @Test
    fun `constructor championship starts in 1958`() {

        assertEquals(1958, Formula1.championshipConstructorStarts)
    }

    @Test
    fun `maxPoints by current year returns correct amounts of points`() {

        assertEquals(25, Formula1.maxDriverPointsBySeason(Year.now().value))
    }


    @ParameterizedTest(name = "Max points in {0} season is {1}")
    @CsvSource(
        "2023,60",
        "2021,47",
        "2020,42",
        "2010,42",
        "2009,19",
        "1991,19",
        "1990,14",
        "1950,14"
    )
    fun `maxTeamPoints by season returns correct amount of points`(season: Int, expectedPoints: Int) {

        assertEquals(expectedPoints, Formula1.maxTeamPointsBySeason(season))
    }
    @Test
    fun `maxTeamPoints by current year returns correct amounts of points`() {

        assertEquals(58, Formula1.maxTeamPointsBySeason(Year.now().value))
    }
}