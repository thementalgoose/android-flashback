package tmg.f1stats.repo_firebase.converters

import androidx.core.graphics.toColorInt
import tmg.f1stats.repo.models.Constructor
import tmg.f1stats.repo_firebase.models.FSeasonOverviewConstructor

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