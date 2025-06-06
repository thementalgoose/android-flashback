package tmg.flashback.formula1.enums

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.Year
import tmg.flashback.formula1.R

internal class TrackLayoutTest {

    @Test
    fun `get icon with single year gets override`() {
        val year = 2010

        assertEquals(R.drawable.circuit_bahrain_2010, TrackLayout.BAHRAIN.getIcon(year, ""))
    }

    @Test
    fun `get icon with single year and name gets override`() {
        val name = "Sakhir Grand Prix"
        val year = 2020

        assertEquals(R.drawable.circuit_sakhir, TrackLayout.BAHRAIN.getIcon(year, name))
    }

    @Test
    fun `get icon with no override gets default icon`() {
        val year = 2020

        assertEquals(R.drawable.circuit_nurburgring, TrackLayout.NURBURGRING.getIcon(year, ""))
        assertEquals(R.drawable.circuit_nurburgring, TrackLayout.NURBURGRING.getDefaultIcon())
    }

    @Test
    fun `get icon with year gets override from min max range min inclusive`() {
        val year = 1975

        assertEquals(R.drawable.circuit_silverstone_1975_1986, TrackLayout.SILVERSTONE.getIcon(year, ""))
    }

    @Test
    fun `get icon with year gets override from min max range max inclusive`() {
        val year = 1993

        assertEquals(R.drawable.circuit_silverstone_1991_1993, TrackLayout.SILVERSTONE.getIcon(year, ""))
    }

    @Test
    fun `get icon with year gets override from min max range between range`() {
        val year = 1965

        assertEquals(R.drawable.circuit_silverstone_1950_1973, TrackLayout.SILVERSTONE.getIcon(year, ""))
    }
}