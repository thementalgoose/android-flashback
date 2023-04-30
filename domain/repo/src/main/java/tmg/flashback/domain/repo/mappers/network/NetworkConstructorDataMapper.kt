package tmg.flashback.domain.repo.mappers.network

import tmg.flashback.domain.persistence.models.constructors.Constructor
import javax.inject.Inject

class NetworkConstructorDataMapper @Inject constructor() {

    @Throws(RuntimeException::class)
    fun mapConstructorData(constructor: tmg.flashback.statistics.network.models.constructors.Constructor): Constructor {
        return Constructor(
            id = constructor.id,
            colour = constructor.colour,
            name = constructor.name,
            photoUrl = constructor.photoUrl,
            nationality = constructor.nationality,
            nationalityISO = constructor.nationalityISO,
            wikiUrl = constructor.wikiUrl
        )
    }
}