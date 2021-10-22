package tmg.flashback.firebase.mappers

import tmg.flashback.data.models.stats.Location
import tmg.flashback.firebase.models.FCircuitLocation

class LocationMapper {

    fun mapCircuitLocation(input: FCircuitLocation?): Location? {
        if (input == null) {
            return null
        }
        val lat = input.lat?.toDoubleOrNull() ?: return null
        val lng = input.lng?.toDoubleOrNull() ?: return null

        return Location(
            lat = lat,
            lng = lng
        )
    }
}