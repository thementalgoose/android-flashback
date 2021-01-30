package tmg.flashback.firebase.converters

import tmg.flashback.firebase.base.ConverterUtils.fromDateRequired
import tmg.flashback.firebase.base.ConverterUtils.fromTime
import tmg.flashback.firebase.base.ConverterUtils.isDateValid
import tmg.flashback.firebase.models.FCircuit
import tmg.flashback.firebase.models.FCircuitResult
import tmg.flashback.data.models.stats.CircuitSummary
import tmg.flashback.firebase.models.FSeasonOverviewRaceCircuit
import tmg.flashback.data.models.stats.Circuit
import tmg.flashback.data.models.stats.CircuitRace

fun FSeasonOverviewRaceCircuit.convert(): CircuitSummary {
    return CircuitSummary(
        id = id,
        name = name,
        wikiUrl = wikiUrl,
        locality = locality,
        country = country,
        countryISO = countryISO,
        locationLat = location.lat?.toDoubleOrNull() ?: 0.0,
        locationLng = location.lng?.toDoubleOrNull() ?: 0.0
    )
}

fun FCircuit.convert(): Circuit {
    return Circuit(
        id = this.id,
        name = this.circuitName,
        wikiUrl = this.wikiUrl ?: "",
        locality = this.locality,
        country = this.country,
        countryISO = this.countryISO ?: "",
        locationLat = this.locationLat,
        locationLng = this.locationLng,
        results = this.results?.values?.mapNotNull { it.convert() } ?: emptyList()
    )
}

fun FCircuitResult.convert(): CircuitRace? {
    if (!isDateValid(this.date)) {
        return null
    }
    return CircuitRace(
        name = this.name,
        season = this.season,
        round = this.round,
        wikiUrl = this.wikiUrl ?: "",
        date = fromDateRequired(this.date!!),
        time = fromTime(this.time)
    )
}