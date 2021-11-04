package tmg.flashback.statistics.repo.mappers.network

import java.lang.RuntimeException
import kotlin.jvm.Throws

class NetworkCircuitDataMapper {

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