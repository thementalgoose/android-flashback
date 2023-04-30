package tmg.flashback.statistics.repo.mappers.app

import androidx.core.graphics.toColorInt
import tmg.flashback.formula1.model.Constructor
import javax.inject.Inject

class ConstructorDataMapper @Inject constructor() {

    fun mapConstructorData(constructor: tmg.flashback.statistics.room.models.constructors.Constructor): Constructor {
        return Constructor(
            id = constructor.id,
            name = constructor.name,
            wikiUrl = constructor.wikiUrl,
            photoUrl = constructor.photoUrl,
            nationality = constructor.nationality,
            nationalityISO = constructor.nationalityISO,
            color = constructor.colour.toColorInt(),
        )
    }
}