package tmg.flashback.firebase.converters

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.data.models.stats.Circuit
import tmg.flashback.firebase.models.FCircuit
import tmg.flashback.firebase.models.FCircuitResult
import tmg.flashback.firebase.models.FSeasonOverviewRaceCircuit
import tmg.flashback.firebase.models.FSeasonOverviewRaceCircuitLocation
import tmg.flashback.data.models.stats.CircuitRace
import tmg.testutils.BaseTest

internal class CircuitConverterTest: BaseTest() {

    @Test
    fun `circuit maps fields correctly`() {

        val expected = Circuit(
            id = "circuitId",
            name = "circuitName",
            wikiUrl = "wikiUrl",
            locality = "locality",
            country = "country",
            countryISO = "countryISO",
            locationLat = 1.0,
            locationLng = 2.0,
            results = listOf(
                CircuitRace(
                    name = "name",
                    season = 2020,
                    round = 1,
                    wikiUrl = "wikiUrl",
                    date = LocalDate.of(2020, 1, 1),
                    time = LocalTime.of(12, 0, 0)
                )
            )
        )

        val input = FCircuit.exampleData()

        assertEquals(expected, input.convert())
    }

    @Test
    fun `season overview race summary location null results in 0,0`() {

        val model = FSeasonOverviewRaceCircuit(location = FSeasonOverviewRaceCircuitLocation(lat = null, lng = null))
        assertEquals(0.0, model.convert().locationLng)
        assertEquals(0.0, model.convert().locationLat)
    }

    @Test
    fun `season overview race summary location invalid results in 0,0`() {

        val model = FSeasonOverviewRaceCircuit(location = FSeasonOverviewRaceCircuitLocation(lat = "asjld", lng = "zxlzf"))
        assertEquals(0.0, model.convert().locationLng)
        assertEquals(0.0, model.convert().locationLat)
    }

    @Test
    fun `season result doesnt convert if date is null`() {

        val model = FCircuitResult(date = null)
        assertEquals(null, model.convert())
    }

    @Test
    fun `season result doesnt convert if date is invalid`() {

        val model = FCircuitResult(date = "2020-01-34")
        assertEquals(null, model.convert())
    }
}

internal fun FCircuit.Companion.exampleData(): FCircuit {
    return FCircuit(
        circuitName = "circuitName",
        country = "country",
        countryISO = "countryISO",
        id = "circuitId",
        locality = "locality",
        locationLat = 1.0,
        locationLng = 2.0,
        wikiUrl = "wikiUrl",
        results = mapOf(
            "s2020r1" to FCircuitResult.exampleData()
        )
    )
}

internal fun FCircuitResult.Companion.exampleData(): FCircuitResult {
    return FCircuitResult(
        date = "2020-01-01",
        time = "12:00:00",
        name = "name",
        season = 2020,
        round = 1,
        wikiUrl = "wikiUrl"
    )
}