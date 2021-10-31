package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.network.models.circuits.CircuitData
import tmg.flashback.statistics.network.models.circuits.CircuitResult
import tmg.flashback.statistics.network.models.circuits.CircuitResultRace
import tmg.flashback.statistics.network.models.circuits.Circuits
import tmg.flashback.statistics.room.models.circuit.Circuit
import tmg.flashback.statistics.room.models.circuit.CircuitHistory
import tmg.flashback.statistics.room.models.circuit.CircuitRound
import tmg.flashback.statistics.room.models.circuit.CircuitRoundWithResults
import java.lang.RuntimeException
import kotlin.jvm.Throws

class NetworkCircuitMapper {

    @Throws(RuntimeException::class)
    fun mapCircuit(circuit: Circuits?): CircuitHistory? {
        if (circuit == null) return null
        return CircuitHistory(
            circuit = mapCircuitData(circuit.data),
            races = (circuit.results ?: emptyMap())
                .map { (_, value) ->
                    mapCircuitResult(circuit.data.id, value)
                }
        )
    }

    @Throws(RuntimeException::class)
    fun mapCircuitData(data: CircuitData): Circuit {
        return Circuit(
            id = data.id,
            name = data.name,
            wikiUrl = data.wikiUrl,
            locationLat = data.location?.lat?.toDoubleOrNull(),
            locationLng = data.location?.lng?.toDoubleOrNull(),
            city = data.city,
            country = data.country,
            countryISO = data.countryISO,
        )
    }

    private fun mapCircuitResult(constructorId: String, result: CircuitResult): CircuitRoundWithResults {
        return CircuitRoundWithResults(
            round = mapCircuitResultRace(constructorId, result.race),
            // TODO: Save this information properly!
            results = emptyList()
        )
    }

    private fun mapCircuitResultRace(circuitId: String, race: CircuitResultRace): CircuitRound {
        return CircuitRound(
            circuitId = circuitId,
            season = race.season,
            round = race.round,
            name = race.name,
            date = race.date,
            time = race.time,
            wikiUrl = race.wikiUrl,
        )
    }
}