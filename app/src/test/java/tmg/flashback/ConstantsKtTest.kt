package tmg.flashback

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ConstantsKtTest {

    @ParameterizedTest
    @CsvSource(
            "2020,25",
            "2010,25",
            "2009,10",
            "1991,10",
            "1990,8",
            "1950,8"
    )
    fun `ConstantsKt maxPoints by season returns correct amount of points`(season: Int, expectedPoints: Int) {

        assertEquals(expectedPoints, maxPointsBySeason(season))
    }

    @Test
    fun `ConstantsKt maxPoints by current year returns correct amounts of points`() {

        assertEquals(25, maxPointsBySeason(currentYear))
    }

    @ParameterizedTest
    @CsvSource(
        "2020,Sakhir Grand Prix,SAKHIR"
    )
    fun `TrackLayout testing override values pull overriden enum`(year: Int, raceName: String, expected: TrackLayout) {

        val actual = TrackLayout.getOverride(year, raceName)
        assertEquals(expected, actual)
    }
}