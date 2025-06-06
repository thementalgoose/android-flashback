package tmg.flashback.data.repo.mappers.network

import tmg.flashback.data.persistence.models.circuit.Circuit
import javax.inject.Inject

class NetworkCircuitDataMapper @Inject constructor() {

    @Throws(RuntimeException::class)
    fun mapCircuitData(data: tmg.flashback.flashbackapi.api.models.circuits.Circuit): Circuit {
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