package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.network.models.circuits.CircuitData
import tmg.flashback.statistics.room.models.circuit.Circuit
import java.lang.RuntimeException
import kotlin.jvm.Throws

class NetworkCircuitDataMapper {

    @Throws(RuntimeException::class)
    fun mapCircuitData(data: CircuitData): Circuit {
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