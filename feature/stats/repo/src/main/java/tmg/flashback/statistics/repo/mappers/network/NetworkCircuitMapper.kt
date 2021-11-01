package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.network.models.circuits.CircuitResult
import tmg.flashback.statistics.network.models.circuits.CircuitResultRace
import tmg.flashback.statistics.network.models.circuits.Circuit
import tmg.flashback.statistics.room.models.circuit.*
import java.lang.RuntimeException
import kotlin.jvm.Throws

class NetworkCircuitMapper(
    private val networkCircuitDataMapper: NetworkCircuitDataMapper
) {

    @Throws(RuntimeException::class)
    fun mapCircuit(circuit: Circuit?): CircuitHistory? {
        if (circuit == null) return null
        return CircuitHistory(
            circuit = networkCircuitDataMapper.mapCircuitData(circuit.data),
            races = (circuit.results ?: emptyMap())
                .map { (_, value) ->
                    mapCircuitResult(circuit.data.id, value)
                }
        )
    }

    @Throws(RuntimeException::class)
    fun mapCircuitRounds(circuitId: String, resultRace: CircuitResult): CircuitRound {
        return CircuitRound(
            circuitId = circuitId,
            season = resultRace.race.season,
            round = resultRace.race.round,
            name = resultRace.race.name,
            date = resultRace.race.date,
            time = resultRace.race.time,
            wikiUrl = resultRace.race.wikiUrl,
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