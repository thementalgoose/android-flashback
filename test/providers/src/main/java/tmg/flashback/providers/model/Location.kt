package tmg.flashback.providers.model

import tmg.flashback.formula1.model.Location

fun Location.Companion.model(
    lat: Double = 51.101,
    lng: Double = -1.101
): Location = Location(
    lat = lat,
    lng = lng
)