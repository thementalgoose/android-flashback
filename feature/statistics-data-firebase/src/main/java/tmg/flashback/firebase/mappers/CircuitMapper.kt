package tmg.flashback.firebase.mappers

import java.lang.NullPointerException
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.models.stats.Circuit
import tmg.flashback.data.models.stats.CircuitRace
import tmg.flashback.data.models.stats.Location
import tmg.flashback.firebase.models.FCircuit
import tmg.flashback.firebase.models.FCircuitResult
import tmg.utilities.utils.LocalDateUtils.Companion.isDateValid
import tmg.utilities.utils.LocalDateUtils.Companion.requireFromDate
import tmg.utilities.utils.LocalTimeUtils.Companion.fromTime

class CircuitMapper(
    private val crashController: CrashController,
    private val locationMapper: LocationMapper
) {

    fun mapCircuit(input: FCircuit): Circuit {
        return Circuit(
            id = input.id,
            name = input.circuitName,
            wikiUrl = input.wikiUrl,
            locality = input.locality,
            country = input.country,
            countryISO = input.countryISO ?: "",
            location = getLocation(input),
            results = input.results
                ?.mapNotNull { (_, value) ->
                    mapCircuitRace(input.id, value)
                }
                ?: emptyList(),
        )
    }

    private fun getLocation(input: FCircuit): Location? {
        val location = locationMapper.mapCircuitLocation(input.location)

        if (location != null) {
            return location
        } else {
            val lat = input.locationLat ?: return null
            val lng = input.locationLng ?: return null

            return Location(lat, lng)
        }
    }

    fun mapCircuitRace(circuitId: String, input: FCircuitResult): CircuitRace? {
        if (!isDateValid(input.date)) {
            crashController.logException(NullPointerException("CircuitMapper.mapCircuitRace circuitId=$circuitId input date of \"${input.date}\" is not valid"))
            return null
        }
        return CircuitRace(
            name = input.name,
            season = input.season,
            round = input.round,
            wikiUrl = input.wikiUrl ?: "",
            date = requireFromDate(input.date!!),
            time = fromTime(input.time)
        )
    }
}