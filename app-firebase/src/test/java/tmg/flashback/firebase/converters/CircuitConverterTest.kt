package tmg.flashback.firebase.converters

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.firebase.models.FCircuit
import tmg.flashback.firebase.models.FCircuitResult
import tmg.flashback.firebase.models.FSeasonOverviewRaceCircuit
import tmg.flashback.firebase.models.FSeasonOverviewRaceCircuitLocation
import tmg.flashback.firebase.testutils.BaseTest
import tmg.flashback.repo.models.stats.CircuitRace

internal class CircuitConverterTest: BaseTest() {

    @Test
    fun `CircuitConverter season overview race summary location null results in 0,0`() {

        val model = FSeasonOverviewRaceCircuit(location = FSeasonOverviewRaceCircuitLocation(lat = null, lng = null))
        assertEquals(0.0, model.convert().locationLng)
        assertEquals(0.0, model.convert().locationLat)
    }

    @Test
    fun `CircuitConverter season overview race summary location invalid results in 0,0`() {

        val model = FSeasonOverviewRaceCircuit(location = FSeasonOverviewRaceCircuitLocation(lat = "asjld", lng = "zxlzf"))
        assertEquals(0.0, model.convert().locationLng)
        assertEquals(0.0, model.convert().locationLat)
    }

    @Test
    fun `CircuitConverter convert result map`() {

        val invalidResult = FCircuitResult(date = "sjddsjkld")
        val model = FCircuit(results = mapOf("id" to invalidResult))
        assertEquals(emptyList<CircuitRace>(), model.convert().results)
    }

    @Test
    fun `CircuitConverter season result doesnt convert if date is null`() {

        val model = FCircuitResult(date = null)
        assertEquals(null, model.convert())
    }

    @Test
    fun `CircuitConverter season result doesnt convert if date is invalid`() {

        val model = FCircuitResult(date = "2020-01-34")
        assertEquals(null, model.convert())
    }
}