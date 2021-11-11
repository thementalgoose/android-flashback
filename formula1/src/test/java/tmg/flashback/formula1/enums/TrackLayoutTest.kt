package tmg.flashback.formula1.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.formula1.enums.TrackLayout.SAKHIR

internal class TrackLayoutTest {

    @ParameterizedTest(name = "For the {0} {1} the {2} track layout will be used")
    @CsvSource(
            "2020,Sakhir Grand Prix,SAKHIR"
    )
    fun `testing override values pull overriden enum`(year: Int, raceName: String, expected: TrackLayout) {

        val actual = TrackLayout.getTrack(SAKHIR.circuitId, year, raceName)
        assertEquals(expected, actual)
    }
}