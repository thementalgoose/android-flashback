package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.model.Circuit
import tmg.flashback.formula1.model.Location

class CircuitMapper {

    fun mapCircuit(circuit: tmg.flashback.statistics.room.models.circuit.Circuit?): Circuit? {
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