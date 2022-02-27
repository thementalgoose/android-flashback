package tmg.flashback.formula1.enums

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.threeten.bp.Year

internal class TrackLayoutTest {

    @ParameterizedTest(name = "silverstone track layout for {0} returns {1} layout")
    @CsvSource(
        "1950,SILVERSTONE_1950_1973",
        "1951,SILVERSTONE_1950_1973",
        "1952,SILVERSTONE_1950_1973",
        "1953,SILVERSTONE_1950_1973",
        "1954,SILVERSTONE_1950_1973",
        "1955,SILVERSTONE_1950_1973",
        "1956,SILVERSTONE_1950_1973",
        "1957,SILVERSTONE_1950_1973",
        "1958,SILVERSTONE_1950_1973",
        "1959,SILVERSTONE_1950_1973",
        "1960,SILVERSTONE_1950_1973",
        "1961,SILVERSTONE_1950_1973",
        "1962,SILVERSTONE_1950_1973",
        "1963,SILVERSTONE_1950_1973",
        "1964,SILVERSTONE_1950_1973",
        "1965,SILVERSTONE_1950_1973",
        "1966,SILVERSTONE_1950_1973",
        "1967,SILVERSTONE_1950_1973",
        "1968,SILVERSTONE_1950_1973",
        "1969,SILVERSTONE_1950_1973",
        "1970,SILVERSTONE_1950_1973",
        "1971,SILVERSTONE_1950_1973",
        "1972,SILVERSTONE_1950_1973",
        "1973,SILVERSTONE_1950_1973",
        "1975,SILVERSTONE_1975_1986",
        "1976,SILVERSTONE_1975_1986",
        "1977,SILVERSTONE_1975_1986",
        "1978,SILVERSTONE_1975_1986",
        "1979,SILVERSTONE_1975_1986",
        "1980,SILVERSTONE_1975_1986",
        "1981,SILVERSTONE_1975_1986",
        "1982,SILVERSTONE_1975_1986",
        "1983,SILVERSTONE_1975_1986",
        "1984,SILVERSTONE_1975_1986",
        "1985,SILVERSTONE_1975_1986",
        "1986,SILVERSTONE_1975_1986",
        "1987,SILVERSTONE_1987_1990",
        "1988,SILVERSTONE_1987_1990",
        "1989,SILVERSTONE_1987_1990",
        "1990,SILVERSTONE_1987_1990",
        "1991,SILVERSTONE_1991_1993",
        "1992,SILVERSTONE_1991_1993",
        "1993,SILVERSTONE_1991_1993",
        "1994,SILVERSTONE_1994_1995",
        "1995,SILVERSTONE_1994_1995",
        "1996,SILVERSTONE_1996",
        "1997,SILVERSTONE_1997_2009",
        "1998,SILVERSTONE_1997_2009",
        "1999,SILVERSTONE_1997_2009",
        "2000,SILVERSTONE_1997_2009",
        "2001,SILVERSTONE_1997_2009",
        "2002,SILVERSTONE_1997_2009",
        "2003,SILVERSTONE_1997_2009",
        "2004,SILVERSTONE_1997_2009",
        "2005,SILVERSTONE_1997_2009",
        "2006,SILVERSTONE_1997_2009",
        "2007,SILVERSTONE_1997_2009",
        "2008,SILVERSTONE_1997_2009",
        "2009,SILVERSTONE_1997_2009",
        ",SILVERSTONE",
    )
    fun `silverstone track layout`(year: Int?, trackLayout: TrackLayout) {
        val layout = TrackLayout.getTrack("silverstone", year ?: Year.now().value, "British Grand Prix")
        assertEquals(trackLayout, layout)
    }

    @ParameterizedTest(name = "bahrain track layout for {0} returns {1} layout")
    @CsvSource(
        "2010,BAHRAIN_2010",
        ",BAHRAIN",
    )
    fun `bahrain track layout`(year: Int?, trackLayout: TrackLayout) {
        val layout = TrackLayout.getTrack("bahrain", year ?: Year.now().value, "Bahrain Grand Prix")
        assertEquals(trackLayout, layout)
    }

    @ParameterizedTest(name = "yas marina track layout for {0} returns {1} layout")
    @CsvSource(
        "2009,YAS_MARINA_2009_2020",
        "2010,YAS_MARINA_2009_2020",
        "2011,YAS_MARINA_2009_2020",
        "2012,YAS_MARINA_2009_2020",
        "2013,YAS_MARINA_2009_2020",
        "2014,YAS_MARINA_2009_2020",
        "2015,YAS_MARINA_2009_2020",
        "2016,YAS_MARINA_2009_2020",
        "2017,YAS_MARINA_2009_2020",
        "2018,YAS_MARINA_2009_2020",
        "2019,YAS_MARINA_2009_2020",
        "2020,YAS_MARINA_2009_2020",
        ",YAS_MARINA",
    )
    fun `yas marina track layout`(year: Int?, trackLayout: TrackLayout) {
        val layout = TrackLayout.getTrack("yas_marina", year ?: Year.now().value, "Abu Dhabi Grand Prix")
        assertEquals(trackLayout, layout)
    }


    @ParameterizedTest(name = "galvez track layout for {0} returns {1} layout")
    @CsvSource(
        "1974,GALVEZ_1974_1981",
        "1975,GALVEZ_1974_1981",
        "1976,GALVEZ_1974_1981",
        "1977,GALVEZ_1974_1981",
        "1978,GALVEZ_1974_1981",
        "1979,GALVEZ_1974_1981",
        "1980,GALVEZ_1974_1981",
        "1981,GALVEZ_1974_1981",
        ",GALVEZ"
    )
    fun `galvez track layout`(year: Int?, trackLayout: TrackLayout) {
        val layout = TrackLayout.getTrack("galvez", year ?: Year.now().value, "Argentine Grand Prix")
        assertEquals(trackLayout, layout)
    }
}