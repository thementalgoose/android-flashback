package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.room.models.circuit.Circuit
import javax.inject.Inject

class NetworkCircuitDataMapper @Inject constructor() {

    @Throws(RuntimeException::class)
    fun mapCircuitData(data: tmg.flashback.statistics.network.models.circuits.Circuit): Circuit {
        return Circuit(
            id = data.id,
            name = data.name,
            wikiUrl = data.wikiUrl,
            locationLat = data.location?.lat?.toDoubleOrNull(),
            locationLng = data.location?.lng?.toDoubleOrNull(),
            city = data.city,
            country = data.country,
            countryISO = data.countryISO,
        )
    }
}