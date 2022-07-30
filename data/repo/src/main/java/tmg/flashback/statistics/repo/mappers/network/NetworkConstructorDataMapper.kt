package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.room.models.constructors.Constructor
import javax.inject.Inject

class NetworkConstructorDataMapper @Inject constructor() {

    @Throws(RuntimeException::class)
    fun mapConstructorData(constructor: tmg.flashback.statistics.network.models.constructors.Constructor): Constructor {
        return Constructor(
            id = constructor.id,
            colour = constructor.colour,
            name = constructor.name,
            nationality = constructor.nationality,
            nationalityISO = constructor.nationalityISO,
            wikiUrl = constructor.wikiUrl
        )
    }
}