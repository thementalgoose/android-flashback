package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.network.models.circuits.CircuitPreviewPosition
import tmg.flashback.statistics.network.models.circuits.CircuitResult
import tmg.flashback.statistics.network.models.circuits.CircuitResultRace
import tmg.flashback.statistics.room.models.circuit.*
import javax.inject.Inject

class NetworkCircuitMapper @Inject constructor() {

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

    @Throws(RuntimeException::class)
    fun mapCircuitPreviews(model: CircuitResult): List<CircuitRoundResult> {
        return model.preview?.map { position ->
            mapCircuitRoundResult(position, model.race.season, model.race.round)
        } ?: emptyList()
    }

    private fun mapCircuitRoundResult(position: CircuitPreviewPosition, season: Int, round: Int): CircuitRoundResult {
        return CircuitRoundResult(
            season = season,
            round = round,
            position = position.position,
            driverId = position.driver.id,
            constructorId = position.constructor.id
        )
    }
}