package tmg.flashback.firebase.mappers.seasonoverview

import androidx.core.graphics.toColorInt
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.firebase.models.FSeasonOverviewConstructor

class SeasonOverviewConstructorMapper {

    /**
     * Map a constructor
     * @param input Constructor to map
     */
    fun mapConstructor(input: FSeasonOverviewConstructor): tmg.flashback.formula1.model.Constructor {
        return tmg.flashback.formula1.model.Constructor(
            id = input.id,
            name = input.name,
            wikiUrl = input.wikiUrl,
            nationality = input.nationality,
            nationalityISO = input.nationalityISO,
            color = input.colour.toColorInt()
        )
    }
}