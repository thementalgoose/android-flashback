package tmg.flashback.formula1.enums

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class TrackLayoutTest {

    @ParameterizedTest(name = "For the {0} {1} the {2} track layout will be used")
    @CsvSource(
            "bahrain,2020,Sakhir Grand Prix,SAKHIR",
            "bahrain,,Bahrain Grand Prix,BAHRAIN",
            "yas_marina,2009,Abu Dhabi Grand Prix,YAS_MARINA_2009_2020",
            "yas_marina,2010,Abu Dhabi Grand Prix,YAS_MARINA_2009_2020",
            "yas_marina,2011,Abu Dhabi Grand Prix,YAS_MARINA_2009_2020",
            "yas_marina,2012,Abu Dhabi Grand Prix,YAS_MARINA_2009_2020",
            "yas_marina,2013,Abu Dhabi Grand Prix,YAS_MARINA_2009_2020",
            "yas_marina,2014,Abu Dhabi Grand Prix,YAS_MARINA_2009_2020",
            "yas_marina,2015,Abu Dhabi Grand Prix,YAS_MARINA_2009_2020",
            "yas_marina,2016,Abu Dhabi Grand Prix,YAS_MARINA_2009_2020",
            "yas_marina,2017,Abu Dhabi Grand Prix,YAS_MARINA_2009_2020",
            "yas_marina,2018,Abu Dhabi Grand Prix,YAS_MARINA_2009_2020",
            "yas_marina,2019,Abu Dhabi Grand Prix,YAS_MARINA_2009_2020",
            "yas_marina,2020,Abu Dhabi Grand Prix,YAS_MARINA_2009_2020",
            "yas_marina,,Abu Dhabi Grand Prix,YAS_MARINA",
            "galvez,1974,Argentine Grand Prix,GALVEZ_1974_1981",
            "galvez,1975,Argentine Grand Prix,GALVEZ_1974_1981",
            "galvez,1976,Argentine Grand Prix,GALVEZ_1974_1981",
            "galvez,1977,Argentine Grand Prix,GALVEZ_1974_1981",
            "galvez,1978,Argentine Grand Prix,GALVEZ_1974_1981",
            "galvez,1979,Argentine Grand Prix,GALVEZ_1974_1981",
            "galvez,1980,Argentine Grand Prix,GALVEZ_1974_1981",
            "galvez,1981,Argentine Grand Prix,GALVEZ_1974_1981",
            "galvez,,Argentine Grand Prix,GALVEZ"
    )
    fun `testing override values pull overriden enum`(circuitId: String, year: Int?, raceName: String, expected: TrackLayout) {

        val actual = TrackLayout.getTrack(circuitId, year, raceName)
        assertEquals(expected, actual)
    }
}