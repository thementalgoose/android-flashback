package tmg.flashback.firebase.converters

import tmg.flashback.firebase.models.FCircuit
import tmg.flashback.firebase.models.FCircuitResult
import tmg.flashback.repo.models.stats.CircuitSummary
import tmg.flashback.firebase.models.FSeasonOverviewRaceCircuit
import tmg.flashback.repo.models.stats.Circuit
import tmg.flashback.repo.models.stats.CircuitRace

fun FSeasonOverviewRaceCircuit.convert(): CircuitSummary {
    return CircuitSummary(
        id = id,
        name = name,
        wikiUrl = wikiUrl,
        locality = locality,
        country = country,
        countryISO = countryISO,
        locationLat = location.lat.toDoubleOrNull() ?: 0.0,
        locationLng = location.lng.toDoubleOrNull() ?: 0.0
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
        results = this.results?.values?.map { it.convert() } ?: emptyList()
    )
}

fun FCircuitResult.convert(): CircuitRace {
    return CircuitRace(
        name = this.name,
        season = this.season,
        round = this.round,
        wikiUrl = this.wikiUrl ?: "",
        date = fromDate(this.date),
        time = fromTime(this.time)
    )
}