package tmg.flashback.data.repo.mappers.network

import tmg.flashback.data.persistence.models.constructors.Constructor
import javax.inject.Inject

class NetworkConstructorDataMapper @Inject constructor() {

    @Throws(RuntimeException::class)
    fun mapConstructorData(constructor: tmg.flashback.flashbackapi.api.models.constructors.Constructor): Constructor {
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