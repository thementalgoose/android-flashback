package tmg.flashback.formula1.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.formula1.enums.TrackLayout.SAKHIR

internal class TrackLayoutTest {

    @ParameterizedTest(name = "For the {0} {1} the {2} track layout will be used")
    @CsvSource(
            "2020,Sakhir Grand Prix,SAKHIR",
            ",Sakhir Grand Prix,BAHRAIN",
            "2009,Abu Dhabi Grand Prix,YAS_MARINA_1",
            "2010,Abu Dhabi Grand Prix,YAS_MARINA_1",
            "2011,Abu Dhabi Grand Prix,YAS_MARINA_1",
            "2012,Abu Dhabi Grand Prix,YAS_MARINA_1",
            "2013,Abu Dhabi Grand Prix,YAS_MARINA_1",
            "2014,Abu Dhabi Grand Prix,YAS_MARINA_1",
            "2015,Abu Dhabi Grand Prix,YAS_MARINA_1",
            "2016,Abu Dhabi Grand Prix,YAS_MARINA_1",
            "2017,Abu Dhabi Grand Prix,YAS_MARINA_1",
            "2018,Abu Dhabi Grand Prix,YAS_MARINA_1",
            "2019,Abu Dhabi Grand Prix,YAS_MARINA_1",
            "2020,Abu Dhabi Grand Prix,YAS_MARINA_1",
            ",Abu Dhabi Grand Prix,YAS_MARINA_2"
    )
    fun `testing override values pull overriden enum`(year: Int?, raceName: String, expected: TrackLayout) {

        val actual = TrackLayout.getTrack(SAKHIR.circuitId, year, raceName)
        assertEquals(expected, actual)
    }
}