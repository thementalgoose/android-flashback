package tmg.flashback.statistics.repo.mappers.app

import androidx.core.graphics.toColorInt
import tmg.flashback.formula1.model.Constructor

class ConstructorDataMapper {

    fun mapConstructorData(constructor: tmg.flashback.statistics.room.models.constructors.Constructor): Constructor {
        return Constructor(
            id = constructor.id,
            name = constructor.name,
            wikiUrl = constructor.wikiUrl,
            nationality = constructor.nationality,
            nationalityISO = constructor.nationalityISO,
            color = constructor.colour.toColorInt(),
        )
    }
}