package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.formula1.model.CircuitRace
import tmg.flashback.formula1.model.Location
import tmg.flashback.statistics.room.models.circuit.CircuitRoundWithResults
import tmg.utilities.utils.LocalDateUtils.Companion.requireFromDate
import tmg.utilities.utils.LocalTimeUtils.Companion.fromTime
import java.lang.NullPointerException
import kotlin.jvm.Throws

class CircuitMapper {

    @Throws(NullPointerException::class)
    fun mapCircuit(circuit: tmg.flashback.statistics.room.models.circuit.Circuit?): Circuit? {
        if (circuit == null) return null
        return Circuit(
            id = circuit.id,
            name = circuit.name,
            wikiUrl = circuit.wikiUrl,
            city = circuit.city,
            country = circuit.country,
            countryISO = circuit.countryISO,
            location = mapLocation(circuit.locationLat, circuit.locationLng),
        )
    }

    @Throws(NullPointerException::class)
    fun mapCircuitHistory(circuitHistory: tmg.flashback.statistics.room.models.circuit.CircuitHistory?): CircuitHistory? {
        val circuit = mapCircuit(circuitHistory?.circuit) ?: return null
        return CircuitHistory(
            data = circuit,
            results = (circuitHistory?.races ?: emptyList())
                .map { item ->
                    mapCircuitRace(item)
                }
        )
    }

    private fun mapCircuitRace(circuitRoundWithResults: CircuitRoundWithResults): CircuitRace {
        return CircuitRace(
            name = circuitRoundWithResults.round.name,
            season = circuitRoundWithResults.round.season,
            round = circuitRoundWithResults.round.round,
            wikiUrl = circuitRoundWithResults.round.wikiUrl,
            date = requireFromDate(circuitRoundWithResults.round.date),
            time = fromTime(circuitRoundWithResults.round.time),
        )
    }

    private fun mapLocation(lat: Double?, lng: Double?): Location? {
        if (lat == null || lng == null) {
            return null
        }
        return Location(
            lat = lat,
            lng = lng
        )
    }
}