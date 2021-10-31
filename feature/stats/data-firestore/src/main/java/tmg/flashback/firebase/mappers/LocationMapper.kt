package tmg.flashback.firebase.mappers

import tmg.flashback.formula1.model.Location
import tmg.flashback.firebase.models.FCircuitLocation

class LocationMapper {

    fun mapCircuitLocation(input: FCircuitLocation?): tmg.flashback.formula1.model.Location? {
        if (input == null) {
            return null
        }
        val lat = input.lat?.toDoubleOrNull() ?: return null
        val lng = input.lng?.toDoubleOrNull() ?: return null

        return tmg.flashback.formula1.model.Location(
            lat = lat,
            lng = lng
        )
    }
}