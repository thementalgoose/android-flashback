package tmg.flashback.repo_firebase.converters

import androidx.core.graphics.toColorInt
import tmg.flashback.repo.models.Constructor
import tmg.flashback.repo_firebase.models.FSeasonOverviewConstructor

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