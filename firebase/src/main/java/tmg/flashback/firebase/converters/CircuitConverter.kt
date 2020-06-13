package tmg.flashback.firebase.converters

import tmg.flashback.repo.models.stats.Circuit
import tmg.flashback.firebase.models.FSeasonOverviewRaceCircuit

fun FSeasonOverviewRaceCircuit.convert(): Circuit {
    return Circuit(
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