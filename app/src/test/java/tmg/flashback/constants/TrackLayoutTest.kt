package tmg.flashback.constants

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.statistics.enums.TrackLayout
import tmg.flashback.testutils.BaseTest

internal class TrackLayoutTest: BaseTest() {

    @ParameterizedTest(name = "For the {0} {1} the {2} track layout will be used")
    @CsvSource(
            "2020,Sakhir Grand Prix,SAKHIR"
    )
    fun `testing override values pull overriden enum`(year: Int, raceName: String, expected: TrackLayout) {

        val actual = TrackLayout.getOverride(year, raceName)
        assertEquals(expected, actual)
    }
}