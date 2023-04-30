package tmg.flashback.domain.repo.mappers.app

import tmg.flashback.formula1.model.*
import tmg.flashback.domain.persistence.models.circuit.CircuitRoundResultWithDriverConstructor
import tmg.flashback.domain.persistence.models.circuit.CircuitRoundWithResults
import tmg.utilities.utils.LocalDateUtils.Companion.requireFromDate
import tmg.utilities.utils.LocalTimeUtils.Companion.fromTime
import javax.inject.Inject

class CircuitMapper @Inject constructor(
    private val driverMapper: DriverDataMapper,
    private val constructorMapper: ConstructorDataMapper
) {

    @Throws(NullPointerException::class)
    fun mapCircuit(circuit: tmg.flashback.domain.persistence.models.circuit.Circuit?): Circuit? {
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
    fun mapCircuitHistory(circuitHistory: tmg.flashback.domain.persistence.models.circuit.CircuitHistory?): CircuitHistory? {
        val circuit = mapCircuit(circuitHistory?.circuit) ?: return null
        return CircuitHistory(
            data = circuit,
            results = (circuitHistory?.races ?: emptyList())
                .map { item ->
                    mapCircuitRace(item)
                }
        )
    }

    private fun mapCircuitRace(circuitRoundWithResults: CircuitRoundWithResults): CircuitHistoryRace {
        return CircuitHistoryRace(
            name = circuitRoundWithResults.round.name,
            season = circuitRoundWithResults.round.season,
            round = circuitRoundWithResults.round.round,
            wikiUrl = circuitRoundWithResults.round.wikiUrl,
            date = requireFromDate(circuitRoundWithResults.round.date),
            time = fromTime(circuitRoundWithResults.round.time),
            preview = circuitRoundWithResults.results.map { mapCircuitRaceResult(it) }
        )
    }

    private fun mapCircuitRaceResult(model: CircuitRoundResultWithDriverConstructor): CircuitHistoryRaceResult {
        return CircuitHistoryRaceResult(
            position = model.result.position,
            driver = driverMapper.mapDriver(model.driver),
            constructor = constructorMapper.mapConstructorData(model.constructor)
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