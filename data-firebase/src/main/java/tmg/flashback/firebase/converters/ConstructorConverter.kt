package tmg.flashback.firebase.converters

import androidx.core.graphics.toColorInt
import tmg.flashback.data.models.stats.Constructor
import tmg.flashback.firebase.models.FSeasonOverviewConstructor

fun FSeasonOverviewConstructor.convert(): Constructor {
    return Constructor(
        id = id,
        name = name,
        wikiUrl = wikiUrl,
        nationality = nationality,
        nationalityISO = nationalityISO,
        color = colour.toColorInt()
    )
}