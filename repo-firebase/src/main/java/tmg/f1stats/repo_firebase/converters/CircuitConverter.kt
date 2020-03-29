package tmg.f1stats.repo_firebase.converters

import tmg.f1stats.repo.models.Circuit
import tmg.f1stats.repo_firebase.models.FSeasonOverviewRaceCircuit

fun FSeasonOverviewRaceCircuit.convert(): Circuit {
    return Circuit(
        id = id,
        name = name,
        wikiUrl = wikiUrl,
        locality = locality,
        country = country,
        countryISO = countryISO,
        locationLat = location.lat,
        locationLng = location.lng
    )
}